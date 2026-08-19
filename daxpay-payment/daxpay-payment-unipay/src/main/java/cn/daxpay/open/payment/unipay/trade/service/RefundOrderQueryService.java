package cn.daxpay.open.payment.unipay.trade.service;

import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.payment.trade.order.dao.RefundOrderManager;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.payment.unipay.param.trade.refund.RefundOrderQueryParam;
import cn.daxpay.open.payment.unipay.result.trade.refund.RefundOrderResult;
import cn.daxpay.open.payment.unipay.trade.convert.UnipayRefundOrderConvert;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 退款订单查询服务(对外)
///
/// 纯查本地退款单, 不调用通道; 需要实时通道状态请走退款同步接口。
/// 支持按平台退款号(refundNo)或商户退款号(bizRefundNo)查询, 优先使用平台退款号。
/// bizRefundNo 幂等唯一维度为商户(uk_refund_order_mch_biz), 定位须绑定商户号。
@Slf4j
@Service
@RequiredArgsConstructor
public class RefundOrderQueryService {

    private final RefundOrderManager refundOrderManager;

    /// 查询退款订单
    public RefundOrderResult queryRefundOrder(RefundOrderQueryParam param) {
        // 校验参数, 平台退款号和商户退款号不能都为空
        if (StrUtil.isBlank(param.getRefundNo()) && StrUtil.isBlank(param.getBizRefundNo())) {
            // 退款: 退款号不能都为空(复用统一接口层通用单号校验 key)
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.orderNoRequired");
        }

        RefundOrder order;
        // 优先按平台退款号查询
        if (StrUtil.isNotBlank(param.getRefundNo())) {
            order = refundOrderManager.findByRefundNo(param.getRefundNo())
                    // 退款: 退款订单不存在
                    .orElseThrow(() -> new DataNotExistException("pay.error.refund.orderNotFound"));
            // 归属校验: refundNo 为全局唯一编号, 防跨商户查单
            if (!Objects.equals(order.getMchNo(), param.getMchNo())) {
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.orderNotBelong");
            }
        } else {
            // 按商户退款号 + 商户号查询(商户维度定位, 与唯一约束维度一致)
            order = refundOrderManager.findByBizRefundNoAndMch(param.getBizRefundNo(), param.getMchNo())
                    .orElseThrow(() -> new DataNotExistException("pay.error.refund.orderNotFound"));
        }
        return UnipayRefundOrderConvert.CONVERT.toResult(order);
    }
}
