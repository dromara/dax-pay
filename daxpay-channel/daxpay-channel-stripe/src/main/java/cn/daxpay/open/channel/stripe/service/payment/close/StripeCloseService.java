package cn.daxpay.open.channel.stripe.service.payment.close;

import cn.daxpay.open.channel.stripe.client.StripeChannelClient;
import cn.daxpay.open.channel.stripe.client.credential.StripeSdkCredential;
import cn.daxpay.open.channel.stripe.client.req.StripeCloseReq;
import cn.daxpay.open.channel.stripe.client.resp.StripeCloseResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # Stripe 支付关闭业务服务
///
/// 通过 [StripeChannelClient] 调用子应用 dax-pay-channel-three 取消 PaymentIntent。
/// Stripe 关单接口不区分撤销/关闭, 统一为 CANCEL。
@Slf4j
@Service
@RequiredArgsConstructor
public class StripeCloseService {

    private final StripeChannelClient stripeChannelClient;

    /// 执行 Stripe 订单关闭
    public CloseTypeEnum close(PayTrade trade, StripeSdkCredential credential, boolean useCancel) {
        StripeCloseReq req = new StripeCloseReq();
        // 通道侧订单号(PaymentIntent ID)
        req.setPaymentIntentId(trade.getOutOrderNo());
        req.setOrderNo(trade.getTradeNo());
        req.setCredential(credential);

        DaxResult<StripeCloseResp> result = stripeChannelClient.close(req);
        if (result.getCode() != 0) {
            // Stripe 关单失败
            throw new BizInfoException(DaxPayErrorCode.OPERATION_FAIL, "error.channel.stripe.closeFailed", result.getMsg());
        }

        // Stripe 只有取消接口, 不区分撤销, 统一返回 CANCEL
        return useCancel ? CloseTypeEnum.CANCEL : CloseTypeEnum.CLOSE;
    }
}
