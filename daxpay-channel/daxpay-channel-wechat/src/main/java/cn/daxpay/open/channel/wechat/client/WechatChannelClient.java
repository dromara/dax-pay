package cn.daxpay.open.channel.wechat.client;

import cn.daxpay.open.channel.wechat.client.req.WechatCallbackParseReq;
import cn.daxpay.open.channel.wechat.client.req.WechatCloseReq;
import cn.daxpay.open.channel.wechat.client.req.WechatPayReq;
import cn.daxpay.open.channel.wechat.client.req.WechatRefundReq;
import cn.daxpay.open.channel.wechat.client.req.WechatRefundSyncReq;
import cn.daxpay.open.channel.wechat.client.req.WechatSyncReq;
import cn.daxpay.open.channel.wechat.client.resp.WechatCallbackParseResp;
import cn.daxpay.open.channel.wechat.client.resp.WechatCloseResp;
import cn.daxpay.open.channel.wechat.client.resp.WechatPayResp;
import cn.daxpay.open.channel.wechat.client.resp.WechatRefundResp;
import cn.daxpay.open.channel.wechat.client.resp.WechatRefundSyncResp;
import cn.daxpay.open.channel.wechat.client.resp.WechatSyncResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/// # 微信通道客户端
///
/// 声明式 HTTP 接口, 调用子应用 dax-pay-channel-one 的微信**支付**通道接口。
/// 公众号/小程序 OAuth 认证见 capability-wechat, 不经本客户端。
@HttpExchange(accept = MediaType.APPLICATION_JSON_VALUE, contentType = MediaType.APPLICATION_JSON_VALUE)
public interface WechatChannelClient {

    /// 支付下单
    @PostExchange("/channel/wechat/pay")
    DaxResult<WechatPayResp> pay(@RequestBody WechatPayReq req);

    /// 支付同步(查询微信订单状态)
    @PostExchange("/channel/wechat/sync")
    DaxResult<WechatSyncResp> sync(@RequestBody WechatSyncReq req);

    /// 关闭微信订单
    @PostExchange("/channel/wechat/close")
    DaxResult<WechatCloseResp> close(@RequestBody WechatCloseReq req);

    /// 退款
    @PostExchange("/channel/wechat/refund")
    DaxResult<WechatRefundResp> refund(@RequestBody WechatRefundReq req);

    /// 退款同步(查询退款状态)
    @PostExchange("/channel/wechat/refund-sync")
    DaxResult<WechatRefundSyncResp> refundSync(@RequestBody WechatRefundSyncReq req);

    // ===== 服务商模式(isv), 对应子应用 /channel/wechat/isv/* 端点 =====

    /// 服务商支付下单
    @PostExchange("/channel/wechat/isv/pay")
    DaxResult<WechatPayResp> isvPay(@RequestBody WechatPayReq req);

    /// 服务商支付同步(查询微信订单状态)
    @PostExchange("/channel/wechat/isv/sync")
    DaxResult<WechatSyncResp> isvSync(@RequestBody WechatSyncReq req);

    /// 服务商关闭微信订单
    @PostExchange("/channel/wechat/isv/close")
    DaxResult<WechatCloseResp> isvClose(@RequestBody WechatCloseReq req);

    /// 服务商退款
    @PostExchange("/channel/wechat/isv/refund")
    DaxResult<WechatRefundResp> isvRefund(@RequestBody WechatRefundReq req);

    /// 服务商退款同步(查询退款状态)
    @PostExchange("/channel/wechat/isv/refund-sync")
    DaxResult<WechatRefundSyncResp> isvRefundSync(@RequestBody WechatRefundSyncReq req);

    /// 支付回调验签解析(转发到子应用验签)
    @PostExchange("/channel/wechat/callback/parse-pay")
    DaxResult<WechatCallbackParseResp> parsePayCallback(@RequestBody WechatCallbackParseReq req);

    /// 退款回调验签解析(转发到子应用验签)
    @PostExchange("/channel/wechat/callback/parse-refund")
    DaxResult<WechatCallbackParseResp> parseRefundCallback(@RequestBody WechatCallbackParseReq req);
}
