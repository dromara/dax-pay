package cn.daxpay.open.channel.alipay.controller.appmch;

import cn.daxpay.open.channel.alipay.param.direct.AlipayTransferConfigParam;
import cn.daxpay.open.channel.alipay.result.direct.AlipayTransferConfigResult;
import cn.daxpay.open.channel.alipay.service.direct.AlipayTransferConfigService;
import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.exception.BizInfoException;
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

/// # 支付宝转账配置管理(小程序商户端镜像)
///
/// 对照 admin 版 [cn.daxpay.open.channel.alipay.controller.direct.AlipayTransferConfigController]。
/// 商户号一律取自 [PaymentContext], 忽略客户端传入, 防越权;
/// 通道商户归属校验由 Service 内 assertChannelMerchant 兜底。
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "小程序商户端-支付宝转账配置管理")
@RestController
@RequestMapping("/app-mch/alipay/transfer-config")
@RequiredArgsConstructor
public class AppMchAlipayTransferConfigController {

    private final AlipayTransferConfigService alipayTransferConfigService;
    private final PaymentContext paymentContext;

    /// 当前登录商户号(上下文必有;缺则视为会话异常)
    private String requireMchNo() {
        String mchNo = paymentContext.getMchNo();
        if (Objects.isNull(mchNo) || mchNo.isBlank()) {
            // 商户上下文缺失
            throw new BizInfoException(CommonCode.FAIL_CODE, "pay.error.assist.mchContextMissing");
        }
        return mchNo;
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "查询通道商户的转账配置")
    @GetMapping("/find-by-channel-mch-no")
    public Result<AlipayTransferConfigResult> findByChannelMchNo(
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo) {
        return Res.ok(alipayTransferConfigService.findByChannelMchNo(requireMchNo(), channelMchNo));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "保存或更新转账配置(一对一)")
    @PostMapping("/save")
    public Result<Void> save(@RequestBody AlipayTransferConfigParam param) {
        // 强制当前商户号, 忽略客户端传入(防越权)
        param.setMchNo(requireMchNo());
        ValidationUtil.validateParam(param);
        alipayTransferConfigService.saveOrUpdate(param);
        return Res.ok();
    }
}
