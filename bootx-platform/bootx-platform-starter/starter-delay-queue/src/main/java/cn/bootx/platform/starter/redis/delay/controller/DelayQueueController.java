package cn.bootx.platform.starter.redis.delay.controller;

import cn.bootx.platform.starter.redis.delay.service.DelayQueueService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 延时队列管理
 * @author xxm
 * @since 2024/9/20
 */
@Tag(name = "")
@RestController
@RequestMapping("/delay/queue")
@RequiredArgsConstructor
public class DelayQueueController {
    private final DelayQueueService delayQueueService;



}
