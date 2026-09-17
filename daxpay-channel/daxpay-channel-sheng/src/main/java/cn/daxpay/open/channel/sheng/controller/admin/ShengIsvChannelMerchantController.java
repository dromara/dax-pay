package cn.daxpay.open.channel.sheng.controller.admin;

import cn.daxpay.open.channel.sheng.param.ShengIsvChannelMerchantCreateParam;
import cn.daxpay.open.channel.sheng.result.ShengIsvChannelMerchantResult;
import cn.daxpay.open.channel.sheng.service.merchant.ShengIsvChannelMerchantService;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 盛付通服务商通道商户管理
///
/// 提供盛付通服务商模式下的子商户绑定创建, 子商户身份(subMchId/sdpAppId)随创建录入,
/// 服务商签名密钥由产品级密钥配置(服务商密钥配置端点)全局统一维护。
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "盛付通服务商通道商户管理")
@RestController
@RequestMapping("/admin/sheng/isv-channel-merchant")
@RequiredArgsConstructor
public class ShengIsvChannelMerchantController {

    private final ShengIsvChannelMerchantService shengIsvChannelMerchantService;

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "创建盛付通服务商通道商户")
    @PostMapping("/create")
    public Result<Void> create(@RequestBody @Validated ShengIsvChannelMerchantCreateParam param) {
        shengIsvChannelMerchantService.create(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据通道商户号查询盛付通服务商子商户绑定")
    @GetMapping("/find-by-channel-mch-no")
    public Result<ShengIsvChannelMerchantResult> findByChannelMchNo(
            @NotBlank(message = "{validation.field.channelMchNo.notBlank}") String channelMchNo) {
        return Res.ok(shengIsvChannelMerchantService.findByChannelMchNo(channelMchNo));
    }
}
