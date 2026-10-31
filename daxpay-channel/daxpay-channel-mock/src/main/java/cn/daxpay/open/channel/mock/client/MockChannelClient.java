package cn.daxpay.open.channel.mock.client;

import cn.daxpay.open.channel.mock.client.req.MockAllocFinishReq;
import cn.daxpay.open.channel.mock.client.req.MockAllocReceiverReq;
import cn.daxpay.open.channel.mock.client.req.MockAllocReq;
import cn.daxpay.open.channel.mock.client.req.MockAllocSyncReq;
import cn.daxpay.open.channel.mock.client.req.MockCallbackParseReq;
import cn.daxpay.open.channel.mock.client.req.MockCloseReq;
import cn.daxpay.open.channel.mock.client.req.MockPayReq;
import cn.daxpay.open.channel.mock.client.req.MockRefundReq;
import cn.daxpay.open.channel.mock.client.req.MockRefundSyncReq;
import cn.daxpay.open.channel.mock.client.req.MockSyncReq;
import cn.daxpay.open.channel.mock.client.resp.MockAllocResp;
import cn.daxpay.open.channel.mock.client.resp.MockCallbackParseResp;
import cn.daxpay.open.channel.mock.client.resp.MockCloseResp;
import cn.daxpay.open.channel.mock.client.resp.MockPayResp;
import cn.daxpay.open.channel.mock.client.resp.MockRefundResp;
import cn.daxpay.open.channel.mock.client.resp.MockRefundSyncResp;
import cn.daxpay.open.channel.mock.client.resp.MockSyncResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/// # Mock 模拟通道客户端
///
/// 声明式 HTTP 接口, 调用通道子应用 one 的 mock 模块
/// (one 内 daxpay-channel-mock 模块扮演「模拟网关 SDK」, 路径前缀 `/channel/mock`)。
/// 凭证随请求 body 下发, 传输层 AES-GCM 加密由平台统一挂载。
/// 一期仅直连单产品 `mock_pay`: 支付/同步/关单/退款/退款同步/回调解析/分账族共 12 个端点;
/// 进件(onb)、终端(terminal)、预授权(preauth)、对账(bill)端点不在一期范围。
/// 通道商户建档为纯本地登记(不调子应用), 无 mch/register 端点。
@HttpExchange(accept = MediaType.APPLICATION_JSON_VALUE, contentType = MediaType.APPLICATION_JSON_VALUE)
public interface MockChannelClient {

    /// 支付下单(qr 挂起返回确认页地址, bar 按场景规则即时定结果)
    @PostExchange("/channel/mock/pay")
    DaxResult<MockPayResp> pay(@RequestBody MockPayReq req);

    /// 支付同步(查询 mock 订单状态)
    @PostExchange("/channel/mock/sync")
    DaxResult<MockSyncResp> sync(@RequestBody MockSyncReq req);

    /// 关闭 mock 订单
    @PostExchange("/channel/mock/close")
    DaxResult<MockCloseResp> close(@RequestBody MockCloseReq req);

    /// 发起退款(按 mock 退款场景规则即时定状态, 未终态交同步/回调纠正)
    @PostExchange("/channel/mock/refund")
    DaxResult<MockRefundResp> refund(@RequestBody MockRefundReq req);

    /// 退款同步(查询 mock 退款单状态)
    @PostExchange("/channel/mock/refund-sync")
    DaxResult<MockRefundSyncResp> refundSync(@RequestBody MockRefundSyncReq req);

    /// 支付回调解析(mock 无验签, 解析自定义报文格式)
    @PostExchange("/channel/mock/callback/parse-pay")
    DaxResult<MockCallbackParseResp> parsePayCallback(@RequestBody MockCallbackParseReq req);

    /// 退款回调解析(mock 无验签, 解析自定义报文格式)
    @PostExchange("/channel/mock/callback/parse-refund")
    DaxResult<MockCallbackParseResp> parseRefundCallback(@RequestBody MockCallbackParseReq req);

    /// 发起分账(按 mock 分账场景规则即时定状态, 处理中交同步/人工推进)
    @PostExchange("/channel/mock/alloc")
    DaxResult<MockAllocResp> alloc(@RequestBody MockAllocReq req);

    /// 发起分账完结(解冻剩余冻结资金给商户本身, 无接收方明细)
    @PostExchange("/channel/mock/alloc-finish")
    DaxResult<MockAllocResp> allocFinish(@RequestBody MockAllocFinishReq req);

    /// 分账同步(查询 mock 分账单状态)
    @PostExchange("/channel/mock/alloc-sync")
    DaxResult<MockAllocResp> allocSync(@RequestBody MockAllocSyncReq req);

    /// 绑定分账接收方(mock 零资料接入恒成功仅留痕)
    @PostExchange("/channel/mock/alloc-receiver/bind")
    DaxResult<Void> allocReceiverBind(@RequestBody MockAllocReceiverReq req);

    /// 解绑分账接收方(mock 零资料接入恒成功仅留痕)
    @PostExchange("/channel/mock/alloc-receiver/unbind")
    DaxResult<Void> allocReceiverUnbind(@RequestBody MockAllocReceiverReq req);
}
