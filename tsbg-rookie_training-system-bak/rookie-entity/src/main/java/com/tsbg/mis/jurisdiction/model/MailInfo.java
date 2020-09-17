package com.tsbg.mis.jurisdiction.model;

import java.io.File;
import java.util.Date;

public class MailInfo {

    private String fromName;//发件人自定义名称

    private String recieveName;//收件人为一人：Dear xxx，收件人为两人：Dear xxx&xxx，如果接收邮件人数>=3：Dear All

    private String recieveUserCode;

    private String[] toStrList;//收件人

    private String[] ccStrList;//抄送

    private String[] bccStrList;//密送

    private String mailSubject;//邮件主题

    private String mailContent;//邮件内容

    private String mailNDA;//保密申明

    private Date sendTime;//邮件发送时间

    private String mailBaseType;//邮件基础类型：External mail 外部邮件、Internal mail 集团内部邮件

    private String mailContentType;//邮件内容类型：活动公告、系统知会邮件、系统签核邮件、系统修改密码邮件

    private String mailFromIp;//发件人IP

    private File[] attachedFiles;//附件

    private Integer maxAttachedFilesSize;//附件总大小

    private Integer projId;//记录哪一个系统调用

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getRecieveName() {
        return recieveName;
    }

    public void setRecieveName(String recieveName) {
        this.recieveName = recieveName;
    }

    public String getRecieveUserCode() {
        return recieveUserCode;
    }

    public void setRecieveUserCode(String recieveUserCode) {
        this.recieveUserCode = recieveUserCode;
    }

    public String[] getToStrList() {
        return toStrList;
    }

    public void setToStrList(String[] toStrList) {
        this.toStrList = toStrList;
    }

    public String[] getCcStrList() {
        return ccStrList;
    }

    public void setCcStrList(String[] ccStrList) {
        this.ccStrList = ccStrList;
    }

    public String[] getBccStrList() {
        return bccStrList;
    }

    public void setBccStrList(String[] bccStrList) {
        this.bccStrList = bccStrList;
    }

    public String getMailSubject() {
        return mailSubject;
    }

    public void setMailSubject(String mailSubject) {
        this.mailSubject = mailSubject;
    }

    public String getMailContent() {
        return mailContent;
    }

    public void setMailContent(String mailContent) {
        this.mailContent = mailContent;
    }

    public String getMailNDA() {
        return mailNDA;
    }

    public void setMailNDA(String mailNDA) {
        this.mailNDA = mailNDA;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public String getMailBaseType() {
        return mailBaseType;
    }

    public void setMailBaseType(String mailBaseType) {
        this.mailBaseType = mailBaseType;
    }

    public String getMailContentType() {
        return mailContentType;
    }

    public void setMailContentType(String mailContentType) {
        this.mailContentType = mailContentType;
    }

    public String getMailFromIp() {
        return mailFromIp;
    }

    public void setMailFromIp(String mailFromIp) {
        this.mailFromIp = mailFromIp;
    }

    public File[] getAttachedFiles() {
        return attachedFiles;
    }

    public void setAttachedFiles(File[] attachedFiles) {
        this.attachedFiles = attachedFiles;
    }

    public Integer getMaxAttachedFilesSize() {
        return maxAttachedFilesSize;
    }

    public void setMaxAttachedFilesSize(Integer maxAttachedFilesSize) {
        this.maxAttachedFilesSize = maxAttachedFilesSize;
    }

    public Integer getProjId() {
        return projId;
    }

    public void setProjId(Integer projId) {
        this.projId = projId;
    }
}
