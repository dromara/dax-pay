package cn.daxpay.open.channel.vbill.service.payment;

import cn.daxpay.open.channel.vbill.client.VbillChannelClient;
import cn.daxpay.open.channel.vbill.client.credential.VbillSdkCredential;
import cn.daxpay.open.channel.vbill.client.req.VbillSyncReq;
import cn.daxpay.open.channel.vbill.client.resp.VbillSyncResp;
import cn.daxpay.open.payment.trade.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 随行付服务商订单同步业务服务
///
/// 通过 [VbillChannelClient] 调用子应用查询随行付订单状态(`/query/tradeQuery`)。
/// tradeState: SUCCESS / PAYING / FAIL / CLOSED。
@Slf4j
@Service
@RequiredArgsConstructor
public class VbillSyncService {

    private final VbillChannelClient vbillChannelClient;

    /// 同步订单状态
    public PaySyncResultBo sync(PayTrade order, VbillSdkCredential credential) {
        VbillSyncReq req = new VbillSyncReq();
        req.setCredential(credential);
        // 优先用网关订单号 uuid, 兜底用商户订单号
        req.setOutOrderNo(order.getOutOrderNo());
        req.setOutTradeNo(order.getTradeNo());

        DaxResult<VbillSyncResp> result = vbillChannelClient.sync(req);
        PaySyncResultBo bo = new PaySyncResultBo();
        if (result.getCode() != 0) {
            // 同步失败(不抛异常, 由核心层决定重试)
            bo.setSyncSuccess(false);
            bo.setSyncErrorMsg(result.getMsg());
            return bo;
        }

        VbillSyncResp resp = result.getData();
        bo.setSyncSuccess(true);
        bo.setSyncData(resp.getSyncData());
        bo.setOutOrderNo(resp.getOutOrderNo());
        bo.setPayStatus(mapTradeState(resp.getTradeState()));
        // 成功时补充金额/时间/买家
        if (Objects.equals(resp.getTradeState(), "SUCCESS")) {
            bo.setAmount(resp.getTotalAmount());
            bo.setRealAmount(resp.getRealAmount());
            bo.setFinishTime(resp.getFinishTime());
            bo.setBuyerId(resp.getBuyerId());
        }
        return bo;
    }

    /// 随行付 tranSts → 平台资金状态
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
        // 其他(PAYING/未知)视为处理中
        return PayFundStatusEnum.PROCESSING;
    }
}
