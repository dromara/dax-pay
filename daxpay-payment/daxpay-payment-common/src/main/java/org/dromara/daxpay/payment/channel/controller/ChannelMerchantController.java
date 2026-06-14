package org.dromara.daxpay.payment.channel.controller;

import org.dromara.daxpay.platform.core.rest.Res;
import org.dromara.daxpay.platform.core.rest.dto.LabelValue;
import org.dromara.daxpay.platform.core.rest.param.PageParam;
import org.dromara.daxpay.platform.core.rest.result.PageResult;
import org.dromara.daxpay.platform.core.rest.result.Result;
import org.dromara.daxpay.payment.channel.param.mch.ChannelMerchantQuery;
import org.dromara.daxpay.payment.channel.result.info.ChannelMerchantResult;
import org.dromara.daxpay.payment.channel.service.info.ChannelMerchantService;
import org.dromara.daxpay.payment.pay.result.masterdata.channel.PayChannelResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/// # 通道商户信息管理
///
@Validated
@Tag(name = "通道商户信息管理")
@RestController
@RequestMapping("/channel/merchant")
@RequiredArgsConstructor
public class ChannelMerchantController {
    private final ChannelMerchantService channelMerchantService;

    @Operation(summary = "根据商户和通道查询通道商户号列表")
    @GetMapping("/dropdown")
    public Result<List<LabelValue>> dropdown(@NotBlank(message = "{validation.field.mchNo.notBlank}") String mchNo, @NotBlank(message = "{validation.field.channel.notBlank}") String channel) {
        return Res.ok(channelMerchantService.dropdown(mchNo, channel));
    }

    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public Result<PageResult<ChannelMerchantResult>> page(PageParam pageParam, ChannelMerchantQuery query) {
        return Res.ok(channelMerchantService.page(pageParam, query));
    }

    @Operation(summary = "查询详情")
    @GetMapping("/get")
    public Result<ChannelMerchantResult> findById(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(channelMerchantService.findById(id));
    }

    @Operation(summary = "根据商户号查询通道")
    @GetMapping("/channel/dropdown-by-mch-no")
    public Result<List<PayChannelResult>> dropdownByMchNo(@NotBlank(message = "{validation.field.mchNo.notBlank}") String mchNo) {
        return Res.ok(channelMerchantService.dropdownByMchNo(mchNo));
    }

    @Operation(summary = "根据商户号查询所有通道商户")
    @GetMapping("/all-by-mch-no")
    public Result<List<ChannelMerchantResult>> findAllByMchNo(@NotBlank(message = "{validation.field.mchNo.notBlank}") String mchNo) {
        return Res.ok(channelMerchantService.findAllByMchNo(mchNo));
    }

    @Operation(summary = "更新启用状态")
    @PostMapping("/update-enable")
    public Result<Void> updateEnable(@NotNull(message = "{validation.field.id.notNull}") Long id, @NotNull(message = "{validation.field.enable.notNull}") Boolean enable) {
        channelMerchantService.updateEnable(id, enable);
        return Res.ok();
    }
}
