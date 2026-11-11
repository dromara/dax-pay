package cn.daxpay.open.payment.app.merchant.controller.check;

import cn.daxpay.open.payment.common.check.model.ConfigCheckResult;
import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.merchant.check.service.MerchantConfigCheckService;
import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 配置检查(商户移动端)
///
/// 对应 Web 商户端 [cn.daxpay.open.payment.merchant.controller.check.MerchantConfigCheckController]
/// (路径 `/mch/config-check`), 镜像其唯一端点 items, 复用同一 [MerchantConfigCheckService]。
/// 工作台「配置待完成」Widget 数据源; 商户号取自 [PaymentContext], 由 Service 隔离查询。
///
/// 不挂菜单权限码: 工作台为登录即达的页面, 任何已认证商户用户均可查看本商户配置完成度
/// (与 [IgnoreAuth#login] 一致的 Web 端口径, 仅校验登录态)。
@Tag(name = "小程序商户端-配置检查")
@IgnoreAuth(login = true)
@Validated
@RestController
@RequestMapping("/app-mch/config-check")
@RequiredArgsConstructor
public class AppMerchantConfigCheckController {

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
