package org.dromara.daxpay.server.controller.admin.merchant;

import cn.bootx.platform.core.annotation.ClientCode;
import cn.bootx.platform.core.annotation.OperateLog;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.core.validation.ValidationGroup;
import org.dromara.daxpay.server.service.admin.MerchantAdminService;
import org.dromara.daxpay.service.common.code.DaxPayCode;
import org.dromara.daxpay.service.merchant.param.info.MerchantCreateParam;
import org.dromara.daxpay.service.merchant.param.info.MerchantParam;
import org.dromara.daxpay.service.merchant.param.info.MerchantQuery;
import org.dromara.daxpay.service.merchant.result.info.MerchantResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.dromara.core.trans.anno.TransMethodResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 商户配置(管理)
 * @author xxm
 * @since 2024/6/25
 */
@Validated
@ClientCode({DaxPayCode.Client.ADMIN})
@Tag(name = "商户配置(管理)")
@RestController
@RequestMapping("/admin/merchant")
@RequestGroup(groupCode = "merchantAdmin", groupName = "商户配置(管理)", moduleCode = "merchant", moduleName = "(DaxPay)商户管理")
@RequiredArgsConstructor
public class MerchantAdminController {
    private final MerchantAdminService merchantService;

    @RequestPath("新增商户")
    @Operation(summary = "新增商户")
    @PostMapping("/add")
    @OperateLog(title = "新增商户 ", businessType = OperateLog.BusinessType.ADD, saveParam = true)
    public Result<Void> add(@RequestBody @Validated(ValidationGroup.add.class) MerchantCreateParam param){
        merchantService.add(param);
        return Res.ok();
    }

    @RequestPath("修改商户")
    @Operation(summary = "修改商户")
    @PostMapping("/update")
    @OperateLog(title = "修改商户 ", businessType = OperateLog.BusinessType.UPDATE, saveParam = true)
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) MerchantParam param){
        merchantService.update(param);
        return Res.ok();
    }

    @TransMethodResult
    @RequestPath("商户分页")
    @Operation(summary = "商户分页")
    @GetMapping("/page")
    public Result<PageResult<MerchantResult>> page(PageParam pageParam, MerchantQuery param){
        return Res.ok(merchantService.page(pageParam, param));
    }

    @TransMethodResult
    @RequestPath("根据id查询商户")
    @Operation(summary = "根据id查询商户")
    @GetMapping("/findById")
    public Result<MerchantResult> findById(@NotNull(message = "id不可为空")Long id){
        return Res.ok(merchantService.findById(id));
    }

    @RequestPath("删除商户")
    @Operation(summary = "删除商户")
    @PostMapping("/delete")
    @OperateLog(title = "删除商户 ", businessType = OperateLog.BusinessType.DELETE, saveParam = true)
    public Result<Void> delete(@NotNull(message = "id不可为空") Long id){
        merchantService.delete(id);
        return Res.ok();
    }

}
