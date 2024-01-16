package cn.bootx.platform.daxpay.service.core.order.refund.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.exception.ValidationFailedException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.daxpay.param.pay.QueryRefundParam;
import cn.bootx.platform.daxpay.result.order.RefundOrderResult;
import cn.bootx.platform.daxpay.service.core.order.refund.convert.PayRefundConvert;
import cn.bootx.platform.daxpay.service.core.order.refund.dao.PayRefundOrderManager;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.PayRefundOrder;
import cn.bootx.platform.daxpay.service.dto.order.refund.PayRefundOrderDto;
import cn.bootx.platform.daxpay.service.param.order.PayRefundOrderQuery;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 退款
 *
 * @author xxm
 * @since 2022/3/2
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayRefundOrderService {

    private final PayRefundOrderManager refundOrderManager;

    /**
     * 分页查询
     */
    public PageResult<PayRefundOrderDto> page(PageParam pageParam, PayRefundOrderQuery query) {
        Page<PayRefundOrder> page = refundOrderManager.page(pageParam, query);
        return MpUtil.convert2DtoPageResult(page);
    }

    /**
     * 根据id查询
     */
    public PayRefundOrderDto findById(Long id) {
        return refundOrderManager.findById(id).map(PayRefundOrder::toDto).orElseThrow(DataNotExistException::new);
    }

    /**
     * 查询退款订单
     */
    public RefundOrderResult queryRefundOrder(QueryRefundParam param) {
        // 校验参数
        if (StrUtil.isBlank(param.getRefundNo()) && Objects.isNull(param.getRefundId())){
            throw new ValidationFailedException("退款号或退款ID不能都为空");
        }

        // 查询支付单
        PayRefundOrder refundOrder = null;
        if (Objects.nonNull(param.getRefundId())){
            refundOrder = refundOrderManager.findById(param.getRefundId())
                    .orElseThrow(() -> new DataNotExistException("未查询到支付订单"));
        }
        if (Objects.isNull(refundOrder)){
            refundOrder = refundOrderManager.findByRefundNo(param.getRefundNo())
                    .orElseThrow(() -> new DataNotExistException("未查询到支付订单"));
        }
        return PayRefundConvert.CONVERT.convertResult(refundOrder);
    }
}
