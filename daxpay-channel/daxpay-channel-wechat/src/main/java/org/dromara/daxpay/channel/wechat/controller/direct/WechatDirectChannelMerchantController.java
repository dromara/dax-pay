package org.dromara.daxpay.channel.wechat.controller.direct;

import org.dromara.daxpay.platform.core.annotation.PermCode;
import org.dromara.daxpay.platform.core.rest.Res;
import org.dromara.daxpay.platform.core.rest.result.Result;
import org.dromara.daxpay.channel.wechat.param.direct.WechatDirectChannelMerchantCreateParam;
import org.dromara.daxpay.channel.wechat.result.direct.WechatDirectChannelMerchantResult;
import org.dromara.daxpay.channel.wechat.service.direct.WechatDirectChannelMerchantService;
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

/// # 微信直连通道商户管理
///
@PermCode(menuCode = "payment:merchant:channelMerchant")
@Validated
@Tag(name = "微信直连通道商户管理")
@RestController
@RequestMapping("/admin/wechat/direct-channel-merchant")
@RequiredArgsConstructor
public class WechatDirectChannelMerchantController {

    private final WechatDirectChannelMerchantService wechatDirectChannelMerchantService;

    @PermCode(code = "view", nameCn = "商户通道商户查看", nameEn = "Merchant Channel Merchant View")
    @Operation(summary = "根据通道商户号查询微信直连通道商户配置")
    @GetMapping("/find-by-channel-mch-no")
    public Result<WechatDirectChannelMerchantResult> findByChannelMchNo(
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo) {
        return Res.ok(wechatDirectChannelMerchantService.findByChannelMchNo(channelMchNo));
    }

    @PermCode(code = "add", nameCn = "商户通道商户新增", nameEn = "Merchant Channel Merchant Add")
    @Operation(summary = "创建微信直连通道商户")
    @PostMapping("/create")
    public Result<Void> create(@RequestBody @Validated WechatDirectChannelMerchantCreateParam param) {
        wechatDirectChannelMerchantService.create(param);
        return Res.ok();
    }
}
