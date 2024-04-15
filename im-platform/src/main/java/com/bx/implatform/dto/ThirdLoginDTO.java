package com.bx.implatform.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@ApiModel("第三方用户登录DTO")
public class ThirdLoginDTO {

    @Max(value = 2, message = "登录终端类型取值范围:0,2")
    @Min(value = 0, message = "登录终端类型取值范围:0,2")
    @ApiModelProperty(value = "登录终端 0:web 1:app 2:pc")
    private Integer terminal;

    @ApiModelProperty(value = "第三方用户id")
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
     * 客服UserId
     */
    private Long kefuUserId;
}
