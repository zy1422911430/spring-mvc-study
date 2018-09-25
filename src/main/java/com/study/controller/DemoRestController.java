package com.study.controller;

import com.study.entity.DemoEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: DemoRestController
 * @Description TODO
 * @Author: zyd
 * @Date: 2018/9/25 14:07
 * @Version: 1.0
 */
@RestController
@RequestMapping("/Rest")
public class DemoRestController {

    @RequestMapping(value = "/getjson",produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<DemoEntity> getJson(){
        return ResponseEntity.ok(new DemoEntity(1,"user1"));
    }

    @RequestMapping(value = "/getxml",produces = {"application/xml;charset=UTF-8"})
    public ResponseEntity<DemoEntity> getXml(){
        return ResponseEntity.ok(new DemoEntity(2,"user2"));
    }
}
