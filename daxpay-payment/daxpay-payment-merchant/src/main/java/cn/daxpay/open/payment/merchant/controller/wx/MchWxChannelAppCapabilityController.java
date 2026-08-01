package cn.daxpay.open.payment.merchant.controller.wx;

import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.merchant.dao.channel.ChannelMerchantManager;
import cn.daxpay.open.payment.merchant.entity.channel.ChannelMerchant;
import cn.daxpay.open.payment.wx.param.channel.WxChannelAppCapabilityBatchParam;
import cn.daxpay.open.payment.wx.result.WxCapabilityOption;
import cn.daxpay.open.payment.wx.result.channel.WxChannelAppCapabilityResult;
import cn.daxpay.open.payment.wx.service.channel.WxChannelAppCapabilityService;
import cn.daxpay.open.payment.wx.service.platform.WxPlatformAppCapabilityService;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.config.ConfigErrorException;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/// # 通道商户微信应用能力绑定（商户端）
///
/// 对照运营端 [WxChannelAppCapabilityController]，路径 `/mch/wx/channel-app-capability`。
/// 校验通道商户归属当前商户后委托 [WxChannelAppCapabilityService]。
@PermCode(menuCode = PermCodes.Payment.Wx.MchApp.MENU)
@Validated
@Tag(name = "通道商户微信应用能力绑定(商户端)")
@RestController
@RequestMapping("/mch/wx/channel-app-capability")
@RequiredArgsConstructor
public class MchWxChannelAppCapabilityController {

    private final WxChannelAppCapabilityService wxChannelAppCapabilityService;
    private final WxPlatformAppCapabilityService wxPlatformAppCapabilityService;
    private final ChannelMerchantManager channelMerchantManager;
    private final PaymentContext paymentContext;

    /// 当前登录商户号
    private String requireMchNo() {
        String mchNo = paymentContext.getMchNo();
        if (mchNo == null || mchNo.isBlank()) {
            // 商户上下文缺失
            throw new BizInfoException(CommonCode.FAIL_CODE, "pay.error.assist.mchContextMissing");
        }
        return mchNo;
    }

    /// 校验通道商户属于当前商户
    private void assertChannelMchOwned(String channelMchNo) {
        ChannelMerchant channelMerchant = channelMerchantManager.findByChannelMchNo(channelMchNo)
                // 微信: 通道商户不存在或商户号不匹配
                .orElseThrow(() -> new ConfigErrorException("error.payment.wx.channelMerchantMismatch"));
        if (!Objects.equals(channelMerchant.getMchNo(), this.requireMchNo())) {
            // 微信: 通道商户与商户号不匹配
            throw new ConfigErrorException("error.payment.wx.channelMerchantMismatch");
        }
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "按通道商户号查询能力绑定")
    @GetMapping("/list-by-channel-mch-no")
    public Result<List<WxChannelAppCapabilityResult>> listByChannelMchNo(
            @NotBlank(message = "{validation.field.channelMchNo.notBlank}") String channelMchNo) {
        this.assertChannelMchOwned(channelMchNo);
        return Res.ok(wxChannelAppCapabilityService.listByChannelMchNo(channelMchNo));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "按产品查询可绑定能力候选")
    @GetMapping("/list-supported-capabilities")
    public Result<List<WxCapabilityOption>> listSupportedCapabilities(
            @NotBlank(message = "{validation.field.product.notBlank}")
            @Parameter(description = "支付产品编码") String product) {
        // 只读产品元数据，无需通道商户归属校验
        return Res.ok(wxPlatformAppCapabilityService.listSupportedCapabilities(product));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "全量保存能力绑定")
    @PostMapping("/save-batch")
    public Result<Void> saveBatch(@RequestBody @Validated WxChannelAppCapabilityBatchParam param) {
        String mchNo = this.requireMchNo();
        this.assertChannelMchOwned(param.getChannelMchNo());
        wxChannelAppCapabilityService.saveBatch(mchNo, param.getChannelMchNo(), param.getItems());
        return Res.ok();
    }
}
