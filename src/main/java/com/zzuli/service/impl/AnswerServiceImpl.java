package com.zzuli.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzuli.dto.AnswerDTO;
import com.zzuli.entity.Answer;
import com.zzuli.entity.Mistake;
import com.zzuli.entity.MyResult;
import com.zzuli.entity.Record;
import com.zzuli.enums.AnswerStatusEnum;
import com.zzuli.enums.ResultCodeEnum;
import com.zzuli.exception.TcodeException;
import com.zzuli.form.AnswerForm;
import com.zzuli.form.MyResultForm;
import com.zzuli.mapper.AnswerMapper;
import com.zzuli.mapper.MistakeMapper;
import com.zzuli.mapper.MyResultMapper;
import com.zzuli.mapper.RecordMapper;
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

    /**
     * 作答题目
     *
     * @param answerDTOList
     * @return
     */
    @Override
    @Transactional
    public Boolean submit(List<AnswerDTO> answerDTOList, Long userId) {
        if (answerDTOList == null || answerDTOList.isEmpty()) {
            return true;
        }

        List<Answer> answerList = new ArrayList<>();
        int errorCount = 0;

        for (AnswerDTO answerDTO : answerDTOList) {
            boolean isCorrect = true;
            Answer answer = new Answer();

            Record record = recordMapper.selectById(answerDTO.getRecordId());
            if (record == null) {
                continue; // 抛出异常
            }

            if (answerDTO.getMyAnswer() == null) {
                throw new TcodeException(ResultCodeEnum.EMPTY_ANSWER);
            }

            // 判断答案是否正确 - 改进版本
            if (!Objects.equals(answerDTO.getMyAnswer(), record.getCorrectAnswer())) {
                // 错题处理
                Mistake mistake = new Mistake();
                mistake.setBankId(record.getBankId());
                mistake.setRecordId(answerDTO.getRecordId());
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
            answer.setRecordId(answerDTO.getRecordId());
            answer.setMyAnswer(answerDTO.getMyAnswer());
            answer.setUserId(userId);
            answerList.add(answer);
        }

        // 创建答题结果记录
        MyResult myResult = new MyResult();
        Record firstRecord = recordMapper.selectById(answerDTOList.get(0).getRecordId());
        myResult.setBankId(firstRecord.getBankId());
        myResult.setTotal(answerDTOList.size());
        myResult.setUserId(userId);
        // 直接保留两位小数的小数值
        myResult.setAccuracy(answerDTOList.isEmpty() ? 0.0 :
                Math.round(100.0 * (answerDTOList.size() - errorCount) / answerDTOList.size()) / 100.0);
        myResultMapper.insert(myResult);

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
    public List<MyResultForm> getResultAccuracy(Long bankId) {
        LambdaQueryWrapper<MyResult> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MyResult::getBankId, bankId);
        List<MyResult> myResultList = myResultMapper.selectList(queryWrapper);

        if (myResultList == null || myResultList.isEmpty()) {
            return new ArrayList<>(); // 返回空列表而不是null
        }

        List<MyResultForm> myResultFormList = new ArrayList<>();
        for (MyResult myResult : myResultList) {
            if (myResult == null) {
                continue;
            }
            MyResultForm myResultForm = new MyResultForm();
            myResultForm.setTotal(myResult.getTotal());
            myResultForm.setAccuracy(myResult.getAccuracy());
            myResultFormList.add(myResultForm);
        }

        return myResultFormList;
    }


}




