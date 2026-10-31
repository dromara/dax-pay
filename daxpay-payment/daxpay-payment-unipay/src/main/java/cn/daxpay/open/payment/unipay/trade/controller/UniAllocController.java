package cn.daxpay.open.payment.unipay.trade.controller;

import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.common.util.DaxRes;
import cn.daxpay.open.payment.unipay.aop.PaymentVerify;
import cn.daxpay.open.payment.unipay.param.trade.alloc.UnipayAllocFinishParam;
import cn.daxpay.open.payment.unipay.param.trade.alloc.UnipayAllocParam;
import cn.daxpay.open.payment.unipay.result.trade.alloc.AllocResult;
import cn.daxpay.open.payment.unipay.trade.service.AllocOrderService;
import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 统一分账接口
///
/// 商户系统通过 RSA 签名调用, 发起分账与分账完结(多次分账收尾)。
/// 原支付订单须在下单时声明 allocation=true(分账订单), 否则通道拒绝分账。
@PaymentVerify
@IgnoreAuth
@Tag(name = "统一分账接口")
@RestController
@RequestMapping("/unipay")
@RequiredArgsConstructor
public class UniAllocController {

    private final AllocOrderService allocOrderService;

    /// 发起分账
    @Operation(summary = "分账接口")
    @PostMapping("/alloc")
    public DaxResult<AllocResult> alloc(@RequestBody UnipayAllocParam param) {
        return DaxRes.ok(allocOrderService.alloc(param));
    }

    /// 发起分账完结(解冻剩余冻结资金, 仅多次分账未完结订单可调用)
    @Operation(summary = "分账完结接口")
    @PostMapping("/alloc-finish")
    public DaxResult<AllocResult> allocFinish(@RequestBody UnipayAllocFinishParam param) {
        return DaxRes.ok(allocOrderService.finish(param));
    }
}
