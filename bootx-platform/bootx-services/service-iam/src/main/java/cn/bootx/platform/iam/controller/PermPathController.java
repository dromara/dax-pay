package cn.bootx.platform.iam.controller;

import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.core.util.ValidationUtil;
import cn.bootx.platform.iam.core.permission.service.PermPathService;
import cn.bootx.platform.iam.dto.permission.PermPathDto;
import cn.bootx.platform.iam.param.permission.PermPathBatchEnableParam;
import cn.bootx.platform.iam.param.permission.PermPathParam;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author xxm
 * @since 2020/5/11 9:36
 */
@Validated
@Tag(name = "请求权限管理")
@RestController
@RequestMapping("/perm/path")
@RequiredArgsConstructor
public class PermPathController {

    private final PermPathService pathService;

    @Operation(summary = "添加权限")
    @PostMapping("/add")
    public ResResult<PermPathDto> add(@RequestBody PermPathParam param) {
        pathService.add(param);
        return Res.ok();
    }

    @Operation(summary = "更新权限")
    @PostMapping("/update")
    public ResResult<PermPathDto> update(@RequestBody PermPathParam param) {
        pathService.update(param);
        return Res.ok();
    }

    @Operation(summary = "批量更新状态")
    @PostMapping("/batchUpdateEnable")
    public ResResult<PermPathDto> batchUpdateEnable(@RequestBody PermPathBatchEnableParam param) {
        ValidationUtil.validateParam(param);
        pathService.batchUpdateEnable(param);
        return Res.ok();
    }

    @Operation(summary = "删除权限")
    @DeleteMapping("/delete")
    public ResResult<Void> delete(@NotNull(message = "id不可为空") Long id) {
        pathService.delete(id);
        return Res.ok();
    }

    @Operation(summary = "批量删除")
    @DeleteMapping("/deleteBatch")
    public ResResult<Void> deleteBatch(@RequestBody @NotEmpty(message = "id集合不可为空") List<Long> ids) {
        pathService.deleteBatch(ids);
        return Res.ok();
    }

    @Operation(summary = "获取详情")
    @GetMapping("/findById")
    public ResResult<PermPathDto> findById(Long id) {
        return Res.ok(pathService.findById(id));
    }

    @Operation(summary = "权限分页")
    @GetMapping("/page")
    public ResResult<PageResult<PermPathDto>> page(PageParam pageParam, PermPathParam param) {
        return Res.ok(pathService.page(pageParam, param));
    }

    @Operation(summary = "权限列表")
    @GetMapping("/findAll")
    public ResResult<List<PermPathDto>> findAll() {
        return Res.ok(pathService.findAll());
    }

    @Operation(summary = "同步系统请求资源")
    @PostMapping("/syncSystem")
    public ResResult<Void> syncSystem() {
        pathService.syncSystem();
        return Res.ok();
    }

}
