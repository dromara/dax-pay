package cn.daxpay.open.channel.alipay.controller.direct;

import cn.daxpay.open.channel.alipay.result.direct.AlipayTransferSceneConfigResult;
import cn.daxpay.open.channel.alipay.result.direct.AlipayTransferSceneOptionResult;
import cn.daxpay.open.channel.alipay.service.direct.AlipayTransferSceneConfigService;
import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.exception.BizInfoException;
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

/// # 支付宝转账场景配置管理(商户端)
///
/// 对照运营端 [AlipayTransferSceneConfigController],路径前缀 `/mch/alipay/transfer-scene`。
/// 商户号一律取自 [PaymentContext],忽略请求中的 mchNo,防越权。
///
@PermCode(menuCode = PermCodes.Channel.App.MENU)
@Validated
@Tag(name = "支付宝转账场景配置管理(商户端)")
@RestController
@RequestMapping("/mch/alipay/transfer-scene")
@RequiredArgsConstructor
public class MchAlipayTransferSceneConfigController {

    private final AlipayTransferSceneConfigService alipayTransferSceneConfigService;
    private final PaymentContext paymentContext;

    /// 当前登录商户号(上下文必有;缺则视为会话异常)
    private String requireMchNo() {
        String mchNo = paymentContext.getMchNo();
        if (mchNo == null || mchNo.isBlank()) {
            // 商户上下文缺失
            throw new BizInfoException(CommonCode.FAIL_CODE, "pay.error.assist.mchContextMissing");
        }
        return mchNo;
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "查询通道商户的转账场景列表")
    @GetMapping("/list")
    public Result<List<AlipayTransferSceneConfigResult>> list(
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo) {
        return Res.ok(alipayTransferSceneConfigService.list(requireMchNo(), channelMchNo));
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
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo,
            @NotBlank(message = "{validation.field.transferSceneName.notBlank}") String sceneName) {
        alipayTransferSceneConfigService.setDefault(requireMchNo(), channelMchNo, sceneName);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "切换转账场景启用状态(最多启用3个, 按场景名称按需创建)")
    @PostMapping("/set-enabled")
    public Result<Void> setEnabled(
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo,
            @NotBlank(message = "{validation.field.transferSceneName.notBlank}") String sceneName,
            @NotNull Boolean enabled) {
        alipayTransferSceneConfigService.setEnabled(requireMchNo(), channelMchNo, sceneName, enabled);
        return Res.ok();
    }
}
