package com.zzuli.form;

import lombok.Data;

import java.util.Date;

/**
 * @author 成都
 * @program MyCalculator
 * @projectName com.zzuli.form
 * @Time 2025/10/29  10:22
 * @description
 */
@Data
public class BankForm {
    private Long bankId;

    private String createdBy;

    private Date createdAt;

    private Integer isCompleted;
}
