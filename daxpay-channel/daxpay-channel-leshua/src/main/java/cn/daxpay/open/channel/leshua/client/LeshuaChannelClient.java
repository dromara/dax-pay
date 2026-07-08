package cn.daxpay.open.channel.leshua.client;

import cn.daxpay.open.channel.leshua.client.req.LeshuaCallbackParseReq;
import cn.daxpay.open.channel.leshua.client.req.LeshuaCloseReq;
import cn.daxpay.open.channel.leshua.client.req.LeshuaPayReq;
import cn.daxpay.open.channel.leshua.client.req.LeshuaRefundReq;
import cn.daxpay.open.channel.leshua.client.req.LeshuaRefundSyncReq;
import cn.daxpay.open.channel.leshua.client.req.LeshuaSyncReq;
import cn.daxpay.open.channel.leshua.client.resp.LeshuaCallbackParseResp;
import cn.daxpay.open.channel.leshua.client.resp.LeshuaCloseResp;
import cn.daxpay.open.channel.leshua.client.resp.LeshuaPayResp;
import cn.daxpay.open.channel.leshua.client.resp.LeshuaRefundResp;
import cn.daxpay.open.channel.leshua.client.resp.LeshuaRefundSyncResp;
import cn.daxpay.open.channel.leshua.client.resp.LeshuaSyncResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/// # 乐刷通道客户端
///
/// 声明式 HTTP 接口, 调用子应用 dax-pay-channel-two 的乐刷通道接口。
/// 乐刷为聚合服务商模式(微信/支付宝/云闪付), 路径前缀 `/channel/leshua`。
/// baseUrl 由 [cn.daxpay.open.channel.leshua.config.LeshuaClientConfig] 从通道配置统一注入。
@HttpExchange(accept = MediaType.APPLICATION_JSON_VALUE, contentType = MediaType.APPLICATION_JSON_VALUE)
public interface LeshuaChannelClient {

    /// 支付下单
    @PostExchange("/channel/leshua/pay")
    DaxResult<LeshuaPayResp> pay(@RequestBody LeshuaPayReq req);

    /// 关闭订单
    @PostExchange("/channel/leshua/close")
    DaxResult<LeshuaCloseResp> close(@RequestBody LeshuaCloseReq req);

    /// 退款
    @PostExchange("/channel/leshua/refund")
    DaxResult<LeshuaRefundResp> refund(@RequestBody LeshuaRefundReq req);

    /// 支付同步(查询订单状态)
    @PostExchange("/channel/leshua/sync")
    DaxResult<LeshuaSyncResp> sync(@RequestBody LeshuaSyncReq req);

    /// 退款同步(查询退款状态)
    @PostExchange("/channel/leshua/refund-sync")
    DaxResult<LeshuaRefundSyncResp> refundSync(@RequestBody LeshuaRefundSyncReq req);

    /// 支付回调验签解析(转发到子应用验签)
    @PostExchange("/channel/leshua/callback/parse-pay")
    DaxResult<LeshuaCallbackParseResp> parsePayCallback(@RequestBody LeshuaCallbackParseReq req);

    /// 退款回调验签解析(转发到子应用验签)
    @PostExchange("/channel/leshua/callback/parse-refund")
    DaxResult<LeshuaCallbackParseResp> parseRefundCallback(@RequestBody LeshuaCallbackParseReq req);
}
