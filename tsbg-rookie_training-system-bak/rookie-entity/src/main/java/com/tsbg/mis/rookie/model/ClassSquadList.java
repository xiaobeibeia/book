package com.tsbg.mis.rookie.model;


import lombok.Data;

import java.util.Date;

//@Data
public class ClassSquadList {

  private Integer squadId;        //小组id
  private Integer gradeId;        //班级id
  private String squadName;       //小组名称
  private String sLeaderCode;     //小组长工号
  private String creatorCode;     //创建人工号
  private Date createDate;        //创建日期
  private String updateCode;      //更新人工号
  private Date updateDate;        //更新日期
  private Integer status;         //状态


  private Integer studentType;    //学生类别id
  private String staffName;       //学生姓名

  public String getStaffName() {
    return staffName;
  }

  public void setStaffName(String staffName) {
    this.staffName = staffName;
  }

  public Integer getStudentType() {
    return studentType;
  }

  public void setStudentType(Integer studentType) {
    this.studentType = studentType;
  }

  public Integer getSquadId() {
    return squadId;
  }

  public void setSquadId(Integer squadId) {
    this.squadId = squadId;
  }

  public Integer getGradeId() {
    return gradeId;
  }

  public void setGradeId(Integer gradeId) {
    this.gradeId = gradeId;
  }

  public String getSquadName() {
    return squadName;
  }

  public void setSquadName(String squadName) {
    this.squadName = squadName;
  }

  public String getsLeaderCode() {
    return sLeaderCode;
  }

  public void setsLeaderCode(String sLeaderCode) {
    this.sLeaderCode = sLeaderCode;
  }

  public String getCreatorCode() {
    return creatorCode;
  }

  public void setCreatorCode(String creatorCode) {
    this.creatorCode = creatorCode;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public String getUpdateCode() {
    return updateCode;
  }

  public void setUpdateCode(String updateCode) {
    this.updateCode = updateCode;
  }

  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }
}
