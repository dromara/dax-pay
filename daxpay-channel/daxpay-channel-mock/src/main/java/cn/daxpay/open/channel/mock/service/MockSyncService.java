package cn.daxpay.open.channel.mock.service;

import cn.daxpay.open.channel.mock.client.MockChannelClient;
import cn.daxpay.open.channel.mock.client.req.MockSyncReq;
import cn.daxpay.open.channel.mock.client.resp.MockSyncResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # Mock 通道支付同步业务服务
///
/// 通过 [MockChannelClient] 调用独立 mock 子应用查询 mock 订单状态,
/// 将 mock tradeStatus 映射为平台 [PayFundStatusEnum]。
///
/// 映射规则:
/// - SUCCESS → SUCCESS(带回 finishTime/金额/实际付款钱包)
/// - FAIL → FAIL
/// - WAIT_BUYER_PAY → PROCESSING(挂起中, 等确认页推进/超时关单)
/// - CLOSED → CLOSE
/// - 查询失败(订单不存在等) → syncSuccess=false
@Slf4j
@Service
@RequiredArgsConstructor
public class MockSyncService {

    /// mock 交易状态: 支付成功
    private static final String TRADE_SUCCESS = "SUCCESS";
    /// mock 交易状态: 支付失败
    private static final String TRADE_FAIL = "FAIL";
    /// mock 交易状态: 等待买家付款(挂起中)
    private static final String WAIT_BUYER_PAY = "WAIT_BUYER_PAY";
    /// mock 交易状态: 已关闭
    private static final String TRADE_CLOSED = "CLOSED";

    private final MockChannelClient mockChannelClient;
    private final MockCredentialSupport mockCredentialSupport;

    /// 执行 mock 支付同步
    ///
    /// @param trade 支付订单(tradeNo 作为商户订单号透传)
    /// @return 同步结果(含映射后的资金状态)
    public PaySyncResultBo sync(PayTrade trade) {
        // 构建请求(支付单无 product, 凭证走通道商户号反查商户级两件套)
        MockSyncReq req = new MockSyncReq();
        req.setOutTradeNo(trade.getTradeNo());
        req.setCredential(mockCredentialSupport.build(trade.getChannelMchNo()));

        // 调用 mock 子应用
        DaxResult<MockSyncResp> result = mockChannelClient.sync(req);
        if (result.getCode() != 0) {
            log.error("模拟通道同步失败: outTradeNo={}, msg={}", trade.getTradeNo(), result.getMsg());
            return new PaySyncResultBo()
                    .setSyncSuccess(false)
                    .setSyncErrorMsg(result.getMsg());
        }

        return toSyncResult(result.getData());
    }

    /// 解析 mock 响应, 映射 tradeStatus → [PayFundStatusEnum]
    private PaySyncResultBo toSyncResult(MockSyncResp resp) {
        PaySyncResultBo bo = new PaySyncResultBo();
        bo.setOutOrderNo(resp.getTradeNo());
        String tradeStatus = resp.getTradeStatus();

        // 支付完成(带回完成时间与金额; 聚合单带回实际付款钱包供平台回填 provider)
        if (TRADE_SUCCESS.equals(tradeStatus)) {
            return bo.setPayStatus(PayFundStatusEnum.SUCCESS)
                    .setFinishTime(resp.getFinishTime())
                    .setAmount(resp.getAmount())
                    .setRealAmount(resp.getAmount())
                    .setProvider(MockPayService.resolveActualProvider(resp.getProvider()));
        }

        // 支付失败
        if (TRADE_FAIL.equals(tradeStatus)) {
            return bo.setPayStatus(PayFundStatusEnum.FAIL)
                    .setFinishTime(resp.getFinishTime());
        }

        // 挂起中(等待买家付款)
        if (WAIT_BUYER_PAY.equals(tradeStatus)) {
            return bo.setPayStatus(PayFundStatusEnum.PROCESSING);
        }

        // 已关闭
        if (TRADE_CLOSED.equals(tradeStatus)) {
            return bo.setPayStatus(PayFundStatusEnum.CLOSE);
        }

        // 未知状态: 按同步失败处理
        return bo.setSyncSuccess(false)
                .setSyncErrorMsg("模拟通道返回未知交易状态: " + tradeStatus);
    }
}
