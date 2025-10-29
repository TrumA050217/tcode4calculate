package com.zzuli.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * @TableName answer
 */
@TableName(value ="answer")
@Data
public class Answer implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long answerId;

    private Long bankId;

    private Long recordId;

    private Double myAnswer;

    private Integer isCorrect;

    private static final long serialVersionUID = 1L;
}