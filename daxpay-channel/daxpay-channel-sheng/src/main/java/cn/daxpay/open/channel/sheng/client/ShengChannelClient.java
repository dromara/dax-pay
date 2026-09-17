package cn.daxpay.open.channel.sheng.client;

import cn.daxpay.open.channel.sheng.client.req.ShengCallbackParseReq;
import cn.daxpay.open.channel.sheng.client.req.ShengCloseReq;
import cn.daxpay.open.channel.sheng.client.req.ShengPayReq;
import cn.daxpay.open.channel.sheng.client.req.ShengRefundReq;
import cn.daxpay.open.channel.sheng.client.req.ShengRefundSyncReq;
import cn.daxpay.open.channel.sheng.client.req.ShengSyncReq;
import cn.daxpay.open.channel.sheng.client.resp.ShengCallbackParseResp;
import cn.daxpay.open.channel.sheng.client.resp.ShengCloseResp;
import cn.daxpay.open.channel.sheng.client.resp.ShengPayResp;
import cn.daxpay.open.channel.sheng.client.resp.ShengRefundResp;
import cn.daxpay.open.channel.sheng.client.resp.ShengRefundSyncResp;
import cn.daxpay.open.channel.sheng.client.resp.ShengSyncResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/// # 盛付通通道客户端
///
/// 声明式 HTTP 接口, 调用子应用 dax-pay-channel-two 的盛付通通道接口。
/// 盛付通商户(sheng_pay)/服务商(sheng_isv)两产品共用本客户端(路径前缀 `/channel/sheng`), 凭证按产品维度组装后随请求下发。
/// 凭证随请求 body 下发, 传输层 AES-GCM 加密由平台统一挂载。
@HttpExchange(accept = MediaType.APPLICATION_JSON_VALUE, contentType = MediaType.APPLICATION_JSON_VALUE)
public interface ShengChannelClient {

    /// 支付下单(主应用透传 payMethod+authCode, 由子应用分流 authPay/unifiedorderOffline)
    @PostExchange("/channel/sheng/pay")
    DaxResult<ShengPayResp> pay(@RequestBody ShengPayReq req);

    /// 支付同步(查询盛付通订单状态)
    @PostExchange("/channel/sheng/sync")
    DaxResult<ShengSyncResp> sync(@RequestBody ShengSyncReq req);

    /// 关闭订单(useCancel=true 当日撤单, false 关单)
    @PostExchange("/channel/sheng/close")
    DaxResult<ShengCloseResp> close(@RequestBody ShengCloseReq req);

    /// 退款
    @PostExchange("/channel/sheng/refund")
    DaxResult<ShengRefundResp> refund(@RequestBody ShengRefundReq req);

    /// 退款同步(查询退款状态)
    @PostExchange("/channel/sheng/refund-sync")
    DaxResult<ShengRefundSyncResp> refundSync(@RequestBody ShengRefundSyncReq req);

    /// 支付回调验签解析(转发子应用)
    @PostExchange("/channel/sheng/callback/parse-pay")
    DaxResult<ShengCallbackParseResp> parsePayCallback(@RequestBody ShengCallbackParseReq req);

    /// 退款回调验签解析(转发子应用)
    @PostExchange("/channel/sheng/callback/parse-refund")
    DaxResult<ShengCallbackParseResp> parseRefundCallback(@RequestBody ShengCallbackParseReq req);
}
