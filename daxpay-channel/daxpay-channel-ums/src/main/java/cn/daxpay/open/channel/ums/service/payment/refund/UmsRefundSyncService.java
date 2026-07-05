package cn.daxpay.open.channel.ums.service.payment.refund;

import cn.daxpay.open.channel.ums.client.UmsChannelClient;
import cn.daxpay.open.channel.ums.client.credential.UmsSdkCredential;
import cn.daxpay.open.channel.ums.client.enums.UmsPayMethod;
import cn.daxpay.open.channel.ums.client.req.UmsRefundSyncReq;
import cn.daxpay.open.channel.ums.client.resp.UmsRefundSyncResp;
import cn.daxpay.open.channel.ums.code.UmsCode;
import cn.daxpay.open.payment.common.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.core.trade.bo.RefundResultBo;
import cn.daxpay.open.payment.core.trade.entity.PayRefundOrder;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

/// # 银联商务退款同步业务服务
///
/// 通过 [UmsChannelClient] 调用子应用查询银联商务退款状态。
@Slf4j
@Service
@RequiredArgsConstructor
public class UmsRefundSyncService {

    private final UmsChannelClient umsChannelClient;

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /// 执行银联商务退款同步查询
    public RefundResultBo sync(PayRefundOrder refundOrder, UmsSdkCredential credential) {
        UmsRefundSyncReq req = new UmsRefundSyncReq();
        req.setOutRefundNo(refundOrder.getRefundNo());
        req.setOutTradeNo(refundOrder.getOrderNo());
        // 首期默认扫码退款查询
        req.setMethod(UmsPayMethod.QRCODE);
        req.setCredential(credential);

        DaxResult<UmsRefundSyncResp> result = umsChannelClient.refundSync(req);
        if (result.getCode() != 0) {
            log.error("银联商务通道退款同步失败: refundNo={}, msg={}", refundOrder.getRefundNo(), result.getMsg());
            return new RefundResultBo()
                    .setSyncSuccess(false)
                    .setSyncErrorMsg(result.getMsg());
        }

        return toSyncResult(result.getData());
    }

    /// 解析子应用响应
    private RefundResultBo toSyncResult(UmsRefundSyncResp resp) {
        RefundResultBo bo = new RefundResultBo();
        bo.setOutRefundNo(resp.getOutRefundNo());
        // 退款完成时间
        if (StrUtil.isNotBlank(resp.getFinishTime())) {
            bo.setFinishTime(OffsetDateTime.parse(resp.getFinishTime(), TIME_FORMAT));
        }
        // 退款金额
        bo.setRefundAmount(resp.getRefundAmount());

        // 统一状态码映射
        return switch (resp.getRefundStatus()) {
            case UmsCode.TRADE_STATUS_SUCCESS -> bo.setComplete(true).setStatus(RefundOrderStatusEnum.SUCCESS);
            case UmsCode.TRADE_STATUS_CLOSED -> bo.setComplete(true).setStatus(RefundOrderStatusEnum.FAIL);
            default -> bo.setComplete(false).setStatus(RefundOrderStatusEnum.PROGRESS);
        };
    }
}
