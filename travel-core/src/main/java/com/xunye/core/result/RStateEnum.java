package com.xunye.core.result;

public enum RStateEnum {

    SUCCESS(200, "操作成功"),
    UNAUTHORIZED(401, "没有权限，请联系管理员授权"),
    FORBIDDEN(403, "访问受限，授权过期"),
    NOT_FOUND(404, "资源,服务未找到"),
    SYSTEM_ERROR(500, "系统内部错误"),
    BUSINESS_ERROR(501, "业务异常"),
    PARAMS_ERROR(502, "请求参数有误"),
    ;

    private final int code;
    private final String msg;

    RStateEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
