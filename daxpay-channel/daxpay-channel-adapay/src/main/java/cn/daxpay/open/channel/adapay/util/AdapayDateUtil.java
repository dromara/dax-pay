package cn.daxpay.open.channel.adapay.util;

import cn.hutool.core.util.StrUtil;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/// # 汇付天下时间解析工具
///
/// 汇付返回的 created_time 字段格式不统一(可能为毫秒时间戳或 ISO 字符串),
/// 本工具统一解析为东八区 OffsetDateTime, 解析失败返回 null(由回调/同步兜底补全时间)。
public final class AdapayDateUtil {

    /// 汇付 ISO 时间格式(带时区, 如 2026-07-06T11:15:48+08:00)
    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    private AdapayDateUtil() {
    }

    /// 解析汇付时间字符串为东八区 OffsetDateTime
    public static OffsetDateTime parse(String time) {
        if (StrUtil.isBlank(time)) {
            return null;
        }
        try {
            // 纯数字视为毫秒时间戳
            if (time.matches("\\d+")) {
                return OffsetDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(time)), ZoneOffset.ofHours(8));
            }
            // ISO 带时区格式
            return OffsetDateTime.parse(time, ISO_FORMATTER);
        } catch (Exception e) {
            return null;
        }
    }
}
