package cn.daxpay.multi.service.service.order.pay;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.ValidationFailedException;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.daxpay.multi.core.exception.TradeNotExistException;
import cn.daxpay.multi.core.param.payment.pay.QueryPayParam;
import cn.daxpay.multi.service.convert.order.pay.PayOrderConvert;
import cn.daxpay.multi.service.dao.order.pay.PayOrderManager;
import cn.daxpay.multi.service.entity.order.pay.PayOrder;
import cn.daxpay.multi.service.param.order.pay.PayOrderQuery;
import cn.daxpay.multi.service.result.order.pay.PayOrderResult;
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
    public PageResult<PayOrderResult> page(PageParam pageParam, PayOrderQuery param) {
        Page<PayOrder> page = payOrderManager.page(pageParam, param);
        return MpUtil.toPageResult(page);
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
            throw new ValidationFailedException("业务号或支付单ID不能都为空");
        }
        // 查询支付单
        return this.findByBizOrOrderNo(param.getOrderNo(), param.getBizOrderNoeNo())
                .map(PayOrderConvert.CONVERT::toResult)
                .orElseThrow(() -> new TradeNotExistException("支付订单不存在"));
    }


    /**
     * 查询支付总金额
     */
    public Integer getTotalAmount(PayOrderQuery param) {
        return payOrderManager.getTalAmount(param);
    }
}
