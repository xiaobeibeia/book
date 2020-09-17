package com.tsbg.mis.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 前端跨域请求头过滤器
 */
@WebFilter(filterName = "CorsFilter ")
public class CorsFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;

        HttpServletRequest reqs = (HttpServletRequest) req;

        //默认允许Access-Control-Allow-Origin为*
//        response.setHeader("Access-Control-Allow-Origin","https://campustest.fii-foxconn.com");
        response.setHeader("Access-Control-Allow-Origin","*");

        //String token = reqs.getHeader("token");
        //System.out.println("filter origin:"+token);//通过打印，可以看到一次非简单请求，会被过滤两次，即请求两次，第一次请求确认是否符合跨域要求（预检），这一次是不带headers的自定义信息，第二次请求会携带自定义信息。
        if ("OPTIONS".equals(reqs.getMethod())){//这里通过判断请求的方法，判断此次是否是预检请求，如果是，立即返回一个204状态吗，标示，允许跨域；预检后，正式请求，这个方法参数就是我们设置的post了
            response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");//当判定为预检请求后，设定允许请求的方法
            response.setHeader("Access-Control-Allow-Headers", "Content-Type, X-Requested-With, token"); //当判定为预检请求后，设定允许请求的头部类型
            response.addHeader("Access-Control-Max-Age", "3600");  // 预检有效保持时间
        }else{
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Methods", "POST, GET, PATCH, DELETE, PUT"); //请求允许的方法
            response.setHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With, If-Modified-Since, Accept, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With, userId, token");
            response.setHeader("Access-Control-Max-Age", "3600");    //身份认证(预检)后，xxS以内发送请求不在需要预检，既可以直接跳过预检，进行请求
        }
        chain.doFilter(req, res);
    }

    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void destroy() {}

}
