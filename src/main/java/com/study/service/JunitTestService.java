package com.study.service;

import org.springframework.stereotype.Service;

/**
 * @ClassName: JunitTestService
 * @Description TODO
 * @Author: zyd
 * @Date: 2018/9/26 9:49
 * @Version: 1.0
 */
@Service
public class JunitTestService {

    public String getStr(){
        return "junit test success";
    }
}
