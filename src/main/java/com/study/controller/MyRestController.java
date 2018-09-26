package com.study.controller;

import com.study.service.JunitTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: MyRestController
 * @Description TODO
 * @Author: zyd
 * @Date: 2018/9/26 10:48
 * @Version: 1.0
 */
@RestController
public class MyRestController {

    @Autowired
    private JunitTestService junitTestService;

    @RequestMapping(value = "testRest",produces = {"text/plain;charset=UTF-8"})
    public String testRest(){
        return junitTestService.getStr();
    }
}
