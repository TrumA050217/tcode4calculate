package com.zzuli.controller;

import com.zzuli.enums.ResultCodeEnum;
import com.zzuli.form.UserForm;
import com.zzuli.service.UserService;
import com.zzuli.service.WxUserService;
import com.zzuli.util.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 成都
 * @program MyCalculator
 * @projectName com.zzuli.controller
 * @Time 2025/10/27  18:54
 * @description
 */
@Tag(name = "用户模块", description = "用户注册或登陆")
@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private WxUserService wxUserService;

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result<String> login(@RequestBody UserForm userForm) {
        String token = userService.login(userForm);
        if (token == null) return Result.fail(ResultCodeEnum.ILLEGAL_REQUEST.getMessage());
        return Result.ok(token);
    }

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public Result<Boolean> register(@RequestBody UserForm userForm) {
        return Result.ok(userService.register(userForm));
    }

    @Operation(summary = "小程序授权登录")
    @GetMapping("/login/{code}")
    public Result<String> login(@PathVariable String code) {
        return Result.ok(wxUserService.login(code));
    }

}
