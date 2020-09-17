package com.tsbg.mis.rookie.bag;

import lombok.Data;

import java.util.Date;

@Data
public class UserPersonalInfoPackage {
    private Integer fileId; //上传附件自增id

    private String fileName; //上传附件名称

    private String dispName; //附件展示名称

    private String filePath; //附件存储路径

    private String viewPath; //文件预览地址

    private Integer projId; //文件关联项目编号

    private String connectTableName; //附件关联数据库表名称

    private String connectFieldsName; //附件关联表字段名称

    private String connectFieldsValue; //附件关联表字段值

    private String updateUserCode; //上传更新用户工号

    private String lastUpdateUser; //最后上传用户工号

    private Date updateDate; //上传更新时间

    private String lastDownloadUser; //最后下载用户工号

    private String keyword; //上传附件关键字

    private Integer status; //附件状态：1有效；0无效

    private String remark; //备注信息

    private String phoneNumber; //用户电话

    private String emailAddress; //用户邮箱

    private String staffCode;//用户工号

    private String staffName; //员工繁体姓名

    private String userName; //用户姓名

    private String staffSimpleName; //员工简体姓名

    private String organizationName;//组织名称

    private String email;//邮箱

    private String phoneNum;//电话

}
