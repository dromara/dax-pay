package org.dromara.daxpay.server.controller.admin.isv.config;

import cn.bootx.platform.core.annotation.ClientCode;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.dromara.daxpay.service.common.code.DaxPayCode;
import org.dromara.daxpay.service.isv.result.config.IsvChannelConfigResult;
import org.dromara.daxpay.service.isv.service.config.IsvChannelConfigService;
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

    @RequestPath("查询服务商配置列表")
    @Operation(summary = "查询服务商配置列表")
    @GetMapping("/findAll")
    public Result<List<IsvChannelConfigResult>> findAll(){
        return Res.ok(service.findAll());
    }

}

