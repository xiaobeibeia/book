package com.tsbg.mis.jurisdiction.vo;

import com.tsbg.mis.rookie.vo.SysMenuListVo;
import io.swagger.annotations.ApiModel;
import lombok.*;

import java.util.Date;
import java.util.List;

/**
 * getMyInfo的Vo对象
 *
 * @author 汪永晖
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@ApiModel(description = "getMyInfo的Vo对象")
public class GetMyInfoVo {

    private Integer userId;

    private String accountName;

    private String staffCode;

    private String userName;

    private Integer gender;

    private String phoneNumber;

    private String emailAddress;

    private Integer status;

    private String userImg;

    private List<SysMenuListVo> sysMenuListVos;

    /**
     * 对应的 role_id
     */
    private List<Integer> roleIds;
}
