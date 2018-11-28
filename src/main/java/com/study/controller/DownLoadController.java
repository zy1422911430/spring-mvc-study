package com.study.controller;

import com.study.service.DownLoadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName: DownLoadController
 * @Description TODO
 * @Author: zyd
 * @Date: 2018/11/22 9:38
 * @Version: 1.0
 */
@Controller
@RequestMapping("/DownLoad")
public class DownLoadController {

    @Autowired
    private DownLoadService downLoadService;

    @RequestMapping("/Excel")
    public void excel(HttpServletResponse response){
        downLoadService.getExcel();
    }
}
