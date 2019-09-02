package com.example.auctionapp.aop;

import com.example.auctionapp.annotation.ObjectNotNull;
import com.example.auctionapp.core.Result;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller防空校验
 *
 * @author 安能
 * @version 1.0.7
 * @date 2019年7月29日10:59:33
 */
@Aspect
@Component
@Slf4j
public class VerifyAspect {


    /**
     * 使用@Pointcut定义一个切入点，可以是一个规则表达式，比如下例中某个package下的所有函数，也可以是一个注解等。
     * public表示访问权限是公有方法，第一个*表示返回类型，第二 *表示类名，第三个*表示方法(..)表示任何参数，包含子包
     */
    @Pointcut("execution(public * com.example.auctionapp.controller.*.*(..))")
    public void webLog() {
    }

    /**
     * 环绕通知,环绕增强，相当于MethodInterceptor
     *
     * @param point
     * @return
     */
    @Around("webLog()")
    public Result doAround(ProceedingJoinPoint point) throws Throwable {

        Result result;

        Class<?> targetClass = point.getTarget().getClass();
        String methodName = point.getSignature().getName();
        Object[] args = point.getArgs();
        Map<String, String> dataMap = new HashMap<>(16);
        for (Method method : targetClass.getMethods()) {
            if (methodName.equals(method.getName())) {

                Parameter[] parameters = method.getParameters();
                for (int i = 0; i < parameters.length; i++) {

                    Class<?> aClass = parameters[i].getType();
                    ClassLoader classLoader = aClass.getClassLoader();
                    if (classLoader != null) {
                        boolean isObjectNotNull = parameters[i].isAnnotationPresent(ObjectNotNull.class);


                        if (isObjectNotNull) {

                            ObjectNotNull annotation = parameters[i].getAnnotation(ObjectNotNull.class);
                            String[] value = annotation.value();

                            Field[] fields = aClass.getDeclaredFields();

                            for (String v : value) {
                                for (Field field : fields) {
                                    field.setAccessible(true);
                                    if (v.equals(field.getName())) {
                                        if (field.get(args[i]) == null) {
                                            dataMap.put(field.getName(), "不能为空!");
                                        }
                                    }
                                }
                            }
                        }
                        //            非自定义类
                    } else {

                        boolean isObjectNotNull = parameters[i].isAnnotationPresent(ObjectNotNull.class);

                        if (isObjectNotNull) {

                            ObjectNotNull annotation = parameters[i].getAnnotation(ObjectNotNull.class);
                            String[] values = annotation.value();
                            if (args[i] instanceof Map) {
                                for (String value : values) {
                                    Map map = (Map) args[i];
                                    if (null == map.get(value)) {
                                        dataMap.put(value, "不能为空!");
                                    }
                                }
                            }
                        }

                        boolean isNotNull = parameters[i].isAnnotationPresent(NotNull.class);
                        if (isNotNull) {
                            if (args[i] == null) {
                                dataMap.put(parameters[i].getName(), "不能为空!");
                            }
                        }
                    }

                }
            }
        }
        if (dataMap.size() > 0) {
            return Result.success(
                    dataMap);
        }
        result = (Result) point.proceed();

        return result;

    }
}
