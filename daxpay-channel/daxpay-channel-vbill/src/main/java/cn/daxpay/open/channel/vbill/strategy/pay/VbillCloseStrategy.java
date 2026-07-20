package cn.daxpay.open.channel.vbill.strategy.pay;

import cn.daxpay.open.channel.vbill.client.credential.VbillSdkCredential;
import cn.daxpay.open.channel.vbill.service.isv.VbillIsvConfigAssembler;
import cn.daxpay.open.channel.vbill.service.payment.VbillCloseService;
import cn.daxpay.open.payment.strategy.pay.AbsPayCloseStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 随行付服务商支付关闭策略
///
/// 从交易凭证读取通道路由参数(channelMchNo / capability),
/// 组装通道凭证(委托 [VbillIsvConfigAssembler]), 关闭执行委托给 [VbillCloseService]。
///
/// 注意: 随行付仅提供关单接口(`/query/close`), 无撤销接口(useCancel 参数由 service 层忽略)。
@Slf4j
@Service
@RequiredArgsConstructor
public class VbillCloseStrategy extends AbsPayCloseStrategy {

    private final VbillCloseService vbillCloseService;
    private final VbillIsvConfigAssembler vbillIsvConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.VBILL_PAY;
    }

    @Override
    public CloseTypeEnum doClose(PayStrategyContext context, boolean useCancel) {
        PayTrade trade = context.getTrade();
        // 直接从 trade 读取路由参数, 不再需要 container 中间层

        // 组装通道调用凭证
        VbillSdkCredential credential = vbillIsvConfigAssembler.buildConfig(
                trade.getMchNo(), context.getChannelMchNo(), context.getCapability());

        return vbillCloseService.close(trade, credential, useCancel);
    }
}
