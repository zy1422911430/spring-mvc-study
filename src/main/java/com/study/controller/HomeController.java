package com.study.controller;

import com.study.entity.DemoEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ClassName: HomeController
 * @Description TODO 访问主页控制器
 * @Author: zyd
 * @Date: 2018/9/25 11:45
 * @Version: 1.0
 */
@Controller//声明此类为控制器，虽然@Controller/@Service/@Repository都组合了@Component注解，但是必须使用@Controller声明
@RequestMapping("Home")
public class HomeController {

    /*
     * @RequestMapping :用来映射请求（访问路径和参数）、处理类和方法的
     * @ResponseBody :将返回值放入Response里，返回的并非一个页面
     * @RequestBody :允许将参数放入request体中，而不是直接放在链接上
     * @PathVariable :用来接收路径参数，如/user/1,可以获取1作为参数
     * @MyRestController :@ResponseBody和@Controller注解的组合，声明此类的所有方法的返回值都放入Response中，而不是返回页面
     **/

    @RequestMapping("/")
    public String home(){
        return "/index";
    }

    //由于设置了忽略request参数的id，此处demoEntity的id将为空
    @RequestMapping("/error")
    public String error(@ModelAttribute("msg") String msg, DemoEntity demoEntity){
        throw new IllegalArgumentException("异常发生了,msg="+msg);
    }
}
