package org.dromara.daxpay.service.controller.merchant;

import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.dto.LabelValue;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.core.util.ValidationUtil;
import cn.bootx.platform.core.validation.ValidationGroup;
import org.dromara.daxpay.service.param.merchant.MchAppParam;
import org.dromara.daxpay.service.param.merchant.MchAppQuery;
import org.dromara.daxpay.service.result.merchant.MchAppResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.dromara.daxpay.service.service.merchant.MchAppService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商户应用控制器
 * @author xxm
 * @since 2024/6/25
 */
@Validated
@Tag(name = "商户应用控制器")
@RestController
@RequestMapping("/mch/app")
@RequestGroup(groupCode = "merchant", groupName = "商户配置", moduleCode = "PayConfig")
@RequiredArgsConstructor
public class MchAppController {
    private final MchAppService mchAppService;

    @RequestPath("新增商户应用")
    @Operation(summary = "新增商户应用")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody MchAppParam param){
        ValidationUtil.validateParam(param, ValidationGroup.add.class);
        mchAppService.add(param);
        return Res.ok();
    }

    @RequestPath("修改商户应用")
    @Operation(summary = "修改商户应用")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody MchAppParam param){
        ValidationUtil.validateParam(param, ValidationGroup.edit.class);
        mchAppService.update(param);
        return Res.ok();
    }

    @RequestPath("商户应用分页")
    @Operation(summary = "商户应用分页")
    @GetMapping("/page")
    public Result<PageResult<MchAppResult>> page(PageParam pageParam, MchAppQuery param){
        return Res.ok(mchAppService.page(pageParam, param));
    }

    @RequestPath("根据id查询商户应用")
    @Operation(summary = "根据id查询商户应用")
    @GetMapping("/findById")
    public Result<MchAppResult> findById(@NotNull(message = "id不可为空")Long id){
        return Res.ok(mchAppService.findById(id));
    }

    @RequestPath("删除商户应用")
    @Operation(summary = "删除商户应用")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "id不可为空") Long id){
        mchAppService.delete(id);
        return Res.ok();
    }

    @RequestPath("根据商户号查询商户应用下拉列表")
    @Operation(summary = "根据商户号查询商户应用下拉列表")
    @GetMapping("/dropdown")
    public Result<List<LabelValue>> dropdown(){
        return Res.ok(mchAppService.dropdown());
    }
}
