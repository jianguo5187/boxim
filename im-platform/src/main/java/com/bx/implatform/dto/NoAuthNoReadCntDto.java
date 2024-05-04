package com.bx.implatform.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("未读消息件数DTO")
public class NoAuthNoReadCntDto {

    @ApiModelProperty(value = " 客服用户ID")
    private Long kefuUserId;

    @ApiModelProperty(value = "客户用户第三方ID")
    private String recvUserId;
}
