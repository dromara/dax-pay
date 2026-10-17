package cn.daxpay.open.channel.lakala.strategy.pay;

import cn.daxpay.open.channel.lakala.client.credential.MossSdkCredential;
import cn.daxpay.open.channel.lakala.service.isv.LakalaMossConfigAssembler;
import cn.daxpay.open.channel.lakala.service.moss.LakalaMossCloseService;
import cn.daxpay.open.payment.strategy.pay.AbsPayCloseStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 拉卡拉 MOSS 支付关闭策略
///
/// 从 trade 读取通道路由参数(channelMchNo / capability),
/// 组装通道凭证(委托 [LakalaMossConfigAssembler]), 关闭执行委托给 [LakalaMossCloseService]。
///
/// 注意: MOSS 无远端关单接口, 关单为「查单确认非成功即视为关闭」语义(useCancel 参数由 service 层忽略)。
@Slf4j
@Service
@RequiredArgsConstructor
public class LakalaMossCloseStrategy extends AbsPayCloseStrategy {

    private final LakalaMossCloseService lakalaMossCloseService;
    private final LakalaMossConfigAssembler lakalaMossConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.LAKALA_MOSS;
    }

    @Override
    public CloseTypeEnum doClose(PayStrategyContext context, boolean useCancel) {
        // 直接从 trade 读取路由参数, 不再需要 container 中间层
        PayTrade trade = context.getTrade();

        // 组装通道调用凭证
        MossSdkCredential credential = lakalaMossConfigAssembler.buildConfig(
                trade.getMchNo(), context.getChannelMchNo(), context.getCapability());

        return lakalaMossCloseService.close(trade, credential, useCancel);
    }
}
