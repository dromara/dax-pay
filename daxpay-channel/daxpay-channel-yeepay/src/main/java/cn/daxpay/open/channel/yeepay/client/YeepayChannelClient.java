package cn.daxpay.open.channel.yeepay.client;

import cn.daxpay.open.channel.yeepay.client.req.YeepayCallbackParseReq;
import cn.daxpay.open.channel.yeepay.client.req.YeepayCloseReq;
import cn.daxpay.open.channel.yeepay.client.req.YeepayPayReq;
import cn.daxpay.open.channel.yeepay.client.req.YeepayRefundReq;
import cn.daxpay.open.channel.yeepay.client.req.YeepayRefundSyncReq;
import cn.daxpay.open.channel.yeepay.client.req.YeepaySyncReq;
import cn.daxpay.open.channel.yeepay.client.resp.YeepayCallbackParseResp;
import cn.daxpay.open.channel.yeepay.client.resp.YeepayCloseResp;
import cn.daxpay.open.channel.yeepay.client.resp.YeepayPayResp;
import cn.daxpay.open.channel.yeepay.client.resp.YeepayRefundResp;
import cn.daxpay.open.channel.yeepay.client.resp.YeepayRefundSyncResp;
import cn.daxpay.open.channel.yeepay.client.resp.YeepaySyncResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/// # 易宝通道客户端
///
/// 声明式 HTTP 接口, 调用子应用 dax-pay-channel-two 的易宝通道接口(路径前缀 `/channel/yeepay`)。
/// baseUrl 由 [cn.daxpay.open.channel.yeepay.config.YeepayClientConfig] 从通道配置统一注入。
@HttpExchange(accept = MediaType.APPLICATION_JSON_VALUE, contentType = MediaType.APPLICATION_JSON_VALUE)
public interface YeepayChannelClient {

    /// 支付下单
    @PostExchange("/channel/yeepay/pay")
    DaxResult<YeepayPayResp> pay(@RequestBody YeepayPayReq req);

    /// 关闭订单
    @PostExchange("/channel/yeepay/close")
    DaxResult<YeepayCloseResp> close(@RequestBody YeepayCloseReq req);

    /// 退款
    @PostExchange("/channel/yeepay/refund")
    DaxResult<YeepayRefundResp> refund(@RequestBody YeepayRefundReq req);

    /// 支付同步(查询订单状态)
    @PostExchange("/channel/yeepay/sync")
    DaxResult<YeepaySyncResp> sync(@RequestBody YeepaySyncReq req);

    /// 退款同步(查询退款状态)
    @PostExchange("/channel/yeepay/refund-sync")
    DaxResult<YeepayRefundSyncResp> refundSync(@RequestBody YeepayRefundSyncReq req);

    /// 支付回调验签解析(转发到子应用)
    @PostExchange("/channel/yeepay/callback/parse-pay")
    DaxResult<YeepayCallbackParseResp> parsePayCallback(@RequestBody YeepayCallbackParseReq req);

    /// 退款回调验签解析(转发到子应用)
    @PostExchange("/channel/yeepay/callback/parse-refund")
    DaxResult<YeepayCallbackParseResp> parseRefundCallback(@RequestBody YeepayCallbackParseReq req);
}
