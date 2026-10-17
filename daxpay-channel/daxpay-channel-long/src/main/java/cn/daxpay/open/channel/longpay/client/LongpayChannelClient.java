package cn.daxpay.open.channel.longpay.client;

import cn.daxpay.open.channel.longpay.client.req.LongpayCallbackParseReq;
import cn.daxpay.open.channel.longpay.client.req.LongpayPayReq;
import cn.daxpay.open.channel.longpay.client.resp.LongpayCallbackParseResp;
import cn.daxpay.open.channel.longpay.client.resp.LongpayPayResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/// # 建行龙支付通道客户端
///
/// 声明式 HTTP 接口, 调用子应用 dax-pay-channel-two 的建行龙支付接口(路径前缀 `/channel/long`)。
/// baseUrl 由 [cn.daxpay.open.channel.longpay.config.LongpayClientConfig] 从通道配置统一注入。
///
/// 建行龙支付聚合线的订单查询交易码与退款接口未获可考证公开资料, 不提供 sync / refund / close /
/// refund-sync 端点(主应用侧分别为软失败 / 明确错误 / 本地关单, 支付终态以异步通知为准)。
@HttpExchange(accept = MediaType.APPLICATION_JSON_VALUE, contentType = MediaType.APPLICATION_JSON_VALUE)
public interface LongpayChannelClient {

    /// 支付下单(龙支付聚合扫码)
    @PostExchange("/channel/long/pay")
    DaxResult<LongpayPayResp> pay(@RequestBody LongpayPayReq req);

    /// 支付回调验签解析(转发到子应用)
    @PostExchange("/channel/long/callback/parse-pay")
    DaxResult<LongpayCallbackParseResp> parsePayCallback(@RequestBody LongpayCallbackParseReq req);
}
