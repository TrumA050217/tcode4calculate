package com.zzuli.controller;

import com.zzuli.annotation.Check;
import com.zzuli.form.MistakeForm;
import com.zzuli.service.RecordService;
import com.zzuli.util.AuthContextHolder;
import com.zzuli.util.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 成都
 * @program MyCalculator
 * @projectName com.zzuli.controller
 * @Time 2025/10/31  12:25
 * @description
 */
@Tag(name = "错题本接口", description = "提供错题本查询等功能")
@RestController
@RequestMapping("/mistake")
@CrossOrigin
@SecurityRequirement(name = "Authorization")
public class MistakeController {

    @Autowired
    private RecordService recordService;

    @Operation(summary = "查询用户错题本")
    @GetMapping("/get")
    @Check
    public Result<List<MistakeForm>> getWrong() {
        Long userId = AuthContextHolder.getUserId();
        List<MistakeForm> recordForms = recordService.getWrong(userId);
        return Result.ok(recordForms);
    }

    @Operation(summary = "统计错题总数")
    @GetMapping("/count")
    @Check
    public Result<Long> getWrongCount() {
        Long userId = AuthContextHolder.getUserId();
        Long count = recordService.getWrongCount(userId);
        return Result.ok(count);
    }

}
