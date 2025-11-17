package org.dromara.daxpay.payment.merchant.controller.gateway;

import cn.bootx.platform.core.annotation.ClientCode;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import org.dromara.daxpay.payment.common.code.DaxPayCode;
import org.dromara.daxpay.payment.merchant.entity.gateway.CashierCodeConfig;
import org.dromara.daxpay.payment.merchant.param.gateway.CashierCodeConfigParam;
import org.dromara.daxpay.payment.merchant.service.gateway.CashierCodeConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 收银码牌配置
 * @author xxm
 * @since 2024/11/20
 */
@Validated
@ClientCode({DaxPayCode.Client.ADMIN, DaxPayCode.Client.MERCHANT})
@Tag(name = "收银码牌配置")
@RestController
@RequestMapping("/cashier/code/config")
@RequestGroup(groupCode = "CashierCodeConfig", groupName = "收银码牌配置", moduleCode = "device")
@RequiredArgsConstructor
public class CashierCodeConfigController {
    private final CashierCodeConfigService codeConfigService;

    @RequestPath("根据应用ID查询")
    @Operation(summary = "根据应用ID查询")
    @GetMapping("/findByAppId")
    public Result<CashierCodeConfig> findByAppId(@NotBlank(message = "应用ID不可为空") String appId){
        return Res.ok(codeConfigService.findByAppId(appId));
    }

    @RequestPath("更新")
    @Operation(summary = "更新")
    @PostMapping("/update")
    public Result<Void> update(@Validated @RequestBody CashierCodeConfigParam param){
        codeConfigService.update(param);
        return Res.ok();
    }

}
