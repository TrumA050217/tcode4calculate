package com.zzuli.enums;

import lombok.Getter;

/**
 * 统一返回结果状态信息类
 *
 */
@Getter
public enum ResultCodeEnum {
    SUCCESS(200,"成功"),
    FAIL(201, "失败"),
    SERVICE_ERROR(202, "服务异常"),
    USER_NOT_EXIST(203, "用户不存在"),
    DATA_ERROR(204, "数据异常"),
    ILLEGAL_REQUEST(205, "非法请求"),
    REPEAT_SUBMIT(206, "重复提交"),
    UPDATE_ERROR(207, "数据更新失败"),

    ARGUMENT_VALID_ERROR(210, "参数校验异常"),
    NO_RESULT(211, "无作答结果"),
    SIGN_ERROR(300, "签名错误"),
    SIGN_OVERDUE(301, "签名已过期"),
    VERIFICATION_CODE_ERROR(218 , "验证码错误"),

    LOGIN_AUTH(208, "未登陆"),
    PERMISSION(209, "没有权限"),
    ACCOUNT_ERROR(214, "账号不正确"),
    PASSWORD_ERROR(215, "密码不正确"),
    PHONE_CODE_ERROR(216, "手机验证码不正确"),
    LOGIN_MOBILE_ERROR( 217, "账号不正确"),
    ACCOUNT_STOP( 218, "账号已停用"),

    IMAGE_AUDITION_FAIL( 219, "图片审核不通过"),
    FACE_ERROR( 250, "未进行人脸识别"),
    NO_SUCH_QUESTIONNAIRE( 251, "问卷不存在"),
    QUESTIONNAIRE_NOT_PUBLIC( 252, "问卷未发布"),
    QUESTIONNAIRE_END( 253, "问卷已结束"),
    OPTION_IS_NOT_EXIST( 254, "选项不存在"),
    EMPTY_ANSWER( 255, "有题目未作答！"),
    SUBMIT_MORE_THAN_ONE_QUESTIONNAIRE( 256, "不能重复提交问卷"),
    SUBMIT_MORE_THAN_ONE_VOTE( 257, "不能重复提交投票");



    private final Integer code;

    private final String message;

    ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
