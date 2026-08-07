package cn.daxpay.open.channel.alipay.controller.direct;

import cn.daxpay.open.channel.alipay.param.direct.AlipayTransferConfigParam;
import cn.daxpay.open.channel.alipay.result.direct.AlipayTransferConfigResult;
import cn.daxpay.open.channel.alipay.service.direct.AlipayTransferConfigService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/// # 支付宝转账配置管理(运营端)
///
/// 管理通道商户的转账配置(转账转出应用), 挂在通道商户菜单下。
///
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "支付宝转账配置管理")
@RestController
@RequestMapping("/admin/alipay/transfer-config")
@RequiredArgsConstructor
public class AlipayTransferConfigController {

    private final AlipayTransferConfigService alipayTransferConfigService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "查询通道商户的转账配置")
    @GetMapping("/find-by-channel-mch-no")
    public Result<AlipayTransferConfigResult> findByChannelMchNo(
            @NotBlank(message = "{validation.field.mchNo.notBlank}") String mchNo,
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo) {
        return Res.ok(alipayTransferConfigService.findByChannelMchNo(mchNo, channelMchNo));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "保存或更新转账配置(一对一)")
    @PostMapping("/save")
    public Result<Void> save(@RequestBody @Validated AlipayTransferConfigParam param) {
        alipayTransferConfigService.saveOrUpdate(param);
        return Res.ok();
    }
}
