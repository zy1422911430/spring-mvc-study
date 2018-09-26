package com.study.config;

import com.study.interceptor.DemoInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.*;
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
@EnableScheduling
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

    //重写addViewControllers，可指定访问路径所指向的页面
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/testAddView").setViewName("/index");
        registry.addViewController("/sse/push").setViewName("/sse");
        registry.addViewController("/defer").setViewName("/defer");
    }

    //这只spring mvc在解析请求路径是，不会忽略.后面的路径，如/Home/xx.yy
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setUseSuffixPatternMatch(false);
    }

    //声明上传文件的bean，这只上传最大文件大小限制
//    @Bean
//    public MultipartResolver multipartResolver(){
//        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
//        multipartResolver.setMaxUploadSize(100000);
//        return multipartResolver;
//    }
}
