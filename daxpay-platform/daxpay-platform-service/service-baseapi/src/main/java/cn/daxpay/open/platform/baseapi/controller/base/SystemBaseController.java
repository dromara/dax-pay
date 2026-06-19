package cn.daxpay.open.platform.baseapi.controller.base;

import cn.daxpay.open.platform.baseapi.service.base.SystemBaseService;
import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.hutool.core.codec.Base64;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/// # 系统基础接口
///
@Validated
@Tag(name = "系统基础接口")
@RestController
@RequiredArgsConstructor
public class SystemBaseController {

    private final SystemBaseService systemBaseService;

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

    @IgnoreAuth
    @Operation(summary = "系统信息")
    @GetMapping("/info")
    public Map<String, Object> info(){
        return systemBaseService.info();
    }

    @SneakyThrows
    @IgnoreAuth
    @Operation(summary = "读取文件文本内容")
    @PostMapping("/read-text")
    public Result<String> readText(@RequestPart MultipartFile file){
        return Res.ok(new String(file.getBytes(), StandardCharsets.UTF_8));
    }

    @SneakyThrows
    @IgnoreAuth
    @Operation(summary = "将文件转换成base64")
    @PostMapping("/read-base64")
    public Result<String> readBase64(@RequestPart MultipartFile file){
        return Res.ok(Base64.encode(file.getBytes()));
    }

}
