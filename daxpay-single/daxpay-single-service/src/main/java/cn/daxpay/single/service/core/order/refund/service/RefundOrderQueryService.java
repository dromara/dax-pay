package cn.daxpay.single.service.core.order.refund.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.single.core.exception.ParamValidationFailException;
import cn.daxpay.single.core.exception.PayFailureException;
import cn.daxpay.single.core.exception.TradeNotExistException;
import cn.daxpay.single.core.param.payment.refund.QueryRefundParam;
import cn.daxpay.single.core.result.order.RefundOrderResult;
import cn.daxpay.single.service.core.order.refund.convert.RefundOrderConvert;
import cn.daxpay.single.service.core.order.refund.dao.RefundOrderManager;
import cn.daxpay.single.service.core.order.refund.entity.RefundOrder;
import cn.daxpay.single.service.dto.order.refund.RefundOrderDto;
import cn.daxpay.single.service.param.order.RefundOrderQuery;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

/**
 * 退款查询接口
 * @author xxm
 * @since 2024/4/24
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RefundOrderQueryService {
    private final RefundOrderManager refundOrderManager;

    /**
     * 分页查询
     */
    public PageResult<RefundOrderDto> page(PageParam pageParam, RefundOrderQuery query) {
        Page<RefundOrder> page = refundOrderManager.page(pageParam, query);
        return MpUtil.convert2DtoPageResult(page);
    }

    /**
     * 根据id查询
     */
    public RefundOrderDto findById(Long id) {
        return refundOrderManager.findById(id).map(RefundOrder::toDto)
                .orElseThrow(() -> new DataNotExistException("退款订单不存在"));
    }

    /**
     * 根据退款号查询
     */
    public RefundOrderDto findByRefundNo(String refundNo){
        return refundOrderManager.findByRefundNo(refundNo).map(RefundOrder::toDto)
                .orElseThrow(() -> new DataNotExistException("退款订单扩展信息不存在"));

    }

    /**
     * 根据退款号和商户退款号查询
     */
    public Optional<RefundOrder> findByBizOrRefundNo(String refundNo, String bizRefundNo) {
        if (StrUtil.isNotBlank(refundNo)){
            return refundOrderManager.findByRefundNo(refundNo);
        } else if (StrUtil.isNotBlank(bizRefundNo)){
            return refundOrderManager.findByBizRefundNo(bizRefundNo);
        } else {
            return Optional.empty();
        }
    }

    /**
     * 查询退款订单
     */
    public RefundOrderResult queryRefundOrder(QueryRefundParam param) {
        // 校验参数
        if (StrUtil.isBlank(param.getRefundNo()) && Objects.isNull(param.getBizRefundNo())){
            throw new ParamValidationFailException("退款号或商户退款号不能都为空");
        }
        // 查询退款单
        RefundOrder refundOrder = this.findByBizOrRefundNo(param.getRefundNo(), param.getBizRefundNo())
                .orElseThrow(() -> new TradeNotExistException("退款订单不存在"));

        return RefundOrderConvert.CONVERT.convertResult(refundOrder);
    }

    /**
     * 查询支付总金额
     */
    public Integer getTotalAmount(RefundOrderQuery param) {
        return refundOrderManager.getTalAmount(param);
    }

}
