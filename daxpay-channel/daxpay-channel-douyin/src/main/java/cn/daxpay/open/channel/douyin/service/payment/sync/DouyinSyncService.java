package cn.daxpay.open.channel.douyin.service.payment.sync;

import cn.daxpay.open.channel.douyin.client.DouyinChannelClient;
import cn.daxpay.open.channel.douyin.client.credential.DouyinSdkCredential;
import cn.daxpay.open.channel.douyin.client.req.DouyinSyncReq;
import cn.daxpay.open.channel.douyin.client.resp.DouyinSyncResp;
import cn.daxpay.open.channel.douyin.code.DouyinPayCode;
import cn.daxpay.open.payment.trade.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Objects;

/// # 抖音支付同步业务服务
///
/// 通过 [DouyinChannelClient] 调用子应用查询抖音订单状态,
/// 将抖音 trade_state 映射为平台 [PayFundStatusEnum]。
///
/// 映射规则:
/// - SUCCESS → SUCCESS(支付成功)
/// - REFUND → SUCCESS(转入退款, 说明已支付过)
/// - NOTPAY / USERPAYING → PROCESSING(未支付/支付中)
/// - CLOSED → CLOSE(已关闭)
/// - PAYERROR → FAIL(支付失败)
@Slf4j
@Service
@RequiredArgsConstructor
public class DouyinSyncService {

    private final DouyinChannelClient douyinChannelClient;

    /// 执行抖音支付同步
    public PaySyncResultBo sync(PayTrade trade, DouyinSdkCredential credential) {
        DouyinSyncReq req = new DouyinSyncReq();
        req.setOutTradeNo(trade.getTradeNo());
        req.setCredential(credential);

        DaxResult<DouyinSyncResp> result = douyinChannelClient.sync(req);
        if (result.getCode() != 0) {
            log.error("抖音通道同步失败: outTradeNo={}, msg={}", trade.getTradeNo(), result.getMsg());
            return new PaySyncResultBo()
                    .setSyncSuccess(false)
                    .setSyncErrorMsg(result.getMsg());
        }

        return toSyncResult(result.getData());
    }

    /// 解析子应用响应, 映射 trade_state → [PayFundStatusEnum]
    private PaySyncResultBo toSyncResult(DouyinSyncResp resp) {
        PaySyncResultBo bo = new PaySyncResultBo();
        bo.setOutOrderNo(resp.getTransactionId());

        // 支付成功时间(RFC3339 → OffsetDateTime)
        if (StrUtil.isNotBlank(resp.getSuccessTime())) {
            bo.setFinishTime(OffsetDateTime.parse(resp.getSuccessTime()));
        }
        // 订单金额
        bo.setRealAmount(resp.getTotalAmount());
        // 买家标识
        bo.setBuyerId(resp.getOpenid());

        String tradeState = resp.getTradeState();
        if (Objects.isNull(tradeState)) {
            return bo.setSyncSuccess(false)
                    .setSyncErrorMsg(StrUtil.blankToDefault(resp.getErrorMsg(), "抖音同步查询失败"));
        }

        // 支付成功 / 转入退款(已支付) → SUCCESS
        if (DouyinPayCode.TRADE_STATE_SUCCESS.equals(tradeState)
                || DouyinPayCode.TRADE_STATE_REFUND.equals(tradeState)) {
            return bo.setPayStatus(PayFundStatusEnum.SUCCESS);
        }
        // 未支付 / 用户支付中 → PROCESSING
        if (DouyinPayCode.TRADE_STATE_NOTPAY.equals(tradeState)
                || DouyinPayCode.TRADE_STATE_USERPAYING.equals(tradeState)) {
            return bo.setPayStatus(PayFundStatusEnum.PROCESSING);
        }
        // 已关闭 → CLOSE
        if (DouyinPayCode.TRADE_STATE_CLOSED.equals(tradeState)) {
            return bo.setPayStatus(PayFundStatusEnum.CLOSE);
        }
        // 支付失败 → FAIL
        if (DouyinPayCode.TRADE_STATE_PAYERROR.equals(tradeState)) {
            return bo.setPayStatus(PayFundStatusEnum.FAIL);
        }
        // 未知状态
        return bo.setSyncSuccess(false)
                .setSyncErrorMsg("抖音未知交易状态: " + tradeState);
    }
}
