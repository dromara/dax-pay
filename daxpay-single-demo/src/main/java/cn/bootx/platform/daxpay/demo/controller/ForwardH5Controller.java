package cn.bootx.platform.daxpay.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 嵌入式h5项目转发控制器, 不用输入 index.html也可以正常访问
 * @author xxm
 * @since 2024/2/10
 */
@Controller
@RequestMapping
public class ForwardH5Controller {
    /**
     * 将/h5/*的访问请求代理到/h5/index.html*
     */
    @GetMapping("/h5/")
    public String toH5(){
        return "forward:/h5/index.html";
    }

}
