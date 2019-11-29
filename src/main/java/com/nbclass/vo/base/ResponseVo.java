package com.nbclass.vo.base;
/**
 * @version V1.0
 * @date 2018年7月11日
 * @author superzheng
 */
public class ResponseVo<T> {
    private Integer code;
    private String msg;
    private T data;

    public Integer getcode() {
        return code;
    }

    public void setcode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ResponseVo(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

}
