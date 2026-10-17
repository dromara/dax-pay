package cn.daxpay.open.channel.helipay.client;

import cn.daxpay.open.channel.helipay.client.req.HelipayCallbackParseReq;
import cn.daxpay.open.channel.helipay.client.req.HelipayPayReq;
import cn.daxpay.open.channel.helipay.client.req.HelipayRefundReq;
import cn.daxpay.open.channel.helipay.client.req.HelipaySyncReq;
import cn.daxpay.open.channel.helipay.client.resp.HelipayCallbackParseResp;
import cn.daxpay.open.channel.helipay.client.resp.HelipayPayResp;
import cn.daxpay.open.channel.helipay.client.resp.HelipayRefundResp;
import cn.daxpay.open.channel.helipay.client.resp.HelipaySyncResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/// # 合利宝通道客户端
///
/// 声明式 HTTP 接口, 调用子应用 dax-pay-channel-two 的合利宝通道接口(路径前缀 `/channel/helipay`)。
/// 凭证随请求 body 下发, 传输层 AES-GCM 加密由平台统一挂载。
///
/// 合利宝扫码产品线无主动关单与退款查询接口(关单为本地语义, 退款为同步结果模式),
/// 不提供 close / refund-sync 端点。
@HttpExchange(accept = MediaType.APPLICATION_JSON_VALUE, contentType = MediaType.APPLICATION_JSON_VALUE)
public interface HelipayChannelClient {

    /// 支付下单(按 payMethod 走扫码/公众号/H5 业务形态)
    @PostExchange("/channel/helipay/pay")
    DaxResult<HelipayPayResp> pay(@RequestBody HelipayPayReq req);

    /// 支付同步(AppPayQuery 订单查询)
    @PostExchange("/channel/helipay/sync")
    DaxResult<HelipaySyncResp> sync(@RequestBody HelipaySyncReq req);

    /// 退款(AppPayRefund, 同步结果模式)
    @PostExchange("/channel/helipay/refund")
    DaxResult<HelipayRefundResp> refund(@RequestBody HelipayRefundReq req);

    /// 支付回调验签解析(转发子应用)
    @PostExchange("/channel/helipay/callback/parse-pay")
    DaxResult<HelipayCallbackParseResp> parsePayCallback(@RequestBody HelipayCallbackParseReq req);
}
