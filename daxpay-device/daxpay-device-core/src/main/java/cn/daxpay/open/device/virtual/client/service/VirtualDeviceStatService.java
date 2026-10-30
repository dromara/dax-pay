package cn.daxpay.open.device.virtual.client.service;

import cn.daxpay.open.device.support.PeriodCounterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/// # 虚拟设备日计数服务(Redis INCR, 写侧埋点 + 读侧直读)
///
/// 推送笔数/回执成功笔数按天计数, 每个计数点两个维度:
/// - 全局 `daxpay:device:stat:{push|ackok}:{yyyyMMdd}` —— 统计端点 O(1) 直读, 免 SCAN;
/// - 每设备 `daxpay:device:stat:{push|ackok}:{yyyyMMdd}:{deviceNo}` —— 设备维度聚合。
///
/// 埋点时机: 推送计数在 [VirtualDevicePushClient] 发布成功后(pay.success 含带 printData 的打印单、
/// 纯打印 print.ticket 都算; audio.test 试播不计); 回执计数在 ack 处理中(cmd=pay.success 且 ok=true)。
/// 首次创建时 EXPIRE 30 天(PeriodCounterService Lua 保证 INCR 与 EXPIRE 原子, 不会漏设 TTL)。
@Service
@RequiredArgsConstructor
public class VirtualDeviceStatService {

    /// 推送笔数 key 前缀(全局/每设备共用)
    public static final String PUSH_PREFIX = "daxpay:device:stat:push";

    /// 回执成功笔数 key 前缀(全局/每设备共用)
    public static final String ACK_OK_PREFIX = "daxpay:device:stat:ackok";

    /// 日计数保留时长(首次创建时设置, 过期自然清理)
    public static final Duration STAT_TTL = Duration.ofDays(30);

    /// 日标签格式(yyyyMMdd)
    private static final DateTimeFormatter DAY_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final PeriodCounterService periodCounterService;

    /// 推送成功计数(全局 + 每设备各 +1; 发布成功后调用, audio.test 试播不计)
    public void onPush(String deviceNo) {
        String day = dayLabel(LocalDate.now());
        periodCounterService.increment(PUSH_PREFIX + ":" + day, STAT_TTL);
        periodCounterService.increment(PUSH_PREFIX + ":" + day + ":" + deviceNo, STAT_TTL);
    }

    /// 回执成功计数(全局 + 每设备各 +1; cmd=pay.success 且 ok=true 时调用)
    public void onAckOk(String deviceNo) {
        String day = dayLabel(LocalDate.now());
        periodCounterService.increment(ACK_OK_PREFIX + ":" + day, STAT_TTL);
        periodCounterService.increment(ACK_OK_PREFIX + ":" + day + ":" + deviceNo, STAT_TTL);
    }

    /// 指定前缀/日期的全局计数(读侧; prefix 见 [#PUSH_PREFIX]/[#ACK_OK_PREFIX])
    public long getTotal(String prefix, LocalDate date) {
        return total(prefix, date);
    }

    /// 指定前缀/日期/设备的计数(读侧, 设备维度)
    public long getDeviceDayCount(String prefix, LocalDate date, String deviceNo) {
        Long value = periodCounterService.get(prefix + ":" + dayLabel(date) + ":" + deviceNo);
        return value == null ? 0L : value;
    }

    /// 全局日计数读取
    private long total(String prefix, LocalDate date) {
        Long value = periodCounterService.get(prefix + ":" + dayLabel(date));
        return value == null ? 0L : value;
    }

    /// 日标签(yyyyMMdd, 东八区自然日)
    private String dayLabel(LocalDate date) {
        return date.format(DAY_FORMATTER);
    }
}
