package cn.bootx.platform.baseapi.controller.dict;

import cn.bootx.platform.baseapi.param.dict.DictionaryItemParam;
import cn.bootx.platform.baseapi.result.dict.DictionaryItemResult;
import cn.bootx.platform.baseapi.service.dict.DictionaryItemService;
import cn.bootx.platform.common.mybatisplus.validation.ValidationGroup;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.core.util.ValidationUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author xxm
 * @since 2020/4/18 19:03
 */
@Tag(name = "字典项")
@RestController
@RequestMapping("/dict/item")
@AllArgsConstructor
public class DictionaryItemController {

    private final DictionaryItemService dictionaryItemService;

    @Operation(summary = "添加字典项（返回字典项对象）")
    @PostMapping("/add")
    public Result<DictionaryItemResult> add(@RequestBody DictionaryItemParam param) {
        ValidationUtil.validateParam(param, ValidationGroup.add.class);
        return Res.ok(dictionaryItemService.add(param));
    }

    @Operation(summary = "修改字典项（返回字典项对象）")
    @PostMapping(value = "/update")
    public Result<DictionaryItemResult> update(@RequestBody DictionaryItemParam param) {
        ValidationUtil.validateParam(param, ValidationGroup.edit.class);
        return Res.ok(dictionaryItemService.update(param));
    }

    @Operation(summary = "删除字典项")
    @DeleteMapping(value = "/delete")
    public Result<Void> delete(Long id) {
        dictionaryItemService.delete(id);
        return Res.ok();
    }

    @Operation(summary = "根据字典项ID查询")
    @GetMapping("/findById")
    public Result<DictionaryItemResult> findById(@Parameter(description = "字典项ID") Long id) {
        return Res.ok(dictionaryItemService.findById(id));
    }

    @Operation(summary = "查询指定字典ID下的所有字典项")
    @GetMapping("/findByDictionaryId")
    public Result<List<DictionaryItemResult>> findByDictionaryId(@Parameter(description = "字典ID") Long dictId) {
        return Res.ok(dictionaryItemService.findByDictionaryId(dictId));
    }

    @Operation(summary = "分页查询指定字典下的字典项")
    @GetMapping("/pageByDictionaryId")
    public Result<PageResult<DictionaryItemResult>> pageByDictionaryId(PageParam pageParam, @Parameter(description = "字典ID") Long dictId) {
        return Res.ok(dictionaryItemService.pageByDictionaryId(dictId, pageParam));
    }

    @Operation(summary = "获取全部字典项")
    @GetMapping("/findAll")
    public Result<List<DictionaryItemResult>> findAll() {
        return Res.ok(dictionaryItemService.findAll());
    }

    @Operation(summary = "获取启用的字典项列表")
    @GetMapping("/findAllByEnable")
    public Result<List<DictionaryItemResult>> findAllByEnable() {
        return Res.ok(dictionaryItemService.findAllByEnable());
    }

    @Operation(summary = "编码是否被使用")
    @GetMapping("/existsByCode")
    public Result<Boolean> existsByCode(@Parameter(description = "编码") String code,@Parameter(description = "字典ID") Long dictId) {
        return Res.ok(dictionaryItemService.existsByCode(code, dictId));
    }

    @Operation(summary = "编码是否被使用(不包含自己)")
    @GetMapping("/existsByCodeNotId")
    public Result<Boolean> existsByCode(@Parameter(description = "编码") String code,
                                        @Parameter(description = "字典ID") Long dictId,
                                        @Parameter(description = "字典项ID") Long id) {
        return Res.ok(dictionaryItemService.existsByCode(code, dictId, id));
    }

}
