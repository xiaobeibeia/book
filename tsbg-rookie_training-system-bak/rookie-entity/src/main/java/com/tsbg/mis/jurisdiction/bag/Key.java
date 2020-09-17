package com.tsbg.mis.jurisdiction.bag;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author 汪永晖
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Key {

    private String userName;
    private String staffCode;
    private String factoryName;
    private String unitName;
    private String departName;
    private String organizationName;
    private Integer roleId;
}
