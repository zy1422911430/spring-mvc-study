package com.study.config;

import org.springframework.context.ApplicationContext;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * @ClassName: WebInitializer
 * @Description TODO servlet 3.0容器配置，实现WebApplicationInitializer，spring容器启动是，自动装载WebInitializer配置类，配置servlet 3.0+配置的接口，替代web.xml
 * @Author: zyd
 * @Date: 2018/9/25 11:36
 * @Version: 1.0
 */
public class WebInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
        applicationContext.register(SpringMvcConfig.class);
        applicationContext.setServletContext(servletContext);//注册servletContext以及配置类SpringMvcConfig
        //注册DispatcherServlet
        ServletRegistration.Dynamic servlet = servletContext.addServlet("dispatcher", new DispatcherServlet(applicationContext));
        servlet.addMapping("/");
        servlet.setLoadOnStartup(1);
        servlet.setAsyncSupported(true);
    }
}
