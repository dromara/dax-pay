package cn.bootx.platform.baseapi.controller.dict;

import cn.bootx.platform.baseapi.param.dict.DictionaryParam;
import cn.bootx.platform.baseapi.result.dict.DictionaryResult;
import cn.bootx.platform.baseapi.service.dict.DictionaryService;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.validation.ValidationGroup;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.core.util.ValidationUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 字典
 *
 * @author xxm
 * @since 2021/8/4
 */
@Tag(name = "字典")
@RestController
@RequestGroup(groupCode = "dict", groupName = "字典管理", moduleCode = "baseapi", moduleName = "基础API" )
@RequestMapping("/dict")
@RequiredArgsConstructor
public class DictionaryController {

    private final DictionaryService dictionaryService;

    @RequestPath("添加字典")
    @Operation(summary = "添加字典")
    @PostMapping("/add")
    public Result<DictionaryResult> add(@RequestBody DictionaryParam param) {
        ValidationUtil.validateParam(param, ValidationGroup.add.class);
        return Res.ok(dictionaryService.add(param));
    }

    @RequestPath("根据主键删除字典")
    @Operation(summary = "根据主键删除")
    @PostMapping("/delete")
    public Result<Boolean> delete(Long id) {
        dictionaryService.delete(id);
        return Res.ok();
    }

    @RequestPath("更新字典")
    @Operation(summary = "更新字典")
    @PostMapping("/update")
    public Result<DictionaryResult> update(@RequestBody DictionaryParam param) {
        ValidationUtil.validateParam(param, ValidationGroup.edit.class);
        return Res.ok(dictionaryService.update(param));
    }

    @RequestPath("根据主键获取字典")
    @Operation(summary = "根据主键获取字典")
    @GetMapping("/findById")
    public Result<DictionaryResult> findById(Long id) {
        return Res.ok(dictionaryService.findById(id));
    }

    @RequestPath("查询全部字典")
    @Operation(summary = "查询全部字典")
    @GetMapping("/findAll")
    public Result<List<DictionaryResult>> findAll() {
        return Res.ok(dictionaryService.findAll());
    }

    @RequestPath("字典分页")
    @Operation(summary = "字典分页")
    @GetMapping("/page")
    public Result<PageResult<DictionaryResult>> page(@ParameterObject PageParam pageParam, DictionaryParam param) {
        return Res.ok(dictionaryService.page(pageParam, param));
    }

    @RequestPath("字典编码是否被使用")
    @Operation(summary = "字典编码是否被使用")
    @GetMapping("/existsByCode")
    public Result<Boolean> existsByCode(String code) {
        return Res.ok(dictionaryService.existsByCode(code));
    }

    @RequestPath("编码是否被使用(不包含自己)")
    @Operation(summary = "编码是否被使用(不包含自己)")
    @GetMapping("/existsByCodeNotId")
    public Result<Boolean> existsByCode(String code, Long id) {
        return Res.ok(dictionaryService.existsByCode(code, id));
    }

}
