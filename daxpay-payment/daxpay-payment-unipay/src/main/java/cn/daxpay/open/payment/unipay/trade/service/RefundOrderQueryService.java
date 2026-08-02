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

/// # 退款订单查询服务(对外)
///
/// 纯查本地退款单, 不调用通道; 需要实时通道状态请走退款同步接口。
/// 支持按平台退款号(refundNo)或商户退款号(bizRefundNo)查询, 优先使用平台退款号。
/// 按商户退款号查询时绑定 appId, 避免同商户多应用串单。
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
        } else {
            // 按商户退款号 + 应用号查询(避免串单)
            order = refundOrderManager.findByBizRefundNo(param.getBizRefundNo(), param.getAppId())
                    .orElseThrow(() -> new DataNotExistException("pay.error.refund.orderNotFound"));
        }
        return UnipayRefundOrderConvert.CONVERT.toResult(order);
    }
}
