package cn.daxpay.multi.admin.controller;

import cn.bootx.platform.common.redis.delay.annotation.DelayTaskJob;
import cn.bootx.platform.common.redis.delay.annotation.DelayTaskJobProcessor;
import cn.bootx.platform.common.redis.delay.annotation.DelayTaskEvent;
import cn.bootx.platform.common.redis.delay.bean.DelayJob;
import cn.bootx.platform.common.redis.delay.bean.Job;
import cn.bootx.platform.common.redis.delay.service.JobService;
import cn.bootx.platform.core.annotation.IgnoreAuth;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.iam.service.permission.PermPathSyncService;
import cn.daxpay.multi.service.entity.order.pay.PayOrder;
import com.alibaba.fastjson.JSON;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicInteger;

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

    private final PermPathSyncService permPathSyncService;

    private final RedisTemplate<String, Object> redisTemplate;
    private final JobService jobService;


    private final static AtomicInteger index = new AtomicInteger(0);

    private final static String[] tag = new String[]{"test","web","java"};
    private final DelayTaskJobProcessor delayTaskJobProcessor;


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
       redisTemplate.opsForValue().set("payOrder",payOrder);
        var payOrder1 = redisTemplate.opsForValue().get("payOrder");
        return Res.ok(payOrder1);
    }

    @Operation(summary = "添加测试任务")
    @PostMapping(value = "addTest")
    public String addDefJobTest() {
        Job request = new Job();
        int i = index.addAndGet(1);
        request.setId(String.valueOf(i));
        int num = i%3;
        request.setTopic("hello");
        request.setMessage(new PayOrder());
        request.setDelayTime(0);
        request.setTtrTime(1000);
        DelayJob delayJob = jobService.addDefJob(request);
        return JSON.toJSONString(delayJob);
    }

    @Operation(summary = "添加测试任务(自定义)")
    @PostMapping("add")
    public String addDefJob(Job request) {
        DelayJob delayJob = jobService.addDefJob(request);
        return JSON.toJSONString(delayJob);
    }

    @Operation(summary = "完成一个执行的任务")
    @DeleteMapping(value = "finish")
    public String finishJob(String jobId) {
        jobService.finishJob(jobId);
        return "success";
    }

    @DelayTaskJob("hello")
    public void hello(DelayTaskEvent<PayOrder> event) {
        log.info("hello:{}",event);
    }

}
