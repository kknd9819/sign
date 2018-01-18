package com.zz.shengyuan.service;

import java.util.List;
import java.util.Map;

import org.springframework.util.MultiValueMap;

import com.zz.shengyuan.model.RequestParams;

public interface SignService {

    /**
     * 获取版本信息
     * @return paramMap
     */
    List<Map<String,Object>>getVersion();

    /**
     * GET 需要的参数
     * @return paramMap
     */
    List<Map<String,Object>> getRequestParams();
    /**
     * POST 需要的参数
     * @return paramMap
     */
    List<MultiValueMap<String,Object>> postRequestParams();
    /**
     * 登录需要的参数
     * @return paramMap
     */
    List<MultiValueMap<String,Object>> paymentLoginParams();

    /**
     * 更新令牌和昵称
     * @param requestParamsId  系统id
     * @param token 令牌
     * @param nickName 昵称
     * @param portraitPath 头像相对路径
     */
    void updateTokenAndNickName(Long requestParamsId, String token, String nickName, String portraitPath);

    /**
     * 更新版本信息
     * @param version 版本号
     * @param versionCode 版本名
     * @param id id
     */
    void updateVersionAndVersionCode(String version, String versionCode, Long id);

    /**
     * 根据ID查找
     * @param requestParamsId id
     * @return RequestParams
     */
    RequestParams findOne(Long requestParamsId);

    List<RequestParams> findAll();
}
