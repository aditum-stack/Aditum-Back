package com.ten.aditum.back.model;

/**
 * [状态码, 状态信息]
 */
public enum AditumCode {
    /**
     * @param 0 success
     * @param 100 failure
     */
    OK(0, "成功"),
    ERROR(100, "失败");

    private final int code;
    private final String msg;

    AditumCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "AditumCode{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
