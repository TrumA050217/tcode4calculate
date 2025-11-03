package com.zzuli.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzuli.dto.AnswerVo;
import com.zzuli.entity.Record;
import com.zzuli.entity.*;
import com.zzuli.enums.AnswerStatusEnum;
import com.zzuli.enums.ResultCodeEnum;
import com.zzuli.exception.TcodeException;
import com.zzuli.form.AnswerForm;
import com.zzuli.form.MyResultForm;
import com.zzuli.mapper.*;
import com.zzuli.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author 73831
 * @description 针对表【answer(用户答案)】的数据库操作Service实现
 * @createDate 2025-10-27 17:28:08
 */
@Service
public class AnswerServiceImpl extends ServiceImpl<AnswerMapper, Answer>
        implements AnswerService {

    @Autowired
    private AnswerMapper answerMapper;

    @Autowired
    private RecordMapper recordMapper;

    @Autowired
    private MistakeMapper mistakeMapper;

    @Autowired
    private MyResultMapper myResultMapper;

    @Autowired
    private BankMapper bankMapper;

    /**
     * 作答题目
     *
     * @param answerVoList
     * @return
     */
    @Override
    @Transactional
    public Boolean submit(List<AnswerVo> answerVoList, Long userId) {
        if (answerVoList == null || answerVoList.isEmpty()) {
            return true;
        }

        Bank bank = bankMapper.selectById(answerVoList.get(0).getBankId());

        List<Answer> answerList = new ArrayList<>();
        int errorCount = 0;

        for (AnswerVo answerVo : answerVoList) {
            boolean isCorrect = true;
            Answer answer = new Answer();

            Record record = recordMapper.selectById(answerVo.getRecordId());
            if (record == null) {
                continue;
            }

            if (answerVo.getMyAnswer() == null) {
                throw new TcodeException(ResultCodeEnum.EMPTY_ANSWER);
            }

            // 判断答案是否正确
            if (!Objects.equals(answerVo.getMyAnswer(), record.getCorrectAnswer())) {
                // 错题处理
                Mistake mistake = new Mistake();
                mistake.setBankId(record.getBankId());
                mistake.setRecordId(answerVo.getRecordId());
                mistake.setOperA(record.getOperandA());
                mistake.setOperB(record.getOperandB());
                mistake.setMistakeType(record.getType());
                mistake.setUserId(userId);
                mistakeMapper.insert(mistake);

                errorCount++;
                isCorrect = false;
            }


            // 设置用户答案
            answer.setIsCorrect(isCorrect ? AnswerStatusEnum.RIGHT.getValue() : AnswerStatusEnum.WRONG.getValue());
            answer.setBankId(record.getBankId());
            answer.setRecordId(answerVo.getRecordId());
            answer.setMyAnswer(answerVo.getMyAnswer());
            answer.setUserId(userId);
            answerList.add(answer);
        }

        // 创建答题结果记录
        MyResult myResult = new MyResult();
        Record firstRecord = recordMapper.selectById(answerVoList.get(0).getRecordId());
        myResult.setBankId(firstRecord.getBankId());
        myResult.setTotal(answerVoList.size());
        myResult.setUserId(userId);
        // 直接保留两位小数的小数值
        myResult.setAccuracy(answerVoList.isEmpty() ? 0.0 :
                Math.round(100.0 * (answerVoList.size() - errorCount) / answerVoList.size()) / 100.0);
        myResultMapper.insert(myResult);
        bank.setIsCompleted(1);
        bankMapper.updateById(bank);
        return saveBatch(answerList);
    }

    /**
     * 查询用户作答结果
     *
     * @param bankId
     * @return
     */
    @Override
    public List<AnswerForm> getResult(Long bankId) {
        LambdaQueryWrapper<Answer> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Answer::getBankId, bankId);
        List<Answer> answerList = answerMapper.selectList(queryWrapper);

        List<AnswerForm> answerFormList = new ArrayList<>();
        for (Answer answer : answerList) {
            Record record = recordMapper.selectById(answer.getRecordId());
            if (record == null) {
                continue; // 跳过无效记录
            }
            AnswerForm answerForm = new AnswerForm();
            answerForm.setOperandA(record.getOperandA());
            answerForm.setOperandB(record.getOperandB());
            answerForm.setType(record.getType());
            answerForm.setMyAnswer(answer.getMyAnswer());
            answerForm.setIsCorrect(answer.getIsCorrect());
            answerForm.setCorrectAnswer(record.getCorrectAnswer());
            answerFormList.add(answerForm);
        }

        return answerFormList;
    }

    /**
     * 查询用户作答正确率
     *
     * @param bankId
     * @return
     */
    @Override
    public MyResultForm getResultAccuracy(Long bankId) {
        LambdaQueryWrapper<MyResult> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MyResult::getBankId, bankId).orderByDesc(MyResult::getResultId);
        List<MyResult> myResults = myResultMapper.selectList(queryWrapper);
        MyResult myResult = myResults.get(0);
        if (myResult == null) {
            throw new TcodeException(ResultCodeEnum.NO_RESULT);
        }
        MyResultForm myResultForm = new MyResultForm();
        myResultForm.setTotal(myResult.getTotal());
        myResultForm.setAccuracy(myResult.getAccuracy());

        return myResultForm;
    }


}




