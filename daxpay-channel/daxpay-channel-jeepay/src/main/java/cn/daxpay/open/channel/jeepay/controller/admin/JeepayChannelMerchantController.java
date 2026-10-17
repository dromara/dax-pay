package cn.daxpay.open.channel.jeepay.controller.admin;

import cn.daxpay.open.channel.jeepay.param.JeepayChannelMerchantCreateParam;
import cn.daxpay.open.channel.jeepay.service.merchant.JeepayChannelMerchantService;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # Jeepay通道商户管理
///
/// 提供 Jeepay 通道商户创建(纯建档, 仅通道商户名称),
/// 平台网关地址/商户号/应用ID/应用密钥等对接配置由密钥配置端点后置维护。
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "Jeepay通道商户管理")
@RestController
@RequestMapping("/admin/jeepay/channel-merchant")
@RequiredArgsConstructor
public class JeepayChannelMerchantController {

    private final JeepayChannelMerchantService jeepayChannelMerchantService;

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "创建Jeepay通道商户")
    @PostMapping("/create")
    public Result<Void> create(@RequestBody @Validated JeepayChannelMerchantCreateParam param) {
        jeepayChannelMerchantService.create(param);
        return Res.ok();
    }
}
