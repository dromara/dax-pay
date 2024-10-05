package org.dromara.daxpay.service.controller;

import cn.bootx.platform.core.annotation.IgnoreAuth;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.starter.redis.delay.annotation.DelayEventListener;
import cn.bootx.platform.starter.redis.delay.annotation.DelayJobEvent;
import cn.bootx.platform.starter.redis.delay.bean.DelayJob;
import cn.bootx.platform.starter.redis.delay.service.DelayJobService;
import org.dromara.daxpay.core.result.assist.AuthResult;
import org.dromara.daxpay.service.entity.order.pay.PayOrder;
import cn.hutool.core.util.RandomUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author xxm
 * @since 2024/7/4
 */
@IgnoreAuth
@Slf4j
@Tag(name = "测试服务")
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final RedisTemplate<String, Object> objectRedisTemplate;

    private final DelayJobService delayJobService;


    @Operation(summary = "测试redis")
    @GetMapping("/redis")
    public Result<Object> redis(){
        PayOrder payOrder = new PayOrder();
        payOrder.setOrderNo("123");
//        redisTemplate.opsForValue().set("payOrder",new AuthResult().setStatus("123"),60000L);
        objectRedisTemplate.opsForValue().set("payOrder",new AuthResult().setStatus("123"),600, TimeUnit.MILLISECONDS);
//        objectRedisTemplate.opsForValue().set("payOrder",payOrder);
        var payOrder1 = objectRedisTemplate.opsForValue().get("payOrder");
        return Res.ok(payOrder1);
    }

    @Operation(summary = "添加测试延时任务")
    @PostMapping(value = "addTest")
    public Result<Void> addDefJobTest() {
        for (int i = 0; i < 100; i++){
            DelayJob<PayOrder> delayJob = new DelayJob<>();
            delayJobService.register(delayJob, "hello", RandomUtil.randomInt(10000,90000));
            delayJobService.register(delayJob, "hello", LocalDateTime.now().plusSeconds(20));
        }
        return Res.ok();
    }


    @DelayEventListener("hello")
    public void hello(DelayJobEvent<PayOrder> event) {
        if (RandomUtil.randomBoolean()){
            throw new RuntimeException("测试异常");
        }
        log.info("接收到消息:{}",event);
    }


}
