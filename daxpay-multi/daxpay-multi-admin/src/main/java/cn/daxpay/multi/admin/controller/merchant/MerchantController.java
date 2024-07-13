package cn.daxpay.multi.admin.controller.merchant;

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
@RequiredArgsConstructor
public class MerchantController {
    private final MerchantService merchantService;

    @Operation(summary = "新增")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody @Validated(ValidationGroup.add.class) MerchantParam param){
        merchantService.add(param);
        return Res.ok();
    }

    @Operation(summary = "修改")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) MerchantParam param){
        merchantService.update(param);
        return Res.ok();
    }

    @Operation(summary = "分页")
    @GetMapping("/page")
    public Result<PageResult<MerchantResult>> page(PageParam pageParam, MerchantQuery param){
        return Res.ok(merchantService.page(pageParam, param));
    }

    @Operation(summary = "根据id查询")
    @GetMapping("/findById")
    public Result<MerchantResult> findById(@NotNull(message = "id不可为空")Long id){
        return Res.ok(merchantService.findById(id));
    }

    @Operation(summary = "删除")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "id不可为空") Long id){
        merchantService.delete(id);
        return Res.ok();
    }

}
