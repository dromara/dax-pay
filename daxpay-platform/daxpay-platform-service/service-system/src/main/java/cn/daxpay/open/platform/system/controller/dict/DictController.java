package cn.daxpay.open.platform.system.controller.dict;


import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.system.param.dict.DictParam;
import cn.daxpay.open.platform.system.result.dict.DictResult;
import cn.daxpay.open.platform.system.service.dict.DictService;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.core.util.ValidationUtil;
import cn.daxpay.open.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/// # 字典控制器
///
@PermCode(menuCode = PermCodes.System.Dict.MENU)
@Validated
@Tag(name = "字典")
@Tag(name = "字典")
@RestController
@RequestMapping("/dict")
@RequiredArgsConstructor
public class DictController {

    private final DictService dictService;

    /// 添加字典
    ///
    /// @param param 字典参数
    @PermCode(code = PermCodes.Action.MANAGE, nameCn = PermCodes.System.Dict.MANAGE_NAME_CN, nameEn = PermCodes.System.Dict.MANAGE_NAME_EN)
    @Operation(summary = "添加字典")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody DictParam param) {
        ValidationUtil.validateParam(param, ValidationGroup.add.class);
        dictService.add(param);
        return Res.ok();
    }

    /// 根据主键删除字典
    ///
    /// @param id 字典ID
    @PermCode(code = PermCodes.Action.MANAGE, nameCn = PermCodes.System.Dict.MANAGE_NAME_CN, nameEn = PermCodes.System.Dict.MANAGE_NAME_EN)
    @Operation(summary = "根据主键删除")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        dictService.delete(id);
        return Res.ok();
    }

    /// 更新字典
    ///
    /// @param param 字典参数
    @PermCode(code = PermCodes.Action.MANAGE, nameCn = PermCodes.System.Dict.MANAGE_NAME_CN, nameEn = PermCodes.System.Dict.MANAGE_NAME_EN)
    @Operation(summary = "更新字典")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody DictParam param) {
        ValidationUtil.validateParam(param, ValidationGroup.edit.class);
        dictService.update(param);
        return Res.ok();
    }

    /// 根据主键获取字典
    ///
    /// @param id 字典ID
    /// @return 字典信息
    @PermCode(code = PermCodes.Action.VIEW, nameCn = PermCodes.System.Dict.VIEW_NAME_CN, nameEn = PermCodes.System.Dict.VIEW_NAME_EN)
    @Operation(summary = "根据主键获取字典")
    @GetMapping("/get")
    public Result<DictResult> findById(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(dictService.findById(id));
    }

    /// 查询全部字典
    ///
    /// @return 字典列表
    @PermCode(code = PermCodes.Action.VIEW, nameCn = PermCodes.System.Dict.VIEW_NAME_CN, nameEn = PermCodes.System.Dict.VIEW_NAME_EN)
    @Operation(summary = "查询全部字典")
    @GetMapping("/all")
    public Result<List<DictResult>> findAll() {
        return Res.ok(dictService.findAll());
    }

    /// 字典分页查询
    ///
    /// @param pageParam 分页参数
    /// @param param 查询参数
    /// @return 字典分页结果
    @PermCode(code = PermCodes.Action.VIEW, nameCn = PermCodes.System.Dict.VIEW_NAME_CN, nameEn = PermCodes.System.Dict.VIEW_NAME_EN)
    @Operation(summary = "字典分页")
    @GetMapping("/page")
    public Result<PageResult<DictResult>> page(PageParam pageParam, DictParam param) {
        return Res.ok(dictService.page(pageParam, param));
    }

    /// 判断字典编码是否被使用
    ///
    /// @param code 字典编码
    /// @return 是否存在
    @PermCode(code = PermCodes.Action.VIEW, nameCn = PermCodes.System.Dict.VIEW_NAME_CN, nameEn = PermCodes.System.Dict.VIEW_NAME_EN)
    @Operation(summary = "字典编码是否被使用")
    @GetMapping("/exists-by-code")
    public Result<Boolean> existsByCode(@NotBlank(message = "{validation.field.code.notBlank}") String code) {
        return Res.ok(dictService.existsByCode(code));
    }

    /// 判断编码是否被使用(不包含自己)
    ///
    /// @param code 字典编码
    /// @param id 字典ID
    /// @return 是否存在
    @PermCode(code = PermCodes.Action.VIEW, nameCn = PermCodes.System.Dict.VIEW_NAME_CN, nameEn = PermCodes.System.Dict.VIEW_NAME_EN)
    @Operation(summary = "编码是否被使用(不包含自己)")
    @GetMapping("/exists-by-code-not-id")
    public Result<Boolean> existsByCode(@NotBlank(message = "{validation.field.code.notBlank}") String code, @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(dictService.existsByCode(code, id));
    }

}


