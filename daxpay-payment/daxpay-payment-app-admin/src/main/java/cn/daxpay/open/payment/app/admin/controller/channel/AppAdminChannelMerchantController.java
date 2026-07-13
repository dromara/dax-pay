package cn.daxpay.open.payment.app.admin.controller.channel;

import cn.daxpay.open.payment.app.admin.service.channel.AppAdminChannelMerchantService;
import cn.daxpay.open.payment.channel.param.mch.ChannelMerchantEditParam;
import cn.daxpay.open.payment.channel.param.mch.ChannelMerchantQuery;
import cn.daxpay.open.payment.channel.result.info.ChannelMerchantResult;
import cn.daxpay.open.payment.masterdata.constants.channel.result.PayChannelResult;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.dto.LabelValue;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/// 运营移动端-商户通道商户管理
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "运营移动端-商户通道商户管理")
@RestController
@RequestMapping("/app-admin/merchant/channel-merchant")
@RequiredArgsConstructor
public class AppAdminChannelMerchantController {

    private final AppAdminChannelMerchantService channelMerchantService;

    @PermCode(code = PermCodes.Action.VIEW, nameCn = PermCodes.Channel.Merchant.VIEW_NAME_CN, nameEn = PermCodes.Channel.Merchant.VIEW_NAME_EN)
    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public Result<PageResult<ChannelMerchantResult>> page(PageParam pageParam, ChannelMerchantQuery query) {
        return Res.ok(channelMerchantService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.VIEW, nameCn = PermCodes.Channel.Merchant.VIEW_NAME_CN, nameEn = PermCodes.Channel.Merchant.VIEW_NAME_EN)
    @Operation(summary = "查询详情")
    @GetMapping("/get")
    public Result<ChannelMerchantResult> findById(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(channelMerchantService.findById(id));
    }

    @PermCode(code = PermCodes.Action.VIEW, nameCn = PermCodes.Channel.Merchant.VIEW_NAME_CN, nameEn = PermCodes.Channel.Merchant.VIEW_NAME_EN)
    @Operation(summary = "根据商户号查询所有通道商户")
    @GetMapping("/all-by-mch-no")
    public Result<List<ChannelMerchantResult>> findAllByMchNo(
            @NotBlank(message = "{validation.field.mchNo.notBlank}") String mchNo) {
        return Res.ok(channelMerchantService.findAllByMchNo(mchNo));
    }

    @PermCode(code = PermCodes.Action.MANAGE, nameCn = PermCodes.Channel.Merchant.MANAGE_NAME_CN, nameEn = PermCodes.Channel.Merchant.MANAGE_NAME_EN)
    @Operation(summary = "更新启用状态")
    @PostMapping("/update-enable")
    public Result<Void> updateEnable(
            @NotNull(message = "{validation.field.id.notNull}") Long id,
            @NotNull(message = "{validation.field.enable.notNull}") Boolean enable) {
        channelMerchantService.updateEnable(id, enable);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE, nameCn = PermCodes.Channel.Merchant.MANAGE_NAME_CN, nameEn = PermCodes.Channel.Merchant.MANAGE_NAME_EN)
    @Operation(summary = "修改商户名称")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated ChannelMerchantEditParam param) {
        channelMerchantService.update(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW, nameCn = PermCodes.Channel.Merchant.VIEW_NAME_CN, nameEn = PermCodes.Channel.Merchant.VIEW_NAME_EN)
    @Operation(summary = "根据商户和通道查询通道商户号列表")
    @GetMapping("/dropdown")
    public Result<List<LabelValue>> dropdown(
            @NotBlank(message = "{validation.field.mchNo.notBlank}") String mchNo,
            @NotBlank(message = "{validation.field.channel.notBlank}") String channel) {
        return Res.ok(channelMerchantService.dropdown(mchNo, channel));
    }

    @PermCode(code = PermCodes.Action.VIEW, nameCn = PermCodes.Channel.Merchant.VIEW_NAME_CN, nameEn = PermCodes.Channel.Merchant.VIEW_NAME_EN)
    @Operation(summary = "根据商户号查询通道")
    @GetMapping("/channel/dropdown-by-mch-no")
    public Result<List<PayChannelResult>> dropdownByMchNo(
            @NotBlank(message = "{validation.field.mchNo.notBlank}") String mchNo) {
        return Res.ok(channelMerchantService.dropdownByMchNo(mchNo));
    }
}
