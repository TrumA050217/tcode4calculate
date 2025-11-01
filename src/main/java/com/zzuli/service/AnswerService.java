package com.zzuli.service;

import com.zzuli.dto.AnswerDTO;
import com.zzuli.entity.Answer;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zzuli.form.AnswerForm;
import com.zzuli.form.MyResultForm;

import java.util.List;

/**
* @author 73831
* @description 针对表【answer(用户答案)】的数据库操作Service
* @createDate 2025-10-27 17:28:08
*/
public interface AnswerService extends IService<Answer> {

    /**
     * 作答题目
     * @param answerDTOList
     * @return
     */
    Boolean submit(List<AnswerDTO> answerDTOList, Long userId);

    /**
     * 查询用户作答结果
     * @param bankId
     * @return
     */
    List<AnswerForm> getResult(Long bankId);

    /**
     * 查询用户作答正确率
     * @param bankId
     * @return
     */
    MyResultForm getResultAccuracy(Long bankId);
}
