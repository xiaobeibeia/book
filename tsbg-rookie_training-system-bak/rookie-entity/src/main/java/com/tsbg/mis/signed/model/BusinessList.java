package com.tsbg.mis.signed.model;

import io.swagger.annotations.ApiModel;
import lombok.*;

import java.io.Serializable;

/**
 * 签核事务列表，用于存储需要签核的事务的详细信息，每种签核流程对应一个id(BusinessList)实体类
 *
 * @author makejava
 * @since 2020-06-08 14:18:50
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@ApiModel(description = "签核事务列表")
public class BusinessList implements Serializable {
    private static final long serialVersionUID = -42100866494287881L;
    /**
    * 需要签核的事物id
    */
    private Integer businessId;
    /**
    * 签核类型编号id
    */
    private Integer signTypeId;
    /**
    * 签核事务名称/描述
    */
    private String businessName;
    /**
    * 事务对应项目系统编号id
    */
    private Integer projId;
    /**
    * 事务对应项目系统名称
    */
    private String proName;
    /**
    * 该事物是否在用：1是；0否
    */
    private Integer status;

}