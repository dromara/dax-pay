package cn.daxpay.open.channel.sheng.controller.admin;

import cn.daxpay.open.channel.sheng.param.ShengChannelMerchantCreateParam;
import cn.daxpay.open.channel.sheng.service.merchant.ShengChannelMerchantService;
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

/// # 盛付通通道商户管理
///
/// 提供盛付通通道商户创建, 盛付通商户身份随创建录入,
/// 签名密钥由同产品的密钥配置(密钥配置端点)后置维护。
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "盛付通通道商户管理")
@RestController
@RequestMapping("/admin/sheng/channel-merchant")
@RequiredArgsConstructor
public class ShengChannelMerchantController {

    private final ShengChannelMerchantService shengChannelMerchantService;

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "创建盛付通通道商户")
    @PostMapping("/create")
    public Result<Void> create(@RequestBody @Validated ShengChannelMerchantCreateParam param) {
        shengChannelMerchantService.create(param);
        return Res.ok();
    }
}
