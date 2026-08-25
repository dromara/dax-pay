package cn.daxpay.open.channel.alipay.controller.appadmin;

import cn.daxpay.open.channel.alipay.param.isv.AlipayIsvChannelMerchantCreateParam;
import cn.daxpay.open.channel.alipay.service.isv.AlipayIsvChannelMerchantService;
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

/// # 支付宝服务商通道商户管理(小程序管理端镜像)
///
/// 对应 admin 版 [AlipayIsvChannelMerchantController], 仅镜像创建通道商户端点, 复用同一 Service 与权限码。
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "小程序管理端-支付宝服务商通道商户管理")
@RestController
@RequestMapping("/app-admin/alipay/isv/channel-merchant")
@RequiredArgsConstructor
public class AppAdminAlipayIsvChannelMerchantController {

    private final AlipayIsvChannelMerchantService alipayIsvChannelMerchantService;

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "创建支付宝服务商通道商户")
    @PostMapping("/create")
    public Result<Void> create(@RequestBody @Validated AlipayIsvChannelMerchantCreateParam param) {
        alipayIsvChannelMerchantService.create(param);
        return Res.ok();
    }
}