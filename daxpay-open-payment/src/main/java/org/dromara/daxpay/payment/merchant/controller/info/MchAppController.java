package org.dromara.daxpay.payment.merchant.controller.info;

import cn.bootx.platform.core.annotation.ClientCode;
import cn.bootx.platform.core.annotation.OperateLog;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.core.validation.ValidationGroup;
import org.dromara.daxpay.payment.common.code.DaxPayCode;
import org.dromara.daxpay.payment.merchant.param.app.MchAppParam;
import org.dromara.daxpay.payment.merchant.param.app.MchAppQuery;
import org.dromara.daxpay.payment.merchant.result.app.MchAppResult;
import org.dromara.daxpay.payment.merchant.service.app.MchAppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商户应用配置
 * @author xxm
 * @since 2024/6/25
 */
@Validated
@ClientCode(DaxPayCode.Client.MERCHANT)
@Tag(name = "商户应用配置")
@RestController
@RequestMapping("/mch/app")
@RequestGroup(groupCode = "mchApp", groupName = "商户应用配置", moduleCode = "merchant")
@RequiredArgsConstructor
public class MchAppController {
    private final MchAppService mchAppService;

    @RequestPath("新增商户应用")
    @Operation(summary = "新增商户应用")
    @OperateLog(title = "新增商户应用信息 ", businessType = OperateLog.BusinessType.ADD, saveParam = true)
    @PostMapping("/add")
    public Result<Void> add(@RequestBody @Validated(ValidationGroup.add.class) MchAppParam param){
        mchAppService.add(param);
        return Res.ok();
    }

    @RequestPath("修改商户应用")
    @Operation(summary = "修改商户应用")
    @OperateLog(title = "修改商户应用信息 ", businessType = OperateLog.BusinessType.UPDATE, saveParam = true)
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) MchAppParam param){
        mchAppService.update(param);
        return Res.ok();
    }

    @RequestPath("商户应用分页")
    @Operation(summary = "商户应用分页")
    @GetMapping("/page")
    public Result<PageResult<MchAppResult>> page(PageParam pageParam, MchAppQuery query){
        return Res.ok(mchAppService.page(pageParam, query));
    }

    @RequestPath("商户应用列表")
    @Operation(summary = "商户应用列表")
    @GetMapping("/list")
    public Result<List<MchAppResult>> list(){
        return Res.ok(mchAppService.list());
    }

    @RequestPath("根据id查询商户应用")
    @Operation(summary = "根据id查询商户应用")
    @GetMapping("/findById")
    public Result<MchAppResult> findById(@NotNull(message = "id不可为空")Long id){
        return Res.ok(mchAppService.findById(id));
    }

    @RequestPath("根据应用AppId获取应用详情")
    @Operation(summary = "根据应用AppId获取应用详情")
    @GetMapping("/findByAppId")
    public Result<MchAppResult> findByAppId(@NotNull(message = "应用AppId不可为空") String appId) {
        return Res.ok(mchAppService.findByAppId(appId));
    }

    @RequestPath("删除商户应用")
    @OperateLog(title = "删除商户应用", businessType = OperateLog.BusinessType.DELETE, saveParam = true)
    @Operation(summary = "删除商户应用")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "id不可为空") Long id){
        mchAppService.delete(id);
        return Res.ok();
    }

    @RequestPath("设置默认商户应用")
    @Operation(summary = "设置默认商户应用")
    @PostMapping("/setDefault")
    @OperateLog(title = "设置默认商户应用 ", businessType = OperateLog.BusinessType.UPDATE, saveParam = true)
    public Result<Void> setDefault(@NotNull(message = "id不可为空") Long id){
        mchAppService.setDefault(id);
        return Res.ok();
    }

    @RequestPath("取消默认商户应用")
    @Operation(summary = "取消默认商户应用")
    @PostMapping("/clearDefault")
    @OperateLog(title = "取消默认商户应用 ", businessType = OperateLog.BusinessType.UPDATE, saveParam = true)
    public Result<Void> clearDefault(@NotNull(message = "id不可为空") Long id){
        mchAppService.clearDefault(id);
        return Res.ok();
    }

}
