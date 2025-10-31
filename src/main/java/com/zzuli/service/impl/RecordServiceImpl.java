package com.zzuli.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzuli.dto.RecordDTO;
import com.zzuli.entity.Record;
import com.zzuli.entity.*;
import com.zzuli.enums.TypeEnum;
import com.zzuli.form.*;
import com.zzuli.mapper.*;
import com.zzuli.service.RecordService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author 73831
 * @description 针对表【record(做题记录)】的数据库操作Service实现
 * @createDate 2025-10-27 17:40:32
 */
@Service
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record>
        implements RecordService {

    @Autowired
    private RecordMapper recordMapper;

    @Autowired
    private AnswerMapper answerMapper;

    @Autowired
    private BankMapper bankMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MistakeMapper mistakeMapper;

    /**
     * 生成题目
     *
     * @param type
     * @param quantity
     * @return
     */
    @Override
    @Transactional
    public Boolean generate(Long bankId, Integer type, Integer quantity) {
        List<Record> recordList = new ArrayList<>();
        if (type.equals(TypeEnum.MIX.getValue())) {
            saveQuestionList(type, quantity, recordList, bankId);
        } else {
            saveQuestionList(type, quantity, recordList, bankId);
        }
        return this.saveBatch(recordList);
    }

    /**
     * 查询题库获取题目列表
     *
     * @param bankId
     * @return
     */
    @Override
    public List<BaseForm> get(Long bankId) {
        LambdaQueryWrapper<Record> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Record::getBankId, bankId);
        List<Record> recordList = recordMapper.selectList(queryWrapper);
        List<BaseForm> baseFormList = new ArrayList<>();
        for (Record record : recordList) {
            BaseForm baseForm = new BaseForm();
            baseForm.setA(record.getOperandA());
            baseForm.setB(record.getOperandB());
            baseForm.setType(record.getType());
            baseFormList.add(baseForm);
        }
        return baseFormList;
    }

    /**
     * 查询用户生成的题库
     *
     * @param userId
     * @return
     */
    @Override
    public List<BankForm> getMyBank(Long userId) {
        User user = userMapper.selectById(userId);
        LambdaQueryWrapper<Bank> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Bank::getCreatedBy, userId);
        List<Bank> bankList = bankMapper.selectList(queryWrapper);
        List<BankForm> bankFormList = new ArrayList<>();
        for (Bank bank : bankList) {
            BankForm bankForm = new BankForm();
            bankForm.setBankId(bank.getBankId());
            bankForm.setCreatedBy(user.getUsername());
            bankForm.setCreatedAt(bank.getCreatedAt());
            bankFormList.add(bankForm);
        }

        return bankFormList;
    }


    /**
     * 查询所有作答记录
     *
     * @param userId
     * @return
     */
    @Override
    public List<AnswerForm> getAll(Long userId) {
        LambdaQueryWrapper<Answer> answerWrapper = new LambdaQueryWrapper<>();
        answerWrapper.eq(Answer::getUserId, userId);
        List<Answer> answerList = answerMapper.selectList(answerWrapper);
        List<AnswerForm> answerFormList = new ArrayList<>();
        for (Answer answer : answerList) {
            LambdaQueryWrapper<Record> recordWrapper = new LambdaQueryWrapper<>();
            recordWrapper.eq(Record::getRecordId, answer.getRecordId());
            Record record = recordMapper.selectOne(recordWrapper);
            AnswerForm answerForm = new AnswerForm();
            answerForm.setMyAnswer(answer.getMyAnswer());
            answerForm.setIsCorrect(answer.getIsCorrect());
            answerForm.setOperandA(record.getOperandA());
            answerForm.setOperandB(record.getOperandB());
            answerForm.setType(record.getType());
            answerFormList.add(answerForm);
        }
        return answerFormList;
    }

    /**
     * 查询用户错题本
     *
     * @param userId
     * @return
     */
    @Override
    public List<MistakeForm> getWrong(Long userId) {
        LambdaQueryWrapper<Mistake> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Mistake::getUserId, userId);
        List<Mistake> mistakeList = mistakeMapper.selectList(queryWrapper);
        List<MistakeForm> mistakeFormList = new ArrayList<>();
        for (Mistake mistake : mistakeList) {
            MistakeForm mistakeForm = new MistakeForm();
            mistakeForm.setOperA(mistake.getOperA());
            mistakeForm.setOperB(mistake.getOperB());
            mistakeForm.setMistakeType(mistake.getMistakeType());
            mistakeFormList.add(mistakeForm);
        }
        return mistakeFormList;
    }

    /**
     * 手动生成题目
     *
     * @param RecordDTOs
     * @param bankId
     * @return
     */
    @Override
    @Transactional
    public Boolean generateManual(List<RecordDTO> RecordDTOs, Long bankId) {
        List<Record> recordList = new ArrayList<>();
        for (RecordDTO recordDTO : RecordDTOs) {
            if (recordDTO == null) continue;
            Record record = new Record();
            BeanUtils.copyProperties(recordDTO, record);
            Base base = new Base();
            base.setType(recordDTO.getType());
            base.setA(recordDTO.getOperandA());
            base.setB(recordDTO.getOperandB());
            Double result = execute(base);
            record.setCorrectAnswer(result);
            record.setBankId(bankId);
            recordList.add(record);
        }
        return this.saveBatch(recordList);
    }

    /**
     * 统计用户错题数量
     *
     * @param userId
     * @return
     */
    @Override
    public Long getWrongCount(Long userId) {
        LambdaQueryWrapper<Mistake> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Mistake::getUserId, userId);
        return mistakeMapper.selectCount(queryWrapper);
    }


    private void saveQuestionList(Integer type, Integer quantity, List<Record> recordList, Long bankId) {
        RecordForm recordForm = generateQuestion(type, quantity);
        for (Base base : recordForm.getBaseList()) {
            Record record = new Record();
            record.setType(base.getType());
            record.setOperandA(base.getA());
            record.setOperandB(base.getB());
            record.setCorrectAnswer(execute(base));
            record.setBankId(bankId);
            recordList.add(record);
        }
    }

    private RecordForm generateQuestion(Integer type, Integer quantity) {
        boolean isMix = type.equals(TypeEnum.MIX.getValue());
        RecordForm recordForm = new RecordForm();
        List<Base> baseList = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < quantity; i++) {
            int thisType;
            if (isMix) {
                thisType = random.nextInt(4);
            } else {
                thisType = type;
            }
            Base base = new Base();
            base.setType(thisType);
            base.setA(random.nextInt(100));
            base.setB(random.nextInt(100));
            baseList.add(base);
        }

        recordForm.setBaseList(baseList);
        return recordForm;
    }


    private Double execute(Base base) {
        switch (base.getType()) {
            case 0 -> {
                return base.getA() + base.getB();
            }
            case 1 -> {
                return base.getA() - base.getB();
            }
            case 2 -> {
                return base.getA() * base.getB();
            }
            case 3 -> {
                if (base.getB() != 0) {
                    // 除法保留两位小数
                    return Math.round(base.getA() / base.getB() * 100.0) / 100.0;
                }
                return 0.0;
            }
            default -> {
            }
        }
        return null;
    }

}




