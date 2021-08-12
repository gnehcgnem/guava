package com.example.guava.aop;

import com.google.common.util.concurrent.RateLimiter;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import com.example.guava.annotation.LimitAop;
import java.lang.annotation.Annotation;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Component
public class CurrentLimitAop {
    private ConcurrentHashMap<String, RateLimiter> ratelimiters = new ConcurrentHashMap<>();

    @Before(value = "@annotation(com.example.guava.annotation.LimitAop)")
    public void before(){
        System.out.println("前置通知。。。。");
    }
    @After(value = "@annotation(com.example.guava.annotation.LimitAop)")
    public void after(){
        System.out.println("后置通知...");
    }
    @Around(value = "@annotation(com.example.guava.annotation.LimitAop)")
    public Object around(ProceedingJoinPoint JoinPoint){
        try {
            //获取拦截方法名称
            MethodSignature signature = (MethodSignature) JoinPoint.getSignature();
            //获取特定注解
            LimitAop limitAop = signature.getMethod().getDeclaredAnnotation(LimitAop.class);

            String name=limitAop.name();

            double token = limitAop.token();

            System.out.println("令牌桶限制"+token);

            RateLimiter rateLimiter = ratelimiters.get(name);
            //不存在此方法的ratelimer类则加入map中
            if (rateLimiter == null) {
                rateLimiter = RateLimiter.create(token);
                ratelimiters.put(name, rateLimiter);
            }
            //尝试获取令牌桶
            boolean b = rateLimiter.tryAcquire();
            if (!b) {
                return "当前请求过多，请稍后重试";
            }

            Object proceed = JoinPoint.proceed();
            return proceed;
        } catch (Throwable e) {
            return "系统错误";
        }
    }
}
