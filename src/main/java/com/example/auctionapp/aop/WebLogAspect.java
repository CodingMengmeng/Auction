package com.example.auctionapp.aop;

import com.example.auctionapp.annotation.WebLog;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * aop日志切面
 *
 * @author 安能
 * @version 1.0.7
 * @date 2019年7月17日10:03:26
 */
@Aspect
@Component
@Slf4j
public class WebLogAspect {

    /**
     * 1、优化- AOP切面中的同步问题
     */
    private ThreadLocal<Long> startTime = new ThreadLocal<>();


    /**
     * 使用@Pointcut定义一个切入点，可以是一个规则表达式，比如下例中某个package下的所有函数，也可以是一个注解等。
     * public表示访问权限是公有方法，第一个*表示返回类型，第二 *表示类名，第三个*表示方法(..)表示任何参数，包含子包
     */
    @Pointcut("execution(public * com.example.auctionapp.controller.*.*(..))")
    public void webLog() {
    }


    /**
     * 使用@Around在切入点前后切入内容，并自己控制何时执行切入点自身的内容</br>
     * i的值越小，优先级越高。在切入点前的操作，按order的值由小到大执行,在切入点后的操作，按order的值由大到小执行
     */
    @Order(1)
    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        startTime.set(System.currentTimeMillis());

        // 下面两个数组中，参数值和参数名的个数和位置是一一对应的。
        Object[] args = joinPoint.getArgs();
        String[] argNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();

        // 记录下请求内容，包含：url,请求方法，ip，类方法，参数
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        String value = "";
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method1 = methodSignature.getMethod();
        if (method1.isAnnotationPresent(WebLog.class)) {
            WebLog webLog = method1.getAnnotation(WebLog.class);
            value = webLog.value();
        }

        String userId = request.getHeader("userId");
        String ip = request.getRemoteAddr();
        String url = request.getRequestURL().toString();
        String method = request.getMethod();
        String queryString = request.getQueryString();
        String declaringTypeName = joinPoint.getSignature().getDeclaringTypeName();
        String name = joinPoint.getSignature().getName();
        String parameter = Arrays.toString(joinPoint.getArgs());

        log.info("* 当前执行操作方法:{}，请求用户id:{} ,方法请求参数:{}", value, userId, queryString);
        log.info("* 方法接收到的参数:{}", getFieldsNameValueMap(joinPoint));
    }


    private Map<String, Object> getFieldsNameValueMap(JoinPoint joinPoint) throws Exception {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();


        Object[] args = joinPoint.getArgs();
        String classType = joinPoint.getTarget().getClass().getName();
        Class<?> clazz = Class.forName(classType);
        String clazzName = clazz.getName();
        String methodName = joinPoint.getSignature().getName();
        //获取方法名称
        Map<String, Object> map = new HashMap<>();
        ClassPool pool = ClassPool.getDefault();
        ClassClassPath classPath = new ClassClassPath(this.getClass());
        pool.insertClassPath(classPath);
        CtClass cc = pool.get(clazzName);
        CtMethod cm = cc.getDeclaredMethod(methodName);
        MethodInfo methodInfo = cm.getMethodInfo();
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
        if (attr == null) {
            throw new RuntimeException();
        }
        int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
        for (int i = 0; i < cm.getParameterTypes().length; i++) {
            //paramNames即参数名
            map.put(attr.variableName(i + pos), args[i]);
        }
        return map;
    }

    @AfterReturning(value = "webLog()",returning = "result")
    public void afterReturning(JoinPoint joinPoint,Object result) {

        Long beginTime = startTime.get();

        log.info("* 方法返回结果:{},方法执行时间: {}毫秒", result, System.currentTimeMillis() - beginTime);
    }



}