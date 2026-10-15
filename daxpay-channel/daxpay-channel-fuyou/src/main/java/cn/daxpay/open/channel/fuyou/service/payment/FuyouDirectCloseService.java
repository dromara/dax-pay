package cn.daxpay.open.channel.fuyou.service.payment;

import cn.daxpay.open.channel.fuyou.client.FuyouDirectChannelClient;
import cn.daxpay.open.channel.fuyou.client.credential.FuyouDirectSdkCredential;
import cn.daxpay.open.channel.fuyou.client.req.FuyouDirectCloseReq;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.trade.runtime.service.PayTradeContainerFields;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 富友直连关闭订单业务服务
///
/// 富友直连仅提供关单接口(`/aggregatePay/closeOrder`), 无撤销接口, `useCancel` 参数被忽略。
/// 关单仅对未支付订单有效(支付宝主扫需用户扫码后才可关闭)。
@Slf4j
@Service
@RequiredArgsConstructor
public class FuyouDirectCloseService {

    private final FuyouDirectChannelClient fuyouDirectChannelClient;
    private final PayTradeContainerFields payTradeContainerFields;

    /// 关闭订单(富友无撤销, 统一关单)
    public CloseTypeEnum close(PayTrade order, FuyouDirectSdkCredential credential, boolean useCancel) {
        FuyouDirectCloseReq req = new FuyouDirectCloseReq();
        req.setCredential(credential);
        var fields = payTradeContainerFields.resolve(order);
        req.setRelationOrderNo(fields.relationOrderNo());
        req.setTradeProduct(fields.tradeProduct());
        fuyouDirectChannelClient.close(req);
        return CloseTypeEnum.CLOSE;
    }
}
