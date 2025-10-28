package com.zzuli.service;

import com.zzuli.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zzuli.form.UserForm;

/**
* @author 73831
* @description 针对表【user】的数据库操作Service
* @createDate 2025-10-27 16:17:05
*/
public interface UserService extends IService<User> {

    /**
     * 登录
     * @param userForm
     * @return
     */
    String login(UserForm userForm);

    /**
     * 微信一键登录
     * @param code
     * @return
     */
    String login4WX(String code);

    /**
     *  注册
     * @param userForm
     * @return
     */
    Boolean register(UserForm userForm);
}
