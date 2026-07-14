package cn.daxpay.open.channel.wechat.controller.isv;

import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.channel.wechat.convert.isv.WechatIsvKeyConfigConvert;
import cn.daxpay.open.channel.wechat.param.isv.WechatIsvKeyConfigParam;
import cn.daxpay.open.channel.wechat.result.isv.WechatIsvKeyConfigResult;
import cn.daxpay.open.channel.wechat.service.isv.WechatIsvKeyConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/// # 微信服务商密钥配置
///
@PermCode(menuCode = PermCodes.Payment.Isv.MENU)
@Validated
@Tag(name = "微信服务商密钥配置")
@RestController
@RequestMapping("/admin/wechat/isv-key-config")
@RequiredArgsConstructor
public class WechatIsvKeyConfigController {

    private final WechatIsvKeyConfigService wechatIsvKeyConfigService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "查询微信服务商密钥配置")
    @GetMapping("/find-config")
    public Result<WechatIsvKeyConfigResult> findConfig(
            @NotBlank(message = "{validation.field.product.notBlank}") String product) {
        return Res.ok(WechatIsvKeyConfigConvert.CONVERT.toResult(wechatIsvKeyConfigService.findByProduct(product)));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "保存微信服务商密钥配置")
    @PostMapping("/save-config")
    public Result<Void> saveConfig(@RequestBody @Validated WechatIsvKeyConfigParam param) {
        wechatIsvKeyConfigService.saveConfig(param);
        return Res.ok();
    }
}
