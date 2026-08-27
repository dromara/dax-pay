package cn.daxpay.open.channel.douyin.controller.merchant;

import cn.daxpay.open.channel.douyin.param.direct.DouyinDirectKeyConfigParam;
import cn.daxpay.open.channel.douyin.result.direct.DouyinDirectChannelMerchantResult;
import cn.daxpay.open.channel.douyin.result.direct.DouyinDirectKeyConfigResult;
import cn.daxpay.open.channel.douyin.result.direct.DouyinTransferSceneOptionResult;
import cn.daxpay.open.channel.douyin.service.direct.DouyinDirectChannelMerchantService;
import cn.daxpay.open.channel.douyin.service.direct.DouyinDirectKeyConfigService;
import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.merchant.dao.channel.ChannelMerchantManager;
import cn.daxpay.open.payment.merchant.entity.channel.ChannelMerchant;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.config.ConfigErrorException;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.core.util.ValidationUtil;
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

import java.util.List;
import java.util.Objects;

/// # 抖音直连通道商户配置（商户端）
///
/// 对照运营端 [cn.daxpay.open.channel.douyin.controller.direct.DouyinDirectChannelMerchantController]，路径前缀 `/mch/douyin/direct-channel-merchant`。
/// 提供抖音直连通道商户的基础信息查看与密钥配置，商户号一律取自 [PaymentContext]，防越权。
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "抖音直连通道商户配置(商户端)")
@RestController
@RequestMapping("/mch/douyin/direct-channel-merchant")
@RequiredArgsConstructor
public class MchDouyinDirectChannelMerchantController {

    private final DouyinDirectChannelMerchantService douyinDirectChannelMerchantService;
    private final DouyinDirectKeyConfigService douyinDirectKeyConfigService;
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

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据通道商户号查询抖音直连通道商户配置")
    @GetMapping("/find-by-channel-mch-no")
    public Result<DouyinDirectChannelMerchantResult> findByChannelMchNo(
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo) {
        this.assertChannelMchOwned(channelMchNo);
        return Res.ok(douyinDirectChannelMerchantService.findByChannelMchNo(channelMchNo));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据通道商户号查询密钥配置")
    @GetMapping("/find-key-config")
    public Result<DouyinDirectKeyConfigResult> findKeyConfig(
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo) {
        this.assertChannelMchOwned(channelMchNo);
        var config = douyinDirectKeyConfigService.findByChannelMchNo(channelMchNo);
        var result = config.toResult();
        result.setPrivateKeyConfigured(config.getMerchantPrivateKey() != null);
        result.setEncryptKeyConfigured(config.getEncryptKey() != null);
        return Res.ok(result);
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "保存密钥配置")
    @PostMapping("/save-key-config")
    public Result<Void> saveKeyConfig(@RequestBody DouyinDirectKeyConfigParam param) {
        this.assertChannelMchOwned(param.getChannelMchNo());
        // 强制当前商户号，忽略客户端传入（防越权）
        param.setMchNo(this.requireMchNo());
        ValidationUtil.validateParam(param);
        douyinDirectKeyConfigService.save(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "查询抖音转账场景选项列表(主数据枚举)")
    @GetMapping("/scene-options")
    public Result<List<DouyinTransferSceneOptionResult>> sceneOptions() {
        return Res.ok(douyinDirectChannelMerchantService.findSceneOptions());
    }
}
