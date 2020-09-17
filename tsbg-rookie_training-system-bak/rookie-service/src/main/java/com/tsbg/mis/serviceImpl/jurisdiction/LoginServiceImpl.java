package com.tsbg.mis.serviceImpl.jurisdiction;

import com.alibaba.fastjson.JSONObject;
import com.tsbg.mis.common.RedisService;
import com.tsbg.mis.dao.rookie.SysMenuListDao;
import com.tsbg.mis.dao.rookie.SysMenuRoleDao;
import com.tsbg.mis.jurisdiction.bag.CheckCodePackage;
import com.tsbg.mis.jurisdiction.model.UserInfo;
import com.tsbg.mis.jurisdiction.vo.GetMyInfoVo;
import com.tsbg.mis.jurisdiction.vo.LoginVo;
import com.tsbg.mis.dao.jurisdiction.LoginDao;
import com.tsbg.mis.dao.jurisdiction.UserRoleDao;
import com.tsbg.mis.rookie.model.SysMenuRole;
import com.tsbg.mis.rookie.vo.SysMenuListVo;
import com.tsbg.mis.service.jurisdiction.*;
import com.tsbg.mis.serviceImpl.baseUtils.IdentifyCodeUtil;
import com.tsbg.mis.serviceImpl.baseUtils.SmsUtil;
import com.tsbg.mis.serviceImpl.jurisdiction.base.TokenAnalysis;
import com.tsbg.mis.util.MD5Tools;
import com.tsbg.mis.util.ResultUtils;
import com.tsbg.mis.util.SaltUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 登录service实现类
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private LoginDao loginDao;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private TokenListService tokenListService;
    @Value("${server.servlet.session.timeout}")
    private long sessionExpire;
    @Autowired
    private TokenAnalysis tokenAnalysis;
    @Autowired
    private UserRoleDao userRoleDao;

    @Autowired
    private SysMenuRoleDao sysMenuRoleDao;

    @Autowired
    private SysMenuListDao sysMenuListDao;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Value("${message.timeout}")
    private long timeout;

    @Override
    public ResultUtils authLogin(JSONObject jsonObject) {
        String accountName = jsonObject.getString("accountName");
        String userPwd = jsonObject.getString("userPwd");
        //根据用户工号查询用户名
        String userName = userInfoService.selectUserNameByUserCode(accountName);
        //根据UserCode查询对应的userId
        Integer userId = userInfoService.selectuidbyuserCode(accountName);
        if (userId == null) {
            return new ResultUtils(500, "未查询到该用户");
        }
        UserInfo userForBase = new UserInfo();
        userForBase.setUserId(userId);
        userForBase.setUserPwd(userPwd);
        userForBase.setAccountName(accountName);
        //添加其他所需业务逻辑
        if (accountName != null) {
            //查询工号是否存在
            Integer count = userInfoService.selectCountByUserCode(accountName);
            if (count == 0) {
                return new ResultUtils(500, "该工号未注册或不存在");
            }
            Integer status = userInfoService.selectStatusByUserCode(accountName);
            if (status == 0) {
                return new ResultUtils(500, "用户被管理员停用！");
            }
        }
        //登录时需要进行密码的判断：如果密码为工号+"123"的形式则提示用户修改密码
		/*if (userPwd!=null){
			if (userPwd.equals(accountName+"123")){
				return new ResultUtils(503,"用户密码被管理员重置需要修改密码！");
			}
		}*/
        Subject currentUser = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(accountName, userPwd);
        try {
            currentUser.login(token);
            //获取token
            String token2 = tokenService.getToken(userForBase);
            //将密码存到redis
            redisService.setCacheObject("pwd" + userId, userPwd, 10800);
            //适配统一登录 将角色和项目信息返给前台
            //List<RoleAndProJPackage> roleAndProJPackages = roleAndProJPackageService.selectRoleAndProj();
            //当前用户的项目编号返给前端
            //List<RoleAndProJPackage> roleAndProJPackages = userRoleService.selectProJMsgByUid(userId);
            ///return new ResultUtils(100,"成功登录",userCode,token2,userRoles);
            return new ResultUtils(100, "成功登录", new LoginVo(accountName, userName, token2));
        } catch (AuthenticationException e) {
            return new ResultUtils(500, "用户名或密码错误登录失败");
        }
    }

    @Override
    public JSONObject getMyUser(String accountName, String userPwd) {
        return loginDao.getMyUser(accountName, userPwd);
    }


    @Override
    public ResultUtils getMyInfo() {
        //从redis里取用户信息
        UserInfo user = tokenAnalysis.getTokenUser();
        String staffCode = user.getStaffCode();
        // 根据 staffCode 查询userImg
        if (staffCode == null) {
            return new ResultUtils(500, "该用户没有工号");
        }
        String viewPath = sysMenuRoleDao.selectUserImg(staffCode);
        List<Integer> roleIds = userRoleDao.selectRoleId(user.getUserId());
        if (roleIds == null || CollectionUtils.isEmpty(roleIds)) {
            return new ResultUtils(500, "该用户没有角色");
        }
        // 根据角色查询对应的菜单
        List<SysMenuRole> sysMenuRoleList = sysMenuRoleDao.queryAllWithRoles(roleIds);
        if (sysMenuRoleList == null || CollectionUtils.isEmpty(sysMenuRoleList)) {
            return new ResultUtils(500, "该用户没有对应的菜单");
        }
        List<Integer> menuIdList = sysMenuRoleList.stream()
                .map(SysMenuRole::getMenuId)
                .collect(Collectors.toList());
        // 对menuId去重
        if (menuIdList == null || CollectionUtils.isEmpty(menuIdList)) {
            return new ResultUtils(500, "该用户没有对应的菜单");
        }
        LinkedHashSet<Integer> set = new LinkedHashSet<>(menuIdList.size());
        set.addAll(menuIdList);
        menuIdList.clear();
        menuIdList.addAll(set);
        // 根据 menu_id 查询菜单信息
        List<SysMenuListVo> sysMenuListVos = sysMenuListDao.queryByMenuIds(menuIdList);
        if (sysMenuListVos == null || CollectionUtils.isEmpty(sysMenuListVos)) {
            return new ResultUtils(500, "该用户没有对应的菜单");
        }

        for (SysMenuListVo sysMenuListVo : sysMenuListVos) {
            // 对应父子菜单
            List<SysMenuListVo> children = sysMenuListVos.stream()
                    .filter(sysMenuVo -> {
                        if (sysMenuListVo.getMenuId() != null && sysMenuListVo.getMenuId().equals(sysMenuVo.getParentId())) {
                            return true;
                        }
                        return false;
                    })
                    .collect(Collectors.toList());
            sysMenuListVo.setChildren(children);
        }

        // 只保留一级菜单
        List<SysMenuListVo> sysMenuListVoList = sysMenuListVos.stream()
                .filter(sysMenuListVo -> {
                    if (sysMenuListVo.getParentId() != null && sysMenuListVo.getParentId().equals(0)) {
                        return true;
                    }
                    return false;
                })
                .collect(Collectors.toList());

        GetMyInfoVo myInfoVo = GetMyInfoVo.builder()
                .accountName(user.getAccountName())
                .userId(user.getUserId())
                .staffCode(user.getStaffCode())
                .userName(user.getUserName())
                .gender(user.getGender())
                .phoneNumber(user.getPhoneNumber())
                .emailAddress(user.getEmailAddress())
                .status(user.getStatus())
                .userImg(viewPath)
                .sysMenuListVos(sysMenuListVoList)
                .roleIds(roleIds)
                .build();

        return new ResultUtils(100, "查询用户角色成功", myInfoVo);
    }

    @Override
    public ResultUtils logout(HttpServletRequest req) {
        try {
            Subject currentUser = SecurityUtils.getSubject();
            String token = req.getHeader("token");
            //在token黑名单中更改token状态
            tokenListService.updateStatusByTokenCode(token);
            currentUser.logout();
            return new ResultUtils(100, "注销成功");
        } catch (Exception e) {
            return new ResultUtils(500, "注销异常");
        }
    }

    /**
     * 发送短信验证码
     *
     * @param staffCode
     * @param phoneNumber
     * @return
     */
    @Override
    public ResultUtils sendMessage(String staffCode, String phoneNumber) {
        // 校验手机号和工号是否属于同一人
        if (!checkStaffCodeAndPhone(staffCode, phoneNumber)) {
            return new ResultUtils(500, "您輸入的手機號和預設的手機號不匹配");
        }

        // 生成随机6位验证码
        String content = IdentifyCodeUtil.getRandom();

        // 存入redis
        if (stringRedisTemplate.opsForValue() == null) {
            return new ResultUtils(500, "驗證碼錯誤");
        }
        stringRedisTemplate.opsForValue().set(staffCode, content, timeout, TimeUnit.SECONDS);

        // 发送短信
        SmsUtil.SendByTemplate(phoneNumber, 5334, 1, content);
        return new ResultUtils(101, "驗證碼已發送至您的手機，請注意查收");
    }

    /**
     * 判断验证码是否正确，然后修改密码
     *
     * @param checkCodePackage
     * @return
     */
    @Override
    public ResultUtils checkIsCorrectCode(CheckCodePackage checkCodePackage) {
        String staffCode = checkCodePackage.getStaffCode();
        String identifyCode = checkCodePackage.getIdentifyCode();
        String shortPwd = checkCodePackage.getPassword();
        if (stringRedisTemplate.opsForValue() == null) {
            return new ResultUtils(500, "驗證碼錯誤");
        }
        String code = stringRedisTemplate.opsForValue().get(staffCode);
        if (code == null) {
            return new ResultUtils(500, "驗證碼已過期，請重新獲取");
        }
        if (!code.equals(identifyCode)) {
            return new ResultUtils(500, "驗證碼錯誤");
        }

        // 修改密码
        String newSalt = SaltUtil.saltGenerator();
        if (shortPwd == null) {
            return new ResultUtils(500, "驗證碼錯誤");
        }
        String newPwd = MD5Tools.encode(shortPwd.trim() + newSalt);
        // 重置密码
        int update = userInfoService.modifyPassword(newSalt, newPwd, staffCode);
        return update > 0 ? new ResultUtils(101, "成功修改密碼") : new ResultUtils(500, "密碼重置失敗");
    }

    /**
     * 校验手机号和工号是否属于同一人
     *
     * @param staffCode
     * @param phoneNumber
     * @return true -> 属于同一人 ， false -> 不属于同一人
     */
    private boolean checkStaffCodeAndPhone(String staffCode, String phoneNumber) {
        // 根据工号查询手机号
        String oldPhoneNumber = userRoleDao.selectPhoneNumberByStaffCode(staffCode);
        if (oldPhoneNumber == null) {
            return false;
        }
        return oldPhoneNumber.equals(phoneNumber) ? true : false;
    }
}
