package cn.bootx.platform.baseapi.controller.parameter;

import cn.bootx.platform.baseapi.param.parameter.SystemParameterParam;
import cn.bootx.platform.baseapi.result.parameter.SystemParameterResult;
import cn.bootx.platform.baseapi.service.parameter.SystemParamService;
import cn.bootx.platform.core.annotation.IgnoreAuth;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.core.util.ValidationUtil;
import cn.bootx.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 系统参数
 *
 * @author xxm
 * @since 2021/10/25
 */
@Tag(name = "系统参数")
@RequestGroup(groupCode = "params", groupName = "系统参数", moduleCode = "baseapi" )
@RestController
@RequestMapping("/system/param")
@RequiredArgsConstructor
public class SystemParamController {

    private final SystemParamService systemParamService;

    @RequestPath("添加")
    @Operation(summary = "添加")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody SystemParameterParam param) {
        ValidationUtil.validateParam(param, ValidationGroup.add.class);
        systemParamService.add(param);
        return Res.ok();
    }

    @RequestPath("更新")
    @Operation(summary = "更新")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody SystemParameterParam param) {
        ValidationUtil.validateParam(param, ValidationGroup.edit.class);
        systemParamService.update(param);
        return Res.ok();
    }

    @RequestPath("分页")
    @Operation(summary = "分页")
    @GetMapping("/page")
    public Result<PageResult<SystemParameterResult>> page(PageParam pageParam, SystemParameterParam param) {
        return Res.ok(systemParamService.page(pageParam, param));
    }

    @RequestPath("获取单条")
    @Operation(summary = "获取单条")
    @GetMapping("/findById")
    public Result<SystemParameterResult> findById(@Parameter(description = "主键") Long id) {
        return Res.ok(systemParamService.findById(id));
    }

    @RequestPath("删除")
    @Operation(summary = "删除")
    @PostMapping("/delete")
    public Result<Void> delete(Long id) {
        systemParamService.delete(id);
        return Res.ok();
    }

    @RequestPath("判断编码是否存在")
    @Operation(summary = "判断编码是否存在")
    @GetMapping("/existsByKey")
    public Result<Boolean> existsByKey(String key) {
        return Res.ok(systemParamService.existsByKey(key));
    }

    @RequestPath("判断编码是否存在(不包含自己)")
    @Operation(summary = "判断编码是否存在(不包含自己)")
    @GetMapping("/existsByKeyNotId")
    public Result<Boolean> existsByKeyNotId(String key, Long id) {
        return Res.ok(systemParamService.existsByKey(key, id));
    }

    @IgnoreAuth
    @Operation(summary = "根据键名获取键值")
    @GetMapping("/findByKey")
    public Result<String> findByKey(String key) {
        return Res.ok(systemParamService.findByKey(key));
    }

}
