package cn.daxpay.open.channel.hkrt.controller.isv;

import cn.daxpay.open.channel.hkrt.param.isv.HkrtIsvChannelMerchantCreateParam;
import cn.daxpay.open.channel.hkrt.result.isv.HkrtIsvChannelMerchantResult;
import cn.daxpay.open.channel.hkrt.service.isv.HkrtIsvChannelMerchantService;
import cn.daxpay.open.platform.core.annotation.PermCode;
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

/// # 海科融通通道商户管理
///
@PermCode(menuCode = "channel:merchant")
@Validated
@Tag(name = "海科融通通道商户管理")
@RestController
@RequestMapping("/admin/hkrt/isv-channel-merchant")
@RequiredArgsConstructor
public class HkrtIsvChannelMerchantController {

    private final HkrtIsvChannelMerchantService hkrtIsvChannelMerchantService;

    @PermCode(code = "view", nameCn = "通道商户查看", nameEn = "Channel Merchant View")
    @Operation(summary = "根据通道商户号查询海科融通通道商户配置")
    @GetMapping("/find-by-channel-mch-no")
    public Result<HkrtIsvChannelMerchantResult> findByChannelMchNo(
            @NotBlank(message = "{validation.field.channelMchNo.notBlank}") String channelMchNo) {
        return Res.ok(hkrtIsvChannelMerchantService.findByChannelMchNo(channelMchNo));
    }

    @PermCode(code = "manage", nameCn = "通道商户管理", nameEn = "Channel Merchant Manage")
    @Operation(summary = "创建海科融通通道商户")
    @PostMapping("/create")
    public Result<Void> create(@RequestBody @Validated HkrtIsvChannelMerchantCreateParam param) {
        hkrtIsvChannelMerchantService.create(param);
        return Res.ok();
    }

    @PermCode(code = "manage", nameCn = "通道商户管理", nameEn = "Channel Merchant Manage")
    @Operation(summary = "更新SAAS终端号")
    @PostMapping("/update-pn")
    public Result<Void> updatePn(
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo,
            @NotBlank(message = "{validation.field.pn.notBlank}") String pn) {
        hkrtIsvChannelMerchantService.updatePn(channelMchNo, pn);
        return Res.ok();
    }
}
