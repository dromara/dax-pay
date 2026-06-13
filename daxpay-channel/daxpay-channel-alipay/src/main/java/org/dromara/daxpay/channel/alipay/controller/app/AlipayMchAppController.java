package org.dromara.daxpay.channel.alipay.controller.app;

import org.dromara.daxpay.platform.core.annotation.PermCode;
import org.dromara.daxpay.platform.core.rest.Res;
import org.dromara.daxpay.platform.core.rest.result.Result;
import org.dromara.daxpay.platform.core.util.ValidationUtil;
import org.dromara.daxpay.platform.core.validation.ValidationGroup;
import org.dromara.daxpay.channel.alipay.param.app.AlipayMchAppParam;
import org.dromara.daxpay.channel.alipay.param.config.AlipayMchAppAuthConfigParam;
import org.dromara.daxpay.channel.alipay.param.config.AlipayMchAppKeyConfigParam;
import org.dromara.daxpay.channel.alipay.result.app.AlipayMchAppResult;
import org.dromara.daxpay.channel.alipay.result.config.AlipayMchAppAuthConfigResult;
import org.dromara.daxpay.channel.alipay.result.config.AlipayMchAppKeyConfigResult;
import org.dromara.daxpay.channel.alipay.service.app.AlipayMchAppService;
import org.dromara.daxpay.channel.alipay.service.config.AlipayMchAppAuthConfigService;
import org.dromara.daxpay.channel.alipay.service.config.AlipayMchAppKeyConfigService;
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
@PermCode(menuCode = "payment:alipay:mch-app")
@Validated
@Tag(name = "支付宝直连商户应用管理")
@RestController
@RequestMapping("/admin/alipay/mch-app")
@RequiredArgsConstructor
public class AlipayMchAppController {

    private final AlipayMchAppService alipayMchAppService;
    private final AlipayMchAppKeyConfigService alipayMchAppKeyConfigService;
    private final AlipayMchAppAuthConfigService alipayMchAppAuthConfigService;

    @PermCode(code = "view", nameCn = "通道商户查看", nameEn = "Channel Merchant View")
    @Operation(summary = "根据商户号和通道商户号查询应用列表")
    @GetMapping("/list-by-channel-mch-no")
    public Result<List<AlipayMchAppResult>> listByChannelMchNo(
            @NotBlank(message = "{validation.field.mchNo.notBlank}") String mchNo,
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo) {
        return Res.ok(alipayMchAppService.listByMchNoAndChannelMchNo(mchNo, channelMchNo));
    }

    @PermCode(code = "view", nameCn = "通道商户查看", nameEn = "Channel Merchant View")
    @Operation(summary = "查询应用详情")
    @GetMapping("/find-by-id")
    public Result<AlipayMchAppResult> findById(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(alipayMchAppService.findById(id));
    }

    @PermCode(code = "view", nameCn = "通道商户查看", nameEn = "Channel Merchant View")
    @Operation(summary = "同一通道商户下支付宝应用ID是否已存在")
    @GetMapping("/exists-ali-app-id-by-channel")
    public Result<Boolean> existsAliAppIdByChannel(
            @NotBlank(message = "{validation.field.mchNo.notBlank}") String mchNo,
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo,
            @NotBlank(message = "{validation.field.aliAppId.notBlank}") String aliAppId) {
        return Res.ok(alipayMchAppService.existsAliAppIdByChannel(mchNo, channelMchNo, aliAppId, null));
    }

    @PermCode(code = "view", nameCn = "通道商户查看", nameEn = "Channel Merchant View")
    @Operation(summary = "同一通道商户下支付宝应用ID是否已存在(排除自身)")
    @GetMapping("/exists-ali-app-id-by-channel-not-id")
    public Result<Boolean> existsAliAppIdByChannelNotId(
            @NotBlank(message = "{validation.field.mchNo.notBlank}") String mchNo,
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo,
            @NotBlank(message = "{validation.field.aliAppId.notBlank}") String aliAppId,
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(alipayMchAppService.existsAliAppIdByChannel(mchNo, channelMchNo, aliAppId, id));
    }

    @PermCode(code = "add", nameCn = "通道商户新增", nameEn = "Channel Merchant Add")
    @Operation(summary = "新增直连商户应用")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody @Validated(ValidationGroup.add.class) AlipayMchAppParam param) {
        ValidationUtil.validateParam(param, ValidationGroup.add.class);
        alipayMchAppService.add(param);
        return Res.ok();
    }

    @PermCode(code = "edit", nameCn = "通道商户编辑", nameEn = "Channel Merchant Edit")
    @Operation(summary = "修改直连商户应用")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) AlipayMchAppParam param) {
        ValidationUtil.validateParam(param, ValidationGroup.edit.class);
        alipayMchAppService.update(param);
        return Res.ok();
    }

    @PermCode(code = "edit", nameCn = "通道商户编辑", nameEn = "Channel Merchant Edit")
    @Operation(summary = "删除直连商户应用")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        alipayMchAppService.delete(id);
        return Res.ok();
    }

    @PermCode(code = "view", nameCn = "通道商户查看", nameEn = "Channel Merchant View")
    @Operation(summary = "查询应用密钥配置")
    @GetMapping("/find-key-config-by-app-id")
    public Result<AlipayMchAppKeyConfigResult> findKeyConfigByAppId(
            @NotNull(message = "{validation.field.id.notNull}") Long appId) {
        return Res.ok(alipayMchAppKeyConfigService.findByAppId(appId));
    }

    @PermCode(code = "edit", nameCn = "通道商户编辑", nameEn = "Channel Merchant Edit")
    @Operation(summary = "保存应用密钥配置")
    @PostMapping("/save-key-config")
    public Result<Void> saveKeyConfig(@RequestBody @Validated AlipayMchAppKeyConfigParam param) {
        alipayMchAppKeyConfigService.save(param);
        return Res.ok();
    }

    @PermCode(code = "view", nameCn = "通道商户查看", nameEn = "Channel Merchant View")
    @Operation(summary = "查询应用授权认证配置")
    @GetMapping("/find-auth-config-by-app-id")
    public Result<AlipayMchAppAuthConfigResult> findAuthConfigByAppId(
            @NotNull(message = "{validation.field.id.notNull}") Long appId) {
        return Res.ok(alipayMchAppAuthConfigService.findByAppId(appId));
    }

    @PermCode(code = "edit", nameCn = "通道商户编辑", nameEn = "Channel Merchant Edit")
    @Operation(summary = "保存应用授权认证配置")
    @PostMapping("/save-auth-config")
    public Result<Void> saveAuthConfig(@RequestBody @Validated AlipayMchAppAuthConfigParam param) {
        alipayMchAppAuthConfigService.save(param);
        return Res.ok();
    }
}
