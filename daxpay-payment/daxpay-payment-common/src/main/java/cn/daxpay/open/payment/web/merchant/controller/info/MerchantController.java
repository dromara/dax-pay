package cn.daxpay.open.payment.web.merchant.controller.info;

import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.payment.merchant.result.info.MerchantInfoResult;
import cn.daxpay.open.payment.merchant.service.info.MerchantInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 商户配置
///
@Validated
@Tag(name = "商户信息")
@RestController
@RequestMapping("/merchant")
@RequiredArgsConstructor
public class MerchantController {
    private final MerchantInfoService merchantInfoService;

    @Operation(summary = "获取商户信息")
    @GetMapping("/get")
    public Result<MerchantInfoResult> getMerchant(){
        return Res.ok(merchantInfoService.getMerchant());
    }

}
