package com.sharedschool.backend.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseCode {

    // 定义一个枚举类型，包含两个枚举常量：SUCCESS和ERROR
    SUCCESS(200, "操作成功！"),
    ERROR(500, "槽糕，出错了，请稍后再试！");
    // 定义一个整型变量code，用于存储枚举常量的值
    private int code;
    // 定义一个字符串变量message，用于存储枚举常量的描述信息
    private String message;
}
