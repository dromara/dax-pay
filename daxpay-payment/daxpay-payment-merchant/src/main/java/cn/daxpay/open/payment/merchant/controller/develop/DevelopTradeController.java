package cn.daxpay.open.payment.merchant.controller.develop;

import cn.daxpay.open.payment.merchant.param.develop.DevelopParam;
import cn.daxpay.open.payment.merchant.result.develop.DevelopSignResult;
import cn.daxpay.open.payment.merchant.service.develop.DevelopTradeService;
import cn.daxpay.open.payment.masterdata.result.provider.PayProviderMethodResult;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.dto.ChannelMchOption;
import cn.daxpay.open.platform.core.rest.dto.LabelValue;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/// 交易开发调试(商户端)
///
/// 仅提供签名与元数据辅助, 真实支付由前端模拟商户请求调用 `/unipay/pay`。
@PermCode(menuCode = PermCodes.Develop.Trade.MENU)
@Tag(name = "交易开发调试服务(商户端)")
@RestController
@RequestMapping("/mch/develop/trade")
@RequiredArgsConstructor
public class DevelopTradeController {

    private final DevelopTradeService developTradeService;

    @PermCode(code = PermCodes.Action.SIGN)
    @Operation(summary = "支付参数签名")
    @PostMapping("/sign")
    public Result<DevelopSignResult> sign(@RequestBody DevelopParam<NormalPayParam> param) {
        return Res.ok(developTradeService.sign(param));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "已启用渠道支付方式目录")
    @GetMapping("/method-directory")
    public Result<List<PayProviderMethodResult>> methodDirectory() {
        return Res.ok(developTradeService.listMethodDirectory());
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "直接指定通道商户候选")
    @GetMapping("/channel-mch-candidates")
    public Result<List<ChannelMchOption>> channelMchCandidates(
            @NotBlank(message = "{validation.field.mchNo.notBlank}") String mchNo,
            @RequestParam(required = false) String provider) {
        return Res.ok(developTradeService.listChannelMchCandidates(mchNo, provider));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "直接指定支付能力候选")
    @GetMapping("/capability-candidates")
    public Result<List<LabelValue>> capabilityCandidates(
            @NotBlank(message = "{validation.field.channelMchNo.notBlank}") String channelMchNo) {
        return Res.ok(developTradeService.listCapabilityCandidates(channelMchNo));
    }
}
