package cn.daxpay.open.channel.xingyi.client;

import cn.daxpay.open.channel.xingyi.client.req.XingyiCallbackParseReq;
import cn.daxpay.open.channel.xingyi.client.req.XingyiPayReq;
import cn.daxpay.open.channel.xingyi.client.req.XingyiRefundReq;
import cn.daxpay.open.channel.xingyi.client.req.XingyiSyncReq;
import cn.daxpay.open.channel.xingyi.client.resp.XingyiCallbackParseResp;
import cn.daxpay.open.channel.xingyi.client.resp.XingyiPayResp;
import cn.daxpay.open.channel.xingyi.client.resp.XingyiRefundResp;
import cn.daxpay.open.channel.xingyi.client.resp.XingyiSyncResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/// # 星驿付通道客户端
///
/// 声明式 HTTP 接口, 调用子应用 dax-pay-channel-two 的星驿付通道接口(路径前缀 `/channel/xingyi`)。
/// baseUrl 由 [cn.daxpay.open.channel.xingyi.config.XingyiClientConfig] 从通道配置统一注入。
///
/// 星驿付无主动关单与退款查询接口, 不提供 close / refund-sync 端点
/// (关单为本地语义, 退款为同步受理即成功); 仅有支付回调解析端点(退款无异步通知)。
@HttpExchange(accept = MediaType.APPLICATION_JSON_VALUE, contentType = MediaType.APPLICATION_JSON_VALUE)
public interface XingyiChannelClient {

    /// 支付下单(官方码/星驿码/JSAPI/付款码)
    @PostExchange("/channel/xingyi/pay")
    DaxResult<XingyiPayResp> pay(@RequestBody XingyiPayReq req);

    /// 支付同步(orderQuery 订单查询)
    @PostExchange("/channel/xingyi/sync")
    DaxResult<XingyiSyncResp> sync(@RequestBody XingyiSyncReq req);

    /// 退款(同步受理即成功)
    @PostExchange("/channel/xingyi/refund")
    DaxResult<XingyiRefundResp> refund(@RequestBody XingyiRefundReq req);

    /// 支付回调验签解析(转发到子应用)
    @PostExchange("/channel/xingyi/callback/parse-pay")
    DaxResult<XingyiCallbackParseResp> parsePayCallback(@RequestBody XingyiCallbackParseReq req);
}
