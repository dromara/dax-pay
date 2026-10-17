package cn.daxpay.open.channel.fuyou.controller.rsa;

import cn.daxpay.open.channel.fuyou.convert.rsa.FuyouRsaKeyConfigConvert;
import cn.daxpay.open.channel.fuyou.param.rsa.FuyouRsaKeyConfigParam;
import cn.daxpay.open.channel.fuyou.result.rsa.FuyouRsaKeyConfigResult;
import cn.daxpay.open.channel.fuyou.service.rsa.FuyouRsaKeyConfigService;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 富友 RSA 密钥对配置
///
/// 「互联网直连」(fuyou_internet) 与「H5 聚合收银台」(fuyou_h5) 共用的 RSA 密钥对配置端点。
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "富友 RSA 密钥对配置")
@RestController
@RequestMapping("/admin/fuyou/rsa-key-config")
@RequiredArgsConstructor
public class FuyouRsaKeyConfigController {

    private final FuyouRsaKeyConfigService fuyouRsaKeyConfigService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "查询富友 RSA 密钥对配置")
    @GetMapping("/find-config")
    public Result<FuyouRsaKeyConfigResult> findConfig(
            @NotBlank(message = "{validation.field.channelMchNo.notBlank}") String channelMchNo) {
        return Res.ok(FuyouRsaKeyConfigConvert.CONVERT.toResult(fuyouRsaKeyConfigService.findByChannelMchNo(channelMchNo)));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "保存富友 RSA 密钥对配置")
    @PostMapping("/save-config")
    public Result<Void> saveConfig(@RequestBody @Validated FuyouRsaKeyConfigParam param) {
        fuyouRsaKeyConfigService.saveConfig(param);
        return Res.ok();
    }
}
