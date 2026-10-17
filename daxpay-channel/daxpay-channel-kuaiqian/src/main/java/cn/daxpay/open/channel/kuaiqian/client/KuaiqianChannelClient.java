package cn.daxpay.open.channel.kuaiqian.client;

import cn.daxpay.open.channel.kuaiqian.client.req.KuaiqianCallbackParseReq;
import cn.daxpay.open.channel.kuaiqian.client.req.KuaiqianPayReq;
import cn.daxpay.open.channel.kuaiqian.client.req.KuaiqianRefundReq;
import cn.daxpay.open.channel.kuaiqian.client.req.KuaiqianSyncReq;
import cn.daxpay.open.channel.kuaiqian.client.resp.KuaiqianCallbackParseResp;
import cn.daxpay.open.channel.kuaiqian.client.resp.KuaiqianPayResp;
import cn.daxpay.open.channel.kuaiqian.client.resp.KuaiqianRefundResp;
import cn.daxpay.open.channel.kuaiqian.client.resp.KuaiqianSyncResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/// # 快钱通道客户端
///
/// 声明式 HTTP 接口, 调用子应用 dax-pay-channel-two 的快钱通道接口(路径前缀 `/channel/kuaiqian`)。
/// baseUrl 由 [cn.daxpay.open.channel.kuaiqian.config.KuaiqianClientConfig] 从通道配置统一注入。
///
/// 快钱当面付线无主动关单与退款查询接口, 不提供 close / refund-sync 端点
/// (关单为本地语义, 退款同步按「通道不支持查询」软失败, 终态以回调为准)。
@HttpExchange(accept = MediaType.APPLICATION_JSON_VALUE, contentType = MediaType.APPLICATION_JSON_VALUE)
public interface KuaiqianChannelClient {

    /// 支付下单(A7007 当面付聚合扫码)
    @PostExchange("/channel/kuaiqian/pay")
    DaxResult<KuaiqianPayResp> pay(@RequestBody KuaiqianPayReq req);

    /// 支付同步(A7006 订单查询)
    @PostExchange("/channel/kuaiqian/sync")
    DaxResult<KuaiqianSyncResp> sync(@RequestBody KuaiqianSyncReq req);

    /// 退款(A7003 当面付退款)
    @PostExchange("/channel/kuaiqian/refund")
    DaxResult<KuaiqianRefundResp> refund(@RequestBody KuaiqianRefundReq req);

    /// 支付回调验签解析(转发到子应用)
    @PostExchange("/channel/kuaiqian/callback/parse-pay")
    DaxResult<KuaiqianCallbackParseResp> parsePayCallback(@RequestBody KuaiqianCallbackParseReq req);

    /// 退款回调验签解析(转发到子应用)
    @PostExchange("/channel/kuaiqian/callback/parse-refund")
    DaxResult<KuaiqianCallbackParseResp> parseRefundCallback(@RequestBody KuaiqianCallbackParseReq req);
}
