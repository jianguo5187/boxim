package com.bx.implatform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>
 * 默认信息
 * </p>
 *
 * @author blue
 * @since 2022-10-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("im_default_message")
public class DefaultMessage extends Model<DefaultMessage>{

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 发送内容
     */
    @TableField("content")
    private String content;

    /**
     * 回答图片内容
     */
    @TableField("answer_img_content")
    private String answerImgContent;

    /**
     * 回答内容
     */
    @TableField("answer_content")
    private String answerContent;

    /**
     * 状态 0:有效 1:无效
     */
    @TableField("status")
    private Integer status;

    /**
     * 欢迎语显示标识 0:不显示 1:显示
     */
    @TableField("welcome_show_flag")
    private String welcomeShowFlag;

    /**
     * 创建时间
     */
    @TableField("created_time")
    private Date createdTime;
}
