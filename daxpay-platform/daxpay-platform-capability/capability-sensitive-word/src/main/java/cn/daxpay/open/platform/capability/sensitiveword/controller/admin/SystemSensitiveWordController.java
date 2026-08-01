package cn.daxpay.open.platform.capability.sensitiveword.controller.admin;

import cn.daxpay.open.platform.capability.sensitiveword.param.SensitiveWordCheckTextParam;
import cn.daxpay.open.platform.capability.sensitiveword.param.SystemSensitiveWordParam;
import cn.daxpay.open.platform.capability.sensitiveword.param.SystemSensitiveWordQuery;
import cn.daxpay.open.platform.capability.sensitiveword.result.SystemSensitiveWordResult;
import cn.daxpay.open.platform.capability.sensitiveword.service.SensitiveWordCheckService;
import cn.daxpay.open.platform.capability.sensitiveword.service.SystemSensitiveWordService;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/// # 敏感词词库管理（运营端）
///
@PermCode(menuCode = PermCodes.System.SensitiveWord.MENU)
@Validated
@Tag(name = "敏感词词库")
@RestController
@RequestMapping("/admin/system/sensitive-word")
@RequiredArgsConstructor
public class SystemSensitiveWordController {

    private final SystemSensitiveWordService systemSensitiveWordService;
    private final SensitiveWordCheckService sensitiveWordCheckService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public Result<PageResult<SystemSensitiveWordResult>> page(PageParam pageParam, SystemSensitiveWordQuery query) {
        return Res.ok(systemSensitiveWordService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "详情")
    @GetMapping("/get-by-id")
    public Result<SystemSensitiveWordResult> getById(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(systemSensitiveWordService.findById(id));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "新增")
    @PostMapping("/add")
    public Result<SystemSensitiveWordResult> add(
            @RequestBody @Validated(ValidationGroup.add.class) SystemSensitiveWordParam param) {
        return Res.ok(systemSensitiveWordService.add(param));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "修改")
    @PostMapping("/update")
    public Result<Void> update(
            @RequestBody @Validated(ValidationGroup.edit.class) SystemSensitiveWordParam param) {
        systemSensitiveWordService.update(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "删除")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        systemSensitiveWordService.delete(id);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "词面是否存在")
    @GetMapping("/exists-by-word")
    public Result<Boolean> existsByWord(
            String word,
            @RequestParam(required = false) Long id) {
        return Res.ok(systemSensitiveWordService.existsByWord(word, id));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "试检文本")
    @PostMapping("/check-text")
    public Result<Map<String, Object>> checkText(@RequestBody @Validated SensitiveWordCheckTextParam param) {
        boolean record = Boolean.TRUE.equals(param.getRecordHit());
        List<String> hits = sensitiveWordCheckService.checkText(param.getText(), record);
        return Res.ok(Map.of("hits", hits, "hit", !hits.isEmpty()));
    }
}

