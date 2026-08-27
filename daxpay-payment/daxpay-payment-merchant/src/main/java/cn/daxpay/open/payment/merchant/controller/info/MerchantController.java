package cn.daxpay.open.payment.merchant.controller.info;

import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.payment.merchant.param.info.MerchantInfoParam;
import cn.daxpay.open.payment.merchant.result.info.MerchantInfoResult;
import cn.daxpay.open.payment.merchant.service.info.MerchantInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 商户配置
///
@PermCode(menuCode = PermCodes.Merchant.Info.MENU)
@Validated
@Tag(name = "商户信息")
@RestController
@RequestMapping("/mch/merchant")
@RequiredArgsConstructor
public class MerchantController {
    private final MerchantInfoService merchantInfoService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "获取商户信息")
    @GetMapping("/get")
    public Result<MerchantInfoResult> getMerchant(){
        return Res.ok(merchantInfoService.getMerchant());
    }

    /// 更新当前登录商户资料（mchNo 取自 PaymentContext，不信任入参）
    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "更新商户信息")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated MerchantInfoParam param) {
        merchantInfoService.update(param);
        return Res.ok();
    }

}
