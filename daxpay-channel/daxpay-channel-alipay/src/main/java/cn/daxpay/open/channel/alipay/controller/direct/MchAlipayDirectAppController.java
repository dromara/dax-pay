package cn.daxpay.open.channel.alipay.controller.direct;

import cn.daxpay.open.channel.alipay.param.direct.AlipayDirectAppAuthConfigParam;
import cn.daxpay.open.channel.alipay.param.direct.AlipayDirectAppKeyConfigParam;
import cn.daxpay.open.channel.alipay.param.direct.AlipayDirectAppParam;
import cn.daxpay.open.channel.alipay.result.direct.AlipayDirectAppAuthConfigResult;
import cn.daxpay.open.channel.alipay.result.direct.AlipayDirectAppKeyConfigResult;
import cn.daxpay.open.channel.alipay.result.direct.AlipayDirectAppResult;
import cn.daxpay.open.channel.alipay.service.direct.AlipayDirectAppAuthConfigService;
import cn.daxpay.open.channel.alipay.service.direct.AlipayDirectAppKeyConfigService;
import cn.daxpay.open.channel.alipay.service.direct.AlipayDirectAppService;
import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/// # 支付宝直连商户应用管理（商户端）
///
/// 对照运营端 [AlipayDirectAppController]，路径前缀 `/mch/alipay/direct-app`。
/// 商户号一律取自 [PaymentContext]，忽略请求中的 mchNo，防越权。
@PermCode(menuCode = PermCodes.Channel.App.MENU)
@Validated
@Tag(name = "支付宝直连商户应用管理(商户端)")
@RestController
@RequestMapping("/mch/alipay/direct-app")
@RequiredArgsConstructor
public class MchAlipayDirectAppController {

    private final AlipayDirectAppService alipayDirectAppService;
    private final AlipayDirectAppKeyConfigService alipayDirectAppKeyConfigService;
    private final AlipayDirectAppAuthConfigService alipayDirectAppAuthConfigService;
    private final PaymentContext paymentContext;

    /// 当前登录商户号（上下文必有；缺则视为会话异常）
    private String requireMchNo() {
        String mchNo = paymentContext.getMchNo();
        if (mchNo == null || mchNo.isBlank()) {
            // 商户上下文缺失
            throw new BizInfoException(CommonCode.FAIL_CODE, "pay.error.assist.mchContextMissing");
        }
        return mchNo;
    }

    /// 校验应用归属当前商户
    private void assertOwned(AlipayDirectAppResult result) {
        if (!Objects.equals(result.getMchNo(), requireMchNo())) {
            // 支付宝直连商户应用不属于当前商户
            throw new BizInfoException(CommonCode.FAIL_CODE, "error.channel.alipay.mchAppNotFound");
        }
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据通道商户号查询应用列表")
    @GetMapping("/list-by-channel-mch-no")
    public Result<List<AlipayDirectAppResult>> listByChannelMchNo(
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo) {
        return Res.ok(alipayDirectAppService.listByMchNoAndChannelMchNo(requireMchNo(), channelMchNo));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "查询应用详情")
    @GetMapping("/find-by-id")
    public Result<AlipayDirectAppResult> findById(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        AlipayDirectAppResult result = alipayDirectAppService.findById(id);
        this.assertOwned(result);
        return Res.ok(result);
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "同一通道商户下支付宝应用ID是否已存在")
    @GetMapping("/exists-ali-app-id-by-channel")
    public Result<Boolean> existsAliAppIdByChannel(
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo,
            @NotBlank(message = "{validation.field.aliAppId.notBlank}") String aliAppId) {
        return Res.ok(alipayDirectAppService.existsAliAppIdByChannel(requireMchNo(), channelMchNo, aliAppId, null));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "同一通道商户下支付宝应用ID是否已存在(排除自身)")
    @GetMapping("/exists-ali-app-id-by-channel-not-id")
    public Result<Boolean> existsAliAppIdByChannelNotId(
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo,
            @NotBlank(message = "{validation.field.aliAppId.notBlank}") String aliAppId,
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(alipayDirectAppService.existsAliAppIdByChannel(requireMchNo(), channelMchNo, aliAppId, id));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "新增直连商户应用")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody @Validated(ValidationGroup.add.class) AlipayDirectAppParam param) {
        // 强制当前商户号，忽略客户端传入（防越权）
        param.setMchNo(requireMchNo());
        alipayDirectAppService.add(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "修改直连商户应用")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) AlipayDirectAppParam param) {
        this.assertOwned(alipayDirectAppService.findById(param.getId()));
        // 强制当前商户号，忽略客户端传入（防越权）
        param.setMchNo(requireMchNo());
        alipayDirectAppService.update(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "删除直连商户应用")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        this.assertOwned(alipayDirectAppService.findById(id));
        alipayDirectAppService.delete(id);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "查询应用密钥配置")
    @GetMapping("/find-key-config-by-app-id")
    public Result<AlipayDirectAppKeyConfigResult> findKeyConfigByAppId(
            @NotNull(message = "{validation.field.alipayDirectAppId.notNull}") Long alipayDirectAppId,
            @NotNull(message = "{validation.field.sandbox.notNull}") Boolean sandbox) {
        // 先校验应用归属当前商户
        this.assertOwned(alipayDirectAppService.findById(alipayDirectAppId));
        return Res.ok(alipayDirectAppKeyConfigService.findByAlipayDirectAppId(alipayDirectAppId, sandbox).toResult());
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "保存应用密钥配置")
    @PostMapping("/save-key-config")
    public Result<Void> saveKeyConfig(@RequestBody @Validated AlipayDirectAppKeyConfigParam param) {
        // 先校验应用归属当前商户
        this.assertOwned(alipayDirectAppService.findById(param.getAlipayDirectAppId()));
        alipayDirectAppKeyConfigService.save(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "查询应用授权认证配置")
    @GetMapping("/find-auth-config-by-app-id")
    public Result<AlipayDirectAppAuthConfigResult> findAuthConfigByAppId(
            @NotNull(message = "{validation.field.alipayDirectAppId.notNull}") Long alipayDirectAppId) {
        // 先校验应用归属当前商户
        this.assertOwned(alipayDirectAppService.findById(alipayDirectAppId));
        return Res.ok(alipayDirectAppAuthConfigService.findByAlipayDirectAppId(alipayDirectAppId).toResult());
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "保存应用授权认证配置")
    @PostMapping("/save-auth-config")
    public Result<Void> saveAuthConfig(@RequestBody @Validated AlipayDirectAppAuthConfigParam param) {
        // 先校验应用归属当前商户
        this.assertOwned(alipayDirectAppService.findById(param.getAlipayDirectAppId()));
        alipayDirectAppAuthConfigService.save(param);
        return Res.ok();
    }
}
