package com.tsbg.mis.serviceImpl.jurisdiction.base;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.tsbg.mis.annotation.PassToken;
import com.tsbg.mis.annotation.UserLoginToken;
import com.tsbg.mis.common.RedisService;
import com.tsbg.mis.jurisdiction.model.UserInfo;
import com.tsbg.mis.service.jurisdiction.TokenListService;
import com.tsbg.mis.service.jurisdiction.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * token拦截器
 */
public class AuthenticationInterceptor extends BaseInterceptor implements HandlerInterceptor {
    private final static String TOKEN_IS_INVALID= "TokenIsInvalid";
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private TokenListService tokenListService;
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
        String token = httpServletRequest.getHeader("token");// 从 http 请求头中取出 token
        // 如果不是映射到方法直接通过
        if(!(object instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod=(HandlerMethod)object;
        Method method=handlerMethod.getMethod();
        //检查是否有PassToken注释，有则跳过认证
        if (null != method && method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (null != passToken && passToken.required()) {
                return true;
            }
        }
        //检查有没有需要用户权限的注解
        if (null != method && method.isAnnotationPresent(UserLoginToken.class)) {
            UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
            if (userLoginToken.required()) {
                // 执行认证
                if (token == null) {
                    //httpServletRequest.setAttribute(TOKEN_IS_INVALID,1);
                    //throw new RuntimeException("无token，请重新登录");
                    setResponse(httpServletRequest, httpServletResponse,"401","无token，请重新登录");
                    return false;
                }
                int i1 = tokenListService.selectCountFromTokenList(token);
                if (i1>0){
                    /*httpServletRequest.setAttribute(TOKEN_IS_INVALID,1);
                    throw new RuntimeException("token已失效，请重新登录");*/
                    setResponse(httpServletRequest, httpServletResponse,"401","token已失效，请重新登录");
                    return false;
                }
                // 获取 token 中的 user id
                String userId;
                try {
                    userId = JWT.decode(token).getAudience().get(0);
                } catch (JWTDecodeException j) {
                    //httpServletRequest.setAttribute(TOKEN_IS_INVALID,1);
                    //throw new RuntimeException("token中的用户信息已失效，请重新登录");
                    setResponse(httpServletRequest, httpServletResponse,"401","token中的用户信息已失效，请重新登录");
                    return false;
                }
                int i = userInfoService.selectIfExistThisUser(Integer.parseInt(userId));
                if (i == 0) {
                    httpServletRequest.setAttribute(TOKEN_IS_INVALID,1);
                    throw new RuntimeException("用户不存在，请重新登录");
                }
                try {
                    // 验证 token
                    if(null != redisService.getCacheObject("pwd"+userId) && null != redisService.getCacheObject("pwd"+userId).toString()){
                        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(redisService.getCacheObject("pwd"+userId).toString())).build();
                        jwtVerifier.verify(token);
                    }
                }  catch (JWTVerificationException e) {
                    httpServletRequest.setAttribute(TOKEN_IS_INVALID,1);
                    throw new RuntimeException("token验证失效，请重新登录");
                }  catch(Exception e){
                    httpServletRequest.setAttribute(TOKEN_IS_INVALID,1);
                    throw new RuntimeException("密码和token失效，请重新登录");
                }
                //根据userId查询用户工号和姓名
                UserInfo userInfo = userInfoService.selectUserMsgbyUserId(userId);
                //将用户编号存进httpServletRequest
                httpServletRequest.setAttribute("userInfo",userInfo);
                return true;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
