package org.hrm.utils;


import org.hrm.enums.ResponseStatus;

/**
 * 统一数据响应的格式
 */
public class Response {
    private int code;
    private String describe;
    private Object data;

    public Response(ResponseStatus status, Object data) {
        this.code = status.getCode();
        this.describe = status.getDescribe();
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
