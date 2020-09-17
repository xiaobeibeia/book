package com.tsbg.mis.jurisdiction.vo;

import com.tsbg.mis.jurisdiction.bag.RoleAndProJPackage;

import java.util.List;

public class LoginVo {

    private String accountName;

    private String userName;

    private String token;

    List<RoleAndProJPackage> userRoles;

    public LoginVo() {
    }

    public LoginVo(String accountName, String userName, String token/*, List<RoleAndProJPackage> userRoles*/) {
        this.accountName = accountName;
        this.userName = userName;
        this.token = token;
        //this.userRoles = userRoles;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<RoleAndProJPackage> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(List<RoleAndProJPackage> userRoles) {
        this.userRoles = userRoles;
    }
}
