package cn.daxpay.open.channel.douyin.controller.merchant;

import cn.daxpay.open.channel.douyin.param.direct.DouyinTransferConfigParam;
import cn.daxpay.open.channel.douyin.result.direct.DouyinTransferConfigResult;
import cn.daxpay.open.channel.douyin.service.direct.DouyinTransferConfigService;
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

import java.util.Objects;

/// # 抖音转账配置管理(商户端)
///
/// 对照运营端 [cn.daxpay.open.channel.douyin.controller.direct.DouyinTransferConfigController]，
/// 路径前缀 `/mch/douyin/transfer-config`。
/// 商户号一律取自 [PaymentContext]，防越权。
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "抖音转账配置管理(商户端)")
@RestController
@RequestMapping("/mch/douyin/transfer-config")
@RequiredArgsConstructor
public class MchDouyinTransferConfigController {

    private final DouyinTransferConfigService douyinTransferConfigService;
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
                .orElseThrow(() -> new ConfigErrorException("error.payment.douyin.channelMerchantMismatch"));
        if (!Objects.equals(channelMerchant.getMchNo(), this.requireMchNo())) {
            // 抖音: 通道商户与商户号不匹配
            throw new ConfigErrorException("error.payment.douyin.channelMerchantMismatch");
        }
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "查询通道商户的转账配置")
    @GetMapping("/find-by-channel-mch-no")
    public Result<DouyinTransferConfigResult> findByChannelMchNo(
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo) {
        this.assertChannelMchOwned(channelMchNo);
        return Res.ok(douyinTransferConfigService.findByChannelMchNo(this.requireMchNo(), channelMchNo));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "保存或更新转账配置(一对一)")
    @PostMapping("/save")
    public Result<Void> save(@RequestBody DouyinTransferConfigParam param) {
        this.assertChannelMchOwned(param.getChannelMchNo());
        // 强制当前商户号，忽略客户端传入（防越权）
        param.setMchNo(this.requireMchNo());
        ValidationUtil.validateParam(param);
        douyinTransferConfigService.saveOrUpdate(param);
        return Res.ok();
    }
}
