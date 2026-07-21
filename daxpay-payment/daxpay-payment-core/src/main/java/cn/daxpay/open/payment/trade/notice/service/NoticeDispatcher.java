package cn.daxpay.open.payment.trade.notice.service;

import cn.daxpay.open.payment.merchant.dao.config.MchAppNotifyConfigManager;
import cn.daxpay.open.payment.merchant.entity.config.MchAppNotifyConfig;
import cn.daxpay.open.payment.trade.notice.command.NoticeDispatchCommand;
import cn.daxpay.open.payment.trade.notice.dao.MchNoticeTaskManager;
import cn.daxpay.open.payment.trade.notice.entity.MchNoticeTask;
import cn.daxpay.open.platform.core.enums.pay.notice.NoticeContentModeEnum;
import cn.daxpay.open.platform.core.enums.pay.notice.NoticeProtocolEnum;
import cn.daxpay.open.platform.core.enums.pay.notice.NoticeSourceEnum;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Objects;

/// # 商户出站通知派发器
///
/// SYSTEM：尝试创建 order + app 两条任务；其它协议：仅 protocol 任务
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
        NoticeProtocolEnum protocol = command.getProtocol() == null
                ? NoticeProtocolEnum.SYSTEM : command.getProtocol();
        NoticeContentModeEnum contentMode = command.getContentMode() == null
                ? NoticeContentModeEnum.SNAPSHOT : command.getContentMode();

        if (protocol == NoticeProtocolEnum.SYSTEM) {
            tryCreate(command, NoticeSourceEnum.ORDER, protocol, contentMode, command.getOrderNotifyUrl());
            tryCreateApp(command, contentMode);
            return;
        }
        tryCreate(command, NoticeSourceEnum.PROTOCOL, protocol, contentMode, command.getProtocolNotifyUrl());
    }

    /// 应用级订阅
    private void tryCreateApp(NoticeDispatchCommand command, NoticeContentModeEnum contentMode) {
        MchAppNotifyConfig config = notifyConfigManager.findByAppId(command.getAppId()).orElse(null);
        if (config == null || !Boolean.TRUE.equals(config.getStatus()) || StrUtil.isBlank(config.getNotifyUrl())) {
            return;
        }
        if (!matchSubscribed(config.getSubscribedEvents(), command.getEvent())) {
            return;
        }
        tryCreate(command, NoticeSourceEnum.APP, NoticeProtocolEnum.SYSTEM, contentMode, config.getNotifyUrl());
    }

    /// 创建任务（幂等）并投递
    private void tryCreate(NoticeDispatchCommand command, NoticeSourceEnum source,
                           NoticeProtocolEnum protocol, NoticeContentModeEnum contentMode, String url) {
        if (StrUtil.isBlank(url)) {
            log.debug("出站通知跳过(无URL): event={}, bizNo={}, source={}",
                    command.getEvent(), command.getBizNo(), source.getCode());
            return;
        }
        var existing = taskManager.findByIdempotentKey(
                command.getMchNo(), command.getAppId(), command.getEvent(),
                command.getBizNo(), protocol.getCode(), source.getCode());
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
                .setProtocol(protocol.getCode())
                .setSource(source.getCode())
                .setContentMode(contentMode.getCode())
                .setContent(command.getContentOrRef())
                .setUrl(url)
                .setSuccess(false)
                .setSendCount(0)
                .setDelayCount(0);
        taskManager.save(task);
        scheduleService.scheduleImmediateAfterCommit(task.getId());
        log.info("注册出站通知: event={}, bizNo={}, protocol={}, source={}",
                command.getEvent(), command.getBizNo(), protocol.getCode(), source.getCode());
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
