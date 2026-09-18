package cn.daxpay.open.channel.easypay.controller.admin;

import cn.daxpay.open.channel.easypay.param.EasyPayChannelMerchantCreateParam;
import cn.daxpay.open.channel.easypay.service.merchant.EasyPayChannelMerchantService;
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

/// # 易支付通道商户管理
///
/// 提供易支付通道商户创建, 平台地址与商户ID(pid)随创建录入,
/// 签名密钥由同产品的密钥配置端点后置维护。
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "易支付通道商户管理")
@RestController
@RequestMapping("/admin/easypay/channel-merchant")
@RequiredArgsConstructor
public class EasyPayChannelMerchantController {

    private final EasyPayChannelMerchantService easyPayChannelMerchantService;

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "创建易支付通道商户")
    @PostMapping("/create")
    public Result<Void> create(@RequestBody @Validated EasyPayChannelMerchantCreateParam param) {
        easyPayChannelMerchantService.create(param);
        return Res.ok();
    }
}
