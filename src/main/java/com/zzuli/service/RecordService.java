package com.zzuli.service;

import com.zzuli.dto.RecordDTO;
import com.zzuli.entity.Base;
import com.zzuli.entity.Record;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zzuli.form.*;

import java.util.List;

/**
* @author 73831
* @description 针对表【record(做题记录)】的数据库操作Service
* @createDate 2025-10-27 17:40:32
*/
public interface RecordService extends IService<Record> {

    /**
     * 生成题目
     * @param type
     * @param quantity
     * @return
     */
    Boolean generate(Long bankId, Integer type, Integer quantity);

    /**
     * 查询题库获取题目列表
     * @param bankId
     * @return
     */
    List<BaseForm> get(Long bankId);

    /**
     * 查询用户生成的题库
     * @param userId
     * @return
     */
    List<BankForm> getMyBank(Long userId);

    /**
     * 查询所有作答记录
     * @param userId
     * @return
     */
    List<AnswerForm> getAll(Long userId);

    /**
     * 查询用户错题本
     * @param userId
     * @return
     */
    List<MistakeForm> getWrong(Long userId);

    /**
     * 手动生成题目
     * @param questionForms
     * @param bankId
     * @param quantity
     * @return
     */
    Boolean generateManual(List<RecordDTO> RecordDTOs, Long bankId);
}
