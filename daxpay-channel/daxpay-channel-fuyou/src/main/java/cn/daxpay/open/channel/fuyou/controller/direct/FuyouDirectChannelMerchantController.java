package cn.daxpay.open.channel.fuyou.controller.direct;

import cn.daxpay.open.channel.fuyou.param.direct.FuyouDirectChannelMerchantCreateParam;
import cn.daxpay.open.channel.fuyou.result.direct.FuyouDirectChannelMerchantResult;
import cn.daxpay.open.channel.fuyou.service.direct.FuyouDirectChannelMerchantService;
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

/// # 富友直连通道商户管理
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "富友直连通道商户管理")
@RestController
@RequestMapping("/admin/fuyou/direct-channel-merchant")
@RequiredArgsConstructor
public class FuyouDirectChannelMerchantController {

    private final FuyouDirectChannelMerchantService fuyouDirectChannelMerchantService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据通道商户号查询富友直连通道商户配置")
    @GetMapping("/find-by-channel-mch-no")
    public Result<FuyouDirectChannelMerchantResult> findByChannelMchNo(
            @NotBlank(message = "{validation.field.channelMchNo.notBlank}") String channelMchNo) {
        return Res.ok(fuyouDirectChannelMerchantService.findByChannelMchNo(channelMchNo));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "创建富友直连通道商户")
    @PostMapping("/create")
    public Result<Void> create(@RequestBody @Validated FuyouDirectChannelMerchantCreateParam param) {
        fuyouDirectChannelMerchantService.create(param);
        return Res.ok();
    }
}
