package cn.daxpay.open.channel.sand.client;

import cn.daxpay.open.channel.sand.client.req.SandCallbackParseReq;
import cn.daxpay.open.channel.sand.client.req.SandPayReq;
import cn.daxpay.open.channel.sand.client.req.SandRefundReq;
import cn.daxpay.open.channel.sand.client.req.SandSyncReq;
import cn.daxpay.open.channel.sand.client.resp.SandCallbackParseResp;
import cn.daxpay.open.channel.sand.client.resp.SandPayResp;
import cn.daxpay.open.channel.sand.client.resp.SandRefundResp;
import cn.daxpay.open.channel.sand.client.resp.SandSyncResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/// # 杉德通道客户端
///
/// 声明式 HTTP 接口, 调用子应用 dax-pay-channel-two 的杉德通道接口(路径前缀 `/channel/sand`)。
/// baseUrl 由 [cn.daxpay.open.channel.sand.config.SandClientConfig] 从通道配置统一注入。
///
/// 杉德 v4 收款线无主动关单与退款查询接口, 不提供 close / refund-sync 端点
/// (关单为本地语义, 退款同步按「通道不支持查询」软失败, 终态以回调为准)。
@HttpExchange(accept = MediaType.APPLICATION_JSON_VALUE, contentType = MediaType.APPLICATION_JSON_VALUE)
public interface SandChannelClient {

    /// 支付下单(trans.order.create 统一下单)
    @PostExchange("/channel/sand/pay")
    DaxResult<SandPayResp> pay(@RequestBody SandPayReq req);

    /// 支付同步(trans.order.query 订单查询)
    @PostExchange("/channel/sand/sync")
    DaxResult<SandSyncResp> sync(@RequestBody SandSyncReq req);

    /// 退款(trans.order.refund 订单退款)
    @PostExchange("/channel/sand/refund")
    DaxResult<SandRefundResp> refund(@RequestBody SandRefundReq req);

    /// 支付回调验签解析(转发到子应用)
    @PostExchange("/channel/sand/callback/parse-pay")
    DaxResult<SandCallbackParseResp> parsePayCallback(@RequestBody SandCallbackParseReq req);

    /// 退款回调验签解析(转发到子应用)
    @PostExchange("/channel/sand/callback/parse-refund")
    DaxResult<SandCallbackParseResp> parseRefundCallback(@RequestBody SandCallbackParseReq req);
}
