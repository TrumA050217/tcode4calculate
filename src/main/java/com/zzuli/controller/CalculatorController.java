package com.zzuli.controller;


import com.zzuli.annotation.Check;
import com.zzuli.dto.AnswerDTO;
import com.zzuli.dto.RecordDTO;
import com.zzuli.form.*;
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
 * @projectName controller
 * @Time 2025/10/27  10:52
 * @description
 */
@Tag(name = "计算器接口", description = "提供答卷题目的提交、查询等功能")
@RestController
@RequestMapping("/calculate")
@CrossOrigin
@SecurityRequirement(name = "Authorization")
public class CalculatorController {

    @Autowired
    private RecordService recordService;

    @Autowired
    private AnswerService answerService;

    @Operation(summary = "生成题目")
    @GetMapping("/generate")
    @Check
    public Result<Boolean> generate(@RequestParam Long bankId, @RequestParam Integer type, @RequestParam Integer quantity) {
        Boolean success = recordService.generate(bankId, type, quantity);
        return success ? Result.ok(true) : Result.fail();
    }

    @Operation(summary = "删除题目")
    @DeleteMapping("/delete")
    @Check
    public Result<Boolean> deleteQuestion(@RequestParam Long questionId) {
        boolean success = recordService.removeById(questionId);
        return success ? Result.ok(true) : Result.fail();
    }

    @Operation(summary = "批量删除题目")
    @DeleteMapping("/delete/batch")
    @Check
    public Result<Boolean> deleteQuestions(@RequestBody List<Long> questionIds) {
        boolean success = recordService.removeByIds(questionIds);
        return success ? Result.ok(true) : Result.fail();
    }

    @Operation(summary = "手动生成题目")
    @PostMapping("/manual")
    @Check
    public Result<Boolean> generateManual(@RequestBody List<RecordDTO> RecordDTOs, @RequestParam Long bankId) {
        Boolean success = recordService.generateManual(RecordDTOs, bankId);
        return success ? Result.ok(true) : Result.fail();
    }

    @Operation(summary = "查询题库中的题目")
    @GetMapping("/get")
    @Check
    public Result<List<BaseForm>> get(@RequestParam Long bankId) {
        List<BaseForm> baseList = recordService.get(bankId);
        return Result.ok(baseList);
    }

    @Operation(summary = "作答题目")
    @PostMapping("/submit")
    @Check
    public Result<Boolean> submit(@RequestBody List<AnswerDTO> answerDTOList) {
        Long userId = AuthContextHolder.getUserId();
        Boolean success = answerService.submit(answerDTOList, userId);
        return success ? Result.ok(true) : Result.fail();
    }

}
