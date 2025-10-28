package com.zzuli.enums;

import lombok.Getter;


/**
 * @author 成都
 * @program MyCalculator
 * @projectName enums
 * @Time 2025/10/27  10:25
 * @description
 */
@Getter
public enum TypeEnum {
    ADD("加法",0),
    SUB("减法",1),
    MUL("乘法",2),
    DIV("除法",3),
    MIX("混合",4);

    private final String name;
    private final int value;

    TypeEnum(String name, int value) {
        this.name = name;
        this.value = value;
    }
}
