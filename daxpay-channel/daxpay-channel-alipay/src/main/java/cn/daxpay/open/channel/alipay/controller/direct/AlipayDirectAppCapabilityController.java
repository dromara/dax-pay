package cn.daxpay.open.channel.alipay.controller.direct;

import cn.daxpay.open.channel.alipay.param.direct.AlipayDirectAppCapabilityBatchParam;
import cn.daxpay.open.channel.alipay.result.direct.AlipayDirectAppCapabilityResult;
import cn.daxpay.open.channel.alipay.result.direct.AlipayDirectCapabilityOption;
import cn.daxpay.open.channel.alipay.service.direct.AlipayDirectAppCapabilityService;
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

import java.util.List;

/// # 支付宝直连商户应用支付能力关联管理
///
/// 提供通道商户维度下「支付能力 → 应用」绑定关系的查询、批量保存及能力候选查询。
///
@PermCode(menuCode = PermCodes.Channel.App.MENU)
@Validated
@Tag(name = "支付宝直连商户应用支付能力关联管理")
@RestController
@RequestMapping("/admin/alipay/mch-app/capability")
@RequiredArgsConstructor
public class AlipayDirectAppCapabilityController {

    private final AlipayDirectAppCapabilityService alipayDirectAppCapabilityService;

    @PermCode(code = PermCodes.Action.VIEW, nameCn = PermCodes.Channel.App.VIEW_NAME_CN, nameEn = PermCodes.Channel.App.VIEW_NAME_EN)
    @Operation(summary = "查询通道商户的能力应用关联列表")
    @GetMapping("/list-by-channel-mch-no")
    public Result<List<AlipayDirectAppCapabilityResult>> listByChannelMchNo(
            @NotBlank(message = "{validation.field.mchNo.notBlank}") String mchNo,
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo) {
        return Res.ok(alipayDirectAppCapabilityService.listByChannelMchNo(mchNo, channelMchNo));
    }

    @PermCode(code = PermCodes.Action.MANAGE, nameCn = PermCodes.Channel.App.MANAGE_NAME_CN, nameEn = PermCodes.Channel.App.MANAGE_NAME_EN)
    @Operation(summary = "全量保存能力应用关联")
    @PostMapping("/save-batch")
    public Result<Void> saveBatch(@RequestBody @Validated AlipayDirectAppCapabilityBatchParam param) {
        alipayDirectAppCapabilityService.saveBatch(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW, nameCn = PermCodes.Channel.App.VIEW_NAME_CN, nameEn = PermCodes.Channel.App.VIEW_NAME_EN)
    @Operation(summary = "查询支付宝直连支持的支付能力候选")
    @GetMapping("/list-supported-capabilities")
    public Result<List<AlipayDirectCapabilityOption>> listSupportedCapabilities() {
        return Res.ok(alipayDirectAppCapabilityService.listSupportedCapabilities());
    }
}
