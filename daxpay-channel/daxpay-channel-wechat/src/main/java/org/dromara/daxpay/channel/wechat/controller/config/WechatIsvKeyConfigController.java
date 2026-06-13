package org.dromara.daxpay.channel.wechat.controller.config;

import org.dromara.daxpay.platform.core.annotation.PermCode;
import org.dromara.daxpay.platform.core.rest.Res;
import org.dromara.daxpay.platform.core.rest.result.Result;
import org.dromara.daxpay.channel.wechat.param.config.WechatIsvKeyConfigParam;
import org.dromara.daxpay.channel.wechat.result.config.WechatIsvKeyConfigResult;
import org.dromara.daxpay.channel.wechat.service.config.WechatIsvKeyConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/// # 微信服务商密钥配置
///
@PermCode(menuCode = "payment:wechat:isv")
@Validated
@Tag(name = "微信服务商密钥配置")
@RestController
@RequestMapping("/admin/wechat/isv-key-config")
@RequiredArgsConstructor
public class WechatIsvKeyConfigController {

    private final WechatIsvKeyConfigService wechatIsvKeyConfigService;

    @PermCode(code = "view", nameCn = "微信服务商查看", nameEn = "WeChat ISV View")
    @Operation(summary = "查询微信服务商密钥配置")
    @GetMapping("/find-config")
    public Result<WechatIsvKeyConfigResult> findConfig(
            @NotBlank(message = "{validation.field.product.notBlank}") String product) {
        return Res.ok(wechatIsvKeyConfigService.findByProduct(product));
    }

    @PermCode(code = "edit", nameCn = "微信服务商编辑", nameEn = "WeChat ISV Edit")
    @Operation(summary = "保存微信服务商密钥配置")
    @PostMapping("/save-config")
    public Result<Void> saveConfig(@RequestBody @Validated WechatIsvKeyConfigParam param) {
        wechatIsvKeyConfigService.saveConfig(param);
        return Res.ok();
    }
}
