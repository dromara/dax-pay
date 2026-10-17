package cn.daxpay.open.channel.fuyou.strategy.refund;

import cn.daxpay.open.channel.fuyou.client.credential.FuyouRsaSdkCredential;
import cn.daxpay.open.channel.fuyou.service.payment.FuyouInternetRefundService;
import cn.daxpay.open.channel.fuyou.service.rsa.FuyouRsaConfigAssembler;
import cn.daxpay.open.payment.strategy.refund.AbsRefundStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 富友互联网直连退款策略
///
/// 富友互联网退款为同步终态(受理成功即退款成功), 无独立退款查询接口, 故无退款同步策略。
@Slf4j
@Service
@RequiredArgsConstructor
public class FuyouInternetRefundStrategy extends AbsRefundStrategy {

    private final FuyouInternetRefundService fuyouInternetRefundService;
    private final FuyouRsaConfigAssembler fuyouRsaConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.FUYOU_INTERNET;
    }

    @Override
    public RefundResultBo doRefund(RefundOrder refundOrder) {
        FuyouRsaSdkCredential credential = fuyouRsaConfigAssembler.buildConfig(refundOrder.getChannelMchNo());
        return fuyouInternetRefundService.refund(refundOrder, credential);
    }
}
