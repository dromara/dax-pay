package cn.daxpay.open.channel.vbill.client;

import cn.daxpay.open.channel.vbill.client.req.VbillCloseReq;
import cn.daxpay.open.channel.vbill.client.req.VbillPayReq;
import cn.daxpay.open.channel.vbill.client.req.VbillRefundReq;
import cn.daxpay.open.channel.vbill.client.req.VbillRefundSyncReq;
import cn.daxpay.open.channel.vbill.client.req.VbillSyncReq;
import cn.daxpay.open.channel.vbill.client.resp.VbillCloseResp;
import cn.daxpay.open.channel.vbill.client.resp.VbillPayResp;
import cn.daxpay.open.channel.vbill.client.resp.VbillRefundResp;
import cn.daxpay.open.channel.vbill.client.resp.VbillRefundSyncResp;
import cn.daxpay.open.channel.vbill.client.resp.VbillSyncResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/// # 随行付通道客户端
///
/// 声明式 HTTP 接口, 调用子应用 dax-pay-channel-two 的随行付通道接口。
/// 随行付为聚合服务商模式, 不区分直连/服务商, 路径前缀 `/channel/vbill`。
@HttpExchange(accept = MediaType.APPLICATION_JSON_VALUE, contentType = MediaType.APPLICATION_JSON_VALUE)
public interface VbillChannelClient {

    /// 支付下单
    @PostExchange("/channel/vbill/pay")
    DaxResult<VbillPayResp> pay(@RequestBody VbillPayReq req);

    /// 支付同步(查询随行付订单状态)
    @PostExchange("/channel/vbill/sync")
    DaxResult<VbillSyncResp> sync(@RequestBody VbillSyncReq req);

    /// 关闭订单
    @PostExchange("/channel/vbill/close")
    DaxResult<VbillCloseResp> close(@RequestBody VbillCloseReq req);

    /// 退款
    @PostExchange("/channel/vbill/refund")
    DaxResult<VbillRefundResp> refund(@RequestBody VbillRefundReq req);

    /// 退款同步(查询退款状态)
    @PostExchange("/channel/vbill/refund-sync")
    DaxResult<VbillRefundSyncResp> refundSync(@RequestBody VbillRefundSyncReq req);
}
