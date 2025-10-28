package com.zzuli.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzuli.entity.User;
import com.zzuli.enums.ResultCodeEnum;
import com.zzuli.exception.TcodeException;
import com.zzuli.form.UserForm;
import com.zzuli.mapper.UserMapper;
import com.zzuli.service.UserService;
import com.zzuli.util.JwtHelper;
import com.zzuli.util.MD5Util;
import com.zzuli.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 73831
 * @description 针对表【user】的数据库操作Service实现
 * @createDate 2025-10-27 16:17:05
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtHelper jwtHelper;

    /**
     * 登录
     *
     * @param userForm
     * @return
     */
    @Override
    public String login(UserForm userForm) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUsername, userForm.getUsername());
        User loginUser = userMapper.selectOne(lambdaQueryWrapper);

        try {
            if (loginUser == null) {
                throw new TcodeException(ResultCodeEnum.USER_NOT_EXIST);
            }
            //对比密码
            if (!StringUtils.isEmpty(userForm.getPassword())
                    && MD5Util.encrypt(userForm.getPassword()).equals(loginUser.getPassword())) {
                //登录成功 根据用户id生成token
                return jwtHelper.createToken(loginUser.getUserId());
            }
        } catch (Exception e) {
            throw new TcodeException(ResultCodeEnum.LOGIN_AUTH);
        }
        return null;
    }

    /**
     * 注册
     *
     * @param userForm
     * @return
     */
    @Override
    public Boolean register(UserForm userForm) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUsername, userForm.getUsername());
        User loginUser = userMapper.selectOne(lambdaQueryWrapper);

        if (loginUser != null) {
            return false;
        }
        User user = new User();
        user.setUsername(userForm.getUsername());
        user.setPassword(MD5Util.encrypt(userForm.getPassword()));
        return userMapper.insert(user) > 0;
    }
}




