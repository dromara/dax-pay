package cn.daxpay.open.channel.hmpay.strategy;

import cn.daxpay.open.channel.hmpay.client.credential.HmpaySdkCredential;
import cn.daxpay.open.channel.hmpay.service.isv.HmpayIsvConfigAssembler;
import cn.daxpay.open.channel.hmpay.service.payment.HmpayCloseService;
import cn.daxpay.open.payment.core.strategy.pay.AbsPayCloseStrategy;
import cn.daxpay.open.payment.core.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.core.trade.order.entity.PayTrade;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 河马付服务商支付关闭策略
///
/// 注意: 杉德仅提供关单接口(trade.close), 无撤销接口(useCancel 参数由 service 层忽略)。
@Slf4j
@Service
@RequiredArgsConstructor
public class HmpayCloseStrategy extends AbsPayCloseStrategy {

    private final HmpayCloseService hmpayCloseService;
    private final HmpayIsvConfigAssembler hmpayIsvConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.HM_PAY;
    }

    @Override
    public CloseTypeEnum doClose(PayStrategyContext context, boolean useCancel) {
        PayTrade trade = context.getTrade();
        // 直接从 trade 读取路由参数, 不再需要 container 中间层
        HmpaySdkCredential credential = hmpayIsvConfigAssembler.buildConfig(
                trade.getMchNo(), context.getChannelMchNo(), context.getCapability());

        return hmpayCloseService.close(trade, credential);
    }
}
