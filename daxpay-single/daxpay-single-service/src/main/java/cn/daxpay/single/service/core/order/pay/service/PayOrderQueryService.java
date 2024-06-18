package cn.daxpay.single.service.core.order.pay.service;

import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.single.core.exception.ParamValidationFailedException;
import cn.daxpay.single.core.exception.TradeNotExistException;
import cn.daxpay.single.core.param.payment.pay.QueryPayParam;
import cn.daxpay.single.core.result.order.PayOrderResult;
import cn.daxpay.single.service.core.order.pay.convert.PayOrderConvert;
import cn.daxpay.single.service.core.order.pay.dao.PayOrderManager;
import cn.daxpay.single.service.core.order.pay.entity.PayOrder;
import cn.daxpay.single.service.dto.order.pay.PayOrderDto;
import cn.daxpay.single.service.param.order.PayOrderQuery;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

/**
 * 支付查询服务
 * @author xxm
 * @since 2024/1/16
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayOrderQueryService {
    private final PayOrderManager payOrderManager;

    /**
     * 分页
     */
    public PageResult<PayOrderDto> page(PageParam pageParam, PayOrderQuery param) {
        Page<PayOrder> page = payOrderManager.page(pageParam, param);
        return MpUtil.convert2DtoPageResult(page);
    }

    /**
     * 根据id查询
     */
    public Optional<PayOrder> findById(Long orderId) {
        return payOrderManager.findById(orderId);
    }

    /**
     * 根据订单号查询
     */
    public Optional<PayOrder> findByOrderNo(String orderNo) {
        return payOrderManager.findByOrderNo(orderNo);
    }

    /**
     * 根据商户订单号查询
     */
    public Optional<PayOrder> findByBizOrderNo(String bizOrderNo) {
        return payOrderManager.findByBizOrderNo(bizOrderNo);
    }

    /**
     * 根据订单号或商户订单号查询
     */
    public Optional<PayOrder> findByBizOrOrderNo(String orderNo, String bizOrderNo) {
        if (Objects.nonNull(orderNo)){
            return this.findByOrderNo(orderNo);
        }
        if (Objects.nonNull(bizOrderNo)){
            return this.findByBizOrderNo(bizOrderNo);
        }
        return Optional.empty();
    }

    /**
     * 查询支付记录
     */
    public PayOrderResult queryPayOrder(QueryPayParam param) {
        // 校验参数
        if (StrUtil.isBlank(param.getBizOrderNoeNo()) && Objects.isNull(param.getOrderNo())){
            throw new ParamValidationFailedException("业务号或支付单ID不能都为空");
        }
        // 查询支付单
        PayOrder payOrder = this.findByBizOrOrderNo(param.getOrderNo(), param.getBizOrderNoeNo())
                .orElseThrow(() -> new TradeNotExistException("支付订单不存在"));
        return PayOrderConvert.CONVERT.convertResult(payOrder);
    }


    /**
     * 查询支付总金额
     */
    public Integer getTotalAmount(PayOrderQuery param) {
        return payOrderManager.getTalAmount(param);
    }
}
