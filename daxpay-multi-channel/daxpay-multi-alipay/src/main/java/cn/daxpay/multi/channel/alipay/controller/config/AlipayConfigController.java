package cn.daxpay.multi.channel.alipay.controller.config;

import cn.bootx.platform.core.annotation.IgnoreAuth;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import cn.daxpay.multi.channel.alipay.param.config.AliPayConfigParam;
import cn.daxpay.multi.channel.alipay.result.config.AlipayConfigResult;
import cn.daxpay.multi.channel.alipay.service.config.AliPayConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;

/**
 * 支付宝配置控制器
 * @author xxm
 * @since 2024/6/26
 */
@Validated
@Tag(name = "支付宝配置控制器")
@RestController
@RequestMapping("/alipay/config")
@RequiredArgsConstructor
@RequestGroup(groupCode = "AlipayConfig", groupName = "支付宝配置", moduleCode = "alipay", moduleName = "支付宝支付")
public class AlipayConfigController {
    private final AliPayConfigService alipayConfigService;

    @RequestPath("获取配置")
    @Operation(summary = "获取配置")
    @GetMapping("/findById")
    public Result<AlipayConfigResult> findById(@NotNull(message = "ID不可为空") Long id) {
        return Res.ok(alipayConfigService.findById(id));
    }

    @RequestPath("新增或更新")
    @Operation(summary = "新增或更新")
    @PostMapping("/saveOrUpdate")
    public Result<Void> saveOrUpdate(@RequestBody AliPayConfigParam param) {
        alipayConfigService.saveOrUpdate(param);
        return Res.ok();
    }

    @SneakyThrows
    @IgnoreAuth
    @Operation(summary = "读取证书文件内容")
    @PostMapping("/readPem")
    public Result<String> readPem(@RequestPart MultipartFile file){
        return Res.ok(new String(file.getBytes(), StandardCharsets.UTF_8));
    }

}
