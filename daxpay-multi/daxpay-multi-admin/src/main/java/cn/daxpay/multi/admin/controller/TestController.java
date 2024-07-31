package cn.daxpay.multi.admin.controller;

import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.iam.service.permission.PermPathSyncService;
import cn.daxpay.multi.service.entity.order.pay.PayOrder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author xxm
 * @since 2024/7/4
 */
@Slf4j
@Tag(name = "测试服务")
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {
    private final PermPathSyncService permPathSyncService;

    private final RedisTemplate<String, Object> redisTemplate;

    @Operation(summary = "测试路径生成")
    @GetMapping("/getRequestPaths")
    public Result<Void> getRequestPaths(){
        permPathSyncService.sync();
        return Res.ok();
    }

    @Operation(summary = "测试redis")
    @GetMapping("/redis")
    public Result<Object> redis(){
        PayOrder payOrder = new PayOrder();
        payOrder.setOrderNo("123");
//        payOrder.setCs("123");
       redisTemplate.opsForValue().set("payOrder",payOrder);
        var payOrder1 = redisTemplate.opsForValue().get("payOrder");
        return Res.ok(payOrder1);
    }
}
