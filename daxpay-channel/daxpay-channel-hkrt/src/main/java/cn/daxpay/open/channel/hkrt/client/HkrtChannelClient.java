package cn.daxpay.open.channel.hkrt.client;

import cn.daxpay.open.channel.hkrt.client.req.HkrtCallbackParseReq;
import cn.daxpay.open.channel.hkrt.client.req.HkrtCloseReq;
import cn.daxpay.open.channel.hkrt.client.req.HkrtPayReq;
import cn.daxpay.open.channel.hkrt.client.req.HkrtRefundReq;
import cn.daxpay.open.channel.hkrt.client.req.HkrtRefundSyncReq;
import cn.daxpay.open.channel.hkrt.client.req.HkrtSyncReq;
import cn.daxpay.open.channel.hkrt.client.resp.HkrtCallbackParseResp;
import cn.daxpay.open.channel.hkrt.client.resp.HkrtCloseResp;
import cn.daxpay.open.channel.hkrt.client.resp.HkrtPayResp;
import cn.daxpay.open.channel.hkrt.client.resp.HkrtRefundResp;
import cn.daxpay.open.channel.hkrt.client.resp.HkrtRefundSyncResp;
import cn.daxpay.open.channel.hkrt.client.resp.HkrtSyncResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/// # 海科融通通道客户端
///
/// 声明式 HTTP 接口, 调用子应用 dax-pay-channel-two 的海科融通通道接口。
/// 海科融通为服务商模式, 路径前缀 `/channel/hkrt`。
/// 回调验签与解析统一转发子应用(parse-pay / parse-refund), 主应用不再自验签。
@HttpExchange(accept = MediaType.APPLICATION_JSON_VALUE, contentType = MediaType.APPLICATION_JSON_VALUE)
public interface HkrtChannelClient {

    /// 支付下单
    @PostExchange("/channel/hkrt/pay")
    DaxResult<HkrtPayResp> pay(@RequestBody HkrtPayReq req);

    /// 支付同步(查询海科融通订单状态)
    @PostExchange("/channel/hkrt/sync")
    DaxResult<HkrtSyncResp> sync(@RequestBody HkrtSyncReq req);

    /// 关闭订单
    @PostExchange("/channel/hkrt/close")
    DaxResult<HkrtCloseResp> close(@RequestBody HkrtCloseReq req);

    /// 退款
    @PostExchange("/channel/hkrt/refund")
    DaxResult<HkrtRefundResp> refund(@RequestBody HkrtRefundReq req);

    /// 退款同步(查询退款状态)
    @PostExchange("/channel/hkrt/refund-sync")
    DaxResult<HkrtRefundSyncResp> refundSync(@RequestBody HkrtRefundSyncReq req);

    /// 支付回调验签解析(转发子应用)
    @PostExchange("/channel/hkrt/callback/parse-pay")
    DaxResult<HkrtCallbackParseResp> parsePayCallback(@RequestBody HkrtCallbackParseReq req);

    /// 退款回调验签解析(转发子应用)
    @PostExchange("/channel/hkrt/callback/parse-refund")
    DaxResult<HkrtCallbackParseResp> parseRefundCallback(@RequestBody HkrtCallbackParseReq req);
}
