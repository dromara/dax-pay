package cn.bootx.platform.daxpay.service.core.order.refund.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.exception.ValidationFailedException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.daxpay.param.payment.refund.QueryRefundParam;
import cn.bootx.platform.daxpay.result.order.RefundOrderResult;
import cn.bootx.platform.daxpay.service.core.order.refund.convert.RefundOrderConvert;
import cn.bootx.platform.daxpay.service.core.order.refund.dao.RefundOrderExtraManager;
import cn.bootx.platform.daxpay.service.core.order.refund.dao.RefundOrderManager;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.RefundOrder;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.RefundOrderExtra;
import cn.bootx.platform.daxpay.service.dto.order.refund.RefundOrderDto;
import cn.bootx.platform.daxpay.service.dto.order.refund.RefundOrderExtraDto;
import cn.bootx.platform.daxpay.service.param.order.RefundOrderQuery;
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
    private final RefundOrderExtraManager refundOrderExtraManager;

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
     * 根据id查询扩展信息
     */
    public RefundOrderExtraDto findExtraById(Long id) {
        return refundOrderExtraManager.findById(id).map(RefundOrderExtra::toDto)
                .orElseThrow(() -> new DataNotExistException("退款订单扩展信息不存在"));
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
            throw new ValidationFailedException("退款号或退款ID不能都为空");
        }
        // 查询退款单
        RefundOrder refundOrder = null;
        if (Objects.nonNull(param.getRefundNo())){
            refundOrder = refundOrderManager.findById(param.getRefundNo())
                    .orElseThrow(() -> new DataNotExistException("未查询到支付订单"));
        }
        if (Objects.isNull(refundOrder)){
            refundOrder = refundOrderManager.findByRefundNo(param.getRefundNo())
                    .orElseThrow(() -> new DataNotExistException("未查询到支付订单"));
        }

        return RefundOrderConvert.CONVERT.convertResult(refundOrder);
    }

}
