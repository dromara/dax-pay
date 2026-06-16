package org.dromara.daxpay.payment.old.pay.service.order.refund;

import org.dromara.daxpay.platform.common.mybatisplus.util.MpUtil;
import org.dromara.daxpay.platform.core.exception.DataNotExistException;
import org.dromara.daxpay.platform.core.exception.ValidationFailedException;
import org.dromara.daxpay.platform.core.exception.BizInfoException;
import org.dromara.daxpay.platform.core.code.CommonErrorCode;
import org.dromara.daxpay.platform.core.code.CommonCode;
import org.dromara.daxpay.platform.core.rest.param.PageParam;
import org.dromara.daxpay.platform.core.rest.result.PageResult;
import org.dromara.daxpay.platform.core.exception.operation.OperationFailException;
import org.dromara.daxpay.platform.core.code.CommonCode;

import org.dromara.daxpay.payment.old.pay.exception.TradeNotExistException;
import org.dromara.daxpay.payment.unipay.param.trade.refund.QueryRefundParam;
import org.dromara.daxpay.payment.unipay.result.trade.refund.RefundOrderResult;
import org.dromara.daxpay.payment.old.pay.convert.order.refund.RefundOrderConvert;
import org.dromara.daxpay.payment.old.pay.dao.order.refund.RefundOrderManager;
import org.dromara.daxpay.payment.old.pay.entity.order.refund.RefundOrder;
import org.dromara.daxpay.payment.old.pay.param.order.refund.RefundOrderQuery;
import org.dromara.daxpay.payment.old.pay.result.order.refund.RefundOrderVo;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

/// # 退款查询接口
///
@Slf4j
@Service
@RequiredArgsConstructor
public class RefundOrderQueryService {
    private final RefundOrderManager refundOrderManager;

    /// 分页查询
    public PageResult<RefundOrderVo> page(PageParam pageParam, RefundOrderQuery query) {
        Page<RefundOrder> page = refundOrderManager.page(pageParam, query);
        return MpUtil.toPageResult(page);
    }

    /// 根据id查询
    public RefundOrderVo findById(Long id) {
        return refundOrderManager.findById(id).map(RefundOrder::toResult)
                // 订单: 退款订单不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.order.refundOrderNotExist"));
    }

    /// 根据退款号查询
    public RefundOrderVo findByRefundNo(String refundNo){
        return refundOrderManager.findByRefundNo(refundNo).map(RefundOrder::toResult)
                // 订单: 退款订单扩展信息不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.order.refundOrderExtNotExist"));

    }

    /// 根据退款号和商户退款号查询
    public Optional<RefundOrder> findByBizOrRefundNo(String refundNo, String bizRefundNo, String appId) {
        if (StrUtil.isNotBlank(refundNo)){
            return refundOrderManager.findByRefundNo(refundNo);
        } else if (StrUtil.isNotBlank(bizRefundNo)){
            return refundOrderManager.findByBizRefundNo(bizRefundNo, appId);
        } else {
            return Optional.empty();
        }
    }

    /// 查询退款订单
    public RefundOrderResult queryRefundOrder(QueryRefundParam param) {
        // 校验参数
        if (StrUtil.isBlank(param.getRefundNo()) && Objects.isNull(param.getBizRefundNo())){
            // 退款号或商户退款号不能都为空
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.orderNoRequired");
        }
        // 查询退款单
        RefundOrder refundOrder = this.findByBizOrRefundNo(param.getRefundNo(), param.getBizRefundNo(),param.getAppId())
                // 订单: 退款订单不存在
                .orElseThrow(() -> new TradeNotExistException("error.payment.order.refundOrderNotExist"));

        return RefundOrderConvert.CONVERT.toResult(refundOrder);
    }

    /// 查询退款总金额
    public BigDecimal getTotalAmount(RefundOrderQuery param) {
        return refundOrderManager.getTotalAmount(param);
    }

    
    

}
