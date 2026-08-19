package cn.daxpay.open.payment.unipay.trade.service;

import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.payment.trade.order.dao.RefundOrderManager;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.payment.trade.runtime.service.refund.RefundSyncService;
import cn.daxpay.open.payment.unipay.param.trade.refund.RefundSyncParam;
import cn.daxpay.open.payment.unipay.result.trade.refund.RefundSyncResult;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 退款订单同步服务(对外)
///
/// 对外统一入口: 按平台退款号或商户退款号定位退款单, 委托 [RefundSyncService] 查询通道终态回写。
/// adjust 标记: 同步前后本地状态发生变化即为 true, 让商户感知本次同步是否触发了状态调整。
/// 与支付同步 [cn.daxpay.open.payment.trade.runtime.service.sync.PaySyncService] 的 adjust 语义对齐。
@Slf4j
@Service
@RequiredArgsConstructor
public class RefundOrderSyncService {

    private final RefundOrderManager refundOrderManager;
    private final RefundSyncService refundSyncService;

    /// 退款同步
    public RefundSyncResult sync(RefundSyncParam param) {
        // 校验参数, 平台退款号和商户退款号不能都为空
        if (StrUtil.isBlank(param.getRefundNo()) && StrUtil.isBlank(param.getBizRefundNo())) {
            // 退款: 退款号不能都为空(复用统一接口层通用单号校验 key)
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.orderNoRequired");
        }

        // 按号定位退款单(优先平台退款号)
        RefundOrder refundOrder;
        if (StrUtil.isNotBlank(param.getRefundNo())) {
            refundOrder = refundOrderManager.findByRefundNo(param.getRefundNo())
                    .orElseThrow(() -> new DataNotExistException("pay.error.refund.orderNotFound"));
            // 归属校验: refundNo 为全局唯一编号, 防跨商户触发同步
            if (!Objects.equals(refundOrder.getMchNo(), param.getMchNo())) {
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.orderNotBelong");
            }
        } else {
            // 按商户退款号 + 商户号查询(商户维度定位, 与唯一约束维度一致)
            refundOrder = refundOrderManager.findByBizRefundNoAndMch(param.getBizRefundNo(), param.getMchNo())
                    .orElseThrow(() -> new DataNotExistException("pay.error.refund.orderNotFound"));
        }

        // 记录同步前状态, 同步后比较得出是否调整
        String statusBefore = refundOrder.getStatus();
        RefundOrder synced = refundSyncService.sync(refundOrder);
        String statusAfter = synced.getStatus();
        boolean adjust = !Objects.equals(statusBefore, statusAfter);

        return new RefundSyncResult()
                .setOrderStatus(statusAfter)
                .setAdjust(adjust);
    }
}
