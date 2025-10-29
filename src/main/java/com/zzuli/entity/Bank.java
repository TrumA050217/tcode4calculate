package com.zzuli.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName bank
 */
@TableName(value ="bank")
@Data
public class Bank implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long bankId;

    private Long createdBy;

    private Date createdAt;

    private static final long serialVersionUID = 1L;
}