package cn.daxpay.open.channel.douyin.controller.direct;

import cn.daxpay.open.channel.douyin.param.direct.DouyinDirectAppCapabilityBatchParam;
import cn.daxpay.open.channel.douyin.result.DouyinCapabilityOption;
import cn.daxpay.open.channel.douyin.result.direct.DouyinDirectAppCapabilityResult;
import cn.daxpay.open.channel.douyin.service.direct.DouyinDirectAppCapabilityService;
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

/// # 抖音直连商户应用支付能力关联管理
///
/// 提供通道商户维度下「支付能力 → 直连应用」绑定关系的查询、批量保存及能力候选查询。
///
@PermCode(menuCode = PermCodes.Channel.DouyinApp.MENU)
@Validated
@Tag(name = "抖音直连商户应用支付能力关联管理")
@RestController
@RequestMapping("/admin/douyin/mch-app/capability")
@RequiredArgsConstructor
public class DouyinDirectAppCapabilityController {

    private final DouyinDirectAppCapabilityService douyinDirectAppCapabilityService;

    @PermCode(code = PermCodes.Action.VIEW, nameCn = PermCodes.Channel.Merchant.VIEW_NAME_CN, nameEn = PermCodes.Channel.Merchant.VIEW_NAME_EN)
    @Operation(summary = "查询通道商户的能力应用关联列表")
    @GetMapping("/list-by-channel-mch-no")
    public Result<List<DouyinDirectAppCapabilityResult>> listByChannelMchNo(
            @NotBlank(message = "{validation.field.mchNo.notBlank}") String mchNo,
            @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}") String channelMchNo) {
        return Res.ok(douyinDirectAppCapabilityService.listByChannelMchNo(mchNo, channelMchNo));
    }

    @PermCode(code = PermCodes.Action.MANAGE, nameCn = PermCodes.Channel.Merchant.MANAGE_NAME_CN, nameEn = PermCodes.Channel.Merchant.MANAGE_NAME_EN)
    @Operation(summary = "全量保存能力应用关联")
    @PostMapping("/save-batch")
    public Result<Void> saveBatch(@RequestBody @Validated DouyinDirectAppCapabilityBatchParam param) {
        douyinDirectAppCapabilityService.saveBatch(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW, nameCn = PermCodes.Channel.Merchant.VIEW_NAME_CN, nameEn = PermCodes.Channel.Merchant.VIEW_NAME_EN)
    @Operation(summary = "查询抖音直连支持的支付能力候选")
    @GetMapping("/list-supported-capabilities")
    public Result<List<DouyinCapabilityOption>> listSupportedCapabilities() {
        return Res.ok(douyinDirectAppCapabilityService.listSupportedCapabilities());
    }
}
