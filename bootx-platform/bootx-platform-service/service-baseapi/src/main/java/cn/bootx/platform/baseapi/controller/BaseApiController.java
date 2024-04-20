package cn.bootx.platform.baseapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统基础接口
 * @author xxm
 * @since 2023/10/14
 */
@Tag(name = "系统基础接口")
@RestController
@RequiredArgsConstructor
public class BaseApiController {

    @Operation(summary = "回声测试")
    @GetMapping("/echo")
    public String echo(String msg){
        return "echo: "+msg;
    }

    @Operation(summary = "回声测试(必须要进行登录)")
    @GetMapping("/auth/echo")
    public String authEcho(String msg){
        return "echo:  "+msg;
    }
}
