package cn.daxpay.open.channel.ums.util;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/// # 银联商务通道日期工具(主应用侧)
///
/// 银联商务属国内通道, 接口返回的时间字段(payTime / finishTime 等)
/// 一律为东八区(GMT+8)本地时间字面量(yyyy-MM-dd HH:mm:ss), 无时区后缀。
///
/// 历史问题: 早期直接用 `OffsetDateTime.parse(str, ofPattern("yyyy-MM-dd HH:mm:ss"))`,
/// 因 formatter 不含时区字段、解析出的 TemporalAccessor 缺 OFFSET_SECONDS,
/// 触发 `Unable to obtain OffsetDateTime from TemporalAccessor` 异常。
///
/// 本工具显式按东八区(+08:00)将本地时间字面量转换为带偏移的绝对时间 [OffsetDateTime],
/// 与子应用(channel-one)侧 [UmsDateUtil] 的时区处理姿势一致, 不再依赖运行环境时区。
public final class UmsDateUtil {

    /// 银联商务时间格式(yyyy-MM-dd HH:mm:ss)
    private static final DateTimeFormatter DATETIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /// 东八区偏移(北京时间)
    private static final ZoneOffset CST = ZoneOffset.ofHours(8);

    private UmsDateUtil() {
    }

    /// 解析银联商务东八区时间字面量为带偏移的 [OffsetDateTime]
    ///
    /// 通道返回的 `yyyy-MM-dd HH:mm:ss` 是东八区本地时间, 先按 [LocalDateTime] 解析,
    /// 再附加 +08:00 偏移得到绝对时间点, 入库(timestamptz)时区正确。
    /// 传入 null 或空白字符串时返回 null。
    public static OffsetDateTime parseCst(String text) {
        if (text == null || text.isBlank()) {
            return null;
        }
        return LocalDateTime.parse(text, DATETIME_FORMATTER).atOffset(CST);
    }
}
