package com.bx.implatform.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

@Data
@ApiModel("App用户注册DTO")
public class AppRegisterDTO {
    private String thirdUserId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 个性签名
     */
    private String signature;

    /**
     * 用户IP
     */
    private String userIp;
}
