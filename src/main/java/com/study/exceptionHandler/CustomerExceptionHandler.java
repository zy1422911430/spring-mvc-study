package com.study.exceptionHandler;

import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

/**
 * @ClassName: CustomerExceptionHandler
 * @Description TODO 全局异常捕获并对异常进行处理，跳转异常页面
 * @Author: zyd
 * @Date: 2018/9/25 14:54
 * @Version: 1.0
 */
@ControllerAdvice
public class CustomerExceptionHandler {

    //定义全局异常处理，value属性指定异常类型
    @ExceptionHandler(value = Exception.class)
    public ModelAndView handler(Exception exception, WebRequest request){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/error");
        modelAndView.addObject("errorMessage",exception.getMessage());
        return modelAndView;
    }

    //定义全局的model，所有注解了@RequestMapping的方法都可以获取到此model的值
    @ModelAttribute
    public void addAttribute(Model model){
        model.addAttribute("msg","加入的自定义信息");
    }

    //通过@InitBinder注解定制WebDataBinder，此处设置忽略request参数的id
    @InitBinder
    public void initBinder(WebDataBinder webDataBinder){
        webDataBinder.setDisallowedFields("id");
    }
}
