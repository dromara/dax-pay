package org.dromara.daxpay.service.controller.unipay;

import cn.bootx.platform.core.annotation.IgnoreAuth;
import org.dromara.daxpay.core.param.reconcile.ReconcileDownParam;
import org.dromara.daxpay.core.result.DaxResult;
import org.dromara.daxpay.core.result.reconcile.ReconcileDownResult;
import org.dromara.daxpay.core.util.DaxRes;
import org.dromara.daxpay.service.common.anno.PaymentVerify;
import org.dromara.daxpay.service.service.reconcile.ReconcileStatementQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 对账接口处理器
 * @author xxm
 * @since 2024/6/4
 */
@PaymentVerify
@IgnoreAuth
@Tag(name = "对账接口处理器")
@RestController
@RequestMapping("/unipay/reconcile")
@RequiredArgsConstructor
public class UniReconcileController {

    private final ReconcileStatementQueryService statementQueryService;

    @Operation(summary = "下载通道对账单链接")
    @PostMapping("/channelDownUrl")
    public DaxResult<ReconcileDownResult> channelDownUrl(@RequestBody ReconcileDownParam param){
        return DaxRes.ok(statementQueryService.getChannelDownUrl(param.getChannel(), param.getDate()));
    }

    @Operation(summary = "下载平台对账单链接")
    @PostMapping("/platformDownUrl")
    public DaxResult<ReconcileDownResult> platformDownUrl(@RequestBody ReconcileDownParam param){
        return DaxRes.ok(statementQueryService.getPlatformDownUrl(param.getChannel(), param.getDate()));
    }

}
