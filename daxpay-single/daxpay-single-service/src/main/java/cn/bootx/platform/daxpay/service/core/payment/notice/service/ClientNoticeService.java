package cn.bootx.platform.daxpay.service.core.payment.notice.service;

import cn.bootx.platform.common.core.exception.RepetitiveOperationException;
import cn.bootx.platform.common.redis.RedisClient;
import cn.bootx.platform.common.spring.exception.RetryableException;
import cn.bootx.platform.daxpay.service.core.payment.notice.dao.ClientNoticeTaskManager;
import cn.bootx.platform.daxpay.service.core.payment.notice.entity.ClientNoticeTask;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

/**
 * 消息通知任务服务
 * 总共会发起15次通知，通知频率为15s/15s/30s/3m/10m/20m/30m/30m/30m/60m/3h/3h/3h/6h/6h
 * @author xxm
 * @since 2024/2/20
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ClientNoticeService {

    private final ClientNoticeTaskManager taskManager;

    private final RedisClient redisClient;

    private final LockTemplate lockTemplate;

    private final String KEY = "client:notice:task";

    /**
     * 注册消息通知任务, 失败重试3次, 间隔一秒
     */
    @Async("bigExecutor")
    @Retryable(value = RetryableException.class)
    public void register(){
        // 将任务写入到任务表中

        // 同时触发一次通知, 如果成功记录结束

        // 不成功更新任务表, 并注册到Redis对应的表中
    }

    /**
     * 从redis中执行任务, 通过定时任务触发
     */
    public void run(long start, long end){
        // 查询Redis任务表,获取任务
        Set<String> taskIds = redisClient.zrangeByScore(KEY, start, end);
        // 发起一个异步任务,
        for (String taskId : taskIds) {
            SpringUtil.getBean(this.getClass()).run(Long.valueOf(taskId));
        }
        // 删除Redis中任务
        redisClient.zremRangeByScore(KEY, start, end);
    }

    /**
     * 执行任务
     */
    @Async("asyncExecutor")
    public void run(Long taskId){
        LocalDateTime now = LocalDateTime.now();
        // 开启分布式锁
        LockInfo lock = lockTemplate.lock(KEY + ":" + taskId,2000, 50);
        if (Objects.isNull(lock)){
            throw new RepetitiveOperationException("支付同步处理中，请勿重复操作");
        }
        // 查询任务, 进行发送
        ClientNoticeTask task = taskManager.findById(taskId).orElse(null);
        try {
            if (Objects.nonNull(task)){
                HttpResponse execute = HttpUtil.createPost(task.getUrl())
                        .body(task.getContent(), ContentType.JSON.getValue())
                        .timeout(5000)
                        .execute();
                String body = execute.body();
                // 如果响应值等于SUCCESS, 说明发送成功
                if (Objects.equals(body, "SUCCESS")){
                    this.successHandler(task);
                } else {
                    this.failHandler(task,now);
                }
            }
        } catch (HttpException e) {
            this.failHandler(task, now);
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /**
     * 成功处理
     */
    public void successHandler(ClientNoticeTask task){
        // 记录成功并保存
        task.setSuccess(true);
    }

    /**
     * 失败处理
     */
    public void failHandler(ClientNoticeTask task, LocalDateTime now){
        // 次数+1


        // 注册任务到redis中
    }
}
