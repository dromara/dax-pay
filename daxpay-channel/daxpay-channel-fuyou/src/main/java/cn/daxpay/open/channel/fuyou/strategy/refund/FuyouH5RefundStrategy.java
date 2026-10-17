package cn.daxpay.open.channel.fuyou.strategy.refund;

import cn.daxpay.open.channel.fuyou.client.credential.FuyouRsaSdkCredential;
import cn.daxpay.open.channel.fuyou.service.payment.FuyouH5RefundService;
import cn.daxpay.open.channel.fuyou.service.rsa.FuyouRsaConfigAssembler;
import cn.daxpay.open.payment.strategy.refund.AbsRefundStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 富友 H5 聚合收银台退款策略
///
/// 富友收银台退款为同步终态(受理成功即退款成功), 退款结果异步通知仅作兜底核对, 无退款同步策略。
@Slf4j
@Service
@RequiredArgsConstructor
public class FuyouH5RefundStrategy extends AbsRefundStrategy {

    private final FuyouH5RefundService fuyouH5RefundService;
    private final FuyouRsaConfigAssembler fuyouRsaConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.FUYOU_H5;
    }

    @Override
    public RefundResultBo doRefund(RefundOrder refundOrder) {
        FuyouRsaSdkCredential credential = fuyouRsaConfigAssembler.buildConfig(refundOrder.getChannelMchNo());
        return fuyouH5RefundService.refund(refundOrder, credential);
    }
}
