package cn.daxpay.open.platform.core.util;

import lombok.experimental.UtilityClass;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/// # java8 时间工具类
///
/// 所有时间基于 UTC，与数据库 timestamptz 列保持一致
/// 前端展示时由客户端按用户时区转换; 无前端转换环节的终点展示(导出文件/小票/对外报文)用 [#formatByZone]
@UtilityClass
public class DateTimeUtil {

    /// 业务时区(东八区)
    ///
    /// 两个用途: ① 无时区时间字面量的解析基准(见 common-json 的 OffsetDateTimeParseUtil);
    /// ② 终端展示时区缺失时的兜底。
    public static final ZoneId ZONE_CST = ZoneId.of("Asia/Shanghai");

    /// 终端展示用时间格式(秒精度)
    ///
    /// 该 formatter 本身不带时区, 必须配合 [#formatByZone] 使用; 直接 format(OffsetDateTime) 会输出 UTC 墙上时间。
    public static final DateTimeFormatter DISPLAY_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /// 小于
    public boolean lt(OffsetDateTime now, OffsetDateTime next) {
        return now.toEpochSecond() < next.toEpochSecond();
    }

    /// 大于等于
    public boolean ge(OffsetDateTime now, OffsetDateTime next) {
        return now.toEpochSecond() >= next.toEpochSecond();
    }

    /// 小于等于
    public boolean le(OffsetDateTime now, OffsetDateTime next) {
        return now.toEpochSecond() <= next.toEpochSecond();
    }

    /// 按指定时区格式化时间为展示字符串
    ///
    /// 仅供**终点展示**场景(Excel 导出、打印小票、对外报文等没有前端再转换的机会), 空值返回空串便于表格留白。
    /// 接口出入参请勿使用: 传输层统一为 UTC/带偏移的 ISO 串, 由前端按用户时区渲染。
    public String formatByZone(OffsetDateTime time, ZoneId zone) {
        if (Objects.isNull(time)) {
            return "";
        }
        ZoneId targetZone = Objects.isNull(zone) ? ZONE_CST : zone;
        return DISPLAY_DATE_TIME_FORMATTER.withZone(targetZone).format(time);
    }

}
