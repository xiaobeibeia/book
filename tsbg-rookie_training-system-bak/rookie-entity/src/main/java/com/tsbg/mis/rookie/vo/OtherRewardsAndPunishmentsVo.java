package com.tsbg.mis.rookie.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @author :张梦雅
 * @description :
 * @create :2020-07-27 18:46:00
 */
@Data
@Builder
public class OtherRewardsAndPunishmentsVo {
    public String examineType;//考核类别
    public Integer countNum;//次数
    public String content;//评分标准
    public String examineGrade;//得分

    public OtherRewardsAndPunishmentsVo() {
    }

    public OtherRewardsAndPunishmentsVo(String examineType, Integer countNum, String content, String examineGrade) {
        this.examineType = examineType;
        this.countNum = countNum;
        this.content = content;
        this.examineGrade = examineGrade;
    }

    public String getExamineType() {
        return examineType;
    }

    public void setExamineType(String examineType) {
        this.examineType = examineType;
    }

    public Integer getCountNum() {
        return countNum;
    }

    public void setCountNum(Integer countNum) {
        this.countNum = countNum;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExamineGrade() {
        return examineGrade;
    }

    public void setExamineGrade(String examineGrade) {
        this.examineGrade = examineGrade;
    }
}
