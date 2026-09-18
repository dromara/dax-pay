package cn.daxpay.open.channel.easypay.strategy.pay;

import cn.daxpay.open.channel.easypay.client.credential.EasyPaySdkCredential;
import cn.daxpay.open.channel.easypay.service.config.EasyPayConfigAssembler;
import cn.daxpay.open.channel.easypay.service.payment.EasyPayCloseService;
import cn.daxpay.open.payment.strategy.pay.AbsPayCloseStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 易支付支付关闭策略
///
/// 从 trade 读取通道路由参数(channelMchNo / capability),
/// 组装通道凭证(委托 [EasyPayConfigAssembler]), 关闭执行委托给 [EasyPayCloseService]。
///
/// 易支付关单接口在部分平台部署中不一定可用, 异常亦视为已关闭(恒返回 CLOSE, 移植自商业版既定语义)。
@Slf4j
@Service
@RequiredArgsConstructor
public class EasyPayCloseStrategy extends AbsPayCloseStrategy {

    private final EasyPayCloseService easyPayCloseService;

    private final EasyPayConfigAssembler easyPayConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.EASY_PAY;
    }

    @Override
    public CloseTypeEnum doClose(PayStrategyContext context, boolean useCancel) {
        // 直接从 trade 读取路由参数
        PayTrade trade = context.getTrade();

        // 组装通道调用凭证
        EasyPaySdkCredential credential = easyPayConfigAssembler.buildConfig(
                trade.getMchNo(), context.getChannelMchNo(), context.getCapability());

        return easyPayCloseService.close(trade, credential);
    }
}
