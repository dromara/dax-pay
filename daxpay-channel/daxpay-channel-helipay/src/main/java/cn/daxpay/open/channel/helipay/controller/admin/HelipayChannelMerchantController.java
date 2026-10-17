package cn.daxpay.open.channel.helipay.controller.admin;

import cn.daxpay.open.channel.helipay.param.HelipayChannelMerchantCreateParam;
import cn.daxpay.open.channel.helipay.service.merchant.HelipayChannelMerchantService;
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

/// # 合利宝通道商户管理
///
/// 提供合利宝通道商户创建(纯建档, 仅通道商户名称),
/// 商户编号/签名密钥/场景AppId 等对接配置由密钥配置端点后置维护。
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "合利宝通道商户管理")
@RestController
@RequestMapping("/admin/helipay/channel-merchant")
@RequiredArgsConstructor
public class HelipayChannelMerchantController {

    private final HelipayChannelMerchantService helipayChannelMerchantService;

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "创建合利宝通道商户")
    @PostMapping("/create")
    public Result<Void> create(@RequestBody @Validated HelipayChannelMerchantCreateParam param) {
        helipayChannelMerchantService.create(param);
        return Res.ok();
    }
}
