package cn.bootx.platform.starter.file.controller;

import cn.bootx.platform.core.annotation.IgnoreAuth;
import cn.bootx.platform.core.annotation.OperateLog;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.starter.file.param.FilePlatformParam;
import cn.bootx.platform.starter.file.result.FilePlatformResult;
import cn.bootx.platform.starter.file.service.FilePlatformService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 文件存储平台
 * @author xxm
 * @since 2024/8/12
 */
@Validated
@RequestGroup(groupCode = "FilePlatfor", groupName = "文件存储平台管理", moduleCode = "starter")
@Tag(name = "文件存储平台")
@RestController
@RequestMapping("/file/platform")
@RequiredArgsConstructor
public class FilePlatformController {
    private final FilePlatformService filePlatformService;

    @IgnoreAuth
    @Operation(summary = "列表")
    @GetMapping("/findAll")
    public Result<List<FilePlatformResult>> findAll(){
        return Res.ok(filePlatformService.findAll());
    }

    @RequestPath("详情")
    @Operation(summary = "详情")
    @GetMapping("/findById")
    public Result<FilePlatformResult> findById(@NotNull(message = "主键不可为空") Long id){
        return Res.ok(filePlatformService.findById(id));
    }

    @RequestPath("更新文件存储平台地址")
    @Operation(summary = "更新文件存储平台地址")
    @PostMapping("/updateUrl")
    @OperateLog(title = "更新文件存储平台地址", businessType = OperateLog.BusinessType.UPDATE, saveParam = true)
    public Result<Void> update(@RequestBody @Validated FilePlatformParam filePlatform){
        filePlatformService.updateUrl(filePlatform);
        return Res.ok();
    }

    @RequestPath("设置默认存储平台地址")
    @Operation(summary = "设置默认存储平台地址")
    @PostMapping("/setDefault")
    @OperateLog(title = "设置默认存储平台地址", businessType = OperateLog.BusinessType.UPDATE, saveParam = true)
    public Result<Void> setDefault(@NotNull(message = "主键不可为空") Long id){
        filePlatformService.setDefault(id);
        return Res.ok();
    }
}
