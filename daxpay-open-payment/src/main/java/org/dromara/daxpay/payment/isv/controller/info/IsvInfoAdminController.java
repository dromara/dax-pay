package org.dromara.daxpay.payment.isv.controller.info;

import cn.bootx.platform.core.annotation.ClientCode;
import cn.bootx.platform.core.annotation.OperateLog;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.core.validation.ValidationGroup;
import org.dromara.daxpay.payment.isv.param.isv.IsvInfoParam;
import org.dromara.daxpay.payment.isv.param.isv.IsvInfoQuery;
import org.dromara.daxpay.payment.isv.result.info.IsvInfoResult;
import org.dromara.daxpay.payment.isv.service.info.IsvInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.dromara.daxpay.payment.common.code.DaxPayCode.Client;

/**
 * 服务商管理控制器
 * @author xxm
 * @since 2024/10/29
 */
@Validated
@ClientCode({Client.ADMIN})
@Tag(name = "服务商管理")
@RestController
@RequestMapping("/admin/isv/info")
@RequestGroup(groupCode = "IsvInfoAdmin", groupName = "服务商管理", moduleCode = "isv", moduleName = "(DaxPay)服务商管理")
@RequiredArgsConstructor
public class IsvInfoAdminController {
    private final IsvInfoService isvInfoService;

    @RequestPath("新增服务商")
    @Operation(summary = "新增服务商")
    @PostMapping("/add")
    @OperateLog(title = "新增服务商 ", businessType = OperateLog.BusinessType.ADD, saveParam = true)
    public Result<Void> add(@RequestBody @Validated(ValidationGroup.add.class) IsvInfoParam param){
        isvInfoService.add(param);
        return Res.ok();
    }

    @RequestPath("修改服务商")
    @Operation(summary = "修改服务商")
    @PostMapping("/update")
    @OperateLog(title = "修改服务商 ", businessType = OperateLog.BusinessType.UPDATE, saveParam = true)
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) IsvInfoParam param){
        isvInfoService.update(param);
        return Res.ok();
    }

    @RequestPath("服务商分页")
    @Operation(summary = "服务商分页")
    @GetMapping("/page")
    public Result<PageResult<IsvInfoResult>> page(PageParam pageParam, IsvInfoQuery query){
        return Res.ok(isvInfoService.page(pageParam, query));
    }
    @RequestPath("根据id查询服务商")
    @Operation(summary = "根据id查询服务商")
    @GetMapping("/findById")
    public Result<IsvInfoResult> findById(@NotNull(message = "id不可为空")Long id){
        return Res.ok(isvInfoService.findById(id));
    }

    @RequestPath("删除服务商")
    @Operation(summary = "删除服务商")
    @OperateLog(title = "删除服务商 ", businessType = OperateLog.BusinessType.DELETE, saveParam = true)
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "id不可为空") Long id){
        isvInfoService.delete(id);
        return Res.ok();
    }

}
