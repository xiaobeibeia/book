package com.tsbg.mis.jurisdiction.bag;

import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.validation.constraints.NotBlank;

/**
 * @author 汪永晖
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@ApiModel(description = "忘记密码前端传输类")
public class CheckCodePackage {

    @NotBlank(message = "工号不能为空")
    private String staffCode;

    @NotBlank(message = "手机号不能为空")
    private String phoneNumber;

    @NotBlank(message = "验证码不能为空")
    private String identifyCode;

    @NotBlank(message = "密码不能为空")
    private String password;
}
