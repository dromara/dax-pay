package cn.daxpay.open.channel.dougong.service.payment;

import cn.daxpay.open.channel.dougong.client.DougongChannelClient;
import cn.daxpay.open.channel.dougong.client.credential.DougongSdkCredential;
import cn.daxpay.open.channel.dougong.client.req.DougongSyncReq;
import cn.daxpay.open.channel.dougong.client.resp.DougongSyncResp;
import cn.daxpay.open.payment.common.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 斗拱服务商订单同步业务服务
///
/// 通过 [DougongChannelClient] 调用子应用查询斗拱(汇付)订单状态。
@Slf4j
@Service
@RequiredArgsConstructor
public class DougongSyncService {

    private final DougongChannelClient dougongChannelClient;

    /// 同步订单状态
    public PaySyncResultBo sync(PayTrade order, DougongSdkCredential credential) {
        DougongSyncReq req = new DougongSyncReq();
        req.setCredential(credential);
        // 原汇付支付流水号
        req.setTradeNo(order.getOutOrderNo());

        DaxResult<DougongSyncResp> result = dougongChannelClient.sync(req);
        PaySyncResultBo bo = new PaySyncResultBo();
        if (result.getCode() != 0) {
            // 同步失败(不抛异常, 由核心层决定重试)
            bo.setSyncSuccess(false);
            bo.setSyncErrorMsg(result.getMsg());
            return bo;
        }

        DougongSyncResp resp = result.getData();
        bo.setSyncSuccess(true);
        bo.setSyncData(resp.getSyncData());
        bo.setOutOrderNo(resp.getTradeNo());
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

    /// 子应用 tradeState(已映射为 SUCCESS/FAIL/CLOSE/原值) → 平台资金状态
    private PayFundStatusEnum mapTradeState(String tradeState) {
        if (Objects.equals(tradeState, "SUCCESS")) {
            return PayFundStatusEnum.SUCCESS;
        }
        if (Objects.equals(tradeState, "FAIL")) {
            return PayFundStatusEnum.FAIL;
        }
        if (Objects.equals(tradeState, "CLOSE")) {
            return PayFundStatusEnum.CLOSE;
        }
        // 其他(处理中)
        return PayFundStatusEnum.PROCESSING;
    }
}
