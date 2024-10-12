package cn.bootx.platform.baseapi.controller;

import cn.bootx.platform.core.annotation.IgnoreAuth;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import cn.hutool.core.codec.Base64;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;

/**
 * 系统基础接口
 * @author xxm
 * @since 2023/10/14
 */
@Slf4j
@Tag(name = "系统基础接口")
@RestController
@RequiredArgsConstructor
public class BaseController {

    @IgnoreAuth
    @Operation(summary = "回声测试")
    @GetMapping("/echo")
    public String echo(String msg){
        return "echo: "+msg;
    }

    @IgnoreAuth(login = true)
    @Operation(summary = "回声测试(必须要进行登录)")
    @GetMapping("/auth/echo")
    public String authEcho(String msg){
        return "echo:  "+msg;
    }

    @SneakyThrows
    @IgnoreAuth
    @Operation(summary = "读取文件文本内容")
    @PostMapping("/readText")
    public Result<String> readText(@RequestPart MultipartFile file){
        return Res.ok(new String(file.getBytes(), StandardCharsets.UTF_8));
    }

    @SneakyThrows
    @IgnoreAuth
    @Operation(summary = "将文件转换成base64")
    @PostMapping("/readBase64")
    public Result<String> readBase64(MultipartFile file){
        return Res.ok(Base64.encode(file.getBytes()));
    }


}
