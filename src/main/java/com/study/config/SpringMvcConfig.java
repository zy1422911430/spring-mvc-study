package com.study.config;

import com.study.interceptor.DemoInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

/**
 * @ClassName: SpringMvcConfig
 * @Description TODO SpringMvcConfig作为web框架的配置类，可继承WebMvcConfigurerAdapter类，重写该类的方法来自定义配置
 * @Author: zyd
 * @Date: 2018/9/25 11:29
 * @Version: 1.0
 */
@Configuration
@EnableWebMvc//使用EnableWebMvc注解开启spring mvc默认配置，如MessageConverter等
@ComponentScan("com.study")
public class SpringMvcConfig extends WebMvcConfigurerAdapter {

    @Bean
    public InternalResourceViewResolver viewResolver(){
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/classes/views");//spring mvc运行时会将views文件夹编译至/WEB-INF/classes目录下,因此指定目录为/WEB-INF/classes/views
        viewResolver.setSuffix(".jsp");
        viewResolver.setViewClass(JstlView.class);
        return viewResolver;
    }

    //声明自定义拦截器
    @Bean
    public DemoInterceptor demoInterceptor(){
        return new DemoInterceptor();
    }

    //注册自定义拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(demoInterceptor());
    }

    //addResourceLocations:添加文件防止目录，addResourceHandler:对外暴露的访问路径
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/assets/**").addResourceLocations("classpath:/assets/");
    }
}
