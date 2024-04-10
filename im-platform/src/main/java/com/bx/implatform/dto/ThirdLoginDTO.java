package com.bx.implatform.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("第三方用户登录DTO")
public class ThirdLoginDTO {

    @Max(value = 2, message = "登录终端类型取值范围:0,2")
    @Min(value = 0, message = "登录终端类型取值范围:0,2")
    @ApiModelProperty(value = "登录终端 0:web 1:app 2:pc")
    private Integer terminal;

    @ApiModelProperty(value = "第三方用户id")
    private Long thirdUserId;
}
