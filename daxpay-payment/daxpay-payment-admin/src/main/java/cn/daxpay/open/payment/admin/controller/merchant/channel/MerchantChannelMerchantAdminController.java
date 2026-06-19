package cn.daxpay.open.payment.admin.controller.merchant.channel;

import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.dto.LabelValue;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.payment.channel.param.mch.ChannelMerchantQuery;
import cn.daxpay.open.payment.channel.result.info.ChannelMerchantResult;
import cn.daxpay.open.payment.channel.service.info.ChannelMerchantService;
import cn.daxpay.open.payment.masterdata.constants.channel.result.PayChannelResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/// 商户通道商户管理(运营平台)
/// 从商户入口进入，管理指定商户的通道商户
@PermCode(menuCode = "payment:merchant:channelMerchant")
@Validated
@Tag(name = "商户通道商户管理(运营平台)")
@RestController
@RequestMapping("/admin/merchant/channel-merchant")
@RequiredArgsConstructor
public class MerchantChannelMerchantAdminController {
    private final ChannelMerchantService channelMerchantService;

    @PermCode(code = "view", nameCn = "商户通道商户查看", nameEn = "Merchant Channel Merchant View")
    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public Result<PageResult<ChannelMerchantResult>> page(PageParam pageParam, ChannelMerchantQuery query) {
        return Res.ok(channelMerchantService.page(pageParam, query));
    }

    @PermCode(code = "view", nameCn = "商户通道商户查看", nameEn = "Merchant Channel Merchant View")
    @Operation(summary = "查询详情")
    @GetMapping("/get")
    public Result<ChannelMerchantResult> findById(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(channelMerchantService.findById(id));
    }

    @PermCode(code = "view", nameCn = "商户通道商户查看", nameEn = "Merchant Channel Merchant View")
    @Operation(summary = "根据商户号查询所有通道商户")
    @GetMapping("/all-by-mch-no")
    public Result<List<ChannelMerchantResult>> findAllByMchNo(@NotBlank(message = "{validation.field.mchNo.notBlank}") String mchNo) {
        return Res.ok(channelMerchantService.findAllByMchNo(mchNo));
    }

    @PermCode(code = "edit", nameCn = "商户通道商户编辑", nameEn = "Merchant Channel Merchant Edit")
    @Operation(summary = "更新启用状态")
    @PostMapping("/update-enable")
    public Result<Void> updateEnable(@NotNull(message = "{validation.field.id.notNull}") Long id, @NotNull(message = "{validation.field.enable.notNull}") Boolean enable) {
        channelMerchantService.updateEnable(id, enable);
        return Res.ok();
    }

    @PermCode(code = "view", nameCn = "商户通道商户查看", nameEn = "Merchant Channel Merchant View")
    @Operation(summary = "根据商户和通道查询通道商户号列表")
    @GetMapping("/dropdown")
    public Result<List<LabelValue>> dropdown(@NotBlank(message = "{validation.field.mchNo.notBlank}") String mchNo, @NotBlank(message = "{validation.field.channel.notBlank}") String channel) {
        return Res.ok(channelMerchantService.dropdown(mchNo, channel));
    }

    @PermCode(code = "view", nameCn = "商户通道商户查看", nameEn = "Merchant Channel Merchant View")
    @Operation(summary = "根据商户号查询通道")
    @GetMapping("/channel/dropdown-by-mch-no")
    public Result<List<PayChannelResult>> dropdownByMchNo(@NotBlank(message = "{validation.field.mchNo.notBlank}") String mchNo) {
        return Res.ok(channelMerchantService.dropdownByMchNo(mchNo));
    }
}
