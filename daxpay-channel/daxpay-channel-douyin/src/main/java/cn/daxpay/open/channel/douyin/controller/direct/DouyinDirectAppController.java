package cn.daxpay.open.channel.douyin.controller.direct;

import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.core.util.ValidationUtil;
import cn.daxpay.open.platform.core.validation.ValidationGroup;
import cn.daxpay.open.channel.douyin.param.direct.DouyinDirectAppParam;
import cn.daxpay.open.channel.douyin.param.direct.DouyinDirectAppAuthConfigParam;
import cn.daxpay.open.channel.douyin.result.direct.DouyinDirectAppResult;
import cn.daxpay.open.channel.douyin.result.direct.DouyinDirectAppAuthConfigResult;
import cn.daxpay.open.channel.douyin.service.direct.DouyinDirectAppService;
import cn.daxpay.open.channel.douyin.service.direct.DouyinDirectAppAuthConfigService;
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

/// # 抖音直连商户应用管理
///
/// 提供直连商户应用及其授权认证配置的 REST API，支持按商户号和通道商户号查询列表。
///
@PermCode(menuCode = "payment:douyin:mch-app")
@Validated
@Tag(name = "抖音直连商户应用管理")
@RestController
@RequestMapping("/admin/douyin/mch-app")
@RequiredArgsConstructor
public class DouyinDirectAppController {

    private final DouyinDirectAppService douyinDirectAppService;
    private final DouyinDirectAppAuthConfigService douyinDirectAppAuthConfigService;

    @PermCode(code = "view", nameCn = "通道商户查看", nameEn = "Channel Merchant View")
    @Operation(summary = "根据商户号和通道商户号查询应用列表")
    @GetMapping("/list-by-channel-mch-no")
    public Result<List<DouyinDirectAppResult>> listByChannelMchNo(
            @NotBlank(message = "{validation.field.mchNo.notBlank}") String mchNo,
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo) {
        return Res.ok(douyinDirectAppService.listByMchNoAndChannelMchNo(mchNo, channelMchNo));
    }

    @PermCode(code = "view", nameCn = "通道商户查看", nameEn = "Channel Merchant View")
    @Operation(summary = "查询应用详情")
    @GetMapping("/find-by-id")
    public Result<DouyinDirectAppResult> findById(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(douyinDirectAppService.findById(id));
    }

    @PermCode(code = "view", nameCn = "通道商户查看", nameEn = "Channel Merchant View")
    @Operation(summary = "同一通道商户下抖音应用ID是否已存在")
    @GetMapping("/exists-douyin-app-id-by-channel")
    public Result<Boolean> existsDouyinAppIdByChannel(
            @NotBlank(message = "{validation.field.mchNo.notBlank}") String mchNo,
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo,
            @NotBlank(message = "{validation.field.douyinAppId.notBlank}") String douyinAppId) {
        return Res.ok(douyinDirectAppService.existsDouyinAppIdByChannel(mchNo, channelMchNo, douyinAppId, null));
    }

    @PermCode(code = "view", nameCn = "通道商户查看", nameEn = "Channel Merchant View")
    @Operation(summary = "同一通道商户下抖音应用ID是否已存在(排除自身)")
    @GetMapping("/exists-douyin-app-id-by-channel-not-id")
    public Result<Boolean> existsDouyinAppIdByChannelNotId(
            @NotBlank(message = "{validation.field.mchNo.notBlank}") String mchNo,
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo,
            @NotBlank(message = "{validation.field.douyinAppId.notBlank}") String douyinAppId,
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(douyinDirectAppService.existsDouyinAppIdByChannel(mchNo, channelMchNo, douyinAppId, id));
    }

    @PermCode(code = "add", nameCn = "通道商户新增", nameEn = "Channel Merchant Add")
    @Operation(summary = "新增直连商户应用")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody @Validated(ValidationGroup.add.class) DouyinDirectAppParam param) {
        ValidationUtil.validateParam(param, ValidationGroup.add.class);
        douyinDirectAppService.add(param);
        return Res.ok();
    }

    @PermCode(code = "edit", nameCn = "通道商户编辑", nameEn = "Channel Merchant Edit")
    @Operation(summary = "修改直连商户应用")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) DouyinDirectAppParam param) {
        ValidationUtil.validateParam(param, ValidationGroup.edit.class);
        douyinDirectAppService.update(param);
        return Res.ok();
    }

    @PermCode(code = "edit", nameCn = "通道商户编辑", nameEn = "Channel Merchant Edit")
    @Operation(summary = "删除直连商户应用")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        douyinDirectAppService.delete(id);
        return Res.ok();
    }

    @PermCode(code = "view", nameCn = "通道商户查看", nameEn = "Channel Merchant View")
    @Operation(summary = "查询应用授权认证配置")
    @GetMapping("/find-auth-config-by-app-id")
    public Result<DouyinDirectAppAuthConfigResult> findAuthConfigByAppId(
            @NotNull(message = "{validation.field.douyinDirectAppId.notNull}") Long douyinDirectAppId) {
        var config = douyinDirectAppAuthConfigService.findByDouyinDirectAppId(douyinDirectAppId);
        return Res.ok(config.toResult());
    }

    @PermCode(code = "edit", nameCn = "通道商户编辑", nameEn = "Channel Merchant Edit")
    @Operation(summary = "保存应用授权认证配置")
    @PostMapping("/save-auth-config")
    public Result<Void> saveAuthConfig(@RequestBody @Validated DouyinDirectAppAuthConfigParam param) {
        douyinDirectAppAuthConfigService.save(param);
        return Res.ok();
    }
}
