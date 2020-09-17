package com.tsbg.mis.rookie.model;

import lombok.Data;

import java.util.Date;

@Data
public class FileInfo {
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
}
