package cn.daxpay.open.platform.system.controller.dict;

import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.system.param.dict.DictItemParam;
import cn.daxpay.open.platform.system.result.dict.DictItemResult;
import cn.daxpay.open.platform.system.service.dict.DictItemService;
import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/// # 字典项控制器
///
@Validated
@PermCode(menuCode = PermCodes.System.Dict.MENU)
@Tag(name = "字典项")
@RestController
@RequestMapping("/dict/item")
@AllArgsConstructor
public class DictItemController {

    private final DictItemService dictItemService;

    /// 添加字典项
    ///
    /// @param param 字典项参数
    /// @return 操作结果
    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "添加字典项")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody @Validated(ValidationGroup.add.class) DictItemParam param) {
        dictItemService.add(param);
        return Res.ok();
    }

    /// 修改字典项
    ///
    /// @param param 字典项参数
    /// @return 操作结果
    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "修改字典项")
    @PostMapping(value = "/update")
    public Result<Void> update(@RequestBody @Validated(ValidationGroup.edit.class) DictItemParam param) {
        dictItemService.update(param);
        return Res.ok();
    }

    /// 删除字典项
    ///
    /// @param id 字典项ID
    /// @return 操作结果
    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "删除字典项")
    @PostMapping(value = "/delete")
    public Result<Void> delete(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        dictItemService.delete(id);
        return Res.ok();
    }

    /// 根据ID查询字典项
    ///
    /// @param id 字典项ID
    /// @return 字典项信息
    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据字典项ID查询")
    @GetMapping("/get")
    public Result<DictItemResult> findById(@NotNull(message = "{validation.field.dictItemId.notNull}") Long id) {
        return Res.ok(dictItemService.findById(id));
    }

    /// 查询指定字典下的所有字典项
    ///
    /// @param dictId 字典ID
    /// @return 字典项列表
    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "查询指定字典ID下的所有字典项")
    @GetMapping("/get-by-dictionary-id")
    public Result<List<DictItemResult>> findByDictionaryId(@NotNull(message = "{validation.field.dictId.notNull}") Long dictId) {
        return Res.ok(dictItemService.findByDictionaryId(dictId));
    }

    /// 分页查询指定字典下的字典项
    ///
    /// @param pageParam 分页参数
    /// @param dictId 字典ID
    /// @return 字典项分页结果
    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "分页查询指定字典下的字典项")
    @GetMapping("/page-by-dictionary-id")
    public Result<PageResult<DictItemResult>> pageByDictionaryId(PageParam pageParam, @Parameter(description = "字典ID") Long dictId) {
        return Res.ok(dictItemService.pageByDictionaryId(dictId, pageParam));
    }

    /// 获取全部字典项
    ///
    /// @return 所有字典项列表
    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "获取全部字典项")
    @GetMapping("/all")
    public Result<List<DictItemResult>> findAll() {
        return Res.ok(dictItemService.findAll());
    }

    /// 获取启用的字典项列表
    ///
    /// @return 启用的字典项列表
    @IgnoreAuth
    @Operation(summary = "获取启用的字典项列表")
    @GetMapping("/all-by-enable")
    public Result<List<DictItemResult>> findAllByEnable() {
        return Res.ok(dictItemService.findAllByEnable());
    }

    /// 判断字典项编码是否被使用
    ///
    /// @param code 字典项编码
    /// @param dictId 字典ID
    /// @return 是否存在
    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "字典项编码是否被使用")
    @GetMapping("/exists-by-code")
    public Result<Boolean> existsByCode(
            @Parameter(description = "编码") @NotBlank(message = "{validation.field.code.notBlank}") String code,
            @Parameter(description = "字典ID") @NotNull(message = "{validation.field.dictId.notNull}")  Long dictId) {
        return Res.ok(dictItemService.existsByCode(code, dictId));
    }

    /// 判断字典项编码是否被使用(不包含自己)
    ///
    /// @param code 字典项编码
    /// @param dictId 字典ID
    /// @param id 字典项ID
    /// @return 是否存在
    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "字典项编码是否被使用(不包含自己)")
    @GetMapping("/exists-by-code-not-id")
    public Result<Boolean> existsByCode(@Parameter(description = "编码") @NotBlank(message = "{validation.field.code.notBlank}") String code,
                                        @Parameter(description = "字典ID") @NotNull(message = "{validation.field.dictId.notNull}") Long dictId,
                                        @Parameter(description = "字典项ID") @NotNull(message = "{validation.field.dictItemId.notNull}") Long id) {
        return Res.ok(dictItemService.existsByCode(code, dictId, id));
    }

}

