package cn.daxpay.open.payment.trade.notice.service;

import org.springframework.stereotype.Component;

import java.util.Map;

/// # 商户出站通知重试策略
///
/// 仿微信通知节奏，约 16 次延时重试，合计约 24h+
/// 间隔：15s/15s/30s/3m/10m/20m/30m×3/60m/3h×3/6h×…
@Component
public class NoticeRetryPolicy {

    /// 最大延时重试次数
    public static final int MAX_DELAY_COUNT = 16;

    /// key: 延时次数(1起), value: 下次间隔秒数
    private static final Map<Integer, Integer> DELAY_SECONDS = Map.ofEntries(
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

    /// 是否还可继续延时重试（delayCount 为已完成的延时次数）
    public boolean canRetry(int delayCount) {
        return delayCount < MAX_DELAY_COUNT;
    }

    /// 获取下一次延时间隔秒数
    ///
    /// @param nextDelayCount 即将执行的延时序号（1..16）
    public int nextDelaySeconds(int nextDelayCount) {
        return DELAY_SECONDS.getOrDefault(nextDelayCount, 6 * 60 * 60);
    }
}
