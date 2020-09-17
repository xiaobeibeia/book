package com.tsbg.mis.serviceImpl.jurisdiction.base;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.tsbg.mis.annotation.RequiredPerms;
import com.tsbg.mis.service.jurisdiction.PermRoleService;
import com.tsbg.mis.service.jurisdiction.PermService;
import com.tsbg.mis.service.jurisdiction.UserInfoService;
import com.tsbg.mis.service.jurisdiction.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 权限拦截器
 */
public class PermissionInterceptor implements HandlerInterceptor {
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private PermRoleService permRoleService;
    @Autowired
    private PermService permService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
        String token = httpServletRequest.getHeader("token");// 从 http 请求头中取出 token
        // 如果不是映射到方法直接通过
        if(!(object instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod=(HandlerMethod)object;
        Method method=handlerMethod.getMethod();
        //检查有没有需要用户权限的注解
        if (null != method && method.isAnnotationPresent(RequiredPerms.class)) {
            RequiredPerms requiredPerms = method.getAnnotation(RequiredPerms.class);
            String perms = requiredPerms.toString();
            String[] permList = {};
            if (perms.length() > 0) {
                int index = perms.indexOf("[");
                int index2 = perms.indexOf("]");
                if(index != -1 && index2 != -1){
                    String power = perms.substring(index + 1, index2);
                    permList = power.split(",");
                }
            }

            // 获取 token 中的 user id
            String userId;
            try {
                userId = JWT.decode(token).getAudience().get(0);
            } catch (JWTDecodeException j) {
                throw new RuntimeException("token中的用户信息已失效");
            }
            //根据userId查询用户当前所有权限
            List<Integer> role = userRoleService.getRole(Integer.parseInt(userId));
            if (role == null) {
                throw new RuntimeException("该用户角色信息不全请重新登录！");
            }
            List<Integer> list = permRoleService.selectPowerListByRoleList(role);
            if (list == null) {
                throw new RuntimeException("该用户尚未拥有权限！");
            }
            List<String> powerDetailList = permService.selectPowerDetailByPowerList(list);
            if (powerDetailList == null) {
                throw new RuntimeException("该用户尚未拥有权限！");
            }
            //将生态系统的权限加入(生态权限独特需要单独加)
            String s = userInfoService.selectPermListByUseId(Integer.parseInt(userId));
            if (s!=null){
                String newPower = s.substring(1, s.length() - 1);
                String s2 = newPower.replaceAll(", ", ",").trim();
                String[] arr = s2.split(",");
                for (int i = 0; i < arr.length; i++) {
                    powerDetailList.add(arr[i]);
                }
            }
            //将固资系统的权限加入（固资权限独特需要单独加）
            String s1 = userInfoService.selectPermListByUseId2(Integer.parseInt(userId));
            if (s1!=null){
                String newPower2 = s1.substring(1, s1.length() - 1);
                String s3 = newPower2.replaceAll(", ", ",").trim();
                String[] arr2 = s3.split(",");
                for (int j = 0; j < arr2.length; j++) {
                    powerDetailList.add(arr2[j]);
                }
            }
            //权限标志
            Boolean powerFlag = true;
            for (int i = 0; i < permList.length; i++) {
                if (!powerDetailList.contains(permList[i].trim())) {
                    powerFlag = false;
                }
            }
            if (!powerFlag) {
                httpServletRequest.setAttribute("PermIsNotEnough",1);
                throw new RuntimeException("权限不足！");
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
