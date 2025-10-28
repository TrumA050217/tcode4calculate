package com.zzuli.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * @TableName wx_user
 */
@TableName(value ="wx_user")
@Data
public class WxUser implements Serializable {
    private Long wxUserId;

    private String openid;

    private static final long serialVersionUID = 1L;
}