package cn.daxpay.open.channel.douyin.controller.merchant;

import cn.daxpay.open.channel.douyin.param.direct.DouyinDirectAppAuthConfigParam;
import cn.daxpay.open.channel.douyin.param.direct.DouyinDirectAppParam;
import cn.daxpay.open.channel.douyin.result.direct.DouyinDirectAppAuthConfigResult;
import cn.daxpay.open.channel.douyin.result.direct.DouyinDirectAppResult;
import cn.daxpay.open.channel.douyin.service.direct.DouyinDirectAppAuthConfigService;
import cn.daxpay.open.channel.douyin.service.direct.DouyinDirectAppService;
import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.merchant.dao.channel.ChannelMerchantManager;
import cn.daxpay.open.payment.merchant.entity.channel.ChannelMerchant;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.config.ConfigErrorException;
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
import java.util.Objects;

/// # 抖音直连商户应用管理（商户端）
///
/// 对照运营端 [cn.daxpay.open.channel.douyin.controller.direct.DouyinDirectAppController]，路径前缀 `/mch/douyin/mch-app`。
/// 商户号一律取自 [PaymentContext]，写操作覆盖请求中的 mchNo，防越权。
@PermCode(menuCode = PermCodes.Channel.App.MENU)
@Validated
@Tag(name = "抖音直连商户应用管理(商户端)")
@RestController
@RequestMapping("/mch/douyin/mch-app")
@RequiredArgsConstructor
public class MchDouyinDirectAppController {

    private final DouyinDirectAppService douyinDirectAppService;
    private final DouyinDirectAppAuthConfigService douyinDirectAppAuthConfigService;
    private final ChannelMerchantManager channelMerchantManager;
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

    /// 校验通道商户属于当前商户（防越权）
    private void assertChannelMchOwned(String channelMchNo) {
        ChannelMerchant channelMerchant = channelMerchantManager.findByChannelMchNo(channelMchNo)
                // 抖音: 通道商户不存在或商户号不匹配
                .orElseThrow(() -> new ConfigErrorException("error.channel.douyin.mchAppNotFound"));
        if (!Objects.equals(channelMerchant.getMchNo(), this.requireMchNo())) {
            // 抖音: 通道商户与商户号不匹配
            throw new ConfigErrorException("error.channel.douyin.mchAppNotFound");
        }
    }

    /// 校验应用归属当前商户
    private void assertAppOwned(DouyinDirectAppResult result) {
        if (!Objects.equals(result.getMchNo(), this.requireMchNo())) {
            // 抖音: 直连商户应用不存在或商户号归属不匹配
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.douyin.mchAppNotFound");
        }
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据通道商户号查询应用列表")
    @GetMapping("/list-by-channel-mch-no")
    public Result<List<DouyinDirectAppResult>> listByChannelMchNo(
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo) {
        this.assertChannelMchOwned(channelMchNo);
        return Res.ok(douyinDirectAppService.listByMchNoAndChannelMchNo(this.requireMchNo(), channelMchNo));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "查询应用详情")
    @GetMapping("/find-by-id")
    public Result<DouyinDirectAppResult> findById(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        DouyinDirectAppResult result = douyinDirectAppService.findById(id);
        this.assertAppOwned(result);
        return Res.ok(result);
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "同一通道商户下抖音应用ID是否已存在")
    @GetMapping("/exists-douyin-app-id-by-channel")
    public Result<Boolean> existsDouyinAppIdByChannel(
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo,
            @NotBlank(message = "{validation.field.douyinAppId.notBlank}") String douyinAppId) {
        this.assertChannelMchOwned(channelMchNo);
        return Res.ok(douyinDirectAppService.existsDouyinAppIdByChannel(
                this.requireMchNo(), channelMchNo, douyinAppId, null));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "同一通道商户下抖音应用ID是否已存在(排除自身)")
    @GetMapping("/exists-douyin-app-id-by-channel-not-id")
    public Result<Boolean> existsDouyinAppIdByChannelNotId(
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo,
            @NotBlank(message = "{validation.field.douyinAppId.notBlank}") String douyinAppId,
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        this.assertChannelMchOwned(channelMchNo);
        return Res.ok(douyinDirectAppService.existsDouyinAppIdByChannel(
                this.requireMchNo(), channelMchNo, douyinAppId, id));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "新增直连商户应用")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody DouyinDirectAppParam param) {
        this.assertChannelMchOwned(param.getChannelMchNo());
        // 强制当前商户号，忽略客户端传入（防越权）
        param.setMchNo(this.requireMchNo());
        ValidationUtil.validateParam(param, ValidationGroup.add.class);
        douyinDirectAppService.add(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "修改直连商户应用")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody DouyinDirectAppParam param) {
        this.assertAppOwned(douyinDirectAppService.findById(param.getId()));
        this.assertChannelMchOwned(param.getChannelMchNo());
        // 强制当前商户号，忽略客户端传入（防越权）
        param.setMchNo(this.requireMchNo());
        ValidationUtil.validateParam(param, ValidationGroup.edit.class);
        douyinDirectAppService.update(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "删除直连商户应用")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        this.assertAppOwned(douyinDirectAppService.findById(id));
        douyinDirectAppService.delete(id);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "查询应用授权认证配置")
    @GetMapping("/find-auth-config-by-app-id")
    public Result<DouyinDirectAppAuthConfigResult> findAuthConfigByAppId(
            @NotNull(message = "{validation.field.douyinDirectAppId.notNull}") Long douyinDirectAppId) {
        this.assertAppOwned(douyinDirectAppService.findById(douyinDirectAppId));
        var config = douyinDirectAppAuthConfigService.findByDouyinDirectAppId(douyinDirectAppId);
        return Res.ok(config.toResult());
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "保存应用授权认证配置")
    @PostMapping("/save-auth-config")
    public Result<Void> saveAuthConfig(@RequestBody DouyinDirectAppAuthConfigParam param) {
        this.assertAppOwned(douyinDirectAppService.findById(param.getDouyinDirectAppId()));
        // 强制当前商户号，忽略客户端传入（防越权）
        param.setMchNo(this.requireMchNo());
        ValidationUtil.validateParam(param);
        douyinDirectAppAuthConfigService.save(param);
        return Res.ok();
    }
}
