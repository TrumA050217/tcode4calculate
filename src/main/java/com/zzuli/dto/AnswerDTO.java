package com.zzuli.dto;

import lombok.Data;

/**
 * @author 成都
 * @program MyCalculator
 * @projectName com.zzuli.dto
 * @Time 2025/10/27  17:28
 * @description
 */
@Data
public class AnswerDTO {
    private Long bankId;

    private Long recordId;

    private Double myAnswer;
}
