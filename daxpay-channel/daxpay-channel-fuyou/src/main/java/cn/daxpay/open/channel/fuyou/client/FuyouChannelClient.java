package cn.daxpay.open.channel.fuyou.client;

import cn.daxpay.open.channel.fuyou.client.req.FuyouCloseReq;
import cn.daxpay.open.channel.fuyou.client.req.FuyouPayReq;
import cn.daxpay.open.channel.fuyou.client.req.FuyouRefundReq;
import cn.daxpay.open.channel.fuyou.client.req.FuyouRefundSyncReq;
import cn.daxpay.open.channel.fuyou.client.req.FuyouSyncReq;
import cn.daxpay.open.channel.fuyou.client.resp.FuyouCloseResp;
import cn.daxpay.open.channel.fuyou.client.resp.FuyouPayResp;
import cn.daxpay.open.channel.fuyou.client.resp.FuyouRefundResp;
import cn.daxpay.open.channel.fuyou.client.resp.FuyouRefundSyncResp;
import cn.daxpay.open.channel.fuyou.client.resp.FuyouSyncResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/// # 富友通道客户端
///
/// 声明式 HTTP 接口, 调用子应用 dax-pay-channel-two 的富友通道接口。
/// 富友为聚合服务商模式, 不区分直连/服务商, 路径前缀 `/channel/fuyou`。
@HttpExchange(accept = MediaType.APPLICATION_JSON_VALUE, contentType = MediaType.APPLICATION_JSON_VALUE)
public interface FuyouChannelClient {

    /// 支付下单
    @PostExchange("/channel/fuyou/pay")
    DaxResult<FuyouPayResp> pay(@RequestBody FuyouPayReq req);

    /// 支付同步(查询富友订单状态)
    @PostExchange("/channel/fuyou/sync")
    DaxResult<FuyouSyncResp> sync(@RequestBody FuyouSyncReq req);

    /// 关闭订单
    @PostExchange("/channel/fuyou/close")
    DaxResult<FuyouCloseResp> close(@RequestBody FuyouCloseReq req);

    /// 退款
    @PostExchange("/channel/fuyou/refund")
    DaxResult<FuyouRefundResp> refund(@RequestBody FuyouRefundReq req);

    /// 退款同步(查询退款状态)
    @PostExchange("/channel/fuyou/refund-sync")
    DaxResult<FuyouRefundSyncResp> refundSync(@RequestBody FuyouRefundSyncReq req);
}
