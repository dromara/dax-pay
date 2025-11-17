package org.dromara.daxpay.payment.isv.controller.config;


import cn.bootx.platform.core.annotation.ClientCode;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import org.dromara.daxpay.payment.isv.result.config.IsvChannelConfigResult;
import org.dromara.daxpay.payment.isv.service.config.IsvChannelConfigService;
import org.dromara.daxpay.payment.common.code.DaxPayCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 服务商通道配置
 * @author xxm
 * @since 2024/10/29
 */
@Validated
@ClientCode({DaxPayCode.Client.ADMIN})
@Tag(name = "服务商通道配置")
@RestController
@RequestGroup(groupCode = "IsvChannelConfig", groupName = "通道配置", moduleCode = "isv")
@RequestMapping("/isv/channel/config")
@RequiredArgsConstructor
public class IsvChannelConfigController {
    private final IsvChannelConfigService service;

    @RequestPath("根据服务商号查询配置列表")
    @Operation(summary = "根据服务商号查询配置列表")
    @GetMapping("/findAllByIsvNo")
    public Result<List<IsvChannelConfigResult>> findAllByIsvNo(@NotBlank(message = "服务商号不可为空") String isvNo){
        return Res.ok(service.findAllByIsvNo(isvNo));
    }

}

