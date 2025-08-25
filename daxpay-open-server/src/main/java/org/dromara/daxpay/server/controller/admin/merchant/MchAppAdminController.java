package org.dromara.daxpay.server.controller.admin.merchant;

import cn.bootx.platform.core.annotation.ClientCode;
import cn.bootx.platform.core.annotation.OperateLog;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.core.util.ValidationUtil;
import cn.bootx.platform.core.validation.ValidationGroup;
import org.dromara.daxpay.service.common.code.DaxPayCode;
import org.dromara.daxpay.service.merchant.param.app.MchAppParam;
import org.dromara.daxpay.service.merchant.param.app.MchAppQuery;
import org.dromara.daxpay.service.merchant.result.app.MchAppResult;
import org.dromara.daxpay.service.merchant.service.app.MchAppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.dromara.core.trans.anno.TransMethodResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 商户应用配置(管理)
 * @author xxm
 * @since 2024/6/25
 */
@Validated
@ClientCode({DaxPayCode.Client.ADMIN})
@Tag(name = "商户应用配置(管理)")
@RestController
@RequestMapping("/admin/mch/app")
@RequestGroup(groupCode = "mchAppAdmin", groupName = "商户应用配置(管理)", moduleCode = "merchant")
@RequiredArgsConstructor
public class MchAppAdminController {
    private final MchAppService mchAppService;

    @RequestPath("新增商户应用")
    @Operation(summary = "新增商户应用")
    @PostMapping("/add")
    @OperateLog(title = "新增商户应用 ", businessType = OperateLog.BusinessType.ADD, saveParam = true)
    public Result<Void> add(@RequestBody @Validated(ValidationGroup.add.class) MchAppParam param){
        ValidationUtil.validateParam(param, ValidationGroup.add.class);
        mchAppService.add(param);
        return Res.ok();
    }

    @RequestPath("修改商户应用")
    @Operation(summary = "修改商户应用")
    @PostMapping("/update")
    @OperateLog(title = "修改商户应用 ", businessType = OperateLog.BusinessType.UPDATE, saveParam = true)
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) MchAppParam param){
        ValidationUtil.validateParam(param, ValidationGroup.edit.class);
        mchAppService.update(param);
        return Res.ok();
    }

    @TransMethodResult
    @RequestPath("商户应用分页")
    @Operation(summary = "商户应用分页")
    @GetMapping("/page")
    public Result<PageResult<MchAppResult>> page(PageParam pageParam, MchAppQuery query){
        return Res.ok(mchAppService.page(pageParam, query));
    }

    @TransMethodResult
    @RequestPath("根据id查询商户应用")
    @Operation(summary = "根据id查询商户应用")
    @GetMapping("/findById")
    public Result<MchAppResult> findById(@NotNull(message = "id不可为空")Long id){
        return Res.ok(mchAppService.findById(id));
    }

    @RequestPath("删除商户应用")
    @Operation(summary = "删除商户应用")
    @PostMapping("/delete")
    @OperateLog(title = "删除商户应用 ", businessType = OperateLog.BusinessType.DELETE, saveParam = true)
    public Result<Void> delete(@NotNull(message = "id不可为空") Long id){
        mchAppService.delete(id);
        return Res.ok();
    }
}
