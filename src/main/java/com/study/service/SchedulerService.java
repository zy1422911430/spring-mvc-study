package com.study.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * @ClassName: SchedulerService
 * @Description TODO
 * @Author: zyd
 * @Date: 2018/9/25 17:45
 * @Version: 1.0
 */
@Service
public class SchedulerService {

    private DeferredResult<String> deferredResult;

    int i = 0;

    public DeferredResult getDeferredResult(){
        deferredResult = new DeferredResult<String>();
        return deferredResult;
    }

    @Scheduled(fixedDelay = 3000)
    public void refresh(){
        System.out.println("执行了"+(i++)+"次");
        if(deferredResult != null){
            deferredResult.setResult(new Long(System.currentTimeMillis()).toString());
        }
    }
}
