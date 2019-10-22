package org.hrm.aspect;

import org.hrm.enums.ResponseStatus;
import org.hrm.utils.Response;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 定义出错处理切面
 */
@Component
@Aspect
public class RequestAspect {
    /**
     * 定义切面
     * 所有由ApiRequest注解到的方法都会被拦截
     */
    @Pointcut("@annotation(org.hrm.utils.ApiRequest)")
    public void ApiRequestAspect(){}

    @Around("ApiRequestAspect()")
    public Object processRequest(ProceedingJoinPoint jp){
        Object result = null;
        try {
            Signature signature = jp.getSignature();
            result = jp.proceed();
        } catch (Throwable throwable) {
            System.out.println("切面拦截到了错误！");
            result = new Response(ResponseStatus.SUCCESS, null);
        } finally {
            return result;
        }
    }
}
