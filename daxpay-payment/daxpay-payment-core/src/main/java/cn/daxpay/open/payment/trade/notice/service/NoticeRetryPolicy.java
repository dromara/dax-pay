package cn.daxpay.open.payment.trade.notice.service;

import cn.daxpay.open.platform.core.enums.pay.notice.NoticeTransportEnum;
import org.springframework.stereotype.Component;

import java.util.Map;

/// # 商户出站通知重试策略
///
/// 按传输通道 [NoticeTransportEnum] 区分:
/// - HTTP: 仿微信通知节奏, 约 16 次延时重试, 合计约 24h+ (业务 ACK 失败兜底)
/// - MQ:   仅 3 次短间隔重试 (publish 失败兜底; 消费侧失败由商户/MQ 自身负责)
@Component
public class NoticeRetryPolicy {

    /// HTTP 最大延时重试次数
    public static final int MAX_DELAY_COUNT_HTTP = 16;

    /// MQ 最大延时重试次数
    public static final int MAX_DELAY_COUNT_MQ = 3;

    /// HTTP 间隔 (仿微信): 15s/15s/30s/3m/10m/20m/30m×3/60m/3h×3/6h×…
    private static final Map<Integer, Integer> DELAY_SECONDS_HTTP = Map.ofEntries(
            Map.entry(1, 15),
            Map.entry(2, 15),
            Map.entry(3, 30),
            Map.entry(4, 3 * 60),
            Map.entry(5, 10 * 60),
            Map.entry(6, 20 * 60),
            Map.entry(7, 30 * 60),
            Map.entry(8, 30 * 60),
            Map.entry(9, 30 * 60),
            Map.entry(10, 60 * 60),
            Map.entry(11, 3 * 60 * 60),
            Map.entry(12, 3 * 60 * 60),
            Map.entry(13, 3 * 60 * 60),
            Map.entry(14, 6 * 60 * 60),
            Map.entry(15, 6 * 60 * 60),
            Map.entry(16, 6 * 60 * 60)
    );

    /// MQ 间隔: 10s/30s/60s
    private static final Map<Integer, Integer> DELAY_SECONDS_MQ = Map.of(
            1, 10,
            2, 30,
            3, 60
    );

    /// 各传输通道最大延时重试次数
    public int maxDelayCount(String transport) {
        return NoticeTransportEnum.MQ.getCode().equals(transport) ? MAX_DELAY_COUNT_MQ : MAX_DELAY_COUNT_HTTP;
    }

    /// 是否还可继续延时重试（delayCount 为已完成的延时次数）
    public boolean canRetry(String transport, int delayCount) {
        return delayCount < maxDelayCount(transport);
    }

    /// 获取下一次延时间隔秒数
    ///
    /// @param nextDelayCount 即将执行的延时序号 (1..max)
    public int nextDelaySeconds(String transport, int nextDelayCount) {
        if (NoticeTransportEnum.MQ.getCode().equals(transport)) {
            return DELAY_SECONDS_MQ.getOrDefault(nextDelayCount, 60);
        }
        return DELAY_SECONDS_HTTP.getOrDefault(nextDelayCount, 6 * 60 * 60);
    }
}
