package cn.daxpay.open.payment.merchant.controller.douyin;

import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.douyin.param.channel.DyChannelAppCapabilityBatchParam;
import cn.daxpay.open.payment.douyin.result.DyCapabilityOption;
import cn.daxpay.open.payment.douyin.result.channel.DyChannelAppCapabilityResult;
import cn.daxpay.open.payment.douyin.service.channel.DyChannelAppCapabilityService;
import cn.daxpay.open.payment.douyin.service.platform.DyPlatformAppCapabilityService;
import cn.daxpay.open.payment.merchant.dao.channel.ChannelMerchantManager;
import cn.daxpay.open.payment.merchant.entity.channel.ChannelMerchant;
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

/// # 通道商户抖音应用能力绑定（商户端）
///
/// 对照运营端 [cn.daxpay.open.payment.admin.controller.douyin.DyChannelAppCapabilityController]，路径 `/mch/douyin/channel-app-capability`。
/// 校验通道商户归属当前商户后委托 [DyChannelAppCapabilityService]。
@PermCode(menuCode = PermCodes.Payment.Douyin.MchApp.MENU)
@Validated
@Tag(name = "通道商户抖音应用能力绑定(商户端)")
@RestController
@RequestMapping("/mch/douyin/channel-app-capability")
@RequiredArgsConstructor
public class MchDyChannelAppCapabilityController {

    private final DyChannelAppCapabilityService dyChannelAppCapabilityService;
    private final DyPlatformAppCapabilityService dyPlatformAppCapabilityService;
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
                // 抖音: 通道商户不存在或商户号不匹配
                .orElseThrow(() -> new ConfigErrorException("error.payment.douyin.channelMerchantMismatch"));
        if (!Objects.equals(channelMerchant.getMchNo(), this.requireMchNo())) {
            // 抖音: 通道商户与商户号不匹配
            throw new ConfigErrorException("error.payment.douyin.channelMerchantMismatch");
        }
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "按通道商户号查询能力绑定")
    @GetMapping("/list-by-channel-mch-no")
    public Result<List<DyChannelAppCapabilityResult>> listByChannelMchNo(
            @NotBlank(message = "{validation.field.channelMchNo.notBlank}") String channelMchNo) {
        this.assertChannelMchOwned(channelMchNo);
        return Res.ok(dyChannelAppCapabilityService.listByChannelMchNo(channelMchNo));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "按产品查询可绑定能力候选")
    @GetMapping("/list-supported-capabilities")
    public Result<List<DyCapabilityOption>> listSupportedCapabilities(
            @NotBlank(message = "{validation.field.product.notBlank}")
            @Parameter(description = "支付产品编码") String product) {
        // 只读产品元数据，无需通道商户归属校验
        return Res.ok(dyPlatformAppCapabilityService.listSupportedCapabilities(product));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "全量保存能力绑定")
    @PostMapping("/save-batch")
    public Result<Void> saveBatch(@RequestBody @Validated DyChannelAppCapabilityBatchParam param) {
        String mchNo = this.requireMchNo();
        this.assertChannelMchOwned(param.getChannelMchNo());
        dyChannelAppCapabilityService.saveBatch(mchNo, param.getChannelMchNo(), param.getItems());
        return Res.ok();
    }
}
