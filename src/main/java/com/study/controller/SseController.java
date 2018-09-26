package com.study.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Random;

/**
 * @ClassName: SseController
 * @Description TODO
 * @Author: zyd
 * @Date: 2018/9/25 15:40
 * @Version: 1.0
 */
@Controller
@RequestMapping(value = "/sse")
public class SseController {

    //text/event-stream 为服务器端SSE的支持，此示例演示没1s中想客户端推送一条消息
    @RequestMapping(value = "/event",produces = "text/event-stream")
    public @ResponseBody String push(){
        Random random = new Random();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "服务器返回了："+random.nextInt();
    }
}
