package cn.daxpay.open.channel.alipay.client;

import cn.daxpay.open.channel.alipay.client.req.AlipayAppAuthTokenReq;
import cn.daxpay.open.channel.alipay.client.req.AlipayCallbackParseReq;
import cn.daxpay.open.channel.alipay.client.req.AlipayCloseReq;
import cn.daxpay.open.channel.alipay.client.req.AlipayPayReq;
import cn.daxpay.open.channel.alipay.client.req.AlipayRefundReq;
import cn.daxpay.open.channel.alipay.client.req.AlipayRefundSyncReq;
import cn.daxpay.open.channel.alipay.client.req.AlipaySyncReq;
import cn.daxpay.open.channel.alipay.client.resp.AlipayAppAuthTokenResp;
import cn.daxpay.open.channel.alipay.client.resp.AlipayCallbackParseResp;
import cn.daxpay.open.channel.alipay.client.resp.AlipayCloseResp;
import cn.daxpay.open.channel.alipay.client.resp.AlipayPayResp;
import cn.daxpay.open.channel.alipay.client.resp.AlipayRefundResp;
import cn.daxpay.open.channel.alipay.client.resp.AlipayRefundSyncResp;
import cn.daxpay.open.channel.alipay.client.resp.AlipaySyncResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/// # 支付宝通道客户端
///
/// 声明式 HTTP 接口, 调用子应用 dax-pay-channel-one 的支付宝通道接口。
@HttpExchange(accept = MediaType.APPLICATION_JSON_VALUE, contentType = MediaType.APPLICATION_JSON_VALUE)
public interface AlipayChannelClient {

    /// 支付下单
    @PostExchange("/channel/alipay/pay")
    DaxResult<AlipayPayResp> pay(@RequestBody AlipayPayReq req);

    /// 支付同步(查询支付宝订单状态)
    @PostExchange("/channel/alipay/sync")
    DaxResult<AlipaySyncResp> sync(@RequestBody AlipaySyncReq req);

    /// 关闭/撤销支付宝订单
    @PostExchange("/channel/alipay/close")
    DaxResult<AlipayCloseResp> close(@RequestBody AlipayCloseReq req);

    /// 退款
    @PostExchange("/channel/alipay/refund")
    DaxResult<AlipayRefundResp> refund(@RequestBody AlipayRefundReq req);

    /// 退款同步(查询退款状态)
    @PostExchange("/channel/alipay/refund-sync")
    DaxResult<AlipayRefundSyncResp> refundSync(@RequestBody AlipayRefundSyncReq req);

    /// 支付回调验签解析(转发子应用)
    @PostExchange("/channel/alipay/callback/parse-pay")
    DaxResult<AlipayCallbackParseResp> parsePayCallback(@RequestBody AlipayCallbackParseReq req);

    /// 退款回调验签解析(转发子应用)
    @PostExchange("/channel/alipay/callback/parse-refund")
    DaxResult<AlipayCallbackParseResp> parseRefundCallback(@RequestBody AlipayCallbackParseReq req);

    /// 应用授权码换取 app_auth_token(代运营授权)
    @PostExchange("/channel/alipay/auth/app-token")
    DaxResult<AlipayAppAuthTokenResp> exchangeAppAuthToken(@RequestBody AlipayAppAuthTokenReq req);
}
