package cn.daxpay.open.channel.leshua.service.payment;

import cn.daxpay.open.channel.leshua.client.LeshuaChannelClient;
import cn.daxpay.open.channel.leshua.client.credential.LeshuaSdkCredential;
import cn.daxpay.open.channel.leshua.client.req.LeshuaSyncReq;
import cn.daxpay.open.channel.leshua.client.resp.LeshuaSyncResp;
import cn.daxpay.open.channel.leshua.code.LeshuaCode;
import cn.daxpay.open.payment.common.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 乐刷服务商订单同步业务服务
///
/// 通过 [LeshuaChannelClient] 调用子应用查询乐刷订单状态。
@Slf4j
@Service
@RequiredArgsConstructor
public class LeshuaSyncService {

    private final LeshuaChannelClient leshuaChannelClient;

    /// 同步订单状态
    public PaySyncResultBo sync(PayTrade order, LeshuaSdkCredential credential) {
        LeshuaSyncReq req = new LeshuaSyncReq();
        req.setCredential(credential);
        req.setLeshuaOrderId(order.getOutOrderNo());
        req.setOutTradeNo(order.getTradeNo());

        DaxResult<LeshuaSyncResp> result = leshuaChannelClient.sync(req);
        PaySyncResultBo bo = new PaySyncResultBo();
        if (result.getCode() != 0) {
            // 同步失败(不抛异常, 由核心层决定重试)
            bo.setSyncSuccess(false);
            bo.setSyncErrorMsg(result.getMsg());
            return bo;
        }

        LeshuaSyncResp resp = result.getData();
        bo.setSyncSuccess(true);
        bo.setSyncData(resp.getSyncData());
        bo.setOutOrderNo(resp.getLeshuaOrderId());
        bo.setPayStatus(mapTradeState(resp.getTradeState()));
        // 成功时补充金额/时间/买家
        if (Objects.equals(resp.getTradeState(), LeshuaCode.PAY_STATUS_SUCCESS)) {
            bo.setAmount(resp.getTotalAmount());
            bo.setRealAmount(resp.getRealAmount());
            bo.setFinishTime(resp.getFinishTime());
            bo.setBuyerId(resp.getBuyerId());
        }
        return bo;
    }

    /// 乐刷 status → 平台资金状态
    private PayFundStatusEnum mapTradeState(String tradeState) {
        if (Objects.equals(tradeState, LeshuaCode.PAY_STATUS_SUCCESS)) {
            return PayFundStatusEnum.SUCCESS;
        }
        if (Objects.equals(tradeState, LeshuaCode.PAY_STATUS_FAIL)) {
            return PayFundStatusEnum.FAIL;
        }
        if (Objects.equals(tradeState, LeshuaCode.PAY_STATUS_CLOSE)) {
            return PayFundStatusEnum.CLOSE;
        }
        // 其他(支付中)
        return PayFundStatusEnum.PROCESSING;
    }
}
