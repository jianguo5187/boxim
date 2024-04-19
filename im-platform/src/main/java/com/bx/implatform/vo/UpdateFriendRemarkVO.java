package com.bx.implatform.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class UpdateFriendRemarkVO {


    /**
     * 好友id
     */
    @TableField("friend_id")
    private Long friendId;

    /**
     * 备注
     */
    @TableField("remark_name")
    private String remarkName;
}
