package cn.daxpay.open.channel.jeepay.client;

import cn.daxpay.open.channel.jeepay.client.req.JeepayCallbackParseReq;
import cn.daxpay.open.channel.jeepay.client.req.JeepayCloseReq;
import cn.daxpay.open.channel.jeepay.client.req.JeepayPayReq;
import cn.daxpay.open.channel.jeepay.client.req.JeepayRefundReq;
import cn.daxpay.open.channel.jeepay.client.req.JeepayRefundSyncReq;
import cn.daxpay.open.channel.jeepay.client.req.JeepaySyncReq;
import cn.daxpay.open.channel.jeepay.client.resp.JeepayCallbackParseResp;
import cn.daxpay.open.channel.jeepay.client.resp.JeepayCloseResp;
import cn.daxpay.open.channel.jeepay.client.resp.JeepayPayResp;
import cn.daxpay.open.channel.jeepay.client.resp.JeepayRefundResp;
import cn.daxpay.open.channel.jeepay.client.resp.JeepayRefundSyncResp;
import cn.daxpay.open.channel.jeepay.client.resp.JeepaySyncResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/// # Jeepay通道客户端
///
/// 声明式 HTTP 接口, 调用子应用 dax-pay-channel-two 的 Jeepay 通道接口(路径前缀 `/channel/jeepay`)。
/// 凭证随请求 body 下发, 传输层 AES-GCM 加密由平台统一挂载。
/// Jeepay 退款通知不接入(退款查询兜底), 仅提供支付回调解析端点。
@HttpExchange(accept = MediaType.APPLICATION_JSON_VALUE, contentType = MediaType.APPLICATION_JSON_VALUE)
public interface JeepayChannelClient {

    /// 支付下单(wayCode 与 channelExtra 由子应用按 payMethod 组装)
    @PostExchange("/channel/jeepay/pay")
    DaxResult<JeepayPayResp> pay(@RequestBody JeepayPayReq req);

    /// 支付同步(查询 Jeepay 订单状态)
    @PostExchange("/channel/jeepay/sync")
    DaxResult<JeepaySyncResp> sync(@RequestBody JeepaySyncReq req);

    /// 关闭订单
    @PostExchange("/channel/jeepay/close")
    DaxResult<JeepayCloseResp> close(@RequestBody JeepayCloseReq req);

    /// 退款
    @PostExchange("/channel/jeepay/refund")
    DaxResult<JeepayRefundResp> refund(@RequestBody JeepayRefundReq req);

    /// 退款同步(查询退款状态)
    @PostExchange("/channel/jeepay/refund-sync")
    DaxResult<JeepayRefundSyncResp> refundSync(@RequestBody JeepayRefundSyncReq req);

    /// 支付回调验签解析(转发子应用)
    @PostExchange("/channel/jeepay/callback/parse-pay")
    DaxResult<JeepayCallbackParseResp> parsePayCallback(@RequestBody JeepayCallbackParseReq req);
}
