package com.study.controller;

import com.study.service.SchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * @ClassName: AsyncController
 * @Description TODO
 * @Author: zyd
 * @Date: 2018/9/25 17:43
 * @Version: 1.0
 */
@Controller
public class AsyncController {

    @Autowired
    private SchedulerService schedulerService;

    @RequestMapping("/async/defer")
    @ResponseBody
    public DeferredResult<String> defer(){
        return schedulerService.getDeferredResult();
    }
}
