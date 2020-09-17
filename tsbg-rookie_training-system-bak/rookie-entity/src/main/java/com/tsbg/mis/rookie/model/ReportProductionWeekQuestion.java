package com.tsbg.mis.rookie.model;

import lombok.Data;

@Data
public class ReportProductionWeekQuestion {
    private Integer questionId;

    private Integer reportId;

    private String questionDescription;

    private String questionSuggestion;

    private Integer status;
}
