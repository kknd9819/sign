package com.zz.shengyuan.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.google.common.collect.Lists;
import com.zz.shengyuan.model.RequestParams;
import com.zz.shengyuan.repository.RequestParamsRepository;
import com.zz.shengyuan.service.SignService;
import com.zz.shengyuan.util.SignUtil;

@Service
public class SignServiceImpl implements SignService {

    @Resource
    private SignUtil signUtil;

    @Resource
    private RequestParamsRepository requestParamsRepository;


    @Override
    public List<Map<String, Object>> getVersion() {
        List<RequestParams> list = requestParamsRepository.findAll(new Sort(Sort.Direction.ASC,"id"));
        List<Map<String,Object>> resultMap = new ArrayList<>();
        for(RequestParams requestParams :list){
            Map<String,Object> paramMap = new HashMap<>();
            paramMap.put("token",requestParams.getToken());
            paramMap.put("num",requestParams.getVersion());
            paramMap.put("vcode",requestParams.getVersionCode());
            paramMap.put("autoSystemId",requestParams.getId());
            String sign = signUtil.createSign(paramMap);
            paramMap.put("_signdata",sign);
            resultMap.add(paramMap);
        }
        return resultMap;
    }

    /**
     * GET 需要的参数
     * @return paramMap
     */
    @Override
    public List<Map<String,Object>> getRequestParams(){
        List<RequestParams> list = requestParamsRepository.findAll(new Sort(Sort.Direction.ASC,"id"));
        List<Map<String,Object>> resultMap = new ArrayList<>();
        for(RequestParams requestParams :list){
            Map<String,Object> paramMap = new HashMap<>();
            paramMap.put("token",requestParams.getToken());
            paramMap.put("_version",requestParams.getVersion());
            paramMap.put("_channel","0");
            paramMap.put("_network","WIFI");
            paramMap.put("_regid","");
            paramMap.put("_timestamp",System.currentTimeMillis());
            paramMap.put("autoSystemId",requestParams.getId());
            String sign = signUtil.createSign(paramMap);
            paramMap.put("_signdata",sign);
            resultMap.add(paramMap);
        }
        return resultMap;
    }

    /**
     * POST 需要的参数
     * @return paramMap
     */
    @Override
    public List<MultiValueMap<String,Object>> postRequestParams(){
        List<RequestParams> list = requestParamsRepository.findAll(new Sort(Sort.Direction.ASC,"id"));
        List<MultiValueMap<String,Object>> resultMap = new ArrayList<>();
        for(RequestParams requestParams :list){
            MultiValueMap<String, Object> paramMap= new LinkedMultiValueMap<>();
            paramMap.put("token", Lists.newArrayList(requestParams.getToken()));
            paramMap.put("_version",Lists.newArrayList(requestParams.getVersion()));
            paramMap.put("_channel",Lists.newArrayList("0"));
            paramMap.put("_network", Lists.newArrayList("WIFI"));
            paramMap.put("_regid",Lists.newArrayList(""));
            paramMap.put("_timestamp",Lists.newArrayList("" + System.currentTimeMillis()));
            paramMap.put("autoSystemId",Lists.newArrayList(requestParams.getId()));
            Map<String,Object> map = new HashMap<>();
            map.put("token",requestParams.getToken());
            map.put("_version",requestParams.getVersion());
            map.put("_channel","0");
            map.put("_network","WIFI");
            map.put("_regid","");
            map.put("_timestamp",System.currentTimeMillis());
            map.put("autoSystemId",requestParams.getId());
            String sign = signUtil.createSign(map);
            paramMap.put("_signdata",Lists.newArrayList(sign));
            resultMap.add(paramMap);
        }
        return resultMap;
    }

    /**
     * 登录需要的参数
     * @return paramMap
     */
    @Override
    public List<MultiValueMap<String,Object>> paymentLoginParams(){
        List<RequestParams> list = requestParamsRepository.findAll(new Sort(Sort.Direction.ASC,"id"));
        List<MultiValueMap<String,Object>> resultMap = new ArrayList<>();
        for(RequestParams requestParams :list){
            MultiValueMap<String,Object> paramMap = new LinkedMultiValueMap<>();
            paramMap.put("password",Lists.newArrayList(requestParams.getPassword()));
            paramMap.put("mobile",Lists.newArrayList(requestParams.getMobile()));
            paramMap.put("systemType",Lists.newArrayList("0"));
            paramMap.put("_version",Lists.newArrayList(requestParams.getVersion()));
            paramMap.put("_channel",Lists.newArrayList("01"));
            paramMap.put("_network", Lists.newArrayList("WIFI"));
            paramMap.put("_regid",Lists.newArrayList(""));
            paramMap.put("regid",Lists.newArrayList(requestParams.getRegidLogin()));
            paramMap.put("_timestamp",Lists.newArrayList("" + System.currentTimeMillis()));
            paramMap.put("autoSystemId",Lists.newArrayList(requestParams.getId()));
            Map<String,Object> map = new HashMap<>();
            map.put("password",requestParams.getPassword());
            map.put("mobile",requestParams.getMobile());
            map.put("systemType","0");
            map.put("_version",requestParams.getVersion());
            map.put("_channel","01");
            map.put("_network","WIFI");
            map.put("_regid","");
            map.put("regid",requestParams.getRegidLogin());
            map.put("_timestamp",System.currentTimeMillis());
            map.put("autoSystemId",requestParams.getId());
            String sign = signUtil.createSign(map);
            paramMap.put("_signdata",Lists.newArrayList(sign));
            resultMap.add(paramMap);
        }
        return resultMap;
    }

    @Override
    public void updateTokenAndNickName(Long requestParamsId,String token,String nickName,String portraitPath) {
    	Optional<RequestParams> requestParams = requestParamsRepository.findById(requestParamsId);
    	RequestParams rp = new RequestParams();
    	BeanUtils.copyProperties(requestParams, rp);
    	rp.setModifyDate(new Date());
    	rp.setNickName(nickName);
    	rp.setPortraitPath(portraitPath);
    	rp.setToken(token);
        requestParamsRepository.saveAndFlush(rp);
    }

    @Override
    public void updateVersionAndVersionCode(String version, String versionCode, Long id) {
        requestParamsRepository.updateVersionAndVersionCode(version,versionCode,id);
    }

    @Override
    public RequestParams findOne(Long requestParamsId) {
    	Optional<RequestParams> findById = requestParamsRepository.findById(requestParamsId);
        return findById.orElse(null);
    }

    @Override
    public List<RequestParams> findAll() {
        return requestParamsRepository.findAll(new Sort(Sort.Direction.ASC,"id"));
    }

}
