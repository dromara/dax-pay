package cn.daxpay.open.payment.trade.notice.service;

import cn.daxpay.open.payment.merchant.dao.config.MchAppNotifyConfigManager;
import cn.daxpay.open.payment.merchant.entity.config.MchAppNotifyConfig;
import cn.daxpay.open.payment.trade.notice.command.NoticeDispatchCommand;
import cn.daxpay.open.payment.trade.notice.dao.MchNoticeTaskManager;
import cn.daxpay.open.payment.trade.notice.entity.MchNoticeTask;
import cn.daxpay.open.payment.trade.runtime.mq.PayArtemisConstants;
import cn.daxpay.open.platform.core.enums.pay.notice.NoticeContentModeEnum;
import cn.daxpay.open.platform.core.enums.pay.notice.NoticeFormatEnum;
import cn.daxpay.open.platform.core.enums.pay.notice.NoticeSourceEnum;
import cn.daxpay.open.platform.core.enums.pay.notice.NoticeTransportEnum;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Objects;

/// # 商户出站通知派发器
///
/// 决定一次事件生成几条任务、各任务用何种传输通道 [NoticeTransportEnum] + 报文格式 [NoticeFormatEnum]:
/// - 订单级 (ORDER): 走订单传入的 notifyUrl, 恒 HTTP + SYSTEM
/// - 应用级 (APP):   走 [MchAppNotifyConfig], 按 notifyWay(http/mq) 决定 transport
/// - 协议级 (PROTOCOL): 协议适配层自带 URL (如易支付), HTTP + 对应 format
@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeDispatcher {

    private final MchNoticeTaskManager taskManager;
    private final MchAppNotifyConfigManager notifyConfigManager;
    private final NoticeTaskScheduleService scheduleService;

    /// 派发通知意图
    public void dispatch(NoticeDispatchCommand command) {
        if (command == null
                || StrUtil.isBlank(command.getMchNo())
                || StrUtil.isBlank(command.getAppId())
                || StrUtil.isBlank(command.getEvent())
                || StrUtil.isBlank(command.getBizNo())) {
            log.warn("出站通知命令缺少必要字段, skip");
            return;
        }
        NoticeTransportEnum transport = command.getTransport() == null
                ? NoticeTransportEnum.HTTP : command.getTransport();
        NoticeFormatEnum format = command.getFormat() == null
                ? NoticeFormatEnum.SYSTEM : command.getFormat();
        NoticeContentModeEnum contentMode = command.getContentMode() == null
                ? NoticeContentModeEnum.SNAPSHOT : command.getContentMode();

        // SYSTEM 格式: 订单级 + 应用级 双轨并行 (订单级恒 HTTP)
        if (format == NoticeFormatEnum.SYSTEM) {
            tryCreate(command, NoticeSourceEnum.ORDER, NoticeTransportEnum.HTTP, format, contentMode, command.getOrderNotifyUrl());
            tryCreateApp(command, contentMode);
            return;
        }
        // 其它格式 (如 easy_pay): 协议适配层自带 URL, 单条 PROTOCOL 任务
        tryCreate(command, NoticeSourceEnum.PROTOCOL, transport, format, contentMode, command.getProtocolNotifyUrl());
    }

    /// 应用级订阅: 按 [MchAppNotifyConfig].notifyWay 决定传输通道
    private void tryCreateApp(NoticeDispatchCommand command, NoticeContentModeEnum contentMode) {
        MchAppNotifyConfig config = notifyConfigManager.findByAppId(command.getAppId()).orElse(null);
        if (config == null || !Boolean.TRUE.equals(config.getStatus())) {
            return;
        }
        if (!matchSubscribed(config.getSubscribedEvents(), command.getEvent())) {
            return;
        }
        // 按配置的通知方式决定传输通道与目标地址
        if (NoticeTransportEnum.MQ.getCode().equals(config.getNotifyWay())) {
            // MQ 方式: 目标地址为按应用隔离的 Topic
            String topic = PayArtemisConstants.MCH_NOTICE_TOPIC_PREFIX + "." + command.getAppId();
            tryCreate(command, NoticeSourceEnum.APP, NoticeTransportEnum.MQ, NoticeFormatEnum.SYSTEM, contentMode, topic);
        } else {
            // HTTP 方式(默认): 目标地址为配置的回调 URL
            if (StrUtil.isBlank(config.getNotifyUrl())) {
                return;
            }
            tryCreate(command, NoticeSourceEnum.APP, NoticeTransportEnum.HTTP, NoticeFormatEnum.SYSTEM, contentMode, config.getNotifyUrl());
        }
    }

    /// 创建任务（幂等）并投递
    private void tryCreate(NoticeDispatchCommand command, NoticeSourceEnum source,
                           NoticeTransportEnum transport, NoticeFormatEnum format,
                           NoticeContentModeEnum contentMode, String url) {
        if (StrUtil.isBlank(url)) {
            log.debug("出站通知跳过(无目标地址): event={}, bizNo={}, source={}",
                    command.getEvent(), command.getBizNo(), source.getCode());
            return;
        }
        var existing = taskManager.findByIdempotentKey(
                command.getMchNo(), command.getAppId(), command.getEvent(),
                command.getBizNo(), transport.getCode(), format.getCode(), source.getCode());
        if (existing.isPresent()) {
            log.info("出站通知任务已存在, 跳过创建: event={}, bizNo={}, source={}",
                    command.getEvent(), command.getBizNo(), source.getCode());
            return;
        }
        MchNoticeTask task = new MchNoticeTask();
        // 运营端写 MchBaseEntity 必须显式 mchNo
        task.setMchNo(command.getMchNo());
        task.setAppId(command.getAppId())
                .setBizId(command.getBizId())
                .setBizNo(command.getBizNo())
                .setEvent(command.getEvent())
                .setTransport(transport.getCode())
                .setFormat(format.getCode())
                .setSource(source.getCode())
                .setContentMode(contentMode.getCode())
                .setContent(command.getContentOrRef())
                .setUrl(url)
                .setSuccess(false)
                .setSendCount(0)
                .setDelayCount(0);
        taskManager.save(task);
        scheduleService.scheduleImmediateAfterCommit(task.getId());
        log.info("注册出站通知: event={}, bizNo={}, transport={}, format={}, source={}",
                command.getEvent(), command.getBizNo(), transport.getCode(), format.getCode(), source.getCode());
    }

    /// 订阅匹配：精确事件码，或前缀（pay 匹配 pay.*）
    public static boolean matchSubscribed(String subscribedEvents, String event) {
        if (StrUtil.isBlank(subscribedEvents) || StrUtil.isBlank(event)) {
            return false;
        }
        return Arrays.stream(subscribedEvents.split(","))
                .map(String::trim)
                .filter(StrUtil::isNotBlank)
                .anyMatch(token -> Objects.equals(token, event)
                        || event.startsWith(token + "."));
    }
}
