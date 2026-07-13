package cn.daxpay.open.channel.wechat.controller.direct;

import cn.daxpay.open.channel.wechat.param.direct.WechatDirectAppCapabilityBatchParam;
import cn.daxpay.open.channel.wechat.result.WechatCapabilityOption;
import cn.daxpay.open.channel.wechat.result.direct.WechatDirectAppCapabilityResult;
import cn.daxpay.open.channel.wechat.service.direct.WechatDirectAppCapabilityService;
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

/// # 微信直连商户应用支付能力关联管理
///
/// 提供通道商户维度下「支付能力 → 直连应用」绑定关系的查询、批量保存及能力候选查询。
///
@PermCode(menuCode = PermCodes.Channel.WechatApp.MENU)
@Validated
@Tag(name = "微信直连商户应用支付能力关联管理")
@RestController
@RequestMapping("/admin/wechat/mch-app/capability")
@RequiredArgsConstructor
public class WechatDirectAppCapabilityController {

    private final WechatDirectAppCapabilityService wechatDirectAppCapabilityService;

    @PermCode(code = PermCodes.Action.VIEW, nameCn = PermCodes.Channel.Merchant.VIEW_NAME_CN, nameEn = PermCodes.Channel.Merchant.VIEW_NAME_EN)
    @Operation(summary = "查询通道商户的能力应用关联列表")
    @GetMapping("/list-by-channel-mch-no")
    public Result<List<WechatDirectAppCapabilityResult>> listByChannelMchNo(
            @NotBlank(message = "{validation.field.mchNo.notBlank}") String mchNo,
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo) {
        return Res.ok(wechatDirectAppCapabilityService.listByChannelMchNo(mchNo, channelMchNo));
    }

    @PermCode(code = PermCodes.Action.MANAGE, nameCn = PermCodes.Channel.Merchant.MANAGE_NAME_CN, nameEn = PermCodes.Channel.Merchant.MANAGE_NAME_EN)
    @Operation(summary = "全量保存能力应用关联")
    @PostMapping("/save-batch")
    public Result<Void> saveBatch(@RequestBody @Validated WechatDirectAppCapabilityBatchParam param) {
        wechatDirectAppCapabilityService.saveBatch(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW, nameCn = PermCodes.Channel.Merchant.VIEW_NAME_CN, nameEn = PermCodes.Channel.Merchant.VIEW_NAME_EN)
    @Operation(summary = "查询微信直连支持的支付能力候选")
    @GetMapping("/list-supported-capabilities")
    public Result<List<WechatCapabilityOption>> listSupportedCapabilities() {
        return Res.ok(wechatDirectAppCapabilityService.listSupportedCapabilities());
    }
}
