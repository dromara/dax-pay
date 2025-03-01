package cn.bootx.platform.baseapi.controller.dict;

import cn.bootx.platform.baseapi.param.dict.DictionaryItemParam;
import cn.bootx.platform.baseapi.result.dict.DictionaryItemResult;
import cn.bootx.platform.baseapi.service.dict.DictionaryItemService;
import cn.bootx.platform.core.annotation.IgnoreAuth;
import cn.bootx.platform.core.annotation.OperateLog;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author xxm
 * @since 2020/4/18 19:03
 */
@Validated
@Tag(name = "字典项")
@RequestGroup(groupCode = "dict", groupName = "字典管理", moduleCode = "baseapi", moduleName = "(Bootx)基础模块API" )
@RestController
@RequestMapping("/dict/item")
@AllArgsConstructor
public class DictionaryItemController {

    private final DictionaryItemService dictionaryItemService;

    @RequestPath("添加字典项")
    @Operation(summary = "添加字典项")
    @PostMapping("/add")
    @OperateLog(title = "添加字典项", businessType = OperateLog.BusinessType.ADD, saveParam = true)
    public Result<Void> add(@RequestBody @Validated(ValidationGroup.add.class) DictionaryItemParam param) {
        dictionaryItemService.add(param);
        return Res.ok();
    }

    @RequestPath("修改字典项")
    @Operation(summary = "修改字典项")
    @PostMapping(value = "/update")
    @OperateLog(title = "修改字典项", businessType = OperateLog.BusinessType.UPDATE, saveParam = true)
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) DictionaryItemParam param) {
        dictionaryItemService.update(param);
        return Res.ok();
    }

    @RequestPath("删除字典项")
    @Operation(summary = "删除字典项")
    @PostMapping(value = "/delete")
    @OperateLog(title = "删除字典项", businessType = OperateLog.BusinessType.DELETE, saveParam = true)
    public Result<Void> delete(@NotNull(message = "主键不可为空") Long id) {
        dictionaryItemService.delete(id);
        return Res.ok();
    }

    @RequestPath("查询字典项")
    @Operation(summary = "根据字典项ID查询")
    @GetMapping("/findById")
    public Result<DictionaryItemResult> findById(@NotNull(message = "字典项主键不可为空") Long id) {
        return Res.ok(dictionaryItemService.findById(id));
    }

    @RequestPath("查询字典项列表")
    @Operation(summary = "查询指定字典ID下的所有字典项")
    @GetMapping("/findByDictionaryId")
    public Result<List<DictionaryItemResult>> findByDictionaryId(@NotNull(message = "字典主键不可为空") Long dictId) {
        return Res.ok(dictionaryItemService.findByDictionaryId(dictId));
    }

    @RequestPath("字典项分页")
    @Operation(summary = "分页查询指定字典下的字典项")
    @GetMapping("/pageByDictionaryId")
    public Result<PageResult<DictionaryItemResult>> pageByDictionaryId(PageParam pageParam, @Parameter(description = "字典ID") Long dictId) {
        return Res.ok(dictionaryItemService.pageByDictionaryId(dictId, pageParam));
    }

    @RequestPath("获取全部字典项")
    @Operation(summary = "获取全部字典项")
    @GetMapping("/findAll")
    public Result<List<DictionaryItemResult>> findAll() {
        return Res.ok(dictionaryItemService.findAll());
    }

    @IgnoreAuth
    @Operation(summary = "获取启用的字典项列表")
    @GetMapping("/findAllByEnable")
    public Result<List<DictionaryItemResult>> findAllByEnable() {
        return Res.ok(dictionaryItemService.findAllByEnable());
    }

    @RequestPath("字典项编码是否被使用")
    @Operation(summary = "字典项编码是否被使用")
    @GetMapping("/existsByCode")
    public Result<Boolean> existsByCode(
            @Parameter(description = "编码") @NotBlank(message = "字典编码不可为空") String code,
            @Parameter(description = "字典ID") @NotNull(message = "字典主键不可为空")  Long dictId) {
        return Res.ok(dictionaryItemService.existsByCode(code, dictId));
    }

    @RequestPath("字典项编码是否被使用(不包含自己)")
    @Operation(summary = "字典项编码是否被使用(不包含自己)")
    @GetMapping("/existsByCodeNotId")
    public Result<Boolean> existsByCode(@Parameter(description = "编码") @NotBlank(message = "字典编码不可为空") String code,
                                        @Parameter(description = "字典ID") @NotNull(message = "字典主键不可为空") Long dictId,
                                        @Parameter(description = "字典项ID") @NotNull(message = "字典项主键不可为空") Long id) {
        return Res.ok(dictionaryItemService.existsByCode(code, dictId, id));
    }

}
