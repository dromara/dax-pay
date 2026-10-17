package cn.daxpay.open.channel.shouqianba.client;

import cn.daxpay.open.channel.shouqianba.client.req.ShouqianbaCallbackParseReq;
import cn.daxpay.open.channel.shouqianba.client.req.ShouqianbaPayReq;
import cn.daxpay.open.channel.shouqianba.client.req.ShouqianbaRefundReq;
import cn.daxpay.open.channel.shouqianba.client.req.ShouqianbaSyncReq;
import cn.daxpay.open.channel.shouqianba.client.resp.ShouqianbaCallbackParseResp;
import cn.daxpay.open.channel.shouqianba.client.resp.ShouqianbaPayResp;
import cn.daxpay.open.channel.shouqianba.client.resp.ShouqianbaRefundResp;
import cn.daxpay.open.channel.shouqianba.client.resp.ShouqianbaSyncResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/// # 收钱吧通道客户端
///
/// 声明式 HTTP 接口, 调用子应用 dax-pay-channel-two 的收钱吧通道接口(路径前缀 `/channel/shouqianba`)。
/// baseUrl 由 [cn.daxpay.open.channel.shouqianba.config.ShouqianbaClientConfig] 从通道配置统一注入。
///
/// 收钱吧退款为同步终态(REFUND_SUCCESS)且无退款查询接口, 不提供 refund-sync 端点;
/// 关单为本地语义(无主动关单接口), 亦不提供 close 端点。
@HttpExchange(accept = MediaType.APPLICATION_JSON_VALUE, contentType = MediaType.APPLICATION_JSON_VALUE)
public interface ShouqianbaChannelClient {

    /// 支付下单(precreate 扫码 / wap api pro 收银台)
    @PostExchange("/channel/shouqianba/pay")
    DaxResult<ShouqianbaPayResp> pay(@RequestBody ShouqianbaPayReq req);

    /// 支付同步(query 订单查询)
    @PostExchange("/channel/shouqianba/sync")
    DaxResult<ShouqianbaSyncResp> sync(@RequestBody ShouqianbaSyncReq req);

    /// 退款(refund 同步终态)
    @PostExchange("/channel/shouqianba/refund")
    DaxResult<ShouqianbaRefundResp> refund(@RequestBody ShouqianbaRefundReq req);

    /// 支付回调验签解析(转发到子应用用官方公钥验签)
    @PostExchange("/channel/shouqianba/callback/parse-pay")
    DaxResult<ShouqianbaCallbackParseResp> parsePayCallback(@RequestBody ShouqianbaCallbackParseReq req);
}
