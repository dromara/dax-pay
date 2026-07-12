package cn.daxpay.open.channel.lakala.service.payment;

import cn.daxpay.open.channel.lakala.client.LakalaChannelClient;
import cn.daxpay.open.channel.lakala.client.credential.LakalaSdkCredential;
import cn.daxpay.open.channel.lakala.client.req.LakalaSyncReq;
import cn.daxpay.open.channel.lakala.client.resp.LakalaSyncResp;
import cn.daxpay.open.payment.common.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.core.trade.runtime.bo.PaySyncResultBo;
import cn.daxpay.open.payment.core.trade.order.entity.PayTrade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 拉卡拉服务商订单同步业务服务
///
/// 通过 [LakalaChannelClient] 调用子应用查询拉卡拉订单状态。
@Slf4j
@Service
@RequiredArgsConstructor
public class LakalaSyncService {

    private final LakalaChannelClient lakalaChannelClient;

    /// 同步订单状态
    public PaySyncResultBo sync(PayTrade order, LakalaSdkCredential credential) {
        LakalaSyncReq req = new LakalaSyncReq();
        req.setCredential(credential);
        req.setOutTradeNo(order.getTradeNo());
        req.setTradeNo(order.getOutOrderNo());

        DaxResult<LakalaSyncResp> result = lakalaChannelClient.sync(req);
        PaySyncResultBo bo = new PaySyncResultBo();
        if (result.getCode() != 0) {
            // 同步失败(不抛异常, 由核心层决定重试)
            bo.setSyncSuccess(false);
            bo.setSyncErrorMsg(result.getMsg());
            return bo;
        }

        LakalaSyncResp resp = result.getData();
        bo.setSyncSuccess(true);
        bo.setSyncData(resp.getSyncData());
        bo.setOutOrderNo(resp.getTradeNo());
        bo.setPayStatus(mapTradeState(resp.getTradeState()));
        // 成功时补充金额/时间/买家
        if (Objects.equals(resp.getTradeState(), "SUCCESS")) {
            bo.setAmount(resp.getTotalAmount());
            bo.setRealAmount(resp.getTotalAmount());
            bo.setFinishTime(resp.getFinishTime());
            bo.setBuyerId(resp.getBuyerId());
        }
        return bo;
    }

    /// 拉卡拉 trade_state → 平台资金状态
    private PayFundStatusEnum mapTradeState(String tradeState) {
        if (Objects.equals(tradeState, "SUCCESS")) {
            return PayFundStatusEnum.SUCCESS;
        }
        if (Objects.equals(tradeState, "FAIL")) {
            return PayFundStatusEnum.FAIL;
        }
        if (Objects.equals(tradeState, "CLOSED")) {
            return PayFundStatusEnum.CLOSE;
        }
        // 其他(处理中)
        return PayFundStatusEnum.PROCESSING;
    }
}
