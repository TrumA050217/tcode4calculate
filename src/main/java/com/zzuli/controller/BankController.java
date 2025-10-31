package com.zzuli.controller;

import com.zzuli.annotation.Check;
import com.zzuli.entity.Bank;
import com.zzuli.form.BankForm;
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
 * @projectName com.zzuli.controller
 * @Time 2025/10/31  12:22
 * @description
 */
@Tag(name = "题库接口", description = "提供答卷答案的提交、查询等功能")
@RestController
@RequestMapping("/bank")
@CrossOrigin
@SecurityRequirement(name = "Authorization")
public class BankController {

    @Autowired
    private BankService bankService;

    @Autowired
    private RecordService recordService;

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

    @Operation(summary = "删除题库")
    @DeleteMapping("/delete")
    @Check
    public Result<Boolean> deleteBank(@RequestParam Long bankId) {
        boolean success = bankService.removeById(bankId);
        return success ? Result.ok(true) : Result.fail();
    }

    @Operation(summary = "批量删除题库")
    @DeleteMapping("/delete/batch")
    @Check
    public Result<Boolean> deleteBanks(@RequestBody List<Long> bankIds) {
        boolean success = bankService.removeByIds(bankIds);
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

}
