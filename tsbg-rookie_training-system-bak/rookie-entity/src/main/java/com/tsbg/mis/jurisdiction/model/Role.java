package com.tsbg.mis.jurisdiction.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Role {
    private Integer roleId;

    private String roleName;

    private String roleDescribe;

    private Integer projId;

    private Integer status;

    private String remark;

}