package cn.daxpay.open.channel.fuyou.strategy.pay;

import cn.daxpay.open.channel.fuyou.client.credential.FuyouSdkCredential;
import cn.daxpay.open.channel.fuyou.service.isv.FuyouIsvConfigAssembler;
import cn.daxpay.open.channel.fuyou.service.payment.FuyouCloseService;
import cn.daxpay.open.payment.strategy.pay.AbsPayCloseStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 富友合作方支付关闭策略
///
/// 合作方与服务商共用 `/closeorder` 关单实现(富友无撤销接口, useCancel 参数在 service 层被忽略),
/// 仅凭证按 product=fuyou_partner 密钥行组装。
@Slf4j
@Service
@RequiredArgsConstructor
public class FuyouPartnerCloseStrategy extends AbsPayCloseStrategy {

    private final FuyouCloseService fuyouCloseService;
    private final FuyouIsvConfigAssembler fuyouIsvConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.FUYOU_PARTNER;
    }

    @Override
    public CloseTypeEnum doClose(PayStrategyContext context, boolean useCancel) {
        // 直接从 trade 读取路由参数, 不再需要 container 中间层
        PayTrade trade = context.getTrade();
        FuyouSdkCredential credential = fuyouIsvConfigAssembler.buildConfig(
                trade.getMchNo(), context.getChannelMchNo(), context.getCapability(), ProductEnum.FUYOU_PARTNER);
        return fuyouCloseService.close(trade, credential, useCancel);
    }
}
