package com.tsbg.mis.util;

/**
 * 前后端分离信息返回类
 */
public class ResultUtils {

    private static final int SUCCESS = 100;
    //失败时自定义 Code
    private static final int FAILED = 500;

    private int code; //返回的状态码
    private String message; //返回的信息详情
    private Object data; //返回的对象数据
    private Object dataSecond;//返回的第二个对象
    private Object dataThird;//返回的第三个对象

    public ResultUtils() {
    }

    /**
     * 构造注入（无需返回对象时使用）
     */
    public ResultUtils(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 构造方法重载(多个对象类型相同，可以封装到一个VO中返回)
     */
    public ResultUtils(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 构造方法重载
     * 有两个对象传递时使用(多个对象类型不同，可能是两种VO)
     */
    public ResultUtils(int code, String message, Object data, Object dataSecond) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.dataSecond = dataSecond;
    }

    /**
     * 构造方法重载
     * 有三个对象传递时使用(多个对象类型不同,可能是三种VO)
     */
    public ResultUtils(int code, String message, Object data, Object dataSecond, Object dataThird) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.dataSecond = dataSecond;
        this.dataThird = dataThird;
    }

    /**
     * 设值注入
     */
    public static int getSUCCESS() {
        return SUCCESS;
    }

    public static int getFAILED() {
        return FAILED;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getDataSecond() {
        return dataSecond;
    }

    public void setDataSecond(Object dataSecond) {
        this.dataSecond = dataSecond;
    }

    public Object getDataThird() {
        return dataThird;
    }

    public void setDataThird(Object dataThird) {
        this.dataThird = dataThird;
    }
}
