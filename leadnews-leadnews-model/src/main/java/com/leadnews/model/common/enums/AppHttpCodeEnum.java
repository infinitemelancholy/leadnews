package com.leadnews.model.common.enums;

public enum AppHttpCodeEnum {

    // 鎴愬姛娈靛浐瀹氫负200
    SUCCESS(200,"鎿嶄綔鎴愬姛"),
    // 鐧诲綍娈?~50
    NEED_LOGIN(1,"闇€瑕佺櫥褰曞悗鎿嶄綔"),
    LOGIN_PASSWORD_ERROR(2,"瀵嗙爜閿欒"),
    // TOKEN50~100
    TOKEN_INVALID(50,"鏃犳晥鐨凾OKEN"),
    TOKEN_EXPIRE(51,"TOKEN宸茶繃鏈?),
    TOKEN_REQUIRE(52,"TOKEN鏄繀椤荤殑"),
    // SIGN楠岀 100~120
    SIGN_INVALID(100,"鏃犳晥鐨凷IGN"),
    SIG_TIMEOUT(101,"SIGN宸茶繃鏈?),
    // 鍙傛暟閿欒 500~1000
    PARAM_REQUIRE(500,"缂哄皯鍙傛暟"),
    PARAM_INVALID(501,"鏃犳晥鍙傛暟"),
    PARAM_IMAGE_FORMAT_ERROR(502,"鍥剧墖鏍煎紡鏈夎"),
    SERVER_ERROR(503,"鏈嶅姟鍣ㄥ唴閮ㄩ敊璇?),
    // 鏁版嵁閿欒 1000~2000
    DATA_EXIST(1000,"鏁版嵁宸茬粡瀛樺湪"),
    AP_USER_DATA_NOT_EXIST(1001,"ApUser鏁版嵁涓嶅瓨鍦?),
    DATA_NOT_EXIST(1002,"鏁版嵁涓嶅瓨鍦?),
    // 鏁版嵁閿欒 3000~3500
    NO_OPERATOR_AUTH(3000,"鏃犳潈闄愭搷浣?),
    NEED_ADMIND(3001,"闇€瑕佺鐞嗗憳鏉冮檺"),

    // 鑷獟浣撴枃绔犻敊璇?3501~3600
    MATERIASL_REFERENCE_FAIL(3501,"绱犳潗寮曠敤澶辨晥");


    int code;
    String errorMessage;

    AppHttpCodeEnum(int code, String errorMessage){
        this.code = code;
        this.errorMessage = errorMessage;
    }

    public int getCode() {
        return code;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}

