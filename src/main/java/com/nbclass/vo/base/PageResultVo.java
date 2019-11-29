package com.nbclass.vo.base;

import java.util.List;

/**
 * @author superzheng
 * @version V1.0
 * @date 2018年7月11日
 */
public class PageResultVo {
    private Integer code;
    private String msg;
    private List rows;
    private Long total;

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public PageResultVo(Integer code, String msg, List rows, Long total) {
        this.code = code;
        this.msg = msg;
        this.total = total;
        this.rows = rows;
    }


    public PageResultVo(List rows, Long total) {
        this.total = total;
        this.rows = rows;
    }

    public PageResultVo(String msg, List rows, Long total) {
        this.msg = msg;
        this.total = total;
        this.rows = rows;
    }

    public PageResultVo(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public PageResultVo(String msg) {
        this.msg = msg;
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
