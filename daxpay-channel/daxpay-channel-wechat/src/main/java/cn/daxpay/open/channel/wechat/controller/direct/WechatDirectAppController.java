package cn.daxpay.open.channel.wechat.controller.direct;

import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.core.util.ValidationUtil;
import cn.daxpay.open.platform.core.validation.ValidationGroup;
import cn.daxpay.open.channel.wechat.param.direct.WechatDirectAppParam;
import cn.daxpay.open.channel.wechat.param.direct.WechatDirectAppAuthConfigParam;
import cn.daxpay.open.channel.wechat.result.direct.WechatDirectAppResult;
import cn.daxpay.open.channel.wechat.result.direct.WechatDirectAppAuthConfigResult;
import cn.daxpay.open.channel.wechat.service.direct.WechatDirectAppService;
import cn.daxpay.open.channel.wechat.service.direct.WechatDirectAppAuthConfigService;
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

/// # 微信直连商户应用管理
///
/// 提供直连商户应用及其密钥配置、授权认证配置的 REST API，支持按商户号和通道商户号查询列表。
///
@PermCode(menuCode = "channel:wechat:app")
@Validated
@Tag(name = "微信直连商户应用管理")
@RestController
@RequestMapping("/admin/wechat/mch-app")
@RequiredArgsConstructor
public class WechatDirectAppController {

    private final WechatDirectAppService wechatDirectAppService;
    private final WechatDirectAppAuthConfigService wechatDirectAppAuthConfigService;

    @PermCode(code = "view", nameCn = "通道商户查看", nameEn = "Channel Merchant View")
    @Operation(summary = "根据商户号和通道商户号查询应用列表")
    @GetMapping("/list-by-channel-mch-no")
    public Result<List<WechatDirectAppResult>> listByChannelMchNo(
            @NotBlank(message = "{validation.field.mchNo.notBlank}") String mchNo,
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo) {
        return Res.ok(wechatDirectAppService.listByMchNoAndChannelMchNo(mchNo, channelMchNo));
    }

    @PermCode(code = "view", nameCn = "通道商户查看", nameEn = "Channel Merchant View")
    @Operation(summary = "查询应用详情")
    @GetMapping("/find-by-id")
    public Result<WechatDirectAppResult> findById(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(wechatDirectAppService.findById(id));
    }

    @PermCode(code = "view", nameCn = "通道商户查看", nameEn = "Channel Merchant View")
    @Operation(summary = "同一通道商户下微信应用ID是否已存在")
    @GetMapping("/exists-wx-app-id-by-channel")
    public Result<Boolean> existsWxAppIdByChannel(
            @NotBlank(message = "{validation.field.mchNo.notBlank}") String mchNo,
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo,
            @NotBlank(message = "{validation.field.wxAppId.notBlank}") String wxAppId) {
        return Res.ok(wechatDirectAppService.existsWxAppIdByChannel(mchNo, channelMchNo, wxAppId, null));
    }

    @PermCode(code = "view", nameCn = "通道商户查看", nameEn = "Channel Merchant View")
    @Operation(summary = "同一通道商户下微信应用ID是否已存在(排除自身)")
    @GetMapping("/exists-wx-app-id-by-channel-not-id")
    public Result<Boolean> existsWxAppIdByChannelNotId(
            @NotBlank(message = "{validation.field.mchNo.notBlank}") String mchNo,
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo,
            @NotBlank(message = "{validation.field.wxAppId.notBlank}") String wxAppId,
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(wechatDirectAppService.existsWxAppIdByChannel(mchNo, channelMchNo, wxAppId, id));
    }

    @PermCode(code = "manage", nameCn = "通道商户管理", nameEn = "Channel Merchant Manage")
    @Operation(summary = "新增直连商户应用")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody @Validated(ValidationGroup.add.class) WechatDirectAppParam param) {
        ValidationUtil.validateParam(param, ValidationGroup.add.class);
        wechatDirectAppService.add(param);
        return Res.ok();
    }

    @PermCode(code = "manage", nameCn = "通道商户管理", nameEn = "Channel Merchant Manage")
    @Operation(summary = "修改直连商户应用")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) WechatDirectAppParam param) {
        ValidationUtil.validateParam(param, ValidationGroup.edit.class);
        wechatDirectAppService.update(param);
        return Res.ok();
    }

    @PermCode(code = "manage", nameCn = "通道商户管理", nameEn = "Channel Merchant Manage")
    @Operation(summary = "删除直连商户应用")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        wechatDirectAppService.delete(id);
        return Res.ok();
    }

    @PermCode(code = "view", nameCn = "通道商户查看", nameEn = "Channel Merchant View")
    @Operation(summary = "查询应用授权认证配置")
    @GetMapping("/find-auth-config-by-app-id")
    public Result<WechatDirectAppAuthConfigResult> findAuthConfigByAppId(
            @NotNull(message = "{validation.field.wechatDirectAppId.notNull}") Long wechatDirectAppId) {
        var config = wechatDirectAppAuthConfigService.findByWechatDirectAppId(wechatDirectAppId);
        return Res.ok(config.toResult());
    }

    @PermCode(code = "manage", nameCn = "通道商户管理", nameEn = "Channel Merchant Manage")
    @Operation(summary = "保存应用授权认证配置")
    @PostMapping("/save-auth-config")
    public Result<Void> saveAuthConfig(@RequestBody @Validated WechatDirectAppAuthConfigParam param) {
        wechatDirectAppAuthConfigService.save(param);
        return Res.ok();
    }
}
