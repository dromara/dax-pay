package cn.daxpay.open.channel.alipay.controller.direct;

import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.core.validation.ValidationGroup;
import cn.daxpay.open.channel.alipay.param.direct.AlipayDirectAppParam;
import cn.daxpay.open.channel.alipay.param.direct.AlipayDirectAppAuthConfigParam;
import cn.daxpay.open.channel.alipay.param.direct.AlipayDirectAppKeyConfigParam;
import cn.daxpay.open.channel.alipay.result.direct.AlipayDirectAppResult;
import cn.daxpay.open.channel.alipay.result.direct.AlipayDirectAppAuthConfigResult;
import cn.daxpay.open.channel.alipay.result.direct.AlipayDirectAppKeyConfigResult;
import cn.daxpay.open.channel.alipay.service.direct.AlipayDirectAppService;
import cn.daxpay.open.channel.alipay.service.direct.AlipayDirectAppAuthConfigService;
import cn.daxpay.open.channel.alipay.service.direct.AlipayDirectAppKeyConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/// # 支付宝直连商户应用管理
///
/// 提供直连商户应用及其密钥配置、授权认证配置的 REST API，支持按商户号和通道商户号查询列表。
///
@PermCode(menuCode = PermCodes.Channel.App.MENU)
@Validated
@Tag(name = "支付宝直连商户应用管理")
@RestController
@RequestMapping("/admin/alipay/mch-app")
@RequiredArgsConstructor
public class AlipayDirectAppController {

    private final AlipayDirectAppService alipayDirectAppService;
    private final AlipayDirectAppKeyConfigService alipayDirectAppKeyConfigService;
    private final AlipayDirectAppAuthConfigService alipayDirectAppAuthConfigService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据商户号和通道商户号查询应用列表")
    @GetMapping("/list-by-channel-mch-no")
    public Result<List<AlipayDirectAppResult>> listByChannelMchNo(
            @NotBlank(message = "{validation.field.mchNo.notBlank}") String mchNo,
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo) {
        return Res.ok(alipayDirectAppService.listByMchNoAndChannelMchNo(mchNo, channelMchNo));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "查询应用详情")
    @GetMapping("/find-by-id")
    public Result<AlipayDirectAppResult> findById(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(alipayDirectAppService.findById(id));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "同一通道商户下支付宝应用ID是否已存在")
    @GetMapping("/exists-ali-app-id-by-channel")
    public Result<Boolean> existsAliAppIdByChannel(
            @NotBlank(message = "{validation.field.mchNo.notBlank}") String mchNo,
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo,
            @NotBlank(message = "{validation.field.aliAppId.notBlank}") String aliAppId) {
        return Res.ok(alipayDirectAppService.existsAliAppIdByChannel(mchNo, channelMchNo, aliAppId, null));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "同一通道商户下支付宝应用ID是否已存在(排除自身)")
    @GetMapping("/exists-ali-app-id-by-channel-not-id")
    public Result<Boolean> existsAliAppIdByChannelNotId(
            @NotBlank(message = "{validation.field.mchNo.notBlank}") String mchNo,
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo,
            @NotBlank(message = "{validation.field.aliAppId.notBlank}") String aliAppId,
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(alipayDirectAppService.existsAliAppIdByChannel(mchNo, channelMchNo, aliAppId, id));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "新增直连商户应用")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody @Validated(ValidationGroup.add.class) AlipayDirectAppParam param) {
        alipayDirectAppService.add(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "修改直连商户应用")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) AlipayDirectAppParam param) {
        alipayDirectAppService.update(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "删除直连商户应用")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        alipayDirectAppService.delete(id);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "查询应用密钥配置")
    @GetMapping("/find-key-config-by-app-id")
    public Result<AlipayDirectAppKeyConfigResult> findKeyConfigByAppId(
            @NotNull(message = "{validation.field.alipayDirectAppId.notNull}") Long alipayDirectAppId,
            @NotNull(message = "{validation.field.sandbox.notNull}") Boolean sandbox) {
        return Res.ok(alipayDirectAppKeyConfigService.findByAlipayDirectAppId(alipayDirectAppId, sandbox).toResult());
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "保存应用密钥配置")
    @PostMapping("/save-key-config")
    public Result<Void> saveKeyConfig(@RequestBody @Validated AlipayDirectAppKeyConfigParam param) {
        alipayDirectAppKeyConfigService.save(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "查询应用授权认证配置")
    @GetMapping("/find-auth-config-by-app-id")
    public Result<AlipayDirectAppAuthConfigResult> findAuthConfigByAppId(
            @NotNull(message = "{validation.field.alipayDirectAppId.notNull}") Long alipayDirectAppId) {
        return Res.ok(alipayDirectAppAuthConfigService.findByAlipayDirectAppId(alipayDirectAppId).toResult());
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "保存应用授权认证配置")
    @PostMapping("/save-auth-config")
    public Result<Void> saveAuthConfig(@RequestBody @Validated AlipayDirectAppAuthConfigParam param) {
        alipayDirectAppAuthConfigService.save(param);
        return Res.ok();
    }
}
