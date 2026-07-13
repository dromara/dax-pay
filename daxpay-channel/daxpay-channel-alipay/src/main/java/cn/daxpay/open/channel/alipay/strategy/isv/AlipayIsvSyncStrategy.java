package cn.daxpay.open.channel.alipay.strategy.isv;

import cn.daxpay.open.channel.alipay.client.credential.AlipaySdkCredential;
import cn.daxpay.open.channel.alipay.service.isv.AlipayIsvConfigAssembler;
import cn.daxpay.open.channel.alipay.service.payment.sync.AlipaySyncService;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.strategy.sync.AbsSyncPayOrderStrategy;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 支付宝服务商支付同步策略
///
/// 支付宝服务商模式(ProductEnum.ALIPAY_ISV)下的支付同步策略。
/// 组装通道凭证(委托 [AlipayIsvConfigAssembler]), 同步执行委托给 [AlipaySyncService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayIsvSyncStrategy extends AbsSyncPayOrderStrategy {

    private final AlipaySyncService alipaySyncService;
    private final AlipayIsvConfigAssembler alipayIsvConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.ALIPAY_ISV;
    }

    @Override
    public PaySyncResultBo doSync(PayStrategyContext context) {
        PayTrade trade = context.getTrade();
        // 组装服务商模式通道调用凭证(含应用授权令牌)
        AlipaySdkCredential credential = alipayIsvConfigAssembler.buildConfig(trade.getMchNo());
        return alipaySyncService.sync(trade, credential);
    }
}
