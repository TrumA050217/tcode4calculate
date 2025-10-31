package com.zzuli.controller;


import com.zzuli.annotation.Check;
import com.zzuli.dto.AnswerDTO;
import com.zzuli.dto.RecordDTO;
import com.zzuli.entity.Bank;
import com.zzuli.form.*;
import com.zzuli.service.AnswerService;
import com.zzuli.service.BankService;
import com.zzuli.service.RecordService;
import com.zzuli.util.AuthContextHolder;
import com.zzuli.util.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        Long userId = AuthContextHolder.getUserId();
        Bank bank = new Bank();
        bank.setCreatedBy(userId);
        bank.setCreatedAt(new Date());
        bank.setIsCompleted(0);
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

    @Operation(summary = "手动生成题目")
    @PostMapping("/manual")
    @Check
    public Result<Boolean> generateManual(@RequestBody List<RecordDTO> RecordDTOs, @RequestParam Long bankId) {
        Boolean success = recordService.generateManual(RecordDTOs, bankId);
        return success ? Result.ok(true) : Result.fail();
    }

    @Operation(summary = "查询用户生成的题库")
    @GetMapping("/getMyBank")
    @Check
    public Result<List<BankForm>> getMyBank() {
        Long userId = AuthContextHolder.getUserId();
        List<BankForm> recordForms = recordService.getMyBank(userId);
        return Result.ok(recordForms);
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

    @Operation(summary = "查询用户历史作答记录")
    @GetMapping("/all")
    @Check
    public Result<List<AnswerForm>> getAll() {
        Long userId = AuthContextHolder.getUserId();
        List<AnswerForm> recordForms = recordService.getAll(userId);
        return Result.ok(recordForms);
    }

    @Operation(summary = "查询用户错题本")
    @GetMapping("/wrong")
    @Check
    public Result<List<MistakeForm>> getWrong() {
        Long userId = AuthContextHolder.getUserId();
        List<MistakeForm> recordForms = recordService.getWrong(userId);
        return Result.ok(recordForms);
    }

    @Operation(summary = "统计错题总数")
    @GetMapping("/wrong/count")
    @Check
    public Result<Integer> getWrongCount() {
        Long userId = AuthContextHolder.getUserId();
        Integer count = recordService.getWrongCount(userId);
        return Result.ok(count);
    }

}
