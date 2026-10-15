package cn.daxpay.open.channel.fuyou.controller.direct;

import cn.daxpay.open.channel.fuyou.convert.direct.FuyouDirectKeyConfigConvert;
import cn.daxpay.open.channel.fuyou.param.direct.FuyouDirectKeyConfigParam;
import cn.daxpay.open.channel.fuyou.result.direct.FuyouDirectKeyConfigResult;
import cn.daxpay.open.channel.fuyou.service.direct.FuyouDirectKeyConfigService;
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

/// # 富友直连商户密钥配置
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "富友直连商户密钥配置")
@RestController
@RequestMapping("/admin/fuyou/direct-key-config")
@RequiredArgsConstructor
public class FuyouDirectKeyConfigController {

    private final FuyouDirectKeyConfigService fuyouDirectKeyConfigService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "查询富友直连商户密钥配置")
    @GetMapping("/find-config")
    public Result<FuyouDirectKeyConfigResult> findConfig(
            @NotBlank(message = "{validation.field.channelMchNo.notBlank}") String channelMchNo) {
        return Res.ok(FuyouDirectKeyConfigConvert.CONVERT.toResult(fuyouDirectKeyConfigService.findByChannelMchNo(channelMchNo)));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "保存富友直连商户密钥配置")
    @PostMapping("/save-config")
    public Result<Void> saveConfig(@RequestBody @Validated FuyouDirectKeyConfigParam param) {
        fuyouDirectKeyConfigService.saveConfig(param);
        return Res.ok();
    }
}
