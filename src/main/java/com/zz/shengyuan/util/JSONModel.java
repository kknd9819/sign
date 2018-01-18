package com.zz.shengyuan.util;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;


@Data
@ToString
public class JSONModel implements Serializable {
    private static final long serialVersionUID = 2209408920278017192L;

    /** 成功信息编码 */
    public static final String CODE_SUCCESS = "000000";
    /** 请求签名错误 */
    public static final String SIGN_ERROR = "200000";
    /** 用户鉴权失败 */
    public static final String AUTH_ERROR = "300000";
    /** 参数错误【{0}】 */
    public static final String PARAM_ERROR = "400000";
    /** 运行时错误 */
    public static final String RUNTIME_ERROR = "500000";
    /** {0} */
    public static final String TIPS = "600000";

    /** 编码 */
    private String code;
    /** 描述 */
    private String msg;
    /** 结果 */
    private Map<String, Object> result;


}
