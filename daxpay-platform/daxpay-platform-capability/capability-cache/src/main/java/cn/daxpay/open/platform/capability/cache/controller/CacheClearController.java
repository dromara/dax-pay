package cn.daxpay.open.platform.capability.cache.controller;

import cn.daxpay.open.platform.core.annotation.InternalPath;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.capability.cache.handler.CacheClearProcessor;
import cn.daxpay.open.platform.capability.cache.service.CacheClearService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/// # 系统缓存清除
///
@PermCode(menuCode = PermCodes.System.PlatformConfig.MENU)
@Tag(name = "系统缓存清除")
@InternalPath
@RestController
@RequestMapping("/cache/clear")
@RequiredArgsConstructor
public class CacheClearController {
    private final CacheClearProcessor cacheClearProcessor;
    private final CacheClearService cacheClearService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "查询所有缓存前缀")
    @GetMapping("/get-cache-prefix")
    public Result<List<String>> getCachePrefix() {
        return Res.ok(cacheClearProcessor.getCachePrefix());
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "清除指定前缀的缓存")
    @PostMapping("/prefix")
    public Result<Void> clearCacheByPrefix(@RequestBody List<String> prefix) {
        cacheClearService.clearCacheByPrefix(prefix);
        return Res.ok();
    }
}
