package com.study.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ClassName: HomeController
 * @Description TODO 访问主页控制器
 * @Author: zyd
 * @Date: 2018/9/25 11:45
 * @Version: 1.0
 */
@Controller
@RequestMapping("Home")
public class HomeController {

    @RequestMapping("/")
    public String home(){
        return "/index";
    }
}
