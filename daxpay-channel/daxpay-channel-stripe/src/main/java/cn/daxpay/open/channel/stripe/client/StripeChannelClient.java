package cn.daxpay.open.channel.stripe.client;

import cn.daxpay.open.channel.stripe.client.req.StripeCallbackParseReq;
import cn.daxpay.open.channel.stripe.client.req.StripeCloseReq;
import cn.daxpay.open.channel.stripe.client.req.StripePayReq;
import cn.daxpay.open.channel.stripe.client.req.StripeRefundReq;
import cn.daxpay.open.channel.stripe.client.req.StripeRefundSyncReq;
import cn.daxpay.open.channel.stripe.client.req.StripeSyncReq;
import cn.daxpay.open.channel.stripe.client.resp.StripeCallbackParseResp;
import cn.daxpay.open.channel.stripe.client.resp.StripeCloseResp;
import cn.daxpay.open.channel.stripe.client.resp.StripePayResp;
import cn.daxpay.open.channel.stripe.client.resp.StripeRefundResp;
import cn.daxpay.open.channel.stripe.client.resp.StripeRefundSyncResp;
import cn.daxpay.open.channel.stripe.client.resp.StripeSyncResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/// # Stripe 通道客户端
///
/// 声明式 HTTP 接口, 调用子应用 dax-pay-channel-three 的 Stripe 通道接口。
/// baseUrl 由 [cn.daxpay.open.channel.stripe.config.StripeClientConfig] 从通道配置统一注入。
@HttpExchange(accept = MediaType.APPLICATION_JSON_VALUE, contentType = MediaType.APPLICATION_JSON_VALUE)
public interface StripeChannelClient {

    /// 支付下单(Checkout Session 或 PaymentIntent)
    @PostExchange("/channel/stripe/pay")
    DaxResult<StripePayResp> pay(@RequestBody StripePayReq req);

    /// 关闭订单(取消 PaymentIntent)
    @PostExchange("/channel/stripe/close")
    DaxResult<StripeCloseResp> close(@RequestBody StripeCloseReq req);

    /// 退款
    @PostExchange("/channel/stripe/refund")
    DaxResult<StripeRefundResp> refund(@RequestBody StripeRefundReq req);

    /// 支付同步(查询 PaymentIntent 状态)
    @PostExchange("/channel/stripe/sync")
    DaxResult<StripeSyncResp> sync(@RequestBody StripeSyncReq req);

    /// 退款同步(查询 Refund 状态)
    @PostExchange("/channel/stripe/refund-sync")
    DaxResult<StripeRefundSyncResp> refundSync(@RequestBody StripeRefundSyncReq req);

    /// 支付回调验签解析(转发到子应用验签)
    @PostExchange("/channel/stripe/callback/parse-pay")
    DaxResult<StripeCallbackParseResp> parsePayCallback(@RequestBody StripeCallbackParseReq req);

    /// 退款回调验签解析(转发到子应用验签)
    @PostExchange("/channel/stripe/callback/parse-refund")
    DaxResult<StripeCallbackParseResp> parseRefundCallback(@RequestBody StripeCallbackParseReq req);
}
