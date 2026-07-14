package cn.daxpay.open.channel.fuyou.service.payment;

import cn.daxpay.open.channel.fuyou.client.FuyouChannelClient;
import cn.daxpay.open.channel.fuyou.client.credential.FuyouSdkCredential;
import cn.daxpay.open.channel.fuyou.client.req.FuyouSyncReq;
import cn.daxpay.open.channel.fuyou.client.resp.FuyouSyncResp;
import cn.daxpay.open.payment.trade.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.trade.runtime.service.PayTradeContainerFields;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 富友服务商订单同步业务服务
///
/// 通过 [FuyouChannelClient] 调用子应用查询富友订单状态(`/commonQuery`)。
/// 同步状态: SUCCESS / CLOSED / PROGRESS。
@Slf4j
@Service
@RequiredArgsConstructor
public class FuyouSyncService {

    /// 同步状态: 成功(富友 trans_stat)
    private static final String STATUS_SUCCESS = "SUCCESS";
    /// 同步状态: 已关闭
    private static final String STATUS_CLOSED = "CLOSED";

    private final FuyouChannelClient fuyouChannelClient;
    private final PayTradeContainerFields payTradeContainerFields;

    /// 同步订单状态
    public PaySyncResultBo sync(PayTrade order, FuyouSdkCredential credential) {
        FuyouSyncReq req = new FuyouSyncReq();
        req.setCredential(credential);
        var fields = payTradeContainerFields.resolve(order);
        // 富友凭关联订单号(mchnt_order_no) + order_type 查询
        req.setRelationOrderNo(fields.relationOrderNo());
        req.setTradeProduct(fields.tradeProduct());

        DaxResult<FuyouSyncResp> result = fuyouChannelClient.sync(req);
        PaySyncResultBo bo = new PaySyncResultBo();
        if (result.getCode() != 0) {
            bo.setSyncSuccess(false);
            bo.setSyncErrorMsg(result.getMsg());
            return bo;
        }

        FuyouSyncResp resp = result.getData();
        bo.setSyncSuccess(Boolean.TRUE.equals(resp.getSyncSuccess()));
        bo.setSyncErrorMsg(resp.getSyncErrorMsg());
        bo.setOutOrderNo(resp.getOutOrderNo());
        bo.setPayStatus(mapSyncStatus(resp.getSyncStatus()));
        // 成功时补充金额/时间/买家
        if (Objects.equals(resp.getSyncStatus(), STATUS_SUCCESS)) {
            bo.setAmount(resp.getAmount());
            bo.setFinishTime(resp.getFinishTime());
            bo.setBuyerId(resp.getBuyerId());
        }
        return bo;
    }

    /// 富友同步状态 → 平台资金状态
    private PayFundStatusEnum mapSyncStatus(String syncStatus) {
        if (Objects.equals(syncStatus, STATUS_SUCCESS)) {
            return PayFundStatusEnum.SUCCESS;
        }
        if (Objects.equals(syncStatus, STATUS_CLOSED)) {
            return PayFundStatusEnum.CLOSE;
        }
        // PROGRESS / 未知视为处理中
        return PayFundStatusEnum.PROCESSING;
    }
}
