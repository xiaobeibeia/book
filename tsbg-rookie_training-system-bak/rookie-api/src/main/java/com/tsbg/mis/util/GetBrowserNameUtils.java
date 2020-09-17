package com.tsbg.mis.util;

public class GetBrowserNameUtils {
    /**
       * 获取浏览器版本信息
       * @Title: getBrowserName
       * @data:2015-1-12下午05:08:49
       * @author:wolf
       *
       * @param agent
       * @return
       */

    public static String getBrowserName(String agent) {
        agent = agent.toLowerCase();
        if(agent.indexOf("msie 7")>0){
            return "ie7";
        }else if(agent.indexOf("msie 8")>0){
            return "ie8";
        }else if(agent.indexOf("msie 9")>0){
            return "ie9";
        }else if(agent.indexOf("msie 10")>0){
            return "ie10";
        }else if(agent.indexOf("msie")>0){
            return "ie";
        }else if (agent.contains("rv")) {
            return "ie";
        }else if(agent.indexOf("opera")>0){
            return "opera";
        }else if(agent.indexOf("firefox")>0){
            return "firefox";
        }else if(agent.indexOf("webkit")>0) {
            return "webkit";
        }else if(agent.indexOf("edge")>0) {
            return "edge";
        }else if(agent.indexOf("safari")>0) {
            return "safari";
        }else if (agent.contains("chrome")) {
            return "chrome";
        }else{
            return "Others";
        }
    }
}
