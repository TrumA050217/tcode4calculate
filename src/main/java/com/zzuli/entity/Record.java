package com.zzuli.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * @TableName record
 */
@TableName(value ="record")
@Data
public class Record implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long recordId;

    private Long bankId;

    private Integer operandA;

    private Integer operandB;

    private Integer type;

    private Double correctAnswer;

    private static final long serialVersionUID = 1L;
}