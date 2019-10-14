package com.study.controller;

import com.study.service.SchedulerService;
import org.apache.tools.ant.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

    public static void main(String[] args) {
        /*Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -4);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd");
        Date time = calendar.getTime();
        Integer day = Integer.parseInt(simpleDateFormat1.format(time));
        for (int i = 0; i < 5 ; i++) {
            calendar = Calendar.getInstance();
            calendar.setTime(time);
            calendar.add(Calendar.DATE, i);
            Date time1 = calendar.getTime();
            System.out.println(simpleDateFormat.format(time1));
            System.out.println(day + i);
        }*/
    }
}
