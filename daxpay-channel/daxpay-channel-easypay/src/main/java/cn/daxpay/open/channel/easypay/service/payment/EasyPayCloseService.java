package cn.daxpay.open.channel.easypay.service.payment;

import cn.daxpay.open.channel.easypay.client.EasyPayChannelClient;
import cn.daxpay.open.channel.easypay.client.credential.EasyPaySdkCredential;
import cn.daxpay.open.channel.easypay.client.req.EasyPayCloseReq;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 易支付关单业务服务
///
/// 通过 [EasyPayChannelClient] 调用子应用关闭易支付订单。
///
/// 移植约定(3.0 商业版已踩过): 易支付关单接口在部分平台部署中**不一定可用**,
/// 调用异常时同样视为已关闭(恒返回 [CloseTypeEnum#CLOSE]), 保证平台关单流程不因上游
/// 关单能力缺失而阻塞。
@Slf4j
@Service
@RequiredArgsConstructor
public class EasyPayCloseService {

    private final EasyPayChannelClient easyPayChannelClient;

    /// 关闭订单
    ///
    /// @param order      支付订单
    /// @param credential 通道凭证
    /// @return 关闭类型(恒为 CLOSE)
    public CloseTypeEnum close(PayTrade order, EasyPaySdkCredential credential) {
        EasyPayCloseReq req = new EasyPayCloseReq();
        req.setCredential(credential);
        req.setOriginOutTradeNo(order.getTradeNo());
        req.setOriginTradeNo(order.getOutOrderNo());
        try {
            easyPayChannelClient.close(req);
        } catch (Exception e) {
            // 易支付关单接口不一定能用, 无论什么情况都视为已关闭(移植自商业版既定语义)
            log.warn("易支付关单调用失败, 按约定视为已关闭: tradeNo={}", order.getTradeNo(), e);
        }
        return CloseTypeEnum.CLOSE;
    }
}
