package cn.daxpay.open.payment.trade.util;

import cn.daxpay.open.payment.trade.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.trade.enums.PayTradeTypeEnum;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;

import java.util.Objects;

/// # 资金交易金额工具
///
/// 入账金额([PayTrade#getPostedAmount])规则的单一事实源：
/// - 资金态非 SUCCESS → 0
/// - tradeType = authorize（预授权冻结）→ 0（仅占用额度，未入账）
/// - 其余结算类动作 SUCCESS → amount
public final class PayTradeAmountUtil {

    private PayTradeAmountUtil() {
    }

    /// 按 tradeType + status + amount 计算入账金额
    public static long resolvePostedAmount(String tradeType, String status, Long amount) {
        if (!Objects.equals(status, PayFundStatusEnum.SUCCESS.getCode())) {
            return 0L;
        }
        if (Objects.equals(tradeType, PayTradeTypeEnum.AUTHORIZE.getCode())) {
            return 0L;
        }
        return amount == null ? 0L : amount;
    }

    /// 按当前 trade 字段计算入账金额
    public static long resolvePostedAmount(PayTrade trade) {
        if (trade == null) {
            return 0L;
        }
        return resolvePostedAmount(trade.getTradeType(), trade.getStatus(), trade.getAmount());
    }

    /// 根据当前 status/tradeType 回写 [PayTrade#setPostedAmount]
    public static void applyPostedAmount(PayTrade trade) {
        if (trade == null) {
            return;
        }
        trade.setPostedAmount(resolvePostedAmount(trade));
    }
}
