package cn.daxpay.multi.gateway.controller;

import cn.bootx.platform.core.annotation.IgnoreAuth;
import cn.daxpay.multi.core.param.trade.pay.PaySyncParam;
import cn.daxpay.multi.core.param.trade.refund.RefundSyncParam;
import cn.daxpay.multi.core.param.trade.transfer.TransferSyncParam;
import cn.daxpay.multi.core.result.DaxResult;
import cn.daxpay.multi.core.result.trade.pay.PaySyncResult;
import cn.daxpay.multi.core.result.trade.refund.RefundSyncResult;
import cn.daxpay.multi.core.result.trade.transfer.TransferSyncResult;
import cn.daxpay.multi.core.util.DaxRes;
import cn.daxpay.multi.service.service.trade.pay.PaySyncService;
import cn.daxpay.multi.service.service.trade.refund.RefundSyncService;
import cn.daxpay.multi.service.service.trade.transfer.TransferSyncService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 统一同步接口
 * @author xxm
 * @since 2024/6/4
 */
@IgnoreAuth
@Tag(name = "统一同步接口")
@RestController
@RequestMapping("/unipay/sync/order")
@RequiredArgsConstructor
public class UniSyncController {

    private final PaySyncService paySyncService;

    private final RefundSyncService refundSyncService;

    private final TransferSyncService transferSyncService;


    @Operation(summary = "支付同步接口")
    @PostMapping("/pay")
    public DaxResult<PaySyncResult> pay(@RequestBody PaySyncParam param){
        return DaxRes.ok(paySyncService.sync(param));
    }

    @Operation(summary = "退款同步接口")
    @PostMapping("/refund")
    public DaxResult<RefundSyncResult> refund(RefundSyncParam param){
        return DaxRes.ok(refundSyncService.sync(param));
    }

    @Operation(summary = "分账同步接口")
    @PostMapping("/allocation")
    public DaxResult<TransferSyncResult> allocation(@RequestBody TransferSyncParam param){
        return DaxRes.ok(transferSyncService.sync(param));
    }
}
