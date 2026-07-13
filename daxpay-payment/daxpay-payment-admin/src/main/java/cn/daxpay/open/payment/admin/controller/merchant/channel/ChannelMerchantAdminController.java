package cn.daxpay.open.payment.admin.controller.merchant.channel;


import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.payment.merchant.param.channel.ChannelMerchantEditParam;
import cn.daxpay.open.payment.merchant.param.channel.ChannelMerchantQuery;
import cn.daxpay.open.payment.merchant.result.channel.ChannelMerchantResult;
import cn.daxpay.open.payment.merchant.service.channel.ChannelMerchantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/// 通道商户管理(运营平台-综合管理)
/// 全局查看和管理所有通道商户，仅支持查看和编辑
@PermCode(menuCode = PermCodes.Channel.Merchant.MENU)
@Validated
@Tag(name = "通道商户管理(运营平台-综合管理)")
@RestController
@RequestMapping("/admin/channel/merchant")
@RequiredArgsConstructor
public class ChannelMerchantAdminController {
    private final ChannelMerchantService channelMerchantService;

    @PermCode(code = PermCodes.Action.MANAGE, nameCn = PermCodes.Channel.Merchant.MANAGE_NAME_CN, nameEn = PermCodes.Channel.Merchant.MANAGE_NAME_EN)
    @Operation(summary = "修改")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated ChannelMerchantEditParam param) {
        channelMerchantService.update(param);
        return Res.ok();
    }

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

    @PermCode(code = PermCodes.Action.MANAGE, nameCn = PermCodes.Channel.Merchant.MANAGE_NAME_CN, nameEn = PermCodes.Channel.Merchant.MANAGE_NAME_EN)
    @Operation(summary = "更新启用状态")
    @PostMapping("/update-enable")
    public Result<Void> updateEnable(@NotNull(message = "{validation.field.id.notNull}") Long id, @NotNull(message = "{validation.field.enable.notNull}") Boolean enable) {
        channelMerchantService.updateEnable(id, enable);
        return Res.ok();
    }
}
