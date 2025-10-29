package com.zzuli.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzuli.entity.Base;
import com.zzuli.entity.Record;
import com.zzuli.enums.TypeEnum;
import com.zzuli.form.BaseForm;
import com.zzuli.form.RecordForm;
import com.zzuli.mapper.RecordMapper;
import com.zzuli.service.RecordService;
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
        Random random = new Random();
        List<Record> recordList = new ArrayList<>();
        if (type.equals(TypeEnum.MIX.getValue())) {
            type = random.nextInt(4);
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


    private void saveQuestionList(Integer type, Integer quantity, List<Record> recordList, Long bankId) {
        RecordForm recordForm = generateQuestion(type, quantity);
        for (Base base : recordForm.getBaseList()) {
            Record record = new Record();
            record.setType(type);
            record.setOperandA(base.getA());
            record.setOperandB(base.getB());
            record.setCorrectAnswer(execute(base));
            record.setBankId(bankId);
            recordList.add(record);
        }
    }

    private RecordForm generateQuestion(Integer type, Integer quantity) {
        RecordForm recordForm = new RecordForm();
        List<Base> baseList = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            Random random = new Random();
            Base base = new Base();
            base.setType(type);
            base.setA(random.nextInt(100));
            base.setB(random.nextInt(100));
            baseList.add(base);
        }

        recordForm.setBaseList(baseList);

        return recordForm;
    }

    private Double execute(Base base) {
        return switch (base.getType()) {
            case 0 -> (double) (base.getA() + base.getB());
            case 1 -> (double) (base.getA() - base.getB());
            case 2 -> (double) (base.getA() * base.getB());
            case 3 -> (double) (base.getA() / base.getB());
            default -> null;
        };
    }
}




