package org.hrm.enums;

public enum ResponseStatus {
    /**
     * 代表请求处理成功
     */
    SUCCESS(0, "处理成功！"),
    /**
     * 代表参数数量或者格式不对
     */
    PARAMERROR(1, "参数数量或者格式不对！"),
    /**
     * 程序中出现到不可事先预定义的异常
     */
    UNKNOWNERROR(-1, "未知异常");
    /**
     * 响应的状态码
     */
    private int code;
    /**
     * 对当前响应的描述
     */
    private String describe;

    ResponseStatus(int code, String describe) {
        this.code = code;
        this.describe = describe;
    }

    public int getCode() {
        return code;
    }

    public String getDescribe() {
        return describe;
    }
}
