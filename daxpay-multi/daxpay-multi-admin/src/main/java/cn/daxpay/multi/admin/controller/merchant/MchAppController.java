package cn.daxpay.multi.admin.controller.merchant;

import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.core.util.ValidationUtil;
import cn.bootx.platform.core.validation.ValidationGroup;
import cn.daxpay.multi.service.param.merchant.MchAppParam;
import cn.daxpay.multi.service.param.merchant.MchAppQuery;
import cn.daxpay.multi.service.result.merchant.MchAppResult;
import cn.daxpay.multi.service.service.merchant.MchAppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 商户应用控制器
 * @author xxm
 * @since 2024/6/25
 */
@Tag(name = "商户应用控制器")
@RestController
@RequestMapping("/mch/app")
@RequiredArgsConstructor
public class MchAppController {
    private final MchAppService mchAppService;


    @Operation(summary = "新增")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody MchAppParam param){
        ValidationUtil.validateParam(param, ValidationGroup.add.class);
        mchAppService.add(param);
        return Res.ok();
    }

    @Operation(summary = "修改")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody MchAppParam param){
        ValidationUtil.validateParam(param, ValidationGroup.edit.class);
        mchAppService.update(param);
        return Res.ok();
    }

    @Operation(summary = "分页")
    @GetMapping("/page")
    public Result<PageResult<MchAppResult>> page(PageParam pageParam, MchAppQuery param){
        return Res.ok(mchAppService.page(pageParam, param));
    }

    @Operation(summary = "根据id查询")
    @GetMapping("/findById")
    public Result<MchAppResult> findById(@NotNull(message = "id不可为空")Long id){
        return Res.ok(mchAppService.findById(id));
    }

    @Operation(summary = "删除")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "id不可为空") Long id){
        mchAppService.delete(id);
        return Res.ok();
    }
}
