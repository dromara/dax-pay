package cn.daxpay.open.channel.hkrt.strategy;

import cn.daxpay.open.channel.hkrt.client.credential.HkrtSdkCredential;
import cn.daxpay.open.channel.hkrt.service.isv.HkrtIsvConfigAssembler;
import cn.daxpay.open.channel.hkrt.service.payment.HkrtCloseService;
import cn.daxpay.open.payment.core.strategy.pay.AbsPayCloseStrategy;
import cn.daxpay.open.payment.core.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.core.trade.order.entity.PayTrade;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 海科融通服务商支付关闭策略
///
/// 从上下文容器读取通道路由参数(channelMchNo / capability),
/// 组装通道凭证(委托 [HkrtIsvConfigAssembler]), 关闭执行委托给 [HkrtCloseService]。
///
/// 注意: 海科融通仅提供关单接口, 无撤销接口(useCancel 参数由 service 层忽略)。
@Slf4j
@Service
@RequiredArgsConstructor
public class HkrtCloseStrategy extends AbsPayCloseStrategy {

    private final HkrtCloseService hkrtCloseService;
    private final HkrtIsvConfigAssembler hkrtIsvConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.HKRT_PAY;
    }

    @Override
    public CloseTypeEnum doClose(PayStrategyContext context, boolean useCancel) {
        // 直接从 trade 读取路由参数, 不再需要 container 中间层
        PayTrade trade = context.getTrade();

        // 组装通道调用凭证
        HkrtSdkCredential credential = hkrtIsvConfigAssembler.buildConfig(
                trade.getMchNo(), context.getChannelMchNo(), context.getCapability());

        return hkrtCloseService.close(trade, credential, useCancel, context.getClientIp());
    }
}
