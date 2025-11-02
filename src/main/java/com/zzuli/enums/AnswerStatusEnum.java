package com.zzuli.enums;

import lombok.Getter;

/**
 * @author 成都
 * @program MyCalculator
 * @projectName com.zzuli.enums
 * @Time 2025/10/27  20:11
 * @description
 */
@Getter
public enum AnswerStatusEnum {
    RIGHT(1, "正确"),
    WRONG(0, "错误");

    private final Integer value;
    private final String message;

    AnswerStatusEnum(Integer value, String message) {
        this.value = value;
        this.message = message;
    }
}
