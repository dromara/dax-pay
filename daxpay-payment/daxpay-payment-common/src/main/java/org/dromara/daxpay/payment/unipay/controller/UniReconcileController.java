package org.dromara.daxpay.payment.unipay.controller;

import org.dromara.daxpay.platform.core.annotation.IgnoreAuth;
import org.dromara.daxpay.payment.unipay.param.reconcile.ReconcileDownParam;
import org.dromara.daxpay.payment.common.result.DaxResult;
import org.dromara.daxpay.payment.unipay.result.reconcile.ReconcileDownResult;
import org.dromara.daxpay.payment.common.util.DaxRes;
import org.dromara.daxpay.payment.pay.anno.PaymentVerify;
import org.dromara.daxpay.payment.pay.service.reconcile.ReconcileStatementQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 对账接口处理器
///
@PaymentVerify
@IgnoreAuth
@Tag(name = "对账接口处理器")
@RestController
@RequestMapping("/unipay/reconcile")
@RequiredArgsConstructor
public class UniReconcileController {

    private final ReconcileStatementQueryService statementQueryService;

    @Operation(summary = "下载通道对账单链接")
    @PostMapping("/channel-down-url")
    public DaxResult<ReconcileDownResult> channelDownUrl(@RequestBody ReconcileDownParam param){
        return DaxRes.ok(statementQueryService.getChannelDownUrl(param.getProduct(), param.getDate()));
    }

    @Operation(summary = "下载平台对账单链接")
    @PostMapping("/platform-down-url")
    public DaxResult<ReconcileDownResult> platformDownUrl(@RequestBody ReconcileDownParam param){
        return DaxRes.ok(statementQueryService.getPlatformDownUrl(param.getProduct(), param.getDate()));
    }

}
