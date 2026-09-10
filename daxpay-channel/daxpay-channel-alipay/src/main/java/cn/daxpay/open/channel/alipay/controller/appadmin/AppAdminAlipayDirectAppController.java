package cn.daxpay.open.channel.alipay.controller.appadmin;

import cn.daxpay.open.channel.alipay.result.direct.AlipayDirectAppResult;
import cn.daxpay.open.channel.alipay.service.direct.AlipayDirectAppService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/// # 支付宝直连应用管理(小程序管理端镜像)
///
/// 对应 admin 版 [AlipayDirectAppController], 仅镜像转账配置页所需的只读应用列表端点,
/// 复用同一 Service 与权限码。
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "小程序管理端-支付宝直连应用管理")
@RestController
@RequestMapping("/app-admin/alipay/mch-app")
@RequiredArgsConstructor
public class AppAdminAlipayDirectAppController {

    private final AlipayDirectAppService alipayDirectAppService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "按商户号与通道商户号查询支付宝应用列表")
    @GetMapping("/list-by-channel-mch-no")
    public Result<List<AlipayDirectAppResult>> listByChannelMchNo(
            @NotBlank(message = "{validation.field.mchNo.notBlank}") String mchNo,
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo) {
        return Res.ok(alipayDirectAppService.listByMchNoAndChannelMchNo(mchNo, channelMchNo));
    }
}
