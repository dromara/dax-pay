package cn.daxpay.open.payment.merchant.controller.check;

import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.payment.check.model.ConfigCheckResult;
import cn.daxpay.open.payment.check.service.MerchantConfigCheckService;
import cn.daxpay.open.payment.common.context.PaymentContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 配置检查(商户端)
///
/// 工作台「配置待完成」Widget 数据源。
/// 商户号由 `MchContextLocalFilter` 从登录用户装载至 [PaymentContext], 此处读取后隔离查询。
@Tag(name = "配置检查(商户端)")
@Validated
@RestController
@RequestMapping("/mch/config-check")
@RequiredArgsConstructor
public class MerchantConfigCheckController {

    private final PaymentContext paymentContext;
    private final MerchantConfigCheckService merchantConfigCheckService;

    /// 获取当前商户的未完成配置项列表(含分类汇总)
    @Operation(summary = "获取当前商户的未完成配置项")
    @GetMapping("/items")
    public Result<ConfigCheckResult> items() {
        String mchNo = paymentContext.getMchNo();
        return Res.ok(merchantConfigCheckService.check(mchNo));
    }
}
