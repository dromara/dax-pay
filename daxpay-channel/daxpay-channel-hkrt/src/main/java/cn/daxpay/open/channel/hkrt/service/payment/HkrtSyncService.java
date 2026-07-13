package cn.daxpay.open.channel.hkrt.service.payment;

import cn.daxpay.open.channel.hkrt.client.HkrtChannelClient;
import cn.daxpay.open.channel.hkrt.client.credential.HkrtSdkCredential;
import cn.daxpay.open.channel.hkrt.client.req.HkrtSyncReq;
import cn.daxpay.open.channel.hkrt.client.resp.HkrtSyncResp;
import cn.daxpay.open.payment.trade.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 海科融通服务商订单同步业务服务
///
/// 通过 [HkrtChannelClient] 调用子应用查询海科融通订单状态。
@Slf4j
@Service
@RequiredArgsConstructor
public class HkrtSyncService {

    private final HkrtChannelClient hkrtChannelClient;

    /// 同步订单状态
    public PaySyncResultBo sync(PayTrade order, HkrtSdkCredential credential) {
        HkrtSyncReq req = new HkrtSyncReq();
        req.setCredential(credential);
        req.setOutTradeNo(order.getTradeNo());
        req.setTradeNo(order.getOutOrderNo());

        DaxResult<HkrtSyncResp> result = hkrtChannelClient.sync(req);
        PaySyncResultBo bo = new PaySyncResultBo();
        if (result.getCode() != 0) {
            // 同步失败(不抛异常, 由核心层决定重试)
            bo.setSyncSuccess(false);
            bo.setSyncErrorMsg(result.getMsg());
            return bo;
        }

        HkrtSyncResp resp = result.getData();
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

    /// 海科融通 trade_state → 平台资金状态
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
