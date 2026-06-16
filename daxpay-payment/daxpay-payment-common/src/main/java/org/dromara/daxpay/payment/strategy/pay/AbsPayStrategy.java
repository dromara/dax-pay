package org.dromara.daxpay.payment.strategy.pay;

import org.dromara.daxpay.payment.common.strategy.PaymentStrategy;
import org.dromara.daxpay.payment.pay.bo.PayTradeResultBo;
import org.dromara.daxpay.payment.pay.order.entity.PayTrade;
import org.dromara.daxpay.payment.unipay.param.trade.pay.PayParam;
import lombok.Getter;
import lombok.Setter;

/// # 抽象支付策略基类
///
@Getter
@Setter
public abstract class AbsPayStrategy implements PaymentStrategy {

    /// 资金交易凭证
    private PayTrade trade = null;

    /// 支付参数
    private PayParam payParam = null;

    /// 初始化支付的参数
    public void initPayParam(PayTrade trade, PayParam payParam) {
        this.trade = trade;
        this.payParam = payParam;
    }

    /// 支付前处理 包含必要的校验以及对当前通道支付配置信息的初始化
    /// 出现错误不会保存相关信息
    public void doBeforePayHandler(){
    }

    /// 支付操作
    /// 出现错误会保存相关信息
    public abstract PayTradeResultBo doPayHandler();

}

