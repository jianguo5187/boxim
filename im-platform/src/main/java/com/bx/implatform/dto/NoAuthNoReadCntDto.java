package com.bx.implatform.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("未读消息件数DTO")
public class NoAuthNoReadCntDto {

    @ApiModelProperty(value = " 发送者用户名")
    private String sendUserName;

    @ApiModelProperty(value = " 接收者用户名")
    private String recvUserName;
}
