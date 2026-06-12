package org.dromara.daxpay.platform.capability.cache.controller;

import org.dromara.daxpay.platform.core.rest.Res;
import org.dromara.daxpay.platform.core.rest.result.Result;
import org.dromara.daxpay.platform.capability.cache.handler.CacheClearProcessor;
import org.dromara.daxpay.platform.capability.cache.service.CacheClearService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/// # 系统缓存清除
///
@Tag(name = "系统缓存清除")
@RestController
@RequestMapping("/cache/clear")
@RequiredArgsConstructor
public class CacheClearController {
    private final CacheClearProcessor cacheClearProcessor;
    private final CacheClearService cacheClearService;

    @Operation(summary = "查询所有缓存前缀")
    @GetMapping("/get-cache-prefix")
    public Result<List<String>> getCachePrefix() {
        return Res.ok(cacheClearProcessor.getCachePrefix());
    }

    @Operation(summary = "清除指定前缀的缓存")
    @PostMapping("/prefix")
    public Result<Void> clearCacheByPrefix(@RequestBody List<String> prefix) {
        cacheClearService.clearCacheByPrefix(prefix);
        return Res.ok();
    }
}
