package cn.daxpay.open.channel.wechat.controller.isv;

import cn.daxpay.open.channel.wechat.param.isv.WechatIsvMchAppCapabilityBatchParam;
import cn.daxpay.open.channel.wechat.result.WechatCapabilityOption;
import cn.daxpay.open.channel.wechat.result.isv.WechatIsvMchAppCapabilityResult;
import cn.daxpay.open.channel.wechat.service.isv.WechatIsvMchAppCapabilityService;
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

/// # 微信服务商通道商户应用支付能力关联管理
///
/// 提供通道商户(特约商户)维度下「支付能力 → 子商户应用」绑定关系的查询、批量保存及能力候选查询。
///
@PermCode(menuCode = PermCodes.Channel.WechatApp.MENU)
@Validated
@Tag(name = "微信服务商通道商户应用支付能力关联管理")
@RestController
@RequestMapping("/admin/wechat/isv-mch-app/capability")
@RequiredArgsConstructor
public class WechatIsvMchAppCapabilityController {

    private final WechatIsvMchAppCapabilityService wechatIsvMchAppCapabilityService;

    @PermCode(code = PermCodes.Action.VIEW, nameCn = PermCodes.Channel.Merchant.VIEW_NAME_CN, nameEn = PermCodes.Channel.Merchant.VIEW_NAME_EN)
    @Operation(summary = "查询通道商户的能力应用关联列表")
    @GetMapping("/list-by-channel-mch-no")
    public Result<List<WechatIsvMchAppCapabilityResult>> listByChannelMchNo(
            @NotBlank(message = "{validation.field.mchNo.notBlank}") String mchNo,
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo) {
        return Res.ok(wechatIsvMchAppCapabilityService.listByChannelMchNo(mchNo, channelMchNo));
    }

    @PermCode(code = PermCodes.Action.MANAGE, nameCn = PermCodes.Channel.Merchant.MANAGE_NAME_CN, nameEn = PermCodes.Channel.Merchant.MANAGE_NAME_EN)
    @Operation(summary = "全量保存能力应用关联")
    @PostMapping("/save-batch")
    public Result<Void> saveBatch(@RequestBody @Validated WechatIsvMchAppCapabilityBatchParam param) {
        wechatIsvMchAppCapabilityService.saveBatch(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW, nameCn = PermCodes.Channel.Merchant.VIEW_NAME_CN, nameEn = PermCodes.Channel.Merchant.VIEW_NAME_EN)
    @Operation(summary = "查询微信服务商支持的支付能力候选")
    @GetMapping("/list-supported-capabilities")
    public Result<List<WechatCapabilityOption>> listSupportedCapabilities() {
        return Res.ok(wechatIsvMchAppCapabilityService.listSupportedCapabilities());
    }
}
