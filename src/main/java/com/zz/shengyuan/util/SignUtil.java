package com.zz.shengyuan.util;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class SignUtil {

    // 请求公钥
    private static final String PUBLIC_KEY = "Sy-CloudPay-Android";

    public String createSign(Map<String, Object> maps) {
        List<String> keyList = new ArrayList<>(maps.keySet());
        // 将请求参数排序
        String[] keyArray = keyList.toArray(new String[keyList.size()]);
        Arrays.sort(keyArray);
        // 组装参数
        StringBuilder sign = new StringBuilder();
        for (String aKeyArray : keyArray) {
            sign.append(aKeyArray);
            sign.append(maps.get(aKeyArray));
        }
        // 添加私有KEY
        sign.append(PUBLIC_KEY);
        // 参数生成MD5加密签名
        return str2md5(sign.toString());
    }

    private String str2md5(String text) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(text.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuilder buf = new StringBuilder("");
            for (byte aB : b) {
                i = aB;
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }

            result = buf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }
}
