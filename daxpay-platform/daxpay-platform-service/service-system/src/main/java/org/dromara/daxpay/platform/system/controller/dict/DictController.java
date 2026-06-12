package org.dromara.daxpay.platform.system.controller.dict;

import org.dromara.daxpay.platform.core.annotation.PermCode;
import org.dromara.daxpay.platform.system.param.dict.DictParam;
import org.dromara.daxpay.platform.system.result.dict.DictResult;
import org.dromara.daxpay.platform.system.service.dict.DictService;
import org.dromara.daxpay.platform.core.rest.Res;
import org.dromara.daxpay.platform.core.rest.param.PageParam;
import org.dromara.daxpay.platform.core.rest.result.PageResult;
import org.dromara.daxpay.platform.core.rest.result.Result;
import org.dromara.daxpay.platform.core.util.ValidationUtil;
import org.dromara.daxpay.platform.core.validation.ValidationGroup;
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
@PermCode(menuCode = "system:dict")
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
    @PermCode(code = "dict:manage", nameCn = "字典管理", nameEn = "Dict Manage")
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
    @PermCode(code = "dict:manage", nameCn = "字典管理", nameEn = "Dict Manage")
    @Operation(summary = "根据主键删除")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        dictService.delete(id);
        return Res.ok();
    }

    /// 更新字典
    ///
    /// @param param 字典参数
    @PermCode(code = "dict:manage", nameCn = "字典管理", nameEn = "Dict Manage")
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
    @PermCode(code = "dict:view", nameCn = "字典查看", nameEn = "Dict View")
    @Operation(summary = "根据主键获取字典")
    @GetMapping("/get")
    public Result<DictResult> findById(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(dictService.findById(id));
    }

    /// 查询全部字典
    ///
    /// @return 字典列表
    @PermCode(code = "dict:view", nameCn = "字典查看", nameEn = "Dict View")
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
    @PermCode(code = "dict:view", nameCn = "字典查看", nameEn = "Dict View")
    @Operation(summary = "字典分页")
    @GetMapping("/page")
    public Result<PageResult<DictResult>> page(PageParam pageParam, DictParam param) {
        return Res.ok(dictService.page(pageParam, param));
    }

    /// 判断字典编码是否被使用
    ///
    /// @param code 字典编码
    /// @return 是否存在
    @PermCode(code = "dict:view", nameCn = "字典查看", nameEn = "Dict View")
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
    @PermCode(code = "dict:view", nameCn = "字典查看", nameEn = "Dict View")
    @Operation(summary = "编码是否被使用(不包含自己)")
    @GetMapping("/exists-by-code-not-id")
    public Result<Boolean> existsByCode(@NotBlank(message = "{validation.field.code.notBlank}") String code, @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(dictService.existsByCode(code, id));
    }

}


