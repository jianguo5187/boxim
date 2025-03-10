package com.bx.implatform.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

@Data
@ApiModel("自动回复消息DTO")
public class DefaultMessageDTO {

    @ApiModelProperty(value = "自动回复信息id")
    private Long id;

    @Length(max = 1024, message = "问题不得大于1024")
    @NotEmpty(message = "问题不可为空")
    @ApiModelProperty(value = "问题内容")
    private String content;

    @Length(max = 1024, message = "回答内容不得大于1024")
    @ApiModelProperty(value = "回答内容")
    private String answerContent;

    @ApiModelProperty(value = "图片内容")
    private String answerImgContent;

    @ApiModelProperty(value = "状态")
    private String welcomeShowFlag;
}
