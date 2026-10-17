package cn.daxpay.open.channel.lakala.service.moss;

import cn.daxpay.open.channel.lakala.client.LakalaChannelClient;
import cn.daxpay.open.channel.lakala.client.credential.MossSdkCredential;
import cn.daxpay.open.channel.lakala.client.req.MossSyncReq;
import cn.daxpay.open.channel.lakala.client.resp.MossSyncResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 拉卡拉 MOSS 订单同步业务服务
///
/// 通过 [LakalaChannelClient] 调用子应用查询 MOSS 订单状态。
/// trade_state 仅 SUCCESS 为终态成功, 其余按处理中处理(未支付也属处理中)。
@Slf4j
@Service
@RequiredArgsConstructor
public class LakalaMossSyncService {

    private final LakalaChannelClient lakalaChannelClient;

    /// 同步订单状态
    public PaySyncResultBo sync(PayTrade order, MossSdkCredential credential) {
        MossSyncReq req = new MossSyncReq();
        req.setCredential(credential);
        req.setOutTradeNo(order.getTradeNo());

        DaxResult<MossSyncResp> result = lakalaChannelClient.mossSync(req);
        PaySyncResultBo bo = new PaySyncResultBo();
        if (result.getCode() != 0) {
            // 同步失败(不抛异常, 由核心层决定重试)
            bo.setSyncSuccess(false);
            bo.setSyncErrorMsg(result.getMsg());
            return bo;
        }

        MossSyncResp resp = result.getData();
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

    /// MOSS trade_state → 平台资金状态
    private PayFundStatusEnum mapTradeState(String tradeState) {
        if (Objects.equals(tradeState, "SUCCESS")) {
            return PayFundStatusEnum.SUCCESS;
        }
        if (Objects.equals(tradeState, "FAIL")) {
            return PayFundStatusEnum.FAIL;
        }
        // 其他(处理中/未支付)
        return PayFundStatusEnum.PROCESSING;
    }
}
