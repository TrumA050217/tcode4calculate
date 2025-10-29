package com.zzuli.controller;


import com.zzuli.annotation.Check;
import com.zzuli.dto.AnswerDTO;
import com.zzuli.entity.Bank;
import com.zzuli.entity.Base;
import com.zzuli.form.AnswerForm;
import com.zzuli.form.BaseForm;
import com.zzuli.form.MyResultForm;
import com.zzuli.form.RecordForm;
import com.zzuli.service.AnswerService;
import com.zzuli.service.BankService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.zzuli.util.Result;
import com.zzuli.service.RecordService;

import java.util.Date;
import java.util.List;

/**
 * @author 成都
 * @program MyCalculator
 * @projectName controller
 * @Time 2025/10/27  10:52
 * @description
 */
@Tag(name = "计算器接口", description = "提供答卷答案的提交、查询等功能")
@RestController
@RequestMapping("/calculate")
@CrossOrigin
@SecurityRequirement(name = "Authorization")
public class CalculatorController {
    @Autowired
    private RecordService recordService;

    @Autowired
    private BankService bankService;

    @Autowired
    private AnswerService answerService;

    @Operation(summary = "生成题库")
    @GetMapping("/generate")
    @Check
    public Result<Long> generateBank() {
        Bank bank = new Bank();
        bank.setCreatedAt(new Date());
        bankService.save(bank);
        return Result.ok(bank.getBankId());
    }

    @Operation(summary = "生成题目")
    @GetMapping("/generate/questions")
    @Check
    public Result<Boolean> generate(@RequestParam Long bankId, @RequestParam Integer type, @RequestParam Integer quantity) {
        Boolean success = recordService.generate(bankId, type, quantity);
        return success ? Result.ok(true) : Result.fail();
    }

    @Operation(summary = "查询题库")
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
        Boolean success = answerService.submit(answerDTOList);
        return success ? Result.ok() : Result.fail();
    }

    @Operation(summary = "查询用户作答结果")
    @GetMapping("/result")
    @Check
    public Result<List<AnswerForm>> result(@RequestParam Long bankId) {
        List<AnswerForm> answers = answerService.getResult(bankId);
        return Result.ok(answers);
    }

    @Operation(summary = "查询用户作答总数量和正确率")
    @GetMapping("/result/accuracy")
    @Check
    public Result<List<MyResultForm>> getResultAccuracy(@RequestParam Long bankId) {
        List<MyResultForm> answers = answerService.getResultAccuracy(bankId);
        return Result.ok(answers);
    }

}
