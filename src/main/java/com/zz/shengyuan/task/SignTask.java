package com.zz.shengyuan.task;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zz.shengyuan.constant.SignConstant;
import com.zz.shengyuan.model.RequestParams;
import com.zz.shengyuan.service.SignService;
import com.zz.shengyuan.util.ImageDownload;
import com.zz.shengyuan.util.JSONModel;
import com.zz.shengyuan.util.Version;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class SignTask {

    @Resource
    private SignService signService;

    //每隔 1分钟 执行 0 */1 * * * ?
    @Scheduled(cron = "0 1 6,21 * * ?")
    public void getSignPage(){
        try{
            RestTemplate restTemplate = new RestTemplate();
            List<Map<String, Object>> list = signService.getRequestParams();
            if(list != null && list.size() > 0 ){
                for (int i=0;i<list.size();i++) {
                    String result = restTemplate.getForObject(SignConstant.PAGE_URL,String.class,list.get(i));
                    log.info("【签到页面】：id={}， " + result, list.get(i).get(SignConstant.AUTO_SYSTEM_ID));
                    JSONModel jsonModel = parseJson(result);
                    if(jsonModel.getCode().equals(JSONModel.CODE_SUCCESS)){
                        Version version = restTemplate.getForObject(SignConstant.VERSION_URL, Version.class, signService.getVersion().get(i));
                        log.info("【版本信息】：id={}，" + version, list.get(i).get(SignConstant.AUTO_SYSTEM_ID));

                        //获取最新的版本信息
                        String androidVersion = version.getAndroidVersion();
                        String versionCode = version.getVersionCode();
                        Long requestParamsId = (Long)list.get(i).get(SignConstant.AUTO_SYSTEM_ID);
                        RequestParams requestParams = signService.findOne(requestParamsId);

                        //只有版本不一致的时候才更新
                        if(!requestParams.getVersion().equals(androidVersion) || !requestParams.getVersionCode().equals(versionCode)){
                            signService.updateVersionAndVersionCode(androidVersion,versionCode,requestParamsId);
                            log.info("【版本信息】：更新成功，id={}，" + version,list.get(i).get(SignConstant.AUTO_SYSTEM_ID));
                        }

                    }


                }
            } else {
                log.error("【签到页面】：失败，数据库没有待签到的用户！");
            }
        } catch (Exception e){
            log.error("【签到页面】：异常 {}",e.getMessage());
        }
    }

    //每天0：55 执行 0 55 0 * * ?
    @Scheduled(cron = "0 3 6,21 * * ?")
    public void postSign(){
        try{
            RestTemplate restTemplate = new RestTemplate();
            List<MultiValueMap<String,Object>> list = signService.postRequestParams();
            List<MultiValueMap<String,Object>> login_list = signService.paymentLoginParams();
            if(list != null && list.size() > 0 ){
                for(int i=0;i<list.size();i++){
                    HttpEntity<MultiValueMap<String,Object>> request = new HttpEntity<>(list.get(i));
                    ResponseEntity<String> response = restTemplate.postForEntity(SignConstant.SIGN_URL, request, String.class);
                    JSONModel jsonModel = parseJson(response.getBody());

                    //如果没有返回成功，且返回的状态码等于鉴权失败
                    if(jsonModel.getCode().equals(JSONModel.AUTH_ERROR)){
                        log.info("【自动签到】：鉴权失败,系统正准备自动重新登录...");
                        HttpEntity<MultiValueMap<String,Object>> loginRequest = new HttpEntity<>(login_list.get(i));
                        ResponseEntity<String> loginResponse = restTemplate.postForEntity(SignConstant.LOGIN_URL, loginRequest, String.class);
                        JSONModel loginModel = parseJson(loginResponse.getBody());
                        if(loginModel.getCode().equals(JSONModel.CODE_SUCCESS)){
                            log.info("【自动签到】：重新登录成功，id={}，loginResponse={}",getSystemId(list,i),loginModel );
                            //更新数据库token,nickName
                            String token = (String)loginModel.getResult().get("token");
                            String nickName = (String)loginModel.getResult().get("nickName");
                            String portraitPath = (String)loginModel.getResult().get("portraitPath");
                            Long requestParamsId = getSystemId(list,i);
                            signService.updateTokenAndNickName(requestParamsId,token,nickName,portraitPath);
                            
                            HttpEntity<MultiValueMap<String,Object>> request1 = new HttpEntity<>(signService.postRequestParams().get(i));
                            ResponseEntity<JSONModel> response1 = restTemplate.postForEntity(SignConstant.SIGN_URL, request1, JSONModel.class);
                            log.info("【自动签到】：重新登录成功之后继续签到，id={}，" + response1.getBody().toString(),getSystemId(list,i));
                        } else {
                            log.error("【自动签到】：重新登录失败，code={},msg={}",loginModel.getCode(),loginModel.getMsg());
                        }

                    } else {
                        log.info("【自动签到】：id={}，" + jsonModel,getSystemId(list,i));
                    }

                }
            } else {
                log.error("【自动签到】：失败，数据库没有待签到的用户！");
            }
        } catch (Exception e){
            log.error("【自动签到】：异常，{}",e.getMessage());
        }
    }

    @Scheduled(cron = "0 5 6,21 * * ?")
    public void downLoadUserImage(){
        try{
            List<RequestParams> all = signService.findAll().stream().filter(e -> !StringUtils.isEmpty(e.getPortraitPath())).collect(Collectors.toList());
            int count = 1;
            log.info("【下载图片】：准备下载图片，一共有{}张图片...",all.size());
            for (RequestParams requestParams : all){
                ImageDownload.downloadPicture(SignConstant.SERVER_URL,requestParams.getPortraitPath());
                log.info("【下载图片】：已经下完{}张，还有{}张...",count,all.size() - count);
                count++;
            }
        } catch (Exception e){
            log.error("【下载图片】：异常... ",e);
        }
    }


    private Long getSystemId(List<MultiValueMap<String,Object>> list,int i){
        return (Long)list.get(i).get(SignConstant.AUTO_SYSTEM_ID).get(0);
    }

    private JSONModel parseJson(String json) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        return objectMapper.readValue(json, JSONModel.class);
    }
}
