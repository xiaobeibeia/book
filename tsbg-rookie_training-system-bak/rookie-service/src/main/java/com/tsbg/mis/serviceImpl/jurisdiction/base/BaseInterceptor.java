package com.tsbg.mis.serviceImpl.jurisdiction.base;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

/**
 * @Author 海波
 **/
@Slf4j
public class BaseInterceptor extends HandlerInterceptorAdapter {

    public void setResponse(HttpServletRequest request,
                            HttpServletResponse response, String code, String message) {

        response.setContentType("application/json;charset=UTF-8");
        try (Writer writer = response.getWriter()) {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("code", code);
            resultMap.put("message", message);

            JSON.writeJSONString(writer, resultMap);
            writer.flush();
        } catch (IOException e) {
            log.error("response 设置操作异常：" + e);
        }
    }


    public void setResponse(HttpServletRequest request,
                            HttpServletResponse response, String code) {
        setResponse(request,response,code,"OK");

    }
}
