package com.tsbg.mis.serviceImpl.jurisdiction.shiro;

import com.alibaba.fastjson.JSONObject;
import com.tsbg.mis.common.RedisService;
import com.tsbg.mis.service.jurisdiction.LoginService;
import com.tsbg.mis.service.jurisdiction.UserInfoService;
import com.tsbg.mis.util.MD5Tools;
import com.tsbg.mis.util.constants.Constants;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义Realm
 */
public class UserRealm extends AuthorizingRealm {
    //private Logger logger = LoggerFactory.getLogger(UserRealm.class);

    @Autowired
    private LoginService loginService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private RedisService redisService;

    @Override
    @SuppressWarnings("unchecked")
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        JSONObject permission = redisService.getCacheObject(Constants.SESSION_USER_PERMISSION);
        //logger.info("permission的值为:" + permission);
        //logger.info("本用户权限为:" + permission.get("permissionCode"));//原先是遍历前的permissionList
        //此处需要将string转为list
        if(null != permission){
            String aStr = permission.get("permissionCode").toString();
            String newStr = aStr.substring(1, aStr.length() - 1);
            String Str = newStr.replaceAll(", ", ",").trim();
            String[] arr = Str.split(",");
            List<String> aList = new ArrayList<>();
            for (int i = 0; i < arr.length; i++) {
                aList.add(arr[i]);
            }
            //为当前用户设置角色和权限
            SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
            authorizationInfo.addStringPermissions(aList);
            return authorizationInfo;
        }
        return null;
    }

    /**
     * 验证当前登录的Subject
     * currentUser.login()方法中执行Subject.login()时 执行此方法
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        String loginName = (String) authcToken.getPrincipal();
        // 获取用户密码
        String password = new String((char[]) authcToken.getCredentials());
        //根据用户工号查询对应密码盐
        String salt = userInfoService.selectSaltByUserCode(loginName);
        String newPwd = MD5Tools.encode(password + salt);
        JSONObject user = loginService.getMyUser(loginName, newPwd);
        if (user == null || user.getString("accountName") == null) {
            //没找到帐号
            throw new UnknownAccountException();
        }
        //交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，如果觉得人家的不好可以自定义实现
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                user.getString("accountName"),
                password,
                getName()
        );
        //session中不需要保存密码
        user.remove("userPwd");
        //将用户信息放入redis中
        redisService.setCacheObject(Constants.SESSION_USER_INFO, user, 10800);
        return authenticationInfo;
    }
}
