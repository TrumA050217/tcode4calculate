package com.zzuli.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * @TableName my_result
 */
@TableName(value ="my_result")
@Data
public class MyResult implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long resultId;

    private Long bankId;

    private Integer total;

    private Double accuracy;

    private Long userId;

    private static final long serialVersionUID = 1L;
}