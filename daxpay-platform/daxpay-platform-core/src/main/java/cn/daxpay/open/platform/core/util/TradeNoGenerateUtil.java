package cn.daxpay.open.platform.core.util;

import cn.hutool.core.util.IdUtil;
import lombok.Setter;

/// # 各类型订单号生成工具类
///
/// 格式: {环境前缀}{3字母类型}{雪花ID}, 例如 PRODPAY1872635419283456
/// 类型使用3字母缩写: ORD(业务单) PAY(资金交易) REF(退款)
/// orderNo 与 tradeNo 独立生成, 禁止混用
@Setter
public final class TradeNoGenerateUtil {

    /// 环境前缀, 例如 PROD / DEV 等, 为空时不追加
    private static String env;

    /// 生成平台业务单号(orderNo, 容器维度, 默认上送通道)
    public static String order() {
        return prefix() + "ORD" + IdUtil.getSnowflakeNextId();
    }

    /// 生成资金交易号(tradeNo, 资金动作维度)
    public static String pay() {
        return prefix() + "PAY" + IdUtil.getSnowflakeNextId();
    }

    /// 生成退款单号
    public static String refund() {
        return prefix() + "REF" + IdUtil.getSnowflakeNextId();
    }

    /// 生成转账单号
    public static String transfer() {
        return prefix() + "TRF" + IdUtil.getSnowflakeNextId();
    }

    /// 生成分账单号
    public static String alloc() {
        return prefix() + "ALC" + IdUtil.getSnowflakeNextId();
    }

    /// 拼接环境前缀, 不添加分隔符, 部分通道不允许特殊符号
    private static String prefix() {
        return env == null ? "" : env;
    }
}
