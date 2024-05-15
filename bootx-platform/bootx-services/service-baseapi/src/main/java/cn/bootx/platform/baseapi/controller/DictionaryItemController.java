package cn.bootx.platform.baseapi.controller;

import cn.bootx.platform.baseapi.dto.dict.DictionaryItemDto;
import cn.bootx.platform.baseapi.dto.dict.DictionaryItemSimpleDto;
import cn.bootx.platform.common.core.annotation.IgnoreAuth;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.core.util.ValidationUtil;
import cn.bootx.platform.common.core.validation.ValidationGroup;
import cn.bootx.platform.baseapi.core.dict.service.DictionaryItemService;
import cn.bootx.platform.baseapi.param.dict.DictionaryItemParam;
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
    public ResResult<DictionaryItemDto> add(@RequestBody DictionaryItemParam param) {
        ValidationUtil.validateParam(param, ValidationGroup.add.class);
        return Res.ok(dictionaryItemService.add(param));
    }

    @Operation(summary = "修改字典项（返回字典项对象）")
    @PostMapping(value = "/update")
    public ResResult<DictionaryItemDto> update(@RequestBody DictionaryItemParam param) {
        ValidationUtil.validateParam(param, ValidationGroup.edit.class);
        return Res.ok(dictionaryItemService.update(param));
    }

    @Operation(summary = "删除字典项")
    @DeleteMapping(value = "/delete")
    public ResResult<Void> delete(Long id) {
        dictionaryItemService.delete(id);
        return Res.ok();
    }

    @Operation(summary = "根据字典项ID查询")
    @GetMapping("/findById")
    public ResResult<DictionaryItemDto> findById(@Parameter(description = "字典项ID") Long id) {
        return Res.ok(dictionaryItemService.findById(id));
    }

    @Operation(summary = "查询指定字典ID下的所有字典项")
    @GetMapping("/findByDictionaryId")
    public ResResult<List<DictionaryItemDto>> findByDictionaryId(@Parameter(description = "字典ID") Long dictionaryId) {
        return Res.ok(dictionaryItemService.findByDictionaryId(dictionaryId));
    }

    @Operation(summary = "分页查询指定字典下的字典项")
    @GetMapping("/pageByDictionaryId")
    public ResResult<PageResult<DictionaryItemDto>> pageByDictionaryId(PageParam pageParam, Long dictId) {
        return Res.ok(dictionaryItemService.pageByDictionaryId(dictId, pageParam));
    }

    @Operation(summary = "获取全部字典项")
    @GetMapping("/findAll")
    public ResResult<List<DictionaryItemSimpleDto>> findAll() {
        return Res.ok(dictionaryItemService.findAll());
    }

    @IgnoreAuth
    @Operation(summary = "获取启用的字典项列表")
    @GetMapping("/findAllByEnable")
    public ResResult<List<DictionaryItemSimpleDto>> findAllByEnable() {
        return Res.ok(dictionaryItemService.findAllByEnable());
    }

    @Operation(summary = "编码是否被使用")
    @GetMapping("/existsByCode")
    public ResResult<Boolean> existsByCode(String code, Long dictId) {
        return Res.ok(dictionaryItemService.existsByCode(code, dictId));
    }

    @Operation(summary = "编码是否被使用(不包含自己)")
    @GetMapping("/existsByCodeNotId")
    public ResResult<Boolean> existsByCode(String code, Long dictId, Long id) {
        return Res.ok(dictionaryItemService.existsByCode(code, dictId, id));
    }

}
