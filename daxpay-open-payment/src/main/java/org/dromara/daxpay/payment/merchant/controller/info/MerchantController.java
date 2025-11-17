package org.dromara.daxpay.payment.merchant.controller.info;

import cn.bootx.platform.core.annotation.ClientCode;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import org.dromara.daxpay.payment.common.code.DaxPayCode;
import org.dromara.daxpay.payment.merchant.result.info.MerchantResult;
import org.dromara.daxpay.payment.merchant.service.info.MerchantInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.dromara.core.trans.anno.TransMethodResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商户配置
 * @author xxm
 * @since 2024/6/25
 */
@Validated
@Tag(name = "商户信息")
@ClientCode(DaxPayCode.Client.MERCHANT)
@RestController
@RequestMapping("/merchant")
@RequestGroup(groupCode = "MerchantInfo", groupName = "商户信息", moduleCode = "merchant", moduleName = "(DaxPay)商户管理")
@RequiredArgsConstructor
public class MerchantController {
    private final MerchantInfoService merchantInfoService;

    @TransMethodResult
    @RequestPath("获取商户信息")
    @Operation(summary = "获取商户信息")
    @GetMapping("/get")
    public Result<MerchantResult> getMerchant(){
        return Res.ok(merchantInfoService.getMerchant());
    }

}
