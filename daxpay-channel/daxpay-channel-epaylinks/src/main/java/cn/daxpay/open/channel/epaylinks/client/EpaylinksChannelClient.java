package cn.daxpay.open.channel.epaylinks.client;

import cn.daxpay.open.channel.epaylinks.client.req.EpaylinksCallbackParseReq;
import cn.daxpay.open.channel.epaylinks.client.req.EpaylinksPayReq;
import cn.daxpay.open.channel.epaylinks.client.req.EpaylinksRefundReq;
import cn.daxpay.open.channel.epaylinks.client.req.EpaylinksSyncReq;
import cn.daxpay.open.channel.epaylinks.client.resp.EpaylinksCallbackParseResp;
import cn.daxpay.open.channel.epaylinks.client.resp.EpaylinksPayResp;
import cn.daxpay.open.channel.epaylinks.client.resp.EpaylinksRefundResp;
import cn.daxpay.open.channel.epaylinks.client.resp.EpaylinksSyncResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/// # 易票联通道客户端
///
/// 声明式 HTTP 接口, 调用子应用 dax-pay-channel-two 的易票联通道接口(路径前缀 `/channel/epaylinks`)。
/// baseUrl 由 [cn.daxpay.open.channel.epaylinks.config.EpaylinksClientConfig] 从通道配置统一注入。
///
/// 易票联无主动关单与退款查询接口、无退款异步通知, 不提供 close / refund-sync / parse-refund 端点
/// (关单为本地语义, 退款为同步受理即终态)。
@HttpExchange(accept = MediaType.APPLICATION_JSON_VALUE, contentType = MediaType.APPLICATION_JSON_VALUE)
public interface EpaylinksChannelClient {

    /// 支付下单(主扫/收银台/微信 JSAPI/支付宝 JSAPI)
    @PostExchange("/channel/epaylinks/pay")
    DaxResult<EpaylinksPayResp> pay(@RequestBody EpaylinksPayReq req);

    /// 支付同步(PaymentQuery 订单查询)
    @PostExchange("/channel/epaylinks/sync")
    DaxResult<EpaylinksSyncResp> sync(@RequestBody EpaylinksSyncReq req);

    /// 退款(Refund/V2, 同步受理即终态)
    @PostExchange("/channel/epaylinks/refund")
    DaxResult<EpaylinksRefundResp> refund(@RequestBody EpaylinksRefundReq req);

    /// 支付回调验签解析(转发到子应用)
    @PostExchange("/channel/epaylinks/callback/parse-pay")
    DaxResult<EpaylinksCallbackParseResp> parsePayCallback(@RequestBody EpaylinksCallbackParseReq req);
}
