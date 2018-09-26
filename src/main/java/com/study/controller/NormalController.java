package com.study.controller;

import com.study.service.JunitTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ClassName: NormalController
 * @Description TODO
 * @Author: zyd
 * @Date: 2018/9/26 10:47
 * @Version: 1.0
 */
@Controller
public class NormalController {

    @Autowired
    private JunitTestService junitTestService;

    @RequestMapping("normal")
    public String testPage(Model model){
        model.addAttribute("msg",junitTestService.getStr());
        return "/page";
    }
}
