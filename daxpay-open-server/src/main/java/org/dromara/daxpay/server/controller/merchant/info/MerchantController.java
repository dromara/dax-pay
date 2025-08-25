package org.dromara.daxpay.server.controller.merchant.info;

import cn.bootx.platform.core.annotation.ClientCode;
import cn.bootx.platform.core.annotation.OperateLog;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.core.validation.ValidationGroup;
import org.dromara.daxpay.service.merchant.service.info.MerchantInfoService;
import org.dromara.daxpay.service.common.code.DaxPayCode;
import org.dromara.daxpay.service.merchant.param.info.MerchantParam;
import org.dromara.daxpay.service.merchant.result.info.MerchantResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @RequestPath("获取商户信息")
    @Operation(summary = "获取商户信息")
    @GetMapping("/get")
    public Result<MerchantResult> getMerchant(){
        return Res.ok(merchantInfoService.getMerchant());
    }

    @RequestPath("修改商户信息")
    @Operation(summary = "修改商户信息")
    @OperateLog(title = "修改商户信息", businessType = OperateLog.BusinessType.UPDATE, saveParam = true)
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) MerchantParam param){
        merchantInfoService.update(param);
        return Res.ok();
    }

}
