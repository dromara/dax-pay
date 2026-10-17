package cn.daxpay.open.channel.jlpay.client;

import cn.daxpay.open.channel.jlpay.client.req.JlpayCallbackParseReq;
import cn.daxpay.open.channel.jlpay.client.req.JlpayCloseReq;
import cn.daxpay.open.channel.jlpay.client.req.JlpayPayReq;
import cn.daxpay.open.channel.jlpay.client.req.JlpayRefundReq;
import cn.daxpay.open.channel.jlpay.client.req.JlpaySyncReq;
import cn.daxpay.open.channel.jlpay.client.resp.JlpayCallbackParseResp;
import cn.daxpay.open.channel.jlpay.client.resp.JlpayCloseResp;
import cn.daxpay.open.channel.jlpay.client.resp.JlpayPayResp;
import cn.daxpay.open.channel.jlpay.client.resp.JlpayRefundResp;
import cn.daxpay.open.channel.jlpay.client.resp.JlpaySyncResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/// # 嘉联通道客户端
///
/// 声明式 HTTP 接口, 调用子应用 dax-pay-channel-two 的嘉联通道接口(路径前缀 `/channel/jlpay`)。
/// baseUrl 由 [cn.daxpay.open.channel.jlpay.config.JlpayClientConfig] 从通道配置统一注入。
///
/// 嘉联退款为同步终态且无退款异步通知, 不提供 refund-sync / parse-refund 端点
/// (退款同步按「通道不支持查询」软失败, 见策略层注释)。
@HttpExchange(accept = MediaType.APPLICATION_JSON_VALUE, contentType = MediaType.APPLICATION_JSON_VALUE)
public interface JlpayChannelClient {

    /// 支付下单(qrcodepay 主扫 / micropay 被扫)
    @PostExchange("/channel/jlpay/pay")
    DaxResult<JlpayPayResp> pay(@RequestBody JlpayPayReq req);

    /// 支付同步(chnquery 订单查询)
    @PostExchange("/channel/jlpay/sync")
    DaxResult<JlpaySyncResp> sync(@RequestBody JlpaySyncReq req);

    /// 关单(cancel 撤销)
    @PostExchange("/channel/jlpay/close")
    DaxResult<JlpayCloseResp> close(@RequestBody JlpayCloseReq req);

    /// 退款(refund 同步终态)
    @PostExchange("/channel/jlpay/refund")
    DaxResult<JlpayRefundResp> refund(@RequestBody JlpayRefundReq req);

    /// 支付回调验签解析(转发到子应用)
    @PostExchange("/channel/jlpay/callback/parse-pay")
    DaxResult<JlpayCallbackParseResp> parsePayCallback(@RequestBody JlpayCallbackParseReq req);
}
