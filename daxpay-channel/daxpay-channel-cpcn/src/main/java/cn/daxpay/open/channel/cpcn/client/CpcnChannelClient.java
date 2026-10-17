package cn.daxpay.open.channel.cpcn.client;

import cn.daxpay.open.channel.cpcn.client.req.CpcnCallbackParseReq;
import cn.daxpay.open.channel.cpcn.client.req.CpcnPayReq;
import cn.daxpay.open.channel.cpcn.client.req.CpcnRefundReq;
import cn.daxpay.open.channel.cpcn.client.req.CpcnSyncReq;
import cn.daxpay.open.channel.cpcn.client.resp.CpcnCallbackParseResp;
import cn.daxpay.open.channel.cpcn.client.resp.CpcnPayResp;
import cn.daxpay.open.channel.cpcn.client.resp.CpcnRefundResp;
import cn.daxpay.open.channel.cpcn.client.resp.CpcnSyncResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/// # 中金通道客户端
///
/// 声明式 HTTP 接口, 调用子应用 dax-pay-channel-two 的中金通道接口(路径前缀 `/channel/cpcn`)。
/// baseUrl 由 [cn.daxpay.open.channel.cpcn.config.CpcnClientConfig] 从通道配置统一注入。
///
/// 中金线无主动关单与退款查询接口, 不提供 close / refund-sync 端点
/// (关单为本地语义, 退款同步按「通道不支持查询」软失败, 终态以回调为准)。
@HttpExchange(accept = MediaType.APPLICATION_JSON_VALUE, contentType = MediaType.APPLICATION_JSON_VALUE)
public interface CpcnChannelClient {

    /// 支付下单(5011 统一下单)
    @PostExchange("/channel/cpcn/pay")
    DaxResult<CpcnPayResp> pay(@RequestBody CpcnPayReq req);

    /// 支付同步(5016 交易查询)
    @PostExchange("/channel/cpcn/sync")
    DaxResult<CpcnSyncResp> sync(@RequestBody CpcnSyncReq req);

    /// 退款(5021 退款)
    @PostExchange("/channel/cpcn/refund")
    DaxResult<CpcnRefundResp> refund(@RequestBody CpcnRefundReq req);

    /// 支付回调验签解析(转发到子应用)
    @PostExchange("/channel/cpcn/callback/parse-pay")
    DaxResult<CpcnCallbackParseResp> parsePayCallback(@RequestBody CpcnCallbackParseReq req);

    /// 退款回调验签解析(转发到子应用)
    @PostExchange("/channel/cpcn/callback/parse-refund")
    DaxResult<CpcnCallbackParseResp> parseRefundCallback(@RequestBody CpcnCallbackParseReq req);
}
