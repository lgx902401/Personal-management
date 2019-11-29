package com.nbclass.util;
/**
 * @version V1.0
 * @date 2018年7月11日
 * @author superzheng
 */
public class PageUtil {
    public static Integer getPageNo(Integer limit,Integer offset){
        if (offset == 0) return 1;
        else return offset / limit + 1;
    }
}
