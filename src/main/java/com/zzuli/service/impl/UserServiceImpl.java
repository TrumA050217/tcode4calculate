package com.zzuli.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
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
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 73831
 * @description 针对表【user】的数据库操作Service实现
 * @createDate 2025-10-27 16:17:05
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private WxMaService wxMaService;

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

    /**
     * 微信一键登录
     *
     * @param code
     * @return
     */
    @Override
    public String login4WX(String code) {
        // 获取code值，使用微信工具包对象，获取微信唯一标识openid
        String openid = null;
        try {
            // 直接使用 code 获取 session对象
            WxMaJscode2SessionResult sessionInfo = wxMaService.jsCode2SessionInfo(code);
            openid = sessionInfo.getOpenid();
            log.info("获取session成功，openid: {}", openid);
        } catch (WxErrorException e) {
            log.error("微信登录失败: {}", e.getMessage(), e);
            throw new RuntimeException("微信登录失败: " + e.getMessage());
        }

        // 查询用户是否存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getOpenid, openid);
        User user = userMapper.selectOne(wrapper);

        // 如果第一次登录，添加信息到用户表
        if (user == null) {
            user = new User();
            user.setOpenid(openid);
            user.setUsername(String.valueOf(System.currentTimeMillis()));
            user.setPassword(MD5Util.encrypt("123"));
            userMapper.insert(user);
            log.info("新用户注册成功，openid: {}", openid);
        }

        // 生成token
        String token = jwtHelper.createToken(user.getUserId());
        log.info("用户登录成功，openid: {}, token: {}", openid, token);

        return token;
    }


}




