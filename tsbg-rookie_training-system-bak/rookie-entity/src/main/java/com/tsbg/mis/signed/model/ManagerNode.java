package com.tsbg.mis.signed.model;

import io.swagger.annotations.ApiModel;
import lombok.*;

import java.io.Serializable;

/**
 * 签核主管与节点对应表，用于存储具有对应节点签核权限的主管信息(ManagerNode)实体类
 *
 * @author makejava
 * @since 2020-06-08 14:18:54
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@ApiModel(description = "签核主管与节点对应表")
public class ManagerNode implements Serializable {
    private static final long serialVersionUID = -54573846647364175L;
    /**
    * 签核主管与签核节点中间表编号id
    */
    private Integer manaNodeId;
    /**
    * 签核节点编号id，从signed_node表中获取
    */
    private Integer signNodeId;
    /**
    * 对应的事务id
    */
    private Integer businessId;
    /**
    * 签核节点名称 
    */
    private String nodeName;
    /**
    * 签核主管工号
    */
    private String managerCode;
    /**
    * 签核主管姓名
    */
    private String managerName;
    /**
    * 签核主管邮箱地址
    */
    private String managerMailAddress;
    /**
    * 对应项目系统id,从project表中获取
    */
    private Integer projId;
    /**
    * 该主管是否还有该节点签核权限：1是；0否
    */
    private Integer status;

}