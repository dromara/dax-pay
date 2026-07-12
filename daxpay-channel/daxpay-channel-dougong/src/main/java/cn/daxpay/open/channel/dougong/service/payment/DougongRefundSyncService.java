package cn.daxpay.open.channel.dougong.service.payment;

import cn.daxpay.open.channel.dougong.client.DougongChannelClient;
import cn.daxpay.open.channel.dougong.client.credential.DougongSdkCredential;
import cn.daxpay.open.channel.dougong.client.req.DougongRefundSyncReq;
import cn.daxpay.open.channel.dougong.client.resp.DougongRefundSyncResp;
import cn.daxpay.open.payment.common.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.core.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.core.trade.order.entity.PayRefundOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 斗拱服务商退款同步业务服务
///
/// 通过 [DougongChannelClient] 调用子应用查询斗拱(汇付)退款状态。
@Slf4j
@Service
@RequiredArgsConstructor
public class DougongRefundSyncService {

    private final DougongChannelClient dougongChannelClient;

    /// 查询退款状态
    public RefundResultBo sync(PayRefundOrder refundOrder, DougongSdkCredential credential) {
        DougongRefundSyncReq req = new DougongRefundSyncReq();
        req.setCredential(credential);
        // 原汇付退款流水号
        req.setTradeNo(refundOrder.getOutRefundNo());

        DaxResult<DougongRefundSyncResp> result = dougongChannelClient.refundSync(req);
        RefundResultBo bo = new RefundResultBo();
        if (result.getCode() != 0) {
            bo.setComplete(false);
            bo.setStatus(RefundOrderStatusEnum.PROGRESS);
            return bo;
        }

        DougongRefundSyncResp resp = result.getData();
        bo.setOutRefundNo(resp.getTradeNo())
                .setFinishTime(resp.getFinishTime());
        // 退款状态: S→SUCCESS, 其他→PROGRESS
        boolean success = Objects.equals(resp.getRefundState(), "S");
        bo.setStatus(success ? RefundOrderStatusEnum.SUCCESS : RefundOrderStatusEnum.PROGRESS);
        bo.setComplete(success);
        return bo;
    }
}
