package com.zzuli.controller;

import com.zzuli.annotation.Check;
import com.zzuli.form.AnswerForm;
import com.zzuli.form.MyResultForm;
import com.zzuli.service.AnswerService;
import com.zzuli.service.RecordService;
import com.zzuli.util.AuthContextHolder;
import com.zzuli.util.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 成都
 * @program MyCalculator
 * @projectName com.zzuli.controller
 * @Time 2025/10/31  12:28
 * @description
 */
@Tag(name = "用户答案接口", description = "提供答卷答案的提交、查询等功能")
@RestController
@RequestMapping("/result")
@CrossOrigin
@SecurityRequirement(name = "Authorization")
public class MyResultController {

    @Autowired
    private RecordService recordService;

    @Autowired
    private AnswerService answerService;

    @Operation(summary = "查询用户作答结果")
    @GetMapping("/get")
    @Check
    public Result<List<AnswerForm>> result(@RequestParam Long bankId) {
        List<AnswerForm> answers = answerService.getResult(bankId);
        return Result.ok(answers);
    }

    @Operation(summary = "查询用户历史作答记录")
    @GetMapping("/all")
    @Check
    public Result<List<AnswerForm>> getAll() {
        Long userId = AuthContextHolder.getUserId();
        List<AnswerForm> recordForms = recordService.getAll(userId);
        return Result.ok(recordForms);
    }

    @Operation(summary = "查询用户作答总数量和正确率")
    @GetMapping("/accuracy")
    @Check
    public Result<List<MyResultForm>> getResultAccuracy(@RequestParam Long bankId) {
        List<MyResultForm> answers = answerService.getResultAccuracy(bankId);
        return Result.ok(answers);
    }

}
