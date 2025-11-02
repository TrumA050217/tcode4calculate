package com.zzuli.form;

import lombok.Data;

/**
 * @author 成都
 * @program MyCalculator
 * @projectName com.zzuli.form
 * @Time 2025/10/27  20:20
 * @description
 */
@Data
public class AnswerForm {
    private Double operandA;

    private Double operandB;

    private Integer type;

    private Double myAnswer;

    private Integer isCorrect;

    private Double correctAnswer;
}
