package com.bx.implatform.vo;

import com.bx.imcommon.serializer.DateToLongSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("默认消息VO")
public class DefaultMessageVO {

    @ApiModelProperty(value = " 消息id")
    private Long id;

    @ApiModelProperty(value = " 发送内容")
    private String content;

    @ApiModelProperty(value = " 回答图片内容")
    private String answerImgContent;

    @ApiModelProperty(value = " 回答内容")
    private String answerContent;

    @ApiModelProperty(value = " 状态 0:有效 1:无效")
    private Integer status;

    @ApiModelProperty(value = " 欢迎语显示标识 0:不显示 1:显示")
    private String welcomeShowFlag;

    @ApiModelProperty(value = " 创建时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date createdTime;
}
