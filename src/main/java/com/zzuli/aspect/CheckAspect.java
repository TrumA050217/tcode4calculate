package com.zzuli.aspect;


import com.zzuli.annotation.Check;
import com.zzuli.enums.ResultCodeEnum;
import com.zzuli.exception.TcodeException;
import com.zzuli.util.AuthContextHolder;
import com.zzuli.util.JwtHelper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author 成都
 * @program tcode
 * @projectName com.tcode.aspect
 * @Time 2025/5/28  23:18
 * @description
 */
@Aspect
@Component
@Slf4j
public class CheckAspect {

    @Autowired
    private JwtHelper jwtHelper;

    @Around("execution(* com.zzuli.controller..*(..)) && @annotation(check)")
    public Object login(ProceedingJoinPoint joinPoint, Check check) throws Throwable {
        // 获取request对象
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        // 从请求头获取token
        String token = request.getHeader("token");
        //  判断token是否为空，如果为空，返回登录提示
        if (!StringUtils.hasText(token)) {
            throw new TcodeException(ResultCodeEnum.LOGIN_AUTH);
        }
        // token不为空，获取用户id
        Long userId = jwtHelper.getUserId(token);
        // 用户id不为空，把用户id放到ThreadLocal里面
        if (userId != null) {
            AuthContextHolder.setUserId(userId);
        }
        // 执行业务方法
        return joinPoint.proceed();
    }

}