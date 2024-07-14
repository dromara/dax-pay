package cn.daxpay.multi.admin.controller.merchant;

import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.core.validation.ValidationGroup;
import cn.daxpay.multi.service.param.merchant.MerchantParam;
import cn.daxpay.multi.service.param.merchant.MerchantQuery;
import cn.daxpay.multi.service.result.merchant.MerchantResult;
import cn.daxpay.multi.service.service.merchant.MerchantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 商户控制器
 * @author xxm
 * @since 2024/6/25
 */
@Validated
@Tag(name = "商户控制器")
@RestController
@RequestMapping("/merchant")
@RequestGroup(groupCode = "merchant", moduleCode = "PayConfig")
@RequiredArgsConstructor
public class MerchantController {
    private final MerchantService merchantService;

    @RequestPath("新增商户")
    @Operation(summary = "新增商户")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody @Validated(ValidationGroup.add.class) MerchantParam param){
        merchantService.add(param);
        return Res.ok();
    }

    @RequestPath("修改商户")
    @Operation(summary = "修改商户")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) MerchantParam param){
        merchantService.update(param);
        return Res.ok();
    }

    @RequestPath("商户分页")
    @Operation(summary = "商户分页")
    @GetMapping("/page")
    public Result<PageResult<MerchantResult>> page(PageParam pageParam, MerchantQuery param){
        return Res.ok(merchantService.page(pageParam, param));
    }

    @RequestPath("根据id查询商户")
    @Operation(summary = "根据id查询商户")
    @GetMapping("/findById")
    public Result<MerchantResult> findById(@NotNull(message = "id不可为空")Long id){
        return Res.ok(merchantService.findById(id));
    }

    @RequestPath("删除商户")
    @Operation(summary = "删除商户")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "id不可为空") Long id){
        merchantService.delete(id);
        return Res.ok();
    }

}
