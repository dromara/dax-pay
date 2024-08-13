package cn.bootx.platform.starter.file.controller;

import cn.bootx.platform.core.annotation.IgnoreAuth;
import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.core.util.ValidationUtil;
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
@RequestGroup(groupCode = "file", groupName = "文件存储管理", moduleCode = "starter", moduleName = "starter模块")
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

    @Operation(summary = "详情")
    @GetMapping("/findById")
    public Result<FilePlatformResult> findById(Long id){
        return Res.ok(filePlatformService.findById(id));
    }

    @RequestPath("更新文件存储平台地址")
    @Operation(summary = "更新文件存储平台地址")
    @PostMapping("/updateUrl")
    public Result<Void> update(@RequestBody FilePlatformParam filePlatform){
        ValidationUtil.validateParam(filePlatform);
        filePlatformService.updateUrl(filePlatform);
        return Res.ok();
    }

    @RequestPath("设置默认存储平台地址")
    @Operation(summary = "设置默认存储平台地址")
    @PostMapping("/setDefault")
    public Result<Void> setDefault(@NotNull(message = "主键不可为空") Long id){
        filePlatformService.setDefault(id);
        return Res.ok();
    }
}
