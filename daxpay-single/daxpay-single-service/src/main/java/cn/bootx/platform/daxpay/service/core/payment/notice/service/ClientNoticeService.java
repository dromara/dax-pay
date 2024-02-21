package cn.bootx.platform.daxpay.service.core.payment.notice.service;

import cn.bootx.platform.common.core.exception.RepetitiveOperationException;
import cn.bootx.platform.common.core.util.CollUtil;
import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.bootx.platform.common.redis.RedisClient;
import cn.bootx.platform.daxpay.code.PaySignTypeEnum;
import cn.bootx.platform.daxpay.service.code.ClientNoticeTypeEnum;
import cn.bootx.platform.daxpay.service.core.order.pay.dao.PayChannelOrderManager;
import cn.bootx.platform.daxpay.service.core.order.pay.dao.PayOrderExtraManager;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayChannelOrder;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrderExtra;
import cn.bootx.platform.daxpay.service.core.order.refund.dao.RefundChannelOrderManager;
import cn.bootx.platform.daxpay.service.core.order.refund.dao.RefundOrderExtraManager;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.RefundChannelOrder;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.RefundOrder;
import cn.bootx.platform.daxpay.service.core.payment.notice.dao.ClientNoticeTaskManager;
import cn.bootx.platform.daxpay.service.core.payment.notice.entity.ClientNoticeTask;
import cn.bootx.platform.daxpay.service.core.payment.notice.result.PayChannelResult;
import cn.bootx.platform.daxpay.service.core.payment.notice.result.PayNoticeResult;
import cn.bootx.platform.daxpay.service.core.payment.notice.result.RefundChannelResult;
import cn.bootx.platform.daxpay.service.core.payment.notice.result.RefundNoticeResult;
import cn.bootx.platform.daxpay.service.core.system.config.entity.PlatformConfig;
import cn.bootx.platform.daxpay.service.core.system.config.service.PlatformConfigService;
import cn.bootx.platform.daxpay.util.PaySignUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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

    private final PayOrderExtraManager payOrderExtraManager;

    private final PayChannelOrderManager payChannelOrderManager;

    private final RefundOrderExtraManager refundOrderExtraManager;

    private final RefundChannelOrderManager refundChannelOrderManager;

    private final PlatformConfigService configService;

    private final ClientNoticeTaskManager taskManager;

    private final RedisClient redisClient;

    private final LockTemplate lockTemplate;

    private static final String KEY = "client:notice:task";

    private static final Map<Integer,Integer> DELAY_TIME = new HashMap<>();

    /*
     * key: 当前通知次数, value 下次通知的时间间隔
     * 初始化延迟时间表, 总共会发起16次通知吗, 总计 24h4m
     * 通知频率为0s/15s/15s/30s/3m/10m/20m/30m/30m/30m/60m/3h/3h/3h/6h/6h
     */
    static {
        DELAY_TIME.put(1, 15 * 1000);
        DELAY_TIME.put(2, 15 * 1000);
        DELAY_TIME.put(3, 30 * 1000);
        DELAY_TIME.put(4, 3 * 60 * 1000);
        DELAY_TIME.put(5, 10 * 60 * 1000);
        DELAY_TIME.put(6, 20 * 60 * 1000);
        DELAY_TIME.put(7, 30 * 60 * 1000);
        DELAY_TIME.put(8, 30 * 60 * 1000);
        DELAY_TIME.put(9, 30 * 60 * 1000);
        DELAY_TIME.put(10, 60 * 60 * 1000);
        DELAY_TIME.put(11, 3 * 60 * 60 * 1000);
        DELAY_TIME.put(12, 3 * 60 * 60 * 1000);
        DELAY_TIME.put(13, 3 * 60 * 60 * 1000);
        DELAY_TIME.put(14, 6 * 60 * 60 * 1000);
        DELAY_TIME.put(15, 6 * 60 * 60 * 1000);
    }

    /**
     * 注册支付消息通知任务
     * @param order 支付订单
     * @param orderExtra 支付订单扩展信息
     * @param channelOrders 支付通道订单
     */
    @Async("bigExecutor")
    public void registerPayNotice(PayOrder order, PayOrderExtra orderExtra, List<PayChannelOrder> channelOrders) {
        // 支付订单扩展信息为空则进行查询
        if (Objects.isNull(orderExtra)){
            Optional<PayOrderExtra> extraOpt =  payOrderExtraManager.findById(order.getId());
            if (!extraOpt.isPresent()){
                log.error("未找到支付扩展信息，数据错误，订单ID：{}",order.getId());
                return;
            }
            orderExtra = extraOpt.get();
        }
        // 判断是否需要进行通知
        if (StrUtil.isBlank(orderExtra.getNotifyUrl())){
            log.info("支付订单无需通知，订单ID：{}",order.getId());
        }

        // 通道支付订单为空则进行查询
        if (CollUtil.isEmpty(channelOrders)){
            channelOrders = payChannelOrderManager.findAllByPaymentId(order.getId());
        }
        // 创建通知任务并保存
        ClientNoticeTask task = this.buildPayTask(order, orderExtra, channelOrders);
        try {
            taskManager.save(task);
        } catch (Exception e) {
            log.error("注册支付消息通知任务失败，数据错误，订单ID：{}",order.getId());
            throw new RuntimeException(e);
        }
        try {
            // 同时触发一次通知, 如果成功发送, 任务结束
            this.sendData(task, LocalDateTime.now());
        } catch (HttpException e) {
            this.failHandler(task, LocalDateTime.now());
        }
    }

    /**
     * 构建出支付通知任务对象
     */
    private ClientNoticeTask buildPayTask(PayOrder order, PayOrderExtra orderExtra, List<PayChannelOrder> channelOrders){
        // 组装内容
        List<PayChannelResult> channels = channelOrders.stream()
                .map(o->new PayChannelResult().setChannel(o.getChannel()).setWay(o.getPayWay()).setAmount(o.getAmount()))
                .collect(Collectors.toList());

        PayNoticeResult payNoticeResult = new PayNoticeResult()
                .setPaymentId(order.getId())
                .setAsyncPay(order.isAsyncPay())
                .setBusinessNo(order.getBusinessNo())
                .setAmount(order.getAmount())
                .setPayTime(order.getPayTime())
                .setCreateTime(order.getCreateTime())
                .setStatus(order.getStatus())
                .setAttach(orderExtra.getAttach())
                .setPayChannels(channels);

        PlatformConfig config = configService.getConfig();
        // 是否需要签名
        if (orderExtra.isNoticeSign()){
            // 签名
            if (Objects.equals(config.getSignType(), PaySignTypeEnum.MD5.getCode())){
                payNoticeResult.setSign(PaySignUtil.md5Sign(payNoticeResult,config.getSignSecret()));
            } else {
                payNoticeResult.setSign(PaySignUtil.hmacSha256Sign(payNoticeResult,config.getSignSecret()));
            }
        }
        return new ClientNoticeTask()
                .setUrl(orderExtra.getNotifyUrl())
                .setContent(JSONUtil.toJsonStr(payNoticeResult))
                .setType(ClientNoticeTypeEnum.PAY.getType())
                .setSendCount(0)
                .setOrderId(order.getId());
    }


    /**
     * 注册退款消息通知任务
     * @param order 退款订单
     * @param orderExtra 退款订单扩展信息
     * @param channelOrders 退款通道订单
     */
    @Async("bigExecutor")
    public void registerRefundNotice(RefundOrder order, PayOrderExtra orderExtra, List<RefundChannelOrder> channelOrders) {
        // 退款订单扩展信息为空则进行查询
        if (Objects.isNull(orderExtra)){
            Optional<PayOrderExtra> extraOpt =  payOrderExtraManager.findById(order.getId());
            if (!extraOpt.isPresent()){
                log.error("未找到支付扩展信息，数据错误，订单ID：{}",order.getId());
                return;
            }
            orderExtra = extraOpt.get();
        }
        // 判断是否需要进行通知
        if (StrUtil.isBlank(orderExtra.getNotifyUrl())){
            log.info("退款订单无需通知，订单ID：{}",order.getId());
        }

        // 通道退款订单为空则进行查询
        if (CollUtil.isEmpty(channelOrders)){
            channelOrders = refundChannelOrderManager.findAllByRefundId(order.getId());
        }
        // 创建通知任务并保存
        ClientNoticeTask task = this.buildRefundTask(order, orderExtra, channelOrders);
        try {
            taskManager.save(task);
        } catch (Exception e) {
            log.error("注册退款消息通知任务失败，数据错误，订单ID：{}",order.getId());
            throw new RuntimeException(e);
        }
        try {
            // 同时触发一次通知, 如果成功发送, 任务结束
            this.sendData(task, LocalDateTime.now());
        } catch (HttpException e) {
            this.failHandler(task, LocalDateTime.now());
        }
    }

    /**
     * 构建出退款通知任务对象
     */
    private ClientNoticeTask buildRefundTask(RefundOrder order, PayOrderExtra orderExtra, List<RefundChannelOrder> channelOrders){
        // 组装内容
        List<RefundChannelResult> channels = channelOrders.stream()
                .map(o->new RefundChannelResult().setChannel(o.getChannel()).setAmount(o.getAmount()))
                .collect(Collectors.toList());

        RefundNoticeResult payNoticeResult = new RefundNoticeResult()
                .setRefundId(order.getId())
                .setAsyncPay(order.isAsyncPay())
                .setAsyncChannel(order.getAsyncChannel())
                .setRefundNo(order.getRefundNo())
                .setAmount(order.getAmount())
                .setRefundTime(order.getRefundTime())
                .setCreateTime(order.getCreateTime())
                .setStatus(order.getStatus())
                .setAttach(orderExtra.getAttach())
                .setRefundChannels(channels);

        PlatformConfig config = configService.getConfig();
        // 是否需要签名
        if (orderExtra.isNoticeSign()){
            // 签名
            if (Objects.equals(config.getSignType(), PaySignTypeEnum.MD5.getCode())){
                payNoticeResult.setSign(PaySignUtil.md5Sign(payNoticeResult,config.getSignSecret()));
            } else {
                payNoticeResult.setSign(PaySignUtil.hmacSha256Sign(payNoticeResult,config.getSignSecret()));
            }
        }
        return new ClientNoticeTask()
                .setUrl(orderExtra.getNotifyUrl())
                .setContent(JSONUtil.toJsonStr(payNoticeResult))
                .setType(ClientNoticeTypeEnum.PAY.getType())
                .setSendCount(0)
                .setOrderId(order.getId());
    }


    /**
     * 从redis中执行任务, 通过定时任务触发
     */
    @Async("asyncExecutor")
    public void taskStart(long start, long end){
        // 查询Redis任务表,获取任务
        Set<String> taskIds = redisClient.zrangeByScore(KEY, start, end);
        // 发起一个异步任务,
        for (String taskId : taskIds) {
            this.run(Long.valueOf(taskId));
        }
        // 删除Redis中任务
        redisClient.zremRangeByScore(KEY, start, end);
    }

    /**
     * 执行任务
     */
    private void run(Long taskId){
        LocalDateTime now = LocalDateTime.now();
        // 开启分布式锁
        LockInfo lock = lockTemplate.lock(KEY + ":" + taskId,2000, 50);
        if (Objects.isNull(lock)){
            throw new RepetitiveOperationException("支付同步处理中，请勿重复操作");
        }
        // 查询任务, 进行发送
        ClientNoticeTask task = taskManager.findById(taskId).orElse(null);
        try {
            // 不存在任务直接跳过
            if (Objects.isNull(task)) {
                return;
            }
            this.sendData(task, now);
        } catch (HttpException e) {
            //noinspection DataFlowIssue
            this.failHandler(task, now);
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /**
     * 发送通知数据, 如果失败, 注册下次重发的任务
     */
    private void sendData(ClientNoticeTask task, LocalDateTime now){
        HttpResponse execute = HttpUtil.createPost(task.getUrl())
                .body(task.getContent(), ContentType.JSON.getValue())
                .timeout(5000)
                .execute();
        String body = execute.body();
        // 如果响应值等于SUCCESS, 说明发送成功, 进行成功处理
        if (Objects.equals(body, "SUCCESS")){
            this.successHandler(task);
        } else {
            this.failHandler(task,now);
        }
    }

    /**
     * 成功处理
     */
    private void successHandler(ClientNoticeTask task){
        // 记录成功并保存
        task.setSendCount(task.getSendCount() + 1);
        task.setSuccess(true);
        taskManager.updateById(task);
    }

    /**
     * 失败处理, 首先发送次数+1, 然后注册后推指定时间的重试任务
     */
    private void failHandler(ClientNoticeTask task, LocalDateTime now){
        // 次数+1
        task.setSendCount(task.getSendCount() + 1);
        // 判断发送次数是否未超过15次, 注册任务到redis中
        if (task.getSendCount() < 16){
            // 根据当前次数和时间计算出毫秒值
            Integer delay = DELAY_TIME.get(task.getSendCount());
            long taskTime = LocalDateTimeUtil.timestamp(now) + delay;
            redisClient.zadd(KEY, String.valueOf(task.getId()), taskTime);
        }
        // 更新任务信息
        taskManager.updateById(task);
    }
}
