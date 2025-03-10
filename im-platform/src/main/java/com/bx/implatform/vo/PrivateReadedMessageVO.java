package com.bx.implatform.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("私聊消息VO")
public class PrivateReadedMessageVO {

    @ApiModelProperty(value = " 发送消息id")
    private Long sendMessageId;

    @ApiModelProperty(value = " 回复消息id")
    private Long answerMessageId;

    @ApiModelProperty(value = " 回复图片消息id")
    private Long answerImgMessageId;
}
