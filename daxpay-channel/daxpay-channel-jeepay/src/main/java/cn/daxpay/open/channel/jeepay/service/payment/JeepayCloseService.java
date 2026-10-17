package cn.daxpay.open.channel.jeepay.service.payment;

import cn.daxpay.open.channel.jeepay.client.JeepayChannelClient;
import cn.daxpay.open.channel.jeepay.client.credential.JeepaySdkCredential;
import cn.daxpay.open.channel.jeepay.client.req.JeepayCloseReq;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # Jeepay关单业务服务
///
/// 通过 [JeepayChannelClient] 调用子应用关闭 Jeepay 订单(`/api/pay/closeOrder`)。
/// Jeepay 关单为标准官方接口(区别于易支付的部分部署不可用场景), 关单失败正常抛错由框架处理,
/// 终态由后续查单确认。
@Slf4j
@Service
@RequiredArgsConstructor
public class JeepayCloseService {

    private final JeepayChannelClient jeepayChannelClient;

    /// 关闭订单
    ///
    /// @param order      支付订单
    /// @param credential 通道凭证
    /// @param useCancel  是否走撤销(统一切面参数, Jeepay 无独立撤销接口, 恒走关单)
    /// @return 关闭类型(恒为 CLOSE)
    public CloseTypeEnum close(PayTrade order, JeepaySdkCredential credential, boolean useCancel) {
        JeepayCloseReq req = new JeepayCloseReq();
        req.setCredential(credential);
        req.setOriginOutTradeNo(order.getTradeNo());
        req.setOriginTradeNo(order.getOutOrderNo());
        var result = jeepayChannelClient.close(req);
        if (result.getCode() != 0) {
            // Jeepay 关单异常
            throw new BizInfoException(DaxPayErrorCode.OPERATION_FAIL, "error.channel.jeepay.closeFailed", result.getMsg());
        }
        return CloseTypeEnum.CLOSE;
    }
}
