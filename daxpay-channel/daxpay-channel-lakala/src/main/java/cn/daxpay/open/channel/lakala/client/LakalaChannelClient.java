package cn.daxpay.open.channel.lakala.client;

import cn.daxpay.open.channel.lakala.client.req.LakalaCloseReq;
import cn.daxpay.open.channel.lakala.client.req.LakalaPayReq;
import cn.daxpay.open.channel.lakala.client.req.LakalaRefundReq;
import cn.daxpay.open.channel.lakala.client.req.LakalaRefundSyncReq;
import cn.daxpay.open.channel.lakala.client.req.LakalaSyncReq;
import cn.daxpay.open.channel.lakala.client.resp.LakalaCloseResp;
import cn.daxpay.open.channel.lakala.client.resp.LakalaPayResp;
import cn.daxpay.open.channel.lakala.client.resp.LakalaRefundResp;
import cn.daxpay.open.channel.lakala.client.resp.LakalaRefundSyncResp;
import cn.daxpay.open.channel.lakala.client.resp.LakalaSyncResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/// # 拉卡拉通道客户端
///
/// 声明式 HTTP 接口, 调用子应用 dax-pay-channel-one 的拉卡拉通道接口。
/// 拉卡拉为聚合服务商模式, 不区分直连/服务商, 路径前缀 `/channel/lakala`。
@HttpExchange(accept = MediaType.APPLICATION_JSON_VALUE, contentType = MediaType.APPLICATION_JSON_VALUE)
public interface LakalaChannelClient {

    /// 支付下单
    @PostExchange("/channel/lakala/pay")
    DaxResult<LakalaPayResp> pay(@RequestBody LakalaPayReq req);

    /// 支付同步(查询拉卡拉订单状态)
    @PostExchange("/channel/lakala/sync")
    DaxResult<LakalaSyncResp> sync(@RequestBody LakalaSyncReq req);

    /// 关闭订单
    @PostExchange("/channel/lakala/close")
    DaxResult<LakalaCloseResp> close(@RequestBody LakalaCloseReq req);

    /// 退款
    @PostExchange("/channel/lakala/refund")
    DaxResult<LakalaRefundResp> refund(@RequestBody LakalaRefundReq req);

    /// 退款同步(查询退款状态)
    @PostExchange("/channel/lakala/refund-sync")
    DaxResult<LakalaRefundSyncResp> refundSync(@RequestBody LakalaRefundSyncReq req);
}
