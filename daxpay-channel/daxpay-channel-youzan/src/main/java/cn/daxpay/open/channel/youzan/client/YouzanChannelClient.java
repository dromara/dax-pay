package cn.daxpay.open.channel.youzan.client;

import cn.daxpay.open.channel.youzan.client.req.YouzanCallbackParseReq;
import cn.daxpay.open.channel.youzan.client.req.YouzanPayReq;
import cn.daxpay.open.channel.youzan.client.req.YouzanRefundReq;
import cn.daxpay.open.channel.youzan.client.resp.YouzanCallbackParseResp;
import cn.daxpay.open.channel.youzan.client.resp.YouzanPayResp;
import cn.daxpay.open.channel.youzan.client.resp.YouzanRefundResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/// # 有赞通道客户端
///
/// 声明式 HTTP 接口, 调用子应用 dax-pay-channel-two 的有赞通道接口(路径前缀 `/channel/youzan`)。
/// baseUrl 由 [cn.daxpay.open.channel.youzan.config.YouzanClientConfig] 从通道配置统一注入。
///
/// 有赞统收单线无订单查询接口(对齐参考实现能力: 状态依赖异步通知),
/// 不提供 sync / close / refund-sync 端点(关单为本地语义, 支付/退款同步按「通道不支持查询」软失败, 终态以回调为准)。
@HttpExchange(accept = MediaType.APPLICATION_JSON_VALUE, contentType = MediaType.APPLICATION_JSON_VALUE)
public interface YouzanChannelClient {

    /// 支付下单(统一下单 payopen.apply)
    @PostExchange("/channel/youzan/pay")
    DaxResult<YouzanPayResp> pay(@RequestBody YouzanPayReq req);

    /// 退款(统一退款 refundopen.apply)
    @PostExchange("/channel/youzan/refund")
    DaxResult<YouzanRefundResp> refund(@RequestBody YouzanRefundReq req);

    /// 支付回调验签解析(转发到子应用)
    @PostExchange("/channel/youzan/callback/parse-pay")
    DaxResult<YouzanCallbackParseResp> parsePayCallback(@RequestBody YouzanCallbackParseReq req);

    /// 退款回调验签解析(转发到子应用)
    @PostExchange("/channel/youzan/callback/parse-refund")
    DaxResult<YouzanCallbackParseResp> parseRefundCallback(@RequestBody YouzanCallbackParseReq req);
}
