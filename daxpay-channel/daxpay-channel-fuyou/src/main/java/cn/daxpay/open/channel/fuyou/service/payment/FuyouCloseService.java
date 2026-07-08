package cn.daxpay.open.channel.fuyou.service.payment;

import cn.daxpay.open.channel.fuyou.client.FuyouChannelClient;
import cn.daxpay.open.channel.fuyou.client.credential.FuyouSdkCredential;
import cn.daxpay.open.channel.fuyou.client.req.FuyouCloseReq;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import cn.daxpay.open.payment.core.trade.entity.PayTrade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 富友服务商关闭订单业务服务
///
/// 富友仅提供关单接口(`/closeorder`), 无撤销接口, `useCancel` 参数被忽略, 统一走关单。
/// 关单仅对主扫(扫码)未完成订单有效。
@Slf4j
@Service
@RequiredArgsConstructor
public class FuyouCloseService {

    private final FuyouChannelClient fuyouChannelClient;

    /// 关闭订单(富友无撤销, 统一关单)
    public CloseTypeEnum close(PayTrade order, FuyouSdkCredential credential, boolean useCancel) {
        FuyouCloseReq req = new FuyouCloseReq();
        req.setCredential(credential);
        req.setRelationOrderNo(order.getRelationOrderNo());
        req.setTradeProduct(order.getTradeProduct());
        fuyouChannelClient.close(req);
        return CloseTypeEnum.CLOSE;
    }
}
