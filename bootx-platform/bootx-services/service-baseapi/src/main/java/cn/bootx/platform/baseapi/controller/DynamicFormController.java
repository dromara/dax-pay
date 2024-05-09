package cn.bootx.platform.baseapi.controller;

import cn.bootx.platform.baseapi.dto.dynamicform.DynamicFormDto;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.baseapi.core.dynamicform.service.DynamicFormService;
import cn.bootx.platform.baseapi.param.dynamicform.DynamicFormParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 动态表单
 *
 * @author xxm
 * @since 2022-07-28
 */
@Tag(name = "动态表单")
@RestController
@RequestMapping("/dynamic/form")
@RequiredArgsConstructor
public class DynamicFormController {

    private final DynamicFormService dynamicFormService;

    @Operation(summary = "添加")
    @PostMapping(value = "/add")
    public ResResult<Void> add(@RequestBody DynamicFormParam param) {
        dynamicFormService.add(param);
        return Res.ok();
    }

    @Operation(summary = "修改")
    @PostMapping(value = "/update")
    public ResResult<Void> update(@RequestBody DynamicFormParam param) {
        dynamicFormService.update(param);
        return Res.ok();
    }

    @Operation(summary = "删除")
    @DeleteMapping(value = "/delete")
    public ResResult<Void> delete(Long id) {
        dynamicFormService.delete(id);
        return Res.ok();
    }

    @Operation(summary = "通过ID查询")
    @GetMapping(value = "/findById")
    public ResResult<DynamicFormDto> findById(Long id) {
        return Res.ok(dynamicFormService.findById(id));
    }

    @Operation(summary = "查询所有")
    @GetMapping(value = "/findAll")
    public ResResult<List<DynamicFormDto>> findAll() {
        return Res.ok(dynamicFormService.findAll());
    }

    @Operation(summary = "分页查询")
    @GetMapping(value = "/page")
    public ResResult<PageResult<DynamicFormDto>> page(PageParam pageParam, DynamicFormParam dynamicFormParam) {
        return Res.ok(dynamicFormService.page(pageParam, dynamicFormParam));
    }

    @Operation(summary = "编码是否被使用")
    @GetMapping("/existsByCode")
    public ResResult<Boolean> existsByCode(String code) {
        return Res.ok(dynamicFormService.existsByCode(code));
    }

    @Operation(summary = "编码是否被使用(不包含自己)")
    @GetMapping("/existsByCodeNotId")
    public ResResult<Boolean> existsByCode(String code, Long id) {
        return Res.ok(dynamicFormService.existsByCode(code, id));
    }

}
