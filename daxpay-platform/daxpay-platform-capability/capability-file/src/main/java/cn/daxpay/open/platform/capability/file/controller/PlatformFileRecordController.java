package cn.daxpay.open.platform.capability.file.controller;

import cn.daxpay.open.platform.capability.file.param.PlatformFileRecordPageParam;
import cn.daxpay.open.platform.capability.file.result.PlatformFileRecordResult;
import cn.daxpay.open.platform.capability.file.service.PlatformFileRecordService;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/// # 平台文件记录管理控制器
///
@Validated
@Tag(name = "平台文件记录管理")
@RestController
@RequestMapping("/file/platform/record")
@RequiredArgsConstructor
@PermCode(menuCode = PermCodes.System.FilePlatform.MENU)
public class PlatformFileRecordController {

    private final PlatformFileRecordService platformFileRecordService;

    @Operation(summary = "分页查询")
    @GetMapping("/page")
    @PermCode(code = PermCodes.Action.VIEW, nameCn = "文件查看", nameEn = "File View")
    public Result<PageResult<PlatformFileRecordResult>> page(PlatformFileRecordPageParam param) {
        return Res.ok(platformFileRecordService.page(param));
    }

    @Operation(summary = "查询详情")
    @GetMapping("/{id}")
    @PermCode(code = PermCodes.Action.VIEW, nameCn = "文件查看", nameEn = "File View")
    public Result<PlatformFileRecordResult> findById(@PathVariable Long id) {
        return Res.ok(platformFileRecordService.findById(id));
    }
}
