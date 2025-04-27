package org.dromara.daxpay.controller.allocation;

import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.core.validation.ValidationGroup;
import org.dromara.daxpay.service.param.allocation.AllocConfigParam;
import org.dromara.daxpay.service.result.allocation.AllocConfigResult;
import org.dromara.daxpay.service.service.allocation.AllocConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 分账配置
 * @author xxm
 * @since 2024/12/9
 */
@Validated
@Tag(name = "分账配置")
@RestController
@RequestGroup(groupCode = "AllocConfig", groupName = "分账配置", moduleCode = "Alloc", moduleName = "(DaxPay)分账管理" )
@RequestMapping("/allocation/config")
@RequiredArgsConstructor
public class AllocConfigController {
    private final AllocConfigService allocConfigService;

    @RequestPath("保存")
    @Operation(summary = "修改")
    @PostMapping("/save")
    public Result<Void> add(@Validated(ValidationGroup.add.class) @RequestBody AllocConfigParam param) {
        allocConfigService.save(param);
        return Res.ok();
    }

    @RequestPath("修改")
    @Operation(summary = "修改")
    @PostMapping("/update")
    public Result<Void> update(@Validated(ValidationGroup.edit.class) @RequestBody AllocConfigParam param) {
        allocConfigService.update(param);
        return Res.ok();
    }

    @RequestPath("根据应用ID查询详情")
    @Operation(summary = "根据应用ID查询详情")
    @GetMapping("/findByAppId")
    public Result<AllocConfigResult> findByAppId(@NotBlank(message = "应用ID不能为空") String appId) {
        return Res.ok(allocConfigService.findByAppId(appId));
    }
}
