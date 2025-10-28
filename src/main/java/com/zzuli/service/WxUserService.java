package com.zzuli.service;

import com.zzuli.entity.WxUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 73831
* @description 针对表【wx_user(微信登录用户)】的数据库操作Service
* @createDate 2025-10-28 16:12:19
*/
public interface WxUserService extends IService<WxUser> {

    /**
     * 微信一键登录
     * @param code
     * @return
     */
    String login(String code);
}
