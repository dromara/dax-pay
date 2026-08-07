package cn.daxpay.open.channel.alipay.controller.direct;

import cn.daxpay.open.channel.alipay.result.direct.AlipayTransferSceneConfigResult;
import cn.daxpay.open.channel.alipay.result.direct.AlipayTransferSceneOptionResult;
import cn.daxpay.open.channel.alipay.service.direct.AlipayTransferSceneConfigService;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/// # 支付宝转账场景配置管理(运营端)
///
/// 按通道商户维度管理转账场景(2026 新商户转账必配),挂在通道商户菜单下。
///
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "支付宝转账场景配置管理")
@RestController
@RequestMapping("/admin/alipay/transfer-scene")
@RequiredArgsConstructor
public class AlipayTransferSceneConfigController {

    private final AlipayTransferSceneConfigService alipayTransferSceneConfigService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "查询通道商户的转账场景列表")
    @GetMapping("/list")
    public Result<List<AlipayTransferSceneConfigResult>> list(
            @NotBlank(message = "{validation.field.mchNo.notBlank}") String mchNo,
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo) {
        return Res.ok(alipayTransferSceneConfigService.list(mchNo, channelMchNo));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "查询支付宝转账场景选项列表(主数据枚举投影)")
    @GetMapping("/scene-options")
    public Result<List<AlipayTransferSceneOptionResult>> sceneOptions() {
        return Res.ok(alipayTransferSceneConfigService.findSceneOptions());
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "设为默认转账场景(自动启用, 按场景名称按需创建)")
    @PostMapping("/set-default")
    public Result<Void> setDefault(
            @NotBlank(message = "{validation.field.mchNo.notBlank}") String mchNo,
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo,
            @NotBlank(message = "{validation.field.transferSceneName.notBlank}") String sceneName) {
        alipayTransferSceneConfigService.setDefault(mchNo, channelMchNo, sceneName);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "切换转账场景启用状态(最多启用3个, 按场景名称按需创建)")
    @PostMapping("/set-enabled")
    public Result<Void> setEnabled(
            @NotBlank(message = "{validation.field.mchNo.notBlank}") String mchNo,
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo,
            @NotBlank(message = "{validation.field.transferSceneName.notBlank}") String sceneName,
            @NotNull Boolean enabled) {
        alipayTransferSceneConfigService.setEnabled(mchNo, channelMchNo, sceneName, enabled);
        return Res.ok();
    }
}
