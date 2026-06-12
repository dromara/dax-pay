package org.dromara.daxpay.payment.channel.controller;

import org.dromara.daxpay.platform.core.annotation.*;
import org.dromara.daxpay.platform.core.rest.Res;
import org.dromara.daxpay.platform.core.rest.dto.LabelValue;
import org.dromara.daxpay.platform.core.rest.param.PageParam;
import org.dromara.daxpay.platform.core.rest.result.PageResult;
import org.dromara.daxpay.platform.core.rest.result.Result;
import org.dromara.daxpay.payment.channel.param.apply.OnbMchApplyAuditParam;
import org.dromara.daxpay.payment.channel.param.apply.OnbMchApplyParam;
import org.dromara.daxpay.payment.channel.param.apply.OnbMchApplyQuery;
import org.dromara.daxpay.payment.channel.result.apply.OnbMchApplyResult;
import org.dromara.daxpay.payment.channel.service.apply.OnbMchApplyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/// # 商户进件申请
///
@Validated
@Tag(name = "商户进件申请")
@RestController
@RequestMapping("/onb/mch/apply")
@RequiredArgsConstructor
public class OnbMchApplyController {
    private final OnbMchApplyService onbMchApplyService;

    @Operation(summary = "提交申请")
    @PostMapping("/submit")
    public Result<Void> submit(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        onbMchApplyService.submit(id,null);
        return Res.ok();
    }

    @IgnoreTenant
    @Operation(summary = "提交申请(H5)")
    @PostMapping("/h5/submit")
    public Result<Void> submitH5(@NotNull(message = "{validation.field.id.notNull}") Long id,@NotBlank(message = "{validation.field.sign.notBlank}") String sign) {
        onbMchApplyService.submit(id,sign);
        return Res.ok();
    }

    @Operation(summary = "更新状态")
    @PostMapping("/sync")
    public Result<Void> sync(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        onbMchApplyService.sync(id);
        return Res.ok();
    }

    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public Result<PageResult<OnbMchApplyResult>> page(PageParam pageParam, OnbMchApplyQuery query) {
        return Res.ok(onbMchApplyService.page(pageParam, query));
    }

    @Operation(summary = "查询简单详情")
    @GetMapping("/get")
    public Result<OnbMchApplyResult> findById(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(onbMchApplyService.findById(id));
    }

    @Operation(summary = "根据商户号查询进件通道")
    @GetMapping("/channel/dropdown-by-mch-no")
    public Result<List<LabelValue>> dropdownByMchNo(@NotNull(message = "{validation.field.id.notNull}") String mchNo) {
        return Res.ok(onbMchApplyService.dropdownByMchNo(mchNo));
    }

    @IgnoreAuth
    @Operation(summary = "根据通道查询可进件申请类型")
    @GetMapping("/type/dropdown-by-channel")
    public Result<List<LabelValue>> dropdownByChannel(@NotNull(message = "{validation.field.channel.notBlank}") String channel) {
        return Res.ok(onbMchApplyService.dropdownByChannel(channel));
    }

    @Operation(summary = "创建申请")
    @PostMapping("/create")
    public Result<Long> create(@RequestBody @Validated OnbMchApplyParam param) {
        return Res.ok(onbMchApplyService.create(param));
    }

    @Operation(summary = "生成进件商户信息")
    @PostMapping("/gen-mch-info")
    public Result<Void> genMchInfo(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        onbMchApplyService.genMchInfo(id);
        return Res.ok();
    }



    @Operation(summary = "删除")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        onbMchApplyService.delete(id);
        return Res.ok();
    }

    @Operation(summary = "审核")
    @PostMapping("/audit")
    public Result<Void> audit(@RequestBody @Validated OnbMchApplyAuditParam param) {
        onbMchApplyService.audit(param);
        return Res.ok();
    }
}

