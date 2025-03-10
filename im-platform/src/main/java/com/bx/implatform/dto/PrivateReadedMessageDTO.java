package com.bx.implatform.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("自动回复消息DTO")
public class PrivateReadedMessageDTO {

    @ApiModelProperty(value = "接收用户id")
    private Long recvId;

    @Length(max = 1024, message = "内容长度不得大于1024")
    @ApiModelProperty(value = "发送内容")
    private String content;

    @Length(max = 1024, message = "回复内容长度不得大于1024")
    @ApiModelProperty(value = "回复内容")
    private String answerContent;

    @Length(max = 1024, message = "回复图片长度不得大于1024")
    @ApiModelProperty(value = "回复图片内容")
    private String answerImgContent;

    @NotNull(message = "消息类型不可为空")
    @ApiModelProperty(value = "消息类型")
    private Integer type;
}
