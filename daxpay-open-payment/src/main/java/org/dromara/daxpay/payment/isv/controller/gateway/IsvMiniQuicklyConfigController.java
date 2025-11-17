package org.dromara.daxpay.payment.isv.controller.gateway;

import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import org.dromara.daxpay.payment.isv.param.gateway.IsvMiniQuicklyConfigParam;
import org.dromara.daxpay.payment.isv.result.gateway.IsvMiniQuicklyConfigResult;
import org.dromara.daxpay.payment.isv.service.gateway.IsvMiniQuicklyConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 小程序快捷支付配置控制器
 * @author xxm
 * @since 2025/10/10
 */
@Tag(name = "小程序快捷支付配置")
@RestController
@RequestMapping("/isv/mini/quickly/config")
@RequestGroup(groupCode = "IsvMiniQuicklyConfig", groupName = "小程序快捷支付配置", moduleCode = "GatewayPay")
@RequiredArgsConstructor
public class IsvMiniQuicklyConfigController {

    private final IsvMiniQuicklyConfigService service;

    @Operation(summary = "根据服务商号查询配置")
    @GetMapping("/findByIsvNo")
    public Result<IsvMiniQuicklyConfigResult> findByIsvNo(String isvNo) {
        return Res.ok(service.findByIsvNo(isvNo).toResult());
    }


    @Operation(summary = "更新配置")
    @PostMapping("/update")
    public Result<Void> update(@Valid @RequestBody IsvMiniQuicklyConfigParam param) {
        service.update(param);
        return Res.ok();
    }
}
