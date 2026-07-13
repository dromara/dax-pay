package cn.daxpay.open.channel.wechat.controller.isv;

import cn.daxpay.open.channel.wechat.param.isv.WechatIsvMchAppParam;
import cn.daxpay.open.channel.wechat.param.isv.WechatIsvMchAppAuthConfigParam;
import cn.daxpay.open.channel.wechat.result.isv.WechatIsvMchAppAuthConfigResult;
import cn.daxpay.open.channel.wechat.result.isv.WechatIsvMchAppResult;
import cn.daxpay.open.channel.wechat.service.isv.WechatIsvMchAppAuthConfigService;
import cn.daxpay.open.channel.wechat.service.isv.WechatIsvMchAppService;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.core.util.ValidationUtil;
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

/// # 微信服务商通道商户应用管理
///
/// 提供服务商通道商户应用(子商户应用)及其授权认证配置的 REST API,支持按商户号和通道商户号查询列表。
///
@PermCode(menuCode = PermCodes.Channel.WechatApp.MENU)
@Validated
@Tag(name = "微信服务商通道商户应用管理")
@RestController
@RequestMapping("/admin/wechat/isv-mch-app")
@RequiredArgsConstructor
public class WechatIsvMchAppController {

    private final WechatIsvMchAppService wechatIsvMchAppService;
    private final WechatIsvMchAppAuthConfigService wechatIsvMchAppAuthConfigService;

    @PermCode(code = PermCodes.Action.VIEW, nameCn = PermCodes.Channel.Merchant.VIEW_NAME_CN, nameEn = PermCodes.Channel.Merchant.VIEW_NAME_EN)
    @Operation(summary = "根据商户号和通道商户号查询应用列表")
    @GetMapping("/list-by-channel-mch-no")
    public Result<List<WechatIsvMchAppResult>> listByChannelMchNo(
            @NotBlank(message = "{validation.field.mchNo.notBlank}") String mchNo,
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo) {
        return Res.ok(wechatIsvMchAppService.listByMchNoAndChannelMchNo(mchNo, channelMchNo));
    }

    @PermCode(code = PermCodes.Action.VIEW, nameCn = PermCodes.Channel.Merchant.VIEW_NAME_CN, nameEn = PermCodes.Channel.Merchant.VIEW_NAME_EN)
    @Operation(summary = "查询应用详情")
    @GetMapping("/find-by-id")
    public Result<WechatIsvMchAppResult> findById(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(wechatIsvMchAppService.findById(id));
    }

    @PermCode(code = PermCodes.Action.VIEW, nameCn = PermCodes.Channel.Merchant.VIEW_NAME_CN, nameEn = PermCodes.Channel.Merchant.VIEW_NAME_EN)
    @Operation(summary = "同一通道商户下微信应用ID是否已存在")
    @GetMapping("/exists-wx-app-id-by-channel")
    public Result<Boolean> existsWxAppIdByChannel(
            @NotBlank(message = "{validation.field.mchNo.notBlank}") String mchNo,
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo,
            @NotBlank(message = "{validation.field.wxAppId.notBlank}") String wxAppId) {
        return Res.ok(wechatIsvMchAppService.existsWxAppIdByChannel(mchNo, channelMchNo, wxAppId, null));
    }

    @PermCode(code = PermCodes.Action.VIEW, nameCn = PermCodes.Channel.Merchant.VIEW_NAME_CN, nameEn = PermCodes.Channel.Merchant.VIEW_NAME_EN)
    @Operation(summary = "同一通道商户下微信应用ID是否已存在(排除自身)")
    @GetMapping("/exists-wx-app-id-by-channel-not-id")
    public Result<Boolean> existsWxAppIdByChannelNotId(
            @NotBlank(message = "{validation.field.mchNo.notBlank}") String mchNo,
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo,
            @NotBlank(message = "{validation.field.wxAppId.notBlank}") String wxAppId,
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(wechatIsvMchAppService.existsWxAppIdByChannel(mchNo, channelMchNo, wxAppId, id));
    }

    @PermCode(code = PermCodes.Action.MANAGE, nameCn = PermCodes.Channel.Merchant.MANAGE_NAME_CN, nameEn = PermCodes.Channel.Merchant.MANAGE_NAME_EN)
    @Operation(summary = "新增服务商通道商户应用")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody @Validated(ValidationGroup.add.class) WechatIsvMchAppParam param) {
        ValidationUtil.validateParam(param, ValidationGroup.add.class);
        wechatIsvMchAppService.add(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE, nameCn = PermCodes.Channel.Merchant.MANAGE_NAME_CN, nameEn = PermCodes.Channel.Merchant.MANAGE_NAME_EN)
    @Operation(summary = "修改服务商通道商户应用")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) WechatIsvMchAppParam param) {
        ValidationUtil.validateParam(param, ValidationGroup.edit.class);
        wechatIsvMchAppService.update(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE, nameCn = PermCodes.Channel.Merchant.MANAGE_NAME_CN, nameEn = PermCodes.Channel.Merchant.MANAGE_NAME_EN)
    @Operation(summary = "删除服务商通道商户应用")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        wechatIsvMchAppService.delete(id);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW, nameCn = PermCodes.Channel.Merchant.VIEW_NAME_CN, nameEn = PermCodes.Channel.Merchant.VIEW_NAME_EN)
    @Operation(summary = "查询应用授权认证配置")
    @GetMapping("/find-auth-config-by-app-id")
    public Result<WechatIsvMchAppAuthConfigResult> findAuthConfigByAppId(
            @NotNull(message = "{validation.field.wechatIsvMchAppId.notNull}") Long wechatIsvMchAppId) {
        var config = wechatIsvMchAppAuthConfigService.findByWechatIsvMchAppId(wechatIsvMchAppId);
        return Res.ok(config.toResult());
    }

    @PermCode(code = PermCodes.Action.MANAGE, nameCn = PermCodes.Channel.Merchant.MANAGE_NAME_CN, nameEn = PermCodes.Channel.Merchant.MANAGE_NAME_EN)
    @Operation(summary = "保存应用授权认证配置")
    @PostMapping("/save-auth-config")
    public Result<Void> saveAuthConfig(@RequestBody @Validated WechatIsvMchAppAuthConfigParam param) {
        wechatIsvMchAppAuthConfigService.save(param);
        return Res.ok();
    }
}
