package cn.daxpay.multi.channel.wechat.controller.config;

import cn.bootx.platform.core.annotation.IgnoreAuth;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import cn.daxpay.multi.channel.wechat.param.config.WechatPayConfigParam;
import cn.daxpay.multi.channel.wechat.result.config.WechatPayConfigResult;
import cn.daxpay.multi.channel.wechat.service.config.WechatPayConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;

/**
 * @author xxm
 * @since 2021/3/19
 */
@Validated
@Tag(name = "微信支付配置")
@RestController
@RequestMapping("/wechat/pay/config")
@RequestGroup(groupCode = "WechatPayConfig", groupName = "微信支付配置", moduleCode = "wechatPay", moduleName = "微信支付")
@AllArgsConstructor
public class WechatPayConfigController {

    private final WechatPayConfigService wechatPayConfigService;

    @RequestPath("获取配置")
    @Operation(summary = "获取配置")
    @GetMapping("/findById")
    public Result<WechatPayConfigResult> findById(@NotNull(message = "ID不可为空") Long id) {
        return Res.ok(wechatPayConfigService.findById(id));
    }

    @RequestPath("新增或更新")
    @Operation(summary = "新增或更新")
    @PostMapping("/saveOrUpdate")
    public Result<Void> saveOrUpdate(@Valid @RequestBody WechatPayConfigParam param) {
        wechatPayConfigService.saveOrUpdate(param);
        return Res.ok();
    }

    @SneakyThrows
    @IgnoreAuth
    @Operation(summary = "读取证书文件内容")
    @PostMapping("/readPem")
    public Result<String> readPem(MultipartFile file){
        return Res.ok(new String(file.getBytes(), StandardCharsets.UTF_8));
    }

}
