package cn.daxpay.open.channel.fuyou.controller.rsa;

import cn.daxpay.open.channel.fuyou.param.rsa.FuyouRsaChannelMerchantCreateParam;
import cn.daxpay.open.channel.fuyou.result.rsa.FuyouRsaChannelMerchantResult;
import cn.daxpay.open.channel.fuyou.service.rsa.FuyouRsaChannelMerchantService;
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

/// # 富友 RSA 通道商户管理
///
/// 「互联网直连」(fuyou_internet) 与「H5 聚合收银台」(fuyou_h5) 共用的通道商户管理端点。
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "富友 RSA 通道商户管理")
@RestController
@RequestMapping("/admin/fuyou/rsa-channel-merchant")
@RequiredArgsConstructor
public class FuyouRsaChannelMerchantController {

    private final FuyouRsaChannelMerchantService fuyouRsaChannelMerchantService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据通道商户号查询富友 RSA 通道商户配置")
    @GetMapping("/find-by-channel-mch-no")
    public Result<FuyouRsaChannelMerchantResult> findByChannelMchNo(
            @NotBlank(message = "{validation.field.channelMchNo.notBlank}") String channelMchNo) {
        return Res.ok(fuyouRsaChannelMerchantService.findByChannelMchNo(channelMchNo));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "创建富友 RSA 通道商户")
    @PostMapping("/create")
    public Result<Void> create(@RequestBody @Validated FuyouRsaChannelMerchantCreateParam param) {
        fuyouRsaChannelMerchantService.create(param);
        return Res.ok();
    }
}
