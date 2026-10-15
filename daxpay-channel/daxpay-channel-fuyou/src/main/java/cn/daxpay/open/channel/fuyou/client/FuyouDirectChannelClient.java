package cn.daxpay.open.channel.fuyou.client;

import cn.daxpay.open.channel.fuyou.client.req.FuyouDirectCallbackParseReq;
import cn.daxpay.open.channel.fuyou.client.req.FuyouDirectCloseReq;
import cn.daxpay.open.channel.fuyou.client.req.FuyouDirectPayReq;
import cn.daxpay.open.channel.fuyou.client.req.FuyouDirectRefundReq;
import cn.daxpay.open.channel.fuyou.client.req.FuyouDirectRefundSyncReq;
import cn.daxpay.open.channel.fuyou.client.req.FuyouDirectSyncReq;
import cn.daxpay.open.channel.fuyou.client.resp.FuyouCallbackParseResp;
import cn.daxpay.open.channel.fuyou.client.resp.FuyouCloseResp;
import cn.daxpay.open.channel.fuyou.client.resp.FuyouPayResp;
import cn.daxpay.open.channel.fuyou.client.resp.FuyouRefundResp;
import cn.daxpay.open.channel.fuyou.client.resp.FuyouRefundSyncResp;
import cn.daxpay.open.channel.fuyou.client.resp.FuyouSyncResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/// # 富友直连通道客户端
///
/// 声明式 HTTP 接口, 调用子应用 dax-pay-channel-two 的富友直连(商户移动支付前置接口)通道接口。
/// 与服务商模式客户端([FuyouChannelClient])端点平行隔离, 路径前缀 `/channel/fuyou/direct`。
@HttpExchange(accept = MediaType.APPLICATION_JSON_VALUE, contentType = MediaType.APPLICATION_JSON_VALUE)
public interface FuyouDirectChannelClient {

    /// 支付下单
    @PostExchange("/channel/fuyou/direct/pay")
    DaxResult<FuyouPayResp> pay(@RequestBody FuyouDirectPayReq req);

    /// 支付同步(查询富友订单状态)
    @PostExchange("/channel/fuyou/direct/sync")
    DaxResult<FuyouSyncResp> sync(@RequestBody FuyouDirectSyncReq req);

    /// 关闭订单
    @PostExchange("/channel/fuyou/direct/close")
    DaxResult<FuyouCloseResp> close(@RequestBody FuyouDirectCloseReq req);

    /// 退款
    @PostExchange("/channel/fuyou/direct/refund")
    DaxResult<FuyouRefundResp> refund(@RequestBody FuyouDirectRefundReq req);

    /// 退款同步(查询退款状态)
    @PostExchange("/channel/fuyou/direct/refund-sync")
    DaxResult<FuyouRefundSyncResp> refundSync(@RequestBody FuyouDirectRefundSyncReq req);

    /// 支付回调验签解析(转发子应用, 直连无退款结果通知)
    @PostExchange("/channel/fuyou/direct/callback/parse-pay")
    DaxResult<FuyouCallbackParseResp> parsePayCallback(@RequestBody FuyouDirectCallbackParseReq req);
}
