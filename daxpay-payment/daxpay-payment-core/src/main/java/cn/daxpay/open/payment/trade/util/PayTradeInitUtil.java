package cn.daxpay.open.payment.trade.util;

import cn.daxpay.open.payment.trade.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.platform.core.enums.pay.channel.CurrencyEnum;

/// # 资金交易初始化工具
///
/// PROCESSING 态资金凭证公共字段的单一事实源。
/// 调用方负责 tradeType / container / source / channelMchNo / storeNo / provider 等业务差异，本工具只填
/// 「待处理结算类交易」公共规则：
/// - currency = 传入币种(可空, 缺省 cny)
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
    /// @param currency        币种 code(可空, 缺省 cny; 国际通道透传)
    /// @param relationOrderNo 实际上送串（默认与业务单号对齐，特殊通道可后覆盖）
    /// @param source          订单来源（冗余自容器）
    /// @param channelMchNo    通道商户号（冗余自容器/路由结果，可空仅当路由未定时）
    /// @param storeNo         门店号（冗余自容器，可空）
    /// @param title           订单标题（冗余自容器，资金列表/工作台免JOIN）
    /// @param provider        支付渠道（由 method 派生，可空；权威亦可在容器 provider）
    public static PayTrade initProcessing(
            String appId,
            String tradeNo,
            String tradeType,
            Long containerId,
            Long amount,
            String currency,
            String relationOrderNo,
            String source,
            String channelMchNo,
            String storeNo,
            String title,
            String provider) {
        // 币种: 显式透传优先, 缺省 cny(向后兼容国内通道)
        String resolvedCurrency = (currency == null || currency.isBlank())
                ? CurrencyEnum.CNY.getCode() : currency;
        return new PayTrade()
                .setAppId(appId)
                .setTradeNo(tradeNo)
                .setTitle(title)
                .setTradeType(tradeType)
                .setContainerId(containerId)
                .setAmount(amount)
                .setCurrency(resolvedCurrency)
                // 入账金额: 未成功前为 0
                .setPostedAmount(0L)
                .setRefundableBalance(amount)
                .setStatus(PayFundStatusEnum.PROCESSING.getCode())
                .setRelationOrderNo(relationOrderNo)
                .setSource(source)
                // 轻量组织冗余: 权威在业务容器
                .setChannelMchNo(channelMchNo)
                .setStoreNo(storeNo)
                // 支付渠道: 下单时由 method 派生, 渠道分布报表免 JOIN
                .setProvider(provider);
    }
}
