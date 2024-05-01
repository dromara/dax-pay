package cn.bootx.platform.baseapi.controller.dict;

import cn.bootx.platform.baseapi.param.dict.DictionaryParam;
import cn.bootx.platform.baseapi.result.dict.DictionaryResult;
import cn.bootx.platform.baseapi.service.dict.DictionaryService;
import cn.bootx.platform.common.mybatisplus.validation.ValidationGroup;
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
@RequestMapping("/dict")
@RequiredArgsConstructor
public class DictionaryController {

    private final DictionaryService dictionaryService;

    @Operation(summary = "添加")
    @PostMapping("/add")
    public Result<DictionaryResult> add(@RequestBody DictionaryParam param) {
        ValidationUtil.validateParam(param, ValidationGroup.add.class);
        return Res.ok(dictionaryService.add(param));
    }

    @Operation(summary = "根据id删除")
    @DeleteMapping("/delete")
    public Result<Boolean> delete(Long id) {
        dictionaryService.delete(id);
        return Res.ok();
    }

    @Operation(summary = "更新")
    @PostMapping("/update")
    public Result<DictionaryResult> update(@RequestBody DictionaryParam param) {
        ValidationUtil.validateParam(param, ValidationGroup.edit.class);
        return Res.ok(dictionaryService.update(param));
    }

    @Operation(summary = "根据id获取")
    @GetMapping("/findById")
    public Result<DictionaryResult> findById(Long id) {
        return Res.ok(dictionaryService.findById(id));
    }

    @Operation(summary = "查询全部")
    @GetMapping("/findAll")
    public Result<List<DictionaryResult>> findAll() {
        return Res.ok(dictionaryService.findAll());
    }

    @Operation(summary = "分页")
    @GetMapping("/page")
    public Result<PageResult<DictionaryResult>> page(@ParameterObject PageParam pageParam, DictionaryParam param) {
        return Res.ok(dictionaryService.page(pageParam, param));
    }

    @Operation(summary = "编码是否被使用")
    @GetMapping("/existsByCode")
    public Result<Boolean> existsByCode(String code) {
        return Res.ok(dictionaryService.existsByCode(code));
    }

    @Operation(summary = "编码是否被使用(不包含自己)")
    @GetMapping("/existsByCodeNotId")
    public Result<Boolean> existsByCode(String code, Long id) {
        return Res.ok(dictionaryService.existsByCode(code, id));
    }

}
