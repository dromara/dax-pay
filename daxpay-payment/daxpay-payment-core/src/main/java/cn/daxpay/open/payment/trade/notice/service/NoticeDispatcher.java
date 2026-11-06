package cn.daxpay.open.payment.trade.notice.service;

import cn.daxpay.open.payment.trade.notice.command.NoticeDispatchCommand;
import cn.daxpay.open.payment.trade.notice.dao.MchNoticeTaskManager;
import cn.daxpay.open.payment.trade.notice.entity.MchNoticeTask;
import cn.daxpay.open.platform.core.enums.pay.notice.NoticeContentModeEnum;
import cn.daxpay.open.platform.core.enums.pay.notice.NoticeFormatEnum;
import cn.daxpay.open.platform.core.enums.pay.notice.NoticeSourceEnum;
import cn.daxpay.open.platform.core.enums.pay.notice.NoticeTransportEnum;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 商户出站通知派发器
///
/// 决定一次事件生成几条任务、各任务用何种传输通道 [NoticeTransportEnum] + 报文格式 [NoticeFormatEnum]:
/// - 订单级 (ORDER): 走订单传入的 notifyUrl, 恒 HTTP + SYSTEM
/// - 协议级 (PROTOCOL): 协议适配层自带 URL (如易支付), HTTP + 对应 format
@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeDispatcher {

    private final MchNoticeTaskManager taskManager;
    private final NoticeTaskScheduleService scheduleService;

    /// 派发通知意图
    public void dispatch(NoticeDispatchCommand command) {
        if (Objects.isNull(command)
                || StrUtil.isBlank(command.getMchNo())
                || StrUtil.isBlank(command.getAppId())
                || StrUtil.isBlank(command.getEvent())
                || StrUtil.isBlank(command.getBizNo())) {
            log.warn("出站通知命令缺少必要字段, skip");
            return;
        }
        NoticeTransportEnum transport = Objects.isNull(command.getTransport())
                ? NoticeTransportEnum.HTTP : command.getTransport();
        NoticeFormatEnum format = Objects.isNull(command.getFormat())
                ? NoticeFormatEnum.SYSTEM : command.getFormat();
        NoticeContentModeEnum contentMode = Objects.isNull(command.getContentMode())
                ? NoticeContentModeEnum.SNAPSHOT : command.getContentMode();

        // SYSTEM 格式: 订单级 HTTP 回调
        if (format == NoticeFormatEnum.SYSTEM) {
            tryCreate(command, NoticeSourceEnum.ORDER, NoticeTransportEnum.HTTP, format, contentMode, command.getOrderNotifyUrl());
            return;
        }
        // 其它格式 (如 easy_pay): 协议适配层自带 URL, 单条 PROTOCOL 任务
        tryCreate(command, NoticeSourceEnum.PROTOCOL, transport, format, contentMode, command.getProtocolNotifyUrl());
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
}
