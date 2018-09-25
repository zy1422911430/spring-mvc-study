package com.study.interceptor;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName: DemoInterceptor
 * @Description TODO 自定义拦截器，打印请求耗费时间(实现自定义拦截器有两种方式，1.继承HandlerInterceptorAdapter，2.实现HandlerInterceptor)
 * @Author: zyd
 * @Date: 2018/9/25 14:42
 * @Version: 1.0
 */
public class DemoInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime",startTime);
        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        long startTime = (Long)request.getAttribute("startTime");
        request.removeAttribute("startTime");
        long endTime = System.currentTimeMillis();
        System.out.println("本次请求执行时间为："+new Long(endTime-startTime));
        super.postHandle(request, response, handler, modelAndView);
    }
}
