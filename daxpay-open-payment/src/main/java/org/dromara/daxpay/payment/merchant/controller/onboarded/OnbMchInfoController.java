package org.dromara.daxpay.payment.merchant.controller.onboarded;

import cn.bootx.platform.core.annotation.ClientCode;
import cn.bootx.platform.core.annotation.OperateLog;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.dto.LabelValue;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.core.validation.ValidationGroup;
import org.dromara.core.trans.anno.TransMethodResult;
import org.dromara.daxpay.payment.common.code.DaxPayCode;
import org.dromara.daxpay.payment.merchant.param.onboarded.OnbMchInfoParam;
import org.dromara.daxpay.payment.merchant.param.onboarded.OnbMchInfoQuery;
import org.dromara.daxpay.payment.merchant.result.onboarded.OnbMchInfoResult;
import org.dromara.daxpay.payment.merchant.service.onboarded.OnbMchInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 进件商户信息
 * @author xxm
 * @since 2025/11/11
 */
@Validated
@ClientCode(DaxPayCode.Client.ADMIN)
@Tag(name = "进件商户信息")
@RestController
@RequestMapping("/onb/mch")
@RequestGroup(groupCode = "onbMchInfo", groupName = "进件商户信息", moduleCode = "merchant")
@RequiredArgsConstructor
public class OnbMchInfoController {
    private final OnbMchInfoService onbMchInfoService;

    @RequestPath("新增进件商户信息")
    @Operation(summary = "新增进件商户信息")
    @OperateLog(title = "新增进件商户信息", businessType = OperateLog.BusinessType.ADD, saveParam = true)
    @PostMapping("/add")
    public Result<Void> add(@RequestBody @Validated(ValidationGroup.add.class) OnbMchInfoParam param){
        onbMchInfoService.add(param);
        return Res.ok();
    }

    @RequestPath("修改进件商户信息")
    @Operation(summary = "修改进件商户信息")
    @OperateLog(title = "修改进件商户信息", businessType = OperateLog.BusinessType.UPDATE, saveParam = true)
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) OnbMchInfoParam param){
        onbMchInfoService.update(param);
        return Res.ok();
    }

    @ClientCode({DaxPayCode.Client.MERCHANT, DaxPayCode.Client.ADMIN})
    @RequestPath("根据进件商户号、商户号和所属通道判断是否存在")
    @Operation(summary = "根据进件商户号、商户号和所属通道判断是否存在")
    @GetMapping("/existsByOnbMchNo")
    public Result<Boolean> existsByOnbMchNo(@NotNull(message = "进件商户号不可为空") String onbMchNo, 
                                           @NotNull(message = "商户号不可为空") String mchNo, 
                                           @NotNull(message = "所属通道不可为空") String onbChannel) {
        return Res.ok(onbMchInfoService.existsByOnbMchNo(onbMchNo, mchNo, onbChannel));
    }

    @ClientCode({DaxPayCode.Client.MERCHANT, DaxPayCode.Client.ADMIN})
    @RequestPath("根据进件商户号查询")
    @Operation(summary = "根据进件商户号查询")
    @GetMapping("/findByOnbMchNo")
    public Result<OnbMchInfoResult> findByOnbMchNo(String onbMchNo) {
        return Res.ok(onbMchInfoService.findByOnbMchNo(onbMchNo));
    }

    @TransMethodResult
    @ClientCode({DaxPayCode.Client.MERCHANT, DaxPayCode.Client.ADMIN})
    @RequestPath("进件商户信息分页")
    @Operation(summary = "进件商户信息分页")
    @GetMapping("/page")
    public Result<PageResult<OnbMchInfoResult>> page(PageParam pageParam, OnbMchInfoQuery query){
        return Res.ok(onbMchInfoService.page(pageParam, query));
    }

    @ClientCode({DaxPayCode.Client.MERCHANT, DaxPayCode.Client.ADMIN})
    @RequestPath("根据id查询进件商户信息")
    @Operation(summary = "根据id查询进件商户信息")
    @GetMapping("/findById")
    public Result<OnbMchInfoResult> findById(@NotNull(message = "id不可为空")Long id){
        return Res.ok(onbMchInfoService.findById(id));
    }

    @ClientCode({DaxPayCode.Client.MERCHANT, DaxPayCode.Client.ADMIN})
    @RequestPath("根据所属通道查询进件商户信息")
    @Operation(summary = "根据所属通道查询进件商户信息")
    @GetMapping("/findByChannel")
    public Result<List<LabelValue>> findByChannel(@NotNull(message = "商户号不可为空") String mchNo, @NotNull(message = "所属通道不可为空") String channel) {
        return Res.ok(onbMchInfoService.findByChannel(mchNo, channel));
    }

    @RequestPath("删除进件商户信息")
    @OperateLog(title = "删除进件商户信息", businessType = OperateLog.BusinessType.DELETE, saveParam = true)
    @Operation(summary = "删除进件商户信息")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "id不可为空") Long id){
        onbMchInfoService.delete(id);
        return Res.ok();
    }
}
