package com.bx.implatform.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("第三方用户登录VO")
public class ThirdLoginVO {

    @ApiModelProperty(value = "每次请求都必须在header中携带accessToken")
    private String accessToken;

    @ApiModelProperty(value = "accessToken过期时间(秒)")
    private Integer accessTokenExpiresIn;

    @ApiModelProperty(value = "accessToken过期后，通过refreshToken换取新的token")
    private String refreshToken;

    @ApiModelProperty(value = "refreshToken过期时间(秒)")
    private Integer refreshTokenExpiresIn;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "客服用户信息")
    private UserVO kefuUserInfo;
}
