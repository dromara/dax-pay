package cn.daxpay.open.channel.fuyou.controller.isv;

import cn.daxpay.open.channel.fuyou.param.isv.FuyouIsvChannelMerchantCreateParam;
import cn.daxpay.open.channel.fuyou.result.isv.FuyouIsvChannelMerchantResult;
import cn.daxpay.open.channel.fuyou.service.isv.FuyouIsvChannelMerchantService;
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

/// # 富友通道商户管理
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "富友通道商户管理")
@RestController
@RequestMapping("/admin/fuyou/isv-channel-merchant")
@RequiredArgsConstructor
public class FuyouIsvChannelMerchantController {

    private final FuyouIsvChannelMerchantService fuyouIsvChannelMerchantService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据通道商户号查询富友通道商户配置")
    @GetMapping("/find-by-channel-mch-no")
    public Result<FuyouIsvChannelMerchantResult> findByChannelMchNo(
            @NotBlank(message = "{validation.field.channelMchNo.notBlank}") String channelMchNo) {
        return Res.ok(fuyouIsvChannelMerchantService.findByChannelMchNo(channelMchNo));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "创建富友通道商户")
    @PostMapping("/create")
    public Result<Void> create(@RequestBody @Validated FuyouIsvChannelMerchantCreateParam param) {
        fuyouIsvChannelMerchantService.create(param);
        return Res.ok();
    }
}
