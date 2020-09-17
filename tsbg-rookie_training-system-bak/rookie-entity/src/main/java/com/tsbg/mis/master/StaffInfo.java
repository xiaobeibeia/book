package com.tsbg.mis.master;

import lombok.Data;

import java.util.Date;

@Data
public class StaffInfo {
    private Integer staffId;

    private String staffCode;

    private String staffName;

    private String staffSimpleName;

    private Integer gender;

    private Integer factoryId;

    private String factoryName;

    private String firstBgName;

    private String secondBgName;

    private Integer bgId;

    private String bgName;

    private String organizationName;

    private String organizationCode;

    private Integer unitId;

    private String unitName;

    private Integer BUId;

    private String BUName;

    private Integer departId;

    private String departName;

    private Integer classId;

    private String className;

    private Integer groupId;

    private String groupName;

    private String costCode;

    private Integer legalPersonId;

    private String legalPersonName;

    private Integer staffTypeId;

    private String email;

    private String contactNum;

    private Integer workStatusId;

    private String schoolType;

    private String graduateSchool;

    private Date graduateDate;

    private Date joinDate;

    private String phoneNum;

    private String education;

    private Integer status;

    private String classPeriod;
}
