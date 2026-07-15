package cn.daxpay.open.payment.trade.util;

import cn.daxpay.open.payment.trade.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.platform.core.enums.pay.channel.CurrencyEnum;

/// # 资金交易初始化工具
///
/// PROCESSING 态资金凭证公共字段的单一事实源。
/// 调用方负责 tradeType / container / source 等业务差异，本工具只填「待处理结算类交易」公共规则：
/// - currency = CNY
/// - postedAmount = 0（成功后由 [cn.daxpay.open.payment.trade.runtime.service.pay.common.PayUniHandleService]
///   / [PayTradeAmountUtil] 按规则回写）
/// - refundableBalance = amount
/// - status = PROCESSING
public final class PayTradeInitUtil {

    private PayTradeInitUtil() {
    }

    /// 组装未落库的 PROCESSING 资金凭证
    ///
    /// @param appId           应用 ID
    /// @param tradeNo         资金交易号（调用方生成）
    /// @param tradeType       交易形态（normal / gateway 等）
    /// @param containerId     关联容器 ID（须已落库）
    /// @param amount          交易金额
    /// @param relationOrderNo 实际上送串（默认与业务单号对齐，特殊通道可后覆盖）
    /// @param source          订单来源（冗余自容器）
    public static PayTrade initProcessing(
            String appId,
            String tradeNo,
            String tradeType,
            Long containerId,
            Long amount,
            String relationOrderNo,
            String source) {
        return new PayTrade()
                .setAppId(appId)
                .setTradeNo(tradeNo)
                .setTradeType(tradeType)
                .setContainerId(containerId)
                .setAmount(amount)
                .setCurrency(CurrencyEnum.CNY.getCode())
                // 入账金额: 未成功前为 0
                .setPostedAmount(0L)
                .setRefundableBalance(amount)
                .setStatus(PayFundStatusEnum.PROCESSING.getCode())
                .setRelationOrderNo(relationOrderNo)
                .setSource(source);
    }
}
