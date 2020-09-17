package com.tsbg.mis.signed.model;

import io.swagger.annotations.ApiModel;
import lombok.*;

import java.io.Serializable;

/**
 * 签核节点信息表，用于存储定义出来的所有用于签核的节点信息(SignedNode)实体类
 *
 * @author makejava
 * @since 2020-06-08 14:18:54
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@ApiModel(description = "签核节点信息表")
public class SignedNode implements Serializable {
    private static final long serialVersionUID = -29707318133496223L;
    /**
    * 签核节点编号id
    */
    private Integer signNodeId;
    /**
    * 签核节点名称
    */
    private String nodeName;
    /**
    * 对应的签核事务id
    */
    private Integer businessId;
    /**
    * 该节点是否停用：1是；0否
    */
    private Integer isStop;

}