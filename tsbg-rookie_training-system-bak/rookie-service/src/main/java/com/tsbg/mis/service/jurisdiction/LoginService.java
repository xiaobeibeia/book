package com.tsbg.mis.service.jurisdiction;

import com.alibaba.fastjson.JSONObject;
import com.tsbg.mis.jurisdiction.bag.CheckCodePackage;
import com.tsbg.mis.util.ResultUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 登录Service
 */
public interface LoginService {
    /**
     * 登录表单提交
     */
    ResultUtils authLogin(JSONObject jsonObject);

    /**
     * 根据工号和密码查询对应的用户
     *
     * @param accountName 工号
     * @param userPwd     密码
     */
    JSONObject getMyUser(String accountName, String userPwd);

    /**
     * 根据项目编号来返回权限信息
     */
    ResultUtils getMyInfo();

    /**
     * 退出登录
     */
    ResultUtils logout(HttpServletRequest req);

    /**
     * 发送短信验证码
     *
     * @param staffCode
     * @param phoneNumber
     * @return
     */
    ResultUtils sendMessage(String staffCode, String phoneNumber);

    /**
     * 判断验证码是否正确，然后修改密码
     *
     * @param checkCodePackage
     * @return
     */
    ResultUtils checkIsCorrectCode(CheckCodePackage checkCodePackage);
}
