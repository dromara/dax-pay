package cn.daxpay.open.channel.adapay.util;

import cn.hutool.core.util.StrUtil;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/// # Adapay 时间解析工具
///
/// 统一解析汇付 Adapay 返回的时间字段为东八区 OffsetDateTime:
/// - created_time: 13 位毫秒时间戳(支付对象创建时间, 下单/条码支付成功场景)
/// - end_time: 14 位 yyyyMMddHHmmss(支付成功时间, 同步查询场景, 无时区字面量按东八区)
/// - 其他: ISO 带时区字符串
///
/// 解析失败返回 null(由回调/同步兜底补全时间)。
public final class AdapayDateUtil {

    /// Adapay ISO 时间格式(带时区, 如 2026-07-06T11:15:48+08:00)
    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    /// 支付成功时间格式(无时区, 如 20210706111548)
    private static final DateTimeFormatter PURE_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private AdapayDateUtil() {
    }

    /// 解析Adapay 时间字符串为东八区 OffsetDateTime
    public static OffsetDateTime parse(String time) {
        if (StrUtil.isBlank(time)) {
            return null;
        }
        try {
            // 13 位毫秒时间戳(created_time)
            if (time.matches("\\d{13}")) {
                return OffsetDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(time)), ZoneOffset.ofHours(8));
            }
            // 14 位 yyyyMMddHHmmss(end_time, 无时区字面量, 按东八区解析)
            if (time.matches("\\d{14}")) {
                return LocalDateTime.parse(time, PURE_DATETIME_FORMATTER).atOffset(ZoneOffset.ofHours(8));
            }
            // ISO 带时区格式
            return OffsetDateTime.parse(time, ISO_FORMATTER);
        } catch (Exception e) {
            return null;
        }
    }
}
