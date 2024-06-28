package cn.daxpay.multi.channel.alipay.controller.config;

import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import cn.daxpay.multi.channel.alipay.param.config.AlipayConfigParam;
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
public class AlipayConfigController {
    private final AliPayConfigService alipayConfigService;

    @Operation(summary = "获取配置")
    @GetMapping("/findById")
    public Result<AlipayConfigResult> findById(@NotNull(message = "ID不可为空") Long id) {
        return Res.ok(alipayConfigService.findById(id));
    }

    @Operation(summary = "新增或更新")
    @PostMapping("/saveOrUpdate")
    public Result<Void> saveOrUpdate(@RequestBody AlipayConfigParam param) {
        alipayConfigService.saveOrUpdate(param);
        return Res.ok();
    }

    @SneakyThrows
    @Operation(summary = "读取证书文件内容")
    @PostMapping("/readPem")
    public Result<String> readPem(MultipartFile file){
        return Res.ok(new String(file.getBytes(), StandardCharsets.UTF_8));
    }

    @Operation(summary = "生成异步通知地址")
    @GetMapping("/generateNotifyUrl")
    public Result<String> generateNotifyUrl() {
        return Res.ok();
    }

    @Operation(summary = "生成同步通知地址")
    @GetMapping("/generateReturnUrl")
    public Result<String> generateReturnUrl() {
        return Res.ok();
    }
}
