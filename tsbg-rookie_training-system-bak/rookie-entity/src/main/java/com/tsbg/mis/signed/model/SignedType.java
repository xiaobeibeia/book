package com.tsbg.mis.signed.model;

import io.swagger.annotations.ApiModel;
import lombok.*;

import java.io.Serializable;

/**
 * 签核类型信息表，用于存储各种不同的签核类型详细信息(SignedType)实体类
 *
 * @author makejava
 * @since 2020-06-08 14:18:54
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@ApiModel(description = "签核类型信息表")
public class SignedType implements Serializable {
    private static final long serialVersionUID = 269130333024377956L;
    /**
    * 签核类型编号id
    */
    private Integer signTypeId;
    /**
    * 签核类型名称
    */
    private String signTypeName;
    /**
    * 该签核类型是否停用：1是；0否
    */
    private Integer isStop;

}