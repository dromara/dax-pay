package cn.daxpay.open.channel.wechat.controller.direct;

import cn.daxpay.open.channel.wechat.dao.direct.WechatDirectChannelMerchantManager;
import cn.daxpay.open.channel.wechat.entity.direct.WechatDirectChannelMerchant;
import cn.daxpay.open.channel.wechat.param.direct.WechatDirectKeyConfigParam;
import cn.daxpay.open.channel.wechat.result.direct.WechatDirectKeyConfigResult;
import cn.daxpay.open.channel.wechat.service.direct.WechatDirectKeyConfigService;
import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
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

/// # 微信直连密钥配置（商户端）
///
/// 对照运营端 [WechatDirectChannelMerchantController]，路径前缀 `/mch/wechat/direct-key-config`。
/// 商户号一律取自 [PaymentContext]，忽略请求中的 mchNo，防越权。
@PermCode(menuCode = PermCodes.Channel.App.MENU)
@Validated
@Tag(name = "微信直连密钥配置(商户端)")
@RestController
@RequestMapping("/mch/wechat/direct-key-config")
@RequiredArgsConstructor
public class MchWechatDirectKeyConfigController {

    private final WechatDirectKeyConfigService wechatDirectKeyConfigService;
    private final WechatDirectChannelMerchantManager wechatDirectChannelMerchantManager;
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

    /// 校验通道商户号归属当前商户（TenantLine 兜底之外的显式防越权）
    private void assertOwned(String channelMchNo) {
        WechatDirectChannelMerchant entity = wechatDirectChannelMerchantManager
                .findByChannelMchNo(channelMchNo)
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
        if (!Objects.equals(entity.getMchNo(), requireMchNo())) {
            // 通道商户不属于当前商户
            throw new BizInfoException(CommonCode.FAIL_CODE, "error.payment.merchant.storeNoMatch");
        }
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据通道商户号查询密钥配置")
    @GetMapping("/find-key-config")
    public Result<WechatDirectKeyConfigResult> findKeyConfig(
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo) {
        this.assertOwned(channelMchNo);
        return Res.ok(wechatDirectKeyConfigService.findByChannelMchNo(channelMchNo).toResult());
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "保存密钥配置")
    @PostMapping("/save-key-config")
    public Result<Void> saveKeyConfig(@RequestBody @Validated WechatDirectKeyConfigParam param) {
        this.assertOwned(param.getChannelMchNo());
        // 强制当前商户号，忽略客户端传入（防越权）
        param.setMchNo(requireMchNo());
        wechatDirectKeyConfigService.save(param);
        return Res.ok();
    }
}
