package com.example.auctionapp.filter;

import com.alibaba.fastjson.JSON;
import com.example.auctionapp.core.Result;
import com.example.auctionapp.service.IRedisService;
import javassist.CtClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.example.auctionapp.core.ProjectConstant.AUCTION_LOGIN_TOKEN_KEY;

/**
 * Created by zll on 2018/6/29.
 *
 * @author Administrator
 */
@Slf4j
@Profile({"dev"})
@Configuration
//@WebFilter(urlPatterns = {"customer/user/loginSMS"}, filterName = "tokenAuthorFilter", servletNames = {"/account/getAccoun"})
public class TokenAuthorFilter implements Filter {

    @Resource
    private IRedisService iRedisService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse rep = (HttpServletResponse) response;

        //设置允许跨域的配置
        // 这里填写你允许进行跨域的主机ip（正式上线时可以动态配置具体允许的域名和IP）
        rep.setHeader("Access-Control-Allow-Origin", "*");
        // 允许的访问方法
        rep.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE, PATCH");
        // Access-Control-Max-Age 用于 CORS 相关配置的缓存
        rep.setHeader("Access-Control-Max-Age", "3600");
        rep.setHeader("Access-Control-Allow-Headers", "token,userId,Origin, X-Requested-With, Content-Type, Accept");

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        //header方式
        String token = req.getHeader("token");
        String userId = req.getHeader("userId");

        String url = req.getRequestURI();
        log.info("当前请求url: {}", url);
        String method = ((HttpServletRequest) request).getMethod();
        log.info("获取到的方法值: {}", method);
        List<String> urls = new ArrayList<>();
        urls.add("/customer/user/loginSMS");
        urls.add("/customer/realNameAuthenMessage");
        urls.add("/customer//user/login");
        urls.add("/customer/user/register");
        urls.add("/customer/user/forgetPwd");
        urls.add("/customer/user/forgetPwd");
        urls.add("/customer/selectByOpenIdAndWXLogin");
        urls.add("/customer/insertWXRegister");
        urls.add("/customer/inviteRegisterByType");


        try {
            if (method.equals("OPTIONS")) {
                rep.setStatus(HttpServletResponse.SC_OK);
            } else if (null == token || "".equals(token) || urls.contains(url)) {
                filterChain.doFilter(request, response);
                System.out.println("filterChain.doFilter(request, response);執行完畢");
            } else {
                if (null == userId || userId.isEmpty()) {
                    log.error("缺少userId异常: {}", "缺少必要的参数,请重新登录!");
                    throw new RuntimeException("缺少必要的参数,请重新登录!");
                } else {
                    if (iRedisService.checkUserToken(Integer.parseInt(userId), token)) {
                        filterChain.doFilter(request, response);
                    } else {
                        log.error("token失效异常: {}", "token失效,请重新登录!");
                        throw new RuntimeException("Token失效,请重新登录!");
                    }
                }
            }
        } catch (Exception e) {
            PrintWriter writer = null;
            OutputStreamWriter osw = null;
            try {
                osw = new OutputStreamWriter(response.getOutputStream(),
                        StandardCharsets.UTF_8);
                writer = new PrintWriter(osw, true);

                Result result = Result.errorInfo(Result.ResultCode.UNAUTHORIZED, e.getMessage());
                String jsonStr = JSON.toJSONString(result);
                writer.write(jsonStr);
                writer.flush();
                writer.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (null != writer) {
                    writer.close();
                }
                if (null != osw) {
                    try {
                        osw.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void destroy() {

    }

}
