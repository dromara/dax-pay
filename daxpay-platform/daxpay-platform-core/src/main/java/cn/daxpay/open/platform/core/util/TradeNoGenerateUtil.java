package cn.daxpay.open.platform.core.util;

import cn.hutool.core.util.IdUtil;
import lombok.Setter;

/// # 各类型订单号生成工具类
///
/// 格式: {环境前缀}{3字母类型}{雪花ID}, 例如 PRODPAY1872635419283456
/// 类型使用3字母缩写: PAY(支付) ALL(分账) REP(修复)
@Setter
public final class TradeNoGenerateUtil {

    /// 环境前缀, 例如 PROD / DEV 等, 为空时不追加
    private static String env;

    /// 生成支付订单号
    public static String pay() {
        return prefix() + "PAY" + IdUtil.getSnowflakeNextId();
    }

    /// 生成退款单号
    public static String refund() {
        return prefix() + "REF" + IdUtil.getSnowflakeNextId();
    }

    /// 生成修复单号
    public static String repair() {
        return prefix() + "REP" + IdUtil.getSnowflakeNextId();
    }

    /// 拼接环境前缀, 不添加分隔符, 部分通道不允许特殊符号
    private static String prefix() {
        return env == null ? "" : env;
    }
}
