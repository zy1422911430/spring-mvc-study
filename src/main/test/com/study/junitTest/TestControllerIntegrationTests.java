package com.study.junitTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.study.config.SpringMvcConfig;
import com.study.service.JunitTestService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.net.URI;

/**
 * @ClassName: TestControllerIntegrationTests
 * @Description TODO
 * @Author: zyd
 * @Date: 2018/9/26 10:03
 * @Version: 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringMvcConfig.class})
@WebAppConfiguration("src/main/resources")//声明ApplicationContext为WebApplicationContext,并指定资源路径为src/main/resources
public class TestControllerIntegrationTests {

    private MockMvc mockMvc;

    @Autowired
    private JunitTestService junitTestService;

    @Autowired
    WebApplicationContext webApplicationContext;//注入WebApplicationContext

    @Autowired
    MockHttpSession mockHttpSession;//注入模拟的HttpSession

    //测试之前，初始化调用
    @Before
    public void setup() {
        //初始化模拟mvc对象
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void testNormalController() throws Exception {
        mockMvc.perform(get("/normal"))//模拟想normal发送get请求
                .andExpect(status().isOk())//预期控制返回状态为200
                .andExpect(view().name("/page"))//预期设置view name为page
                .andExpect(forwardedUrl("/WEB-INF/classes/views/page.jsp"))//预期设置页面跳转路径为/WEB-INF/classes/views/page.jsp
                .andExpect(model().attribute("msg", junitTestService.getStr()));//预期设置model的值为getStr的返回值
    }

    @Test
    public void testRestController() throws Exception {
        mockMvc.perform(get("/testRest"))//模拟想testRest发送get请求
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))//预期设置媒体类型为text/plain;charset=UTF-8
                .andExpect(content().string(junitTestService.getStr()));
    }
}
