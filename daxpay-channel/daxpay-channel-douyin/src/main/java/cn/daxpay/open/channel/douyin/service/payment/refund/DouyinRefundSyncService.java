package cn.daxpay.open.channel.douyin.service.payment.refund;

import cn.daxpay.open.channel.douyin.client.DouyinChannelClient;
import cn.daxpay.open.channel.douyin.client.credential.DouyinSdkCredential;
import cn.daxpay.open.channel.douyin.client.req.DouyinRefundSyncReq;
import cn.daxpay.open.channel.douyin.client.resp.DouyinRefundSyncResp;
import cn.daxpay.open.channel.douyin.code.DouyinPayCode;
import cn.daxpay.open.payment.common.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.core.trade.bo.RefundResultBo;
import cn.daxpay.open.payment.core.trade.entity.PayRefundOrder;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

/// # 抖音退款同步业务服务
///
/// 通过 [DouyinChannelClient] 调用子应用查询抖音退款状态,
/// 将抖音 refund_status 映射为平台 [RefundOrderStatusEnum]。
///
/// 映射规则:
/// - SUCCESS → SUCCESS(退款成功)
/// - 其他 → PROGRESS(退款中)
@Slf4j
@Service
@RequiredArgsConstructor
public class DouyinRefundSyncService {

    private final DouyinChannelClient douyinChannelClient;

    /// 执行抖音退款同步查询
    public RefundResultBo sync(PayRefundOrder refundOrder, DouyinSdkCredential credential) {
        DouyinRefundSyncReq req = new DouyinRefundSyncReq();
        req.setOutRefundNo(refundOrder.getRefundNo());
        req.setCredential(credential);

        DaxResult<DouyinRefundSyncResp> result = douyinChannelClient.refundSync(req);
        if (result.getCode() != 0) {
            log.error("抖音通道退款同步失败: refundNo={}, msg={}", refundOrder.getRefundNo(), result.getMsg());
            return new RefundResultBo()
                    .setSyncSuccess(false)
                    .setSyncErrorMsg(result.getMsg());
        }

        return toSyncResult(result.getData());
    }

    /// 解析子应用响应, 映射 refund_status → [RefundOrderStatusEnum]
    private RefundResultBo toSyncResult(DouyinRefundSyncResp resp) {
        RefundResultBo bo = new RefundResultBo();
        bo.setOutRefundNo(resp.getOutRefundNo());

        // 退款完成时间
        if (StrUtil.isNotBlank(resp.getFinishTime())) {
            bo.setFinishTime(OffsetDateTime.parse(resp.getFinishTime()));
        }
        // 退款金额
        bo.setRefundAmount(resp.getRefundAmount());

        // SUCCESS → 退款成功
        if (DouyinPayCode.REFUND_STATUS_SUCCESS.equals(resp.getRefundStatus())) {
            return bo.setComplete(true)
                    .setStatus(RefundOrderStatusEnum.SUCCESS);
        }
        // 异常状态
        if (DouyinPayCode.REFUND_STATUS_ABNORMAL.equals(resp.getRefundStatus())) {
            return bo.setComplete(false)
                    .setStatus(RefundOrderStatusEnum.FAIL)
                    .setSyncSuccess(StrUtil.isBlank(resp.getErrorCode()));
        }
        // 未查询到或处理中
        return bo.setComplete(false)
                .setStatus(RefundOrderStatusEnum.PROGRESS)
                .setSyncSuccess(StrUtil.isBlank(resp.getErrorCode()));
    }
}
