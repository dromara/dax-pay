package cn.bootx.platform.starter.cache.controller;

import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.starter.cache.handler.CacheClearProcessor;
import cn.bootx.platform.starter.cache.service.CacheClearService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统缓存清除
 * @author xxm
 * @since 2025/4/23
 */
@RequestGroup(groupCode = "CacheClearService", groupName = "系统缓存清除", moduleCode = "starter")
@Tag(name = "系统缓存清除")
@RestController
@RequestMapping("/cache/clear")
@RequiredArgsConstructor
public class CacheClearController {
    private final CacheClearProcessor cacheClearProcessor;
    private final CacheClearService cacheClearService;

    @RequestPath("查询所有缓存前缀")
    @Operation(summary = "查询所有缓存前缀")
    @GetMapping("/getCachePrefix")
    public Result<List<String>> getCachePrefix() {
        return Res.ok(cacheClearProcessor.getCachePrefix());
    }

    @RequestPath("清除指定前缀的缓存")
    @Operation(summary = "清除指定前缀的缓存")
    @PostMapping("/prefix")
    public Result<Void> clearCacheByPrefix(@RequestBody List<String> prefix) {
        cacheClearService.clearCacheByPrefix(prefix);
        return Res.ok();
    }
}
