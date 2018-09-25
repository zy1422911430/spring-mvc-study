package com.study.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

/**
 * @ClassName: SpringMvcConfig
 * @Description TODO
 * @Author: zyd
 * @Date: 2018/9/25 11:29
 * @Version: 1.0
 */
@Configuration
@EnableWebMvc//使用EnableWebMvc注解开启spring mvc默认配置，如MessageConverter等
@ComponentScan("com.study")
public class SpringMvcConfig {

    @Bean
    public InternalResourceViewResolver viewResolver(){
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/classes/views");//spring mvc运行时会将views文件夹编译至/WEB-INF/classes目录下,因此指定目录为/WEB-INF/classes/views
        viewResolver.setSuffix(".jsp");
        viewResolver.setViewClass(JstlView.class);
        return viewResolver;
    }
}
