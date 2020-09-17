package com.tsbg.mis.controller.login;

import com.tsbg.mis.annotation.UserLoginToken;
import com.tsbg.mis.jurisdiction.model.UserInfo;
import com.tsbg.mis.service.jurisdiction.UserInfoService;
import com.tsbg.mis.serviceImpl.jurisdiction.base.TokenAnalysis;
import com.tsbg.mis.util.MD5Tools;
import com.tsbg.mis.util.ResultUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: 海波
 * @Date: 2020/4/2 9:47
 */
@RestController
@RequestMapping("/modifyPwd")
@Api(value = "门户平台-修改密码相关", tags = "门户平台-修改密码相关")
public class PwdController {
    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private TokenAnalysis tokenAnalysis;

    /**
     *判断原密码是否正确
     */
    @ApiOperation(value="校验原密码是否正确", notes="校验原密码是否正确")
    @ApiImplicitParam(name = "userPwd", value = "传递参数为：userPwd", required = true)
    @RequestMapping(value="/premodifyPwd", method= RequestMethod.POST)
    @UserLoginToken
    @ResponseBody
    public ResultUtils preModifyPwd(String userPwd){
        //通过从redis获取accountName
        String accountName = tokenAnalysis.getTokenUser().getAccountName();
        //根据accountName用户名 查询对应密码盐
        String salt = userInfoService.selectSaltByUserCode(accountName);
        String newPwd = MD5Tools.encode(userPwd+salt);
        if(accountName!=null && userPwd!=null){
            //调用查询逻辑查找用户是否存在
            int num = userInfoService.judgeIfExistUserByUserPwd(accountName,newPwd);
            if (num==1){
                return new ResultUtils(100,"此用户存在且可以修改密码");
            }
            return new ResultUtils(501,"原密码错误");
        }
        return new ResultUtils(501,"请输入原密码");
    }

    /**
     * 修改密码
     */
    @ApiOperation(value = "修改密码", notes = "通过工号和新密码修改密码")
    @ApiImplicitParam(name = "userPwd", value = "传递参数为：userPwd", required = true)
    @RequestMapping(value="/modifyPassword", method=RequestMethod.POST)
    @UserLoginToken
    @ResponseBody
    public ResultUtils modifyPassword(String userPwd) {
        //获取输入的新密码和账号
        String accountName = tokenAnalysis.getTokenUser().getAccountName();
        //重新生成salt
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<str.length();i++){
            int nu=random.nextInt(6);
            sb.append(str.charAt(nu));
        }
        userInfoService.resetUserSalt(sb.toString(),accountName);
        //加密密码
        String newPwd = MD5Tools.encode(userPwd+sb.toString());
        if (accountName != null && userPwd != null) {
            //修改成功将uid传给页面
            int num = userInfoService.modifyPasswordByUsername(newPwd, accountName);
            if (num>0){
                UserInfo userInfo1 = userInfoService.selectByUserCode(accountName);
                return new ResultUtils(101, "修改成功！", userInfo1);
            }
            return new ResultUtils(500,"修改失败！");
        }
        return new ResultUtils(500,"密码不能为空");
    }

    /**
     * 登录成功后校验密码规范
     */
    @ApiOperation(value = "登录成功后校验密码规范", notes = "")
    @ApiImplicitParam(name = "userInfo", value = "传递参数为：userPwd", required = true, dataType = "UserInfo")
    @RequestMapping(value="/checkpwd", method=RequestMethod.POST)
    @UserLoginToken
    @ResponseBody
    public ResultUtils check(@RequestBody UserInfo userInfo){
        String pwd = userInfo.getUserPwd();
        Pattern pattern = Pattern.compile("[a-zA-Z]*");
        Matcher isNum = pattern.matcher(pwd);
        Pattern patternSe = Pattern.compile("[0-9]*");
        Matcher isNum2 = patternSe.matcher(pwd);
        //判斷密碼是否符合規範
        if (isNum.matches()){
            return new ResultUtils(500,"不符合密碼規範（不能全是字母）");
        }
        if (isNum2.matches()){
            return new ResultUtils(501,"不符合密碼規範（不能全是數字）");
        }
        if (pwd.length()<8 || pwd.length()>16){
            return new ResultUtils(501,"不符合密碼規範（8-16位）");
        }
        return new ResultUtils(0,"密碼符合規範無需提示");
    }

}
