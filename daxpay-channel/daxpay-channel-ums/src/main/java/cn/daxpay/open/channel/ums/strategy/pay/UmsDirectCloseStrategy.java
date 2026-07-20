package cn.daxpay.open.channel.ums.strategy.pay;

import cn.daxpay.open.channel.ums.client.credential.UmsSdkCredential;
import cn.daxpay.open.channel.ums.service.direct.UmsDirectConfigAssembler;
import cn.daxpay.open.channel.ums.service.payment.close.UmsCloseService;
import cn.daxpay.open.payment.strategy.pay.AbsPayCloseStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 银联商务直连支付关闭策略
@Slf4j
@Service
@RequiredArgsConstructor
public class UmsDirectCloseStrategy extends AbsPayCloseStrategy {

    private final UmsCloseService umsCloseService;
    private final UmsDirectConfigAssembler umsDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.UMS_QRCODE;
    }

    @Override
    public CloseTypeEnum doClose(PayStrategyContext context, boolean useCancel) {
        // 直接从 trade 读取路由参数, 不再需要 container 中间层
        PayTrade trade = context.getTrade();

        UmsSdkCredential credential = umsDirectConfigAssembler.buildConfig(
                trade.getMchNo(), context.getChannelMchNo(), context.getCapability());
        return umsCloseService.close(trade, credential, useCancel);
    }
}
