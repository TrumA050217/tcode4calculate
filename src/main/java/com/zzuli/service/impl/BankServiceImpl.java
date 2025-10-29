package com.zzuli.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzuli.entity.Bank;
import com.zzuli.entity.Base;
import com.zzuli.service.BankService;
import com.zzuli.mapper.BankMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 73831
* @description 针对表【bank(题库)】的数据库操作Service实现
* @createDate 2025-10-27 17:40:32
*/
@Service
public class BankServiceImpl extends ServiceImpl<BankMapper, Bank>
    implements BankService{
}




