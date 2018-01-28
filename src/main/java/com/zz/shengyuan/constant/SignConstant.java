package com.zz.shengyuan.constant;

public class SignConstant {

    public static final String SERVER_URL = "http://127.0.0.1:7360";

    public static final String PAGE_URL =  SERVER_URL + "/app/member/signIn_page.jhtml?token={token}&_version={_version}" +
            "&_channel={_channel}&_network={_network}&_regid={_regid}&_timestamp={_timestamp}&autoSystemId={autoSystemId}&_signdata={_signdata}";

    public static final String SIGN_URL = SERVER_URL + "/app/member/signIn.jhtml";

    public static final String LOGIN_URL = SERVER_URL +"/app/common/payment_login.jhtml";

    public static final String VERSION_URL =  SERVER_URL + "/app/common/version.jhtml?num={num}&_signdata={_signdata}&vcode={vcode}&token={token}";

    public static final String AUTO_SYSTEM_ID = "autoSystemId";

    public static final String DOWNLOAD_PATH = "download-dir";
}
