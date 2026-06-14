package org.dromara.daxpay.channel.alipay.controller.isv;

import org.dromara.daxpay.platform.core.annotation.PermCode;
import org.dromara.daxpay.platform.core.rest.Res;
import org.dromara.daxpay.platform.core.rest.result.Result;
import org.dromara.daxpay.channel.alipay.param.isv.AlipayIsvChannelMerchantCreateParam;
import org.dromara.daxpay.channel.alipay.result.isv.AlipayIsvChannelMerchantResult;
import org.dromara.daxpay.channel.alipay.service.isv.AlipayIsvChannelMerchantService;
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

/// # 支付宝服务商通道商户管理
///
@PermCode(menuCode = "payment:merchant:channelMerchant")
@Validated
@Tag(name = "支付宝服务商通道商户管理")
@RestController
@RequestMapping("/admin/alipay/isv-channel-merchant")
@RequiredArgsConstructor
public class AlipayIsvChannelMerchantController {

    private final AlipayIsvChannelMerchantService alipayIsvChannelMerchantService;

    @PermCode(code = "view", nameCn = "商户通道商户查看", nameEn = "Merchant Channel Merchant View")
    @Operation(summary = "根据通道商户号查询支付宝服务商通道商户配置")
    @GetMapping("/find-by-channel-mch-no")
    public Result<AlipayIsvChannelMerchantResult> findByChannelMchNo(
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo) {
        return Res.ok(alipayIsvChannelMerchantService.findByChannelMchNo(channelMchNo));
    }

    @PermCode(code = "add", nameCn = "商户通道商户新增", nameEn = "Merchant Channel Merchant Add")
    @Operation(summary = "创建支付宝服务商通道商户")
    @PostMapping("/create")
    public Result<Void> create(@RequestBody @Validated AlipayIsvChannelMerchantCreateParam param) {
        alipayIsvChannelMerchantService.create(param);
        return Res.ok();
    }
}
