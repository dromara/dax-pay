package cn.daxpay.open.payment.app.merchant.controller.info;

import cn.daxpay.open.payment.app.merchant.service.info.AppMerchantInfoService;
import cn.daxpay.open.payment.merchant.param.info.MerchantInfoParam;
import cn.daxpay.open.payment.merchant.result.info.MerchantInfoResult;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 商户信息(商户移动端)
///
/// 获取/更新当前登录商户资料。业务编排委托 [AppMerchantInfoService]。
@Validated
@Tag(name = "商户信息(商户移动端)")
@RestController
@RequestMapping("/app-mch/merchant")
@RequiredArgsConstructor
public class AppMerchantInfoController {

    private final AppMerchantInfoService merchantInfoService;

    @Operation(summary = "获取商户信息")
    @GetMapping("/get")
    public Result<MerchantInfoResult> getMerchant() {
        return Res.ok(merchantInfoService.getMerchant());
    }

    /// 更新当前登录商户资料（mchNo 取自 PaymentContext，不信任入参）
    @Operation(summary = "更新商户信息")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated MerchantInfoParam param) {
        merchantInfoService.update(param);
        return Res.ok();
    }
}
