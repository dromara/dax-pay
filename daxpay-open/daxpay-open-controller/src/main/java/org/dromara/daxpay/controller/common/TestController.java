package org.dromara.daxpay.controller.common;

import cn.bootx.platform.core.annotation.IgnoreAuth;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.starter.cache.handler.CacheClearProcessor;
import cn.bootx.platform.starter.cache.service.CacheClearService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 * @author xxm
 * @since 2024/7/4
 */
@Validated
@IgnoreAuth
@Slf4j
@Tag(name = "测试服务")
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {
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
    public Result<Void> clearCacheByPrefix(List<String> prefix) {
        cacheClearService.clearCacheByPrefix(prefix);
        return Res.ok();
    }
}
