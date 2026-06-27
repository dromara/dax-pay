package cn.daxpay.open.payment.admin.controller.develop;

import cn.daxpay.open.payment.admin.param.develop.DevelopParam;
import cn.daxpay.open.payment.admin.result.develop.DevelopPayResult;
import cn.daxpay.open.payment.admin.result.develop.DevelopSignResult;
import cn.daxpay.open.payment.admin.service.develop.DevelopTradeService;
import cn.daxpay.open.payment.masterdata.constants.provider.result.PayProviderMethodResult;
import cn.daxpay.open.payment.unipay.param.trade.pay.PayParam;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.rest.Res;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/// 交易开发调试(管理)
@PermCode(menuCode = "develop:trade")
@Tag(name = "交易开发调试服务")
@RestController
@RequestMapping("/admin/develop/trade")
@RequiredArgsConstructor
public class DevelopTradeController {

    private final DevelopTradeService developTradeService;

    @PermCode(code = "sign", nameCn = "签名", nameEn = "Sign")
    @Operation(summary = "支付参数签名")
    @PostMapping("/sign")
    public Result<DevelopSignResult> sign(@RequestBody DevelopParam<PayParam> param) {
        return Res.ok(developTradeService.sign(param));
    }

    @PermCode(code = "pay", nameCn = "支付", nameEn = "Pay")
    @Operation(summary = "支付调试(真实发起)")
    @PostMapping("/pay")
    public Result<DevelopPayResult> pay(@RequestBody DevelopParam<PayParam> param) {
        return Res.ok(developTradeService.pay(param));
    }

    @PermCode(code = "view", nameCn = "查看", nameEn = "View")
    @Operation(summary = "已启用渠道支付方式目录")
    @GetMapping("/method-directory")
    public Result<List<PayProviderMethodResult>> methodDirectory() {
        return Res.ok(developTradeService.listMethodDirectory());
    }

    @PermCode(code = "view", nameCn = "查看", nameEn = "View")
    @Operation(summary = "传值模式通道商户候选")
    @GetMapping("/channel-mch-candidates")
    public Result<List<LabelValue>> channelMchCandidates(
            @NotBlank(message = "{validation.field.mchNo.notBlank}") String mchNo) {
        return Res.ok(developTradeService.listChannelMchCandidates(mchNo));
    }

    @PermCode(code = "view", nameCn = "查看", nameEn = "View")
    @Operation(summary = "传值模式支付能力候选")
    @GetMapping("/capability-candidates")
    public Result<List<LabelValue>> capabilityCandidates(
            @NotBlank(message = "{validation.field.channelMchNo.notBlank}") String channelMchNo) {
        return Res.ok(developTradeService.listCapabilityCandidates(channelMchNo));
    }
}
