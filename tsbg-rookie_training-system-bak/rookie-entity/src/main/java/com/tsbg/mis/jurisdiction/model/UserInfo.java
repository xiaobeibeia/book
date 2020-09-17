package com.tsbg.mis.jurisdiction.model;

import lombok.Data;

import java.util.Date;


@Data
public class UserInfo {
    private Integer userId;

    private String accountName;

    private String staffCode;

    private String userName;

    private String userPwd;

    private String userPwd2;

    private String salt;

    private Integer isStaff;

    private Integer gender;

    private String majorName;

    private String phoneNumber;

    private String emailAddress;

    private String userExt;

    private String graduateSchool;

    private String identityNum;

    private Integer status;

    private String userImg;

    private Date createTime;
}