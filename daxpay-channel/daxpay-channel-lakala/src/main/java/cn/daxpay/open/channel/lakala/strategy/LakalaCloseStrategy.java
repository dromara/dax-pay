package cn.daxpay.open.channel.lakala.strategy;

import cn.daxpay.open.channel.lakala.client.credential.LakalaSdkCredential;
import cn.daxpay.open.channel.lakala.service.isv.LakalaIsvConfigAssembler;
import cn.daxpay.open.channel.lakala.service.payment.LakalaCloseService;
import cn.daxpay.open.payment.strategy.pay.AbsPayCloseStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 拉卡拉服务商支付关闭策略
///
/// 从 trade 读取通道路由参数(channelMchNo / capability),
/// 组装通道凭证(委托 [LakalaIsvConfigAssembler]), 关闭执行委托给 [LakalaCloseService]。
///
/// 注意: 拉卡拉仅提供关单接口, 无撤销接口(useCancel 参数由 service 层忽略)。
@Slf4j
@Service
@RequiredArgsConstructor
public class LakalaCloseStrategy extends AbsPayCloseStrategy {

    private final LakalaCloseService lakalaCloseService;
    private final LakalaIsvConfigAssembler lakalaIsvConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.LAKALA_PAY;
    }

    @Override
    public CloseTypeEnum doClose(PayStrategyContext context, boolean useCancel) {
        // 直接从 trade 读取路由参数, 不再需要 container 中间层
        PayTrade trade = context.getTrade();

        // 组装通道调用凭证
        LakalaSdkCredential credential = lakalaIsvConfigAssembler.buildConfig(
                trade.getMchNo(), context.getChannelMchNo(), context.getCapability());

        return lakalaCloseService.close(trade, credential, useCancel, context.getClientIp());
    }
}
