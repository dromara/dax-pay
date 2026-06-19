package cn.daxpay.open.channel.douyin.strategy;

import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.payment.pay.bo.PayTradeResultBo;
import cn.daxpay.open.payment.strategy.pay.AbsPayStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 抖音支付直连支付策略
///
/// 抖音支付直连模式的支付策略（虚类，暂未实现具体支付逻辑）。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class DouyinDirectPayStrategy extends AbsPayStrategy {

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.DOUYIN_PAY;
    }

    @Override
    public PayTradeResultBo doPayHandler() {
        throw new UnsupportedOperationException("抖音支付直连暂未实现");
    }
}
