package cn.daxpay.open.channel.union.client;

import cn.daxpay.open.channel.union.client.req.UnionCallbackParseReq;
import cn.daxpay.open.channel.union.client.req.UnionCloseReq;
import cn.daxpay.open.channel.union.client.req.UnionPayReq;
import cn.daxpay.open.channel.union.client.req.UnionRefundReq;
import cn.daxpay.open.channel.union.client.req.UnionRefundSyncReq;
import cn.daxpay.open.channel.union.client.req.UnionSyncReq;
import cn.daxpay.open.channel.union.client.resp.UnionCallbackParseResp;
import cn.daxpay.open.channel.union.client.resp.UnionCloseResp;
import cn.daxpay.open.channel.union.client.resp.UnionPayResp;
import cn.daxpay.open.channel.union.client.resp.UnionRefundResp;
import cn.daxpay.open.channel.union.client.resp.UnionRefundSyncResp;
import cn.daxpay.open.channel.union.client.resp.UnionSyncResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/// # 云闪付通道客户端
///
/// 声明式 HTTP 接口, 调用子应用 dax-pay-channel-one 的云闪付通道接口。
/// baseUrl 由 [cn.daxpay.open.channel.union.config.UnionClientConfig] 从通道配置统一注入。
@HttpExchange(accept = MediaType.APPLICATION_JSON_VALUE, contentType = MediaType.APPLICATION_JSON_VALUE)
public interface UnionChannelClient {

    /// 支付下单
    @PostExchange("/channel/union/pay")
    DaxResult<UnionPayResp> pay(@RequestBody UnionPayReq req);

    /// 关闭订单
    @PostExchange("/channel/union/close")
    DaxResult<UnionCloseResp> close(@RequestBody UnionCloseReq req);

    /// 退款
    @PostExchange("/channel/union/refund")
    DaxResult<UnionRefundResp> refund(@RequestBody UnionRefundReq req);

    /// 支付同步(查询订单状态)
    @PostExchange("/channel/union/sync")
    DaxResult<UnionSyncResp> sync(@RequestBody UnionSyncReq req);

    /// 退款同步(查询退款状态)
    @PostExchange("/channel/union/refund-sync")
    DaxResult<UnionRefundSyncResp> refundSync(@RequestBody UnionRefundSyncReq req);

    /// 支付回调验签解析(转发到子应用验签)
    @PostExchange("/channel/union/callback/parse-pay")
    DaxResult<UnionCallbackParseResp> parsePayCallback(@RequestBody UnionCallbackParseReq req);

    /// 退款回调验签解析(转发到子应用验签)
    @PostExchange("/channel/union/callback/parse-refund")
    DaxResult<UnionCallbackParseResp> parseRefundCallback(@RequestBody UnionCallbackParseReq req);
}
