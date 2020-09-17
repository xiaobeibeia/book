package com.tsbg.mis.rookie.model;


import lombok.Data;

import java.util.Date;

@Data
public class ClassGradeInfo {

  private Integer gradeId;         //班級id
  private String gradeName;        //班級名稱
  private String classPeriod;      //屆別名稱
  private String creatorCode;      //創建人工號
  private Date createDate;         //創建日期
  private String updateCode;       //更新人工號
  private Date updateDate;         //更新日期
  private Integer status;          //狀態

}
