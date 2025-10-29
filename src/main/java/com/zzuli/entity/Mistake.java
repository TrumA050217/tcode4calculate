package com.zzuli.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * @TableName mistake
 */
@TableName(value ="mistake")
@Data
public class Mistake implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long mistakeId;

    private Long bankId;

    private Long recordId;

    private Double operA;

    private Double operB;

    private Integer mistakeType;

    private Long userId;

    private static final long serialVersionUID = 1L;
}