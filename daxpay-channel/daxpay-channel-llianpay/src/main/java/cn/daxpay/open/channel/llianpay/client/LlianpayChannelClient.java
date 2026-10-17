package cn.daxpay.open.channel.llianpay.client;

import cn.daxpay.open.channel.llianpay.client.req.LlianpayCallbackParseReq;
import cn.daxpay.open.channel.llianpay.client.req.LlianpayPayReq;
import cn.daxpay.open.channel.llianpay.client.req.LlianpayRefundReq;
import cn.daxpay.open.channel.llianpay.client.req.LlianpaySyncReq;
import cn.daxpay.open.channel.llianpay.client.resp.LlianpayCallbackParseResp;
import cn.daxpay.open.channel.llianpay.client.resp.LlianpayPayResp;
import cn.daxpay.open.channel.llianpay.client.resp.LlianpayRefundResp;
import cn.daxpay.open.channel.llianpay.client.resp.LlianpaySyncResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/// # 连连支付通道客户端
///
/// 声明式 HTTP 接口, 调用子应用 dax-pay-channel-two 的连连通道接口(路径前缀 `/channel/llianpay`)。
/// baseUrl 由 [cn.daxpay.open.channel.llianpay.config.LlianpayClientConfig] 从通道配置统一注入。
///
/// 连连连收单线无主动关单与退款查询接口, 不提供 close / refund-sync 端点
/// (关单为本地语义, 退款同步按「通道不支持查询」软失败, 终态以回调为准)。
@HttpExchange(accept = MediaType.APPLICATION_JSON_VALUE, contentType = MediaType.APPLICATION_JSON_VALUE)
public interface LlianpayChannelClient {

    /// 支付下单(统收单 createpay / 收银台 cashier createpay)
    @PostExchange("/channel/llianpay/pay")
    DaxResult<LlianpayPayResp> pay(@RequestBody LlianpayPayReq req);

    /// 支付同步(orderquery 订单查询)
    @PostExchange("/channel/llianpay/sync")
    DaxResult<LlianpaySyncResp> sync(@RequestBody LlianpaySyncReq req);

    /// 退款(ipay refund)
    @PostExchange("/channel/llianpay/refund")
    DaxResult<LlianpayRefundResp> refund(@RequestBody LlianpayRefundReq req);

    /// 支付回调验签解析(转发到子应用)
    @PostExchange("/channel/llianpay/callback/parse-pay")
    DaxResult<LlianpayCallbackParseResp> parsePayCallback(@RequestBody LlianpayCallbackParseReq req);

    /// 退款回调验签解析(转发到子应用)
    @PostExchange("/channel/llianpay/callback/parse-refund")
    DaxResult<LlianpayCallbackParseResp> parseRefundCallback(@RequestBody LlianpayCallbackParseReq req);
}
