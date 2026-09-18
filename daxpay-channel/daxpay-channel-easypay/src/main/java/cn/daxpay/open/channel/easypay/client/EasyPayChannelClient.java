package cn.daxpay.open.channel.easypay.client;

import cn.daxpay.open.channel.easypay.client.req.EasyPayCallbackParseReq;
import cn.daxpay.open.channel.easypay.client.req.EasyPayCloseReq;
import cn.daxpay.open.channel.easypay.client.req.EasyPayPayReq;
import cn.daxpay.open.channel.easypay.client.req.EasyPayRefundReq;
import cn.daxpay.open.channel.easypay.client.req.EasyPayRefundSyncReq;
import cn.daxpay.open.channel.easypay.client.req.EasyPaySyncReq;
import cn.daxpay.open.channel.easypay.client.resp.EasyPayCallbackParseResp;
import cn.daxpay.open.channel.easypay.client.resp.EasyPayCloseResp;
import cn.daxpay.open.channel.easypay.client.resp.EasyPayPayResp;
import cn.daxpay.open.channel.easypay.client.resp.EasyPayRefundResp;
import cn.daxpay.open.channel.easypay.client.resp.EasyPayRefundSyncResp;
import cn.daxpay.open.channel.easypay.client.resp.EasyPaySyncResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/// # 易支付通道客户端
///
/// 声明式 HTTP 接口, 调用子应用 dax-pay-channel-two 的易支付通道接口(路径前缀 `/channel/easypay`)。
/// 凭证随请求 body 下发, 传输层 AES-GCM 加密由平台统一挂载。
/// 易支付无退款异步通知, 仅提供支付回调解析端点。
@HttpExchange(accept = MediaType.APPLICATION_JSON_VALUE, contentType = MediaType.APPLICATION_JSON_VALUE)
public interface EasyPayChannelClient {

    /// 支付下单(扫码两类, method/device/type 由子应用按 payMethod 组装)
    @PostExchange("/channel/easypay/pay")
    DaxResult<EasyPayPayResp> pay(@RequestBody EasyPayPayReq req);

    /// 支付同步(查询易支付订单状态)
    @PostExchange("/channel/easypay/sync")
    DaxResult<EasyPaySyncResp> sync(@RequestBody EasyPaySyncReq req);

    /// 关闭订单
    @PostExchange("/channel/easypay/close")
    DaxResult<EasyPayCloseResp> close(@RequestBody EasyPayCloseReq req);

    /// 退款
    @PostExchange("/channel/easypay/refund")
    DaxResult<EasyPayRefundResp> refund(@RequestBody EasyPayRefundReq req);

    /// 退款同步(查询退款状态)
    @PostExchange("/channel/easypay/refund-sync")
    DaxResult<EasyPayRefundSyncResp> refundSync(@RequestBody EasyPayRefundSyncReq req);

    /// 支付回调验签解析(转发子应用)
    @PostExchange("/channel/easypay/callback/parse-pay")
    DaxResult<EasyPayCallbackParseResp> parsePayCallback(@RequestBody EasyPayCallbackParseReq req);
}
