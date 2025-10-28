package com.zzuli.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzuli.entity.WxUser;
import com.zzuli.service.WxUserService;
import com.zzuli.mapper.WxUserMapper;
import com.zzuli.util.JwtHelper;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author 73831
* @description 针对表【wx_user(微信登录用户)】的数据库操作Service实现
* @createDate 2025-10-28 16:12:19
*/
@Service
@Slf4j
public class WxUserServiceImpl extends ServiceImpl<WxUserMapper, WxUser>
    implements WxUserService{

    @Autowired
    private WxMaService wxMaService;

    @Autowired
    private WxUserMapper userMapper;

    @Autowired
    private JwtHelper jwtHelper;


    /**
     * 微信一键登录
     *
     * @param code
     * @return
     */
    @Override
    public String login(String code) {
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
        LambdaQueryWrapper<WxUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WxUser::getOpenid, openid);
        WxUser user = userMapper.selectOne(wrapper);

        // 如果第一次登录，添加信息到用户表
        if (user == null) {
            user = new WxUser();
            user.setOpenid(openid);
            userMapper.insert(user);
            log.info("新用户注册成功，openid: {}", openid);
        }

        // 生成token
        String token = jwtHelper.createToken(user.getWxUserId());
        log.info("用户登录成功，openid: {}, token: {}", openid, token);

        return token;
    }
}




