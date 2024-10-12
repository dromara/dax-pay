package cn.bootx.platform.starter.redis.delay.controller;

import cn.bootx.platform.core.annotation.RequestGroup;
import cn.bootx.platform.core.annotation.RequestPath;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.core.rest.result.Result;
import cn.bootx.platform.starter.redis.delay.result.BucketResult;
import cn.bootx.platform.starter.redis.delay.result.DelayJobResult;
import cn.bootx.platform.starter.redis.delay.result.TopicResult;
import cn.bootx.platform.starter.redis.delay.service.DelayQueueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 延时队列管理
 * @author xxm
 * @since 2024/9/20
 */
@Tag(name = "延时队列管理")
@RequestGroup(groupCode = "delay", groupName = "延时队列管理", moduleCode = "starter", moduleName = "starter模块")
@RestController
@RequestMapping("/delay/queue")
@RequiredArgsConstructor
public class DelayQueueController {
    private final DelayQueueService delayQueueService;

    @RequestPath("获取桶信息列表")
    @Operation(summary = "获取桶信息列表")
    @GetMapping("/getBucket")
    public Result<List<BucketResult>> getBucket() {
        return Res.ok(delayQueueService.getBucket());
    }

    @RequestPath("获取死信主题列表")
    @Operation(summary = "获取桶任务分页")
    @GetMapping("/pageBucketJob")
    public Result<PageResult<DelayJobResult>> pageBucketJob(String bucketName, PageParam pageParam) {
        return Res.ok(delayQueueService.pageBucketJob(bucketName, pageParam));
    }

    @RequestPath("获取就绪主题列表")
    @Operation(summary = "获取就绪主题列表")
    @GetMapping("/getReadyTopic")
    public Result<List<TopicResult>> getReadyTopic() {
        return Res.ok(delayQueueService.getDelayTopic());
    }

    @RequestPath("获取就绪主题任务分页")
    @Operation(summary = "获取就绪任务分页")
    @GetMapping("/pageReadyJob")
    public Result<PageResult<DelayJobResult>> pageReadyJob(String topic, PageParam pageParam) {
        return Res.ok(delayQueueService.pageReadyJob(topic, pageParam));
    }

    @RequestPath("获取任务详情")
    @Operation(summary = "获取任务详情")
    @PostMapping("/getJobDetail")
    public Result<DelayJobResult> getJobDetail(String jobId) {
        return Res.ok(delayQueueService.getJobDetail(jobId));
    }

    @RequestPath("获取死信主题列表")
    @Operation(summary = "获取死信主题数量和列表")
    @GetMapping("/getDeadTopic")
    public Result<List<TopicResult>> getDeadTopic() {
        return Res.ok(delayQueueService.getDeadTopic());
    }

    @RequestPath("获取死信主题任务分页")
    @Operation(summary = "获取死信主题任务分页")
    @GetMapping("/pageDeadJob")
    public Result<PageResult<DelayJobResult>> pageDeadJob(String topic, PageParam pageParam) {
        return Res.ok(delayQueueService.pageDeadJob(topic, pageParam));
    }

    @RequestPath("获取死信任务详情")
    @Operation(summary = "获取死信任务详情")
    @PostMapping("/getDeadJobDetail")
    public Result<DelayJobResult> resetDeadJob(String jobId) {
        return Res.ok(delayQueueService.getDeadJobDetail(jobId));
    }

    @RequestPath("删除死信任务")
    @Operation(summary = "删除死信任务")
    @PostMapping("/removeDeadJob")
    public Result<Object> removeDeadJob(String jobId) {
        delayQueueService.removeDeadJob(jobId);
        return Res.ok();
    }

}
