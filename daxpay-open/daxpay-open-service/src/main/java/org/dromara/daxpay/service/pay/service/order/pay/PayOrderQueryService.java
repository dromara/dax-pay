package org.dromara.daxpay.service.pay.service.order.pay;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.ValidationFailedException;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import org.dromara.daxpay.core.exception.TradeNotExistException;
import org.dromara.daxpay.core.param.trade.pay.QueryPayParam;
import org.dromara.daxpay.core.result.trade.pay.PayOrderResult;
import org.dromara.daxpay.service.pay.convert.order.pay.PayOrderConvert;
import org.dromara.daxpay.service.pay.dao.order.pay.PayOrderExpandManager;
import org.dromara.daxpay.service.pay.dao.order.pay.PayOrderManager;
import org.dromara.daxpay.service.pay.entity.order.pay.PayOrder;
import org.dromara.daxpay.service.pay.entity.order.pay.PayOrderExpand;
import org.dromara.daxpay.service.pay.param.order.pay.PayOrderQuery;
import org.dromara.daxpay.service.pay.result.order.pay.PayOrderExpandResult;
import org.dromara.daxpay.service.pay.result.order.pay.PayOrderVo;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    private final PayOrderExpandManager payOrderExpandManager;

    /**
     * 分页
     */
    public PageResult<PayOrderVo> page(PageParam pageParam, PayOrderQuery param) {
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
     * 根据ID查询订单扩展参数
     */
    public PayOrderExpandResult findExpandByById(Long id){
        return payOrderExpandManager.findById(id)
                .map(PayOrderExpand::toResult)
                .orElse(new PayOrderExpandResult());
    }

    /**
     * 根据订单号查询
     */
    public Optional<PayOrder> findByOrderNo(String orderNo) {
        return payOrderManager.findByOrderNo(orderNo);
    }
    /**
     * 根据订单号查询
     */
    public Optional<PayOrder> findByOrderNo(String orderNo, String appId) {
        return payOrderManager.findByOrderNo(orderNo,appId);
    }

    /**
     * 根据商户订单号查询
     */
    public Optional<PayOrder> findByBizOrderNo(String bizOrderNo, String appId) {
        return payOrderManager.findByBizOrderNo(bizOrderNo, appId);
    }

    /**
     * 根据通道订单号查询
     */
    public Optional<PayOrder> findByOutOrderNo(String outOrderNo, String appId) {
        return payOrderManager.findByOutOrderNo(outOrderNo, appId);
    }

    /**
     * 根据订单号或商户订单号查询 平台 > 商户 > 通道
     */
    public Optional<PayOrder> findAnyOrderNo(String orderNo, String bizOrderNo, String outOrderNo, String appId) {
        if (Objects.nonNull(orderNo)){
            return payOrderManager.findByOrderNo(orderNo,appId);
        }
        if (Objects.nonNull(bizOrderNo)){
            return payOrderManager.findByBizOrderNo(bizOrderNo,appId);
        }
        if (Objects.nonNull(outOrderNo)){
            return payOrderManager.findByOutOrderNo(outOrderNo,appId);
        }
        return Optional.empty();
    }

    /**
     * 根据订单号或商户订单号查询 平台 > 商户 > 通道 如果不存在使用下一种查询方式
     */
    public Optional<PayOrder> findAnyOrderNo(String orderNo, String appId) {
        Optional<PayOrder> optional = payOrderManager.findByOrderNo(orderNo, appId);
        if (optional.isEmpty()){
            optional = payOrderManager.findByBizOrderNo(orderNo,appId);
        }
        if (optional.isEmpty()){
            optional = payOrderManager.findByOutOrderNo(orderNo,appId);
        }
        return optional;
    }

    /**
     * 查询支付记录
     */
    public PayOrderResult queryPayOrder(QueryPayParam param) {
        // 校验参数
        if (StrUtil.isBlank(param.getOrderNo()) && Objects.isNull(param.getBizOrderNo())&& Objects.isNull(param.getOutOrderNo())){
            throw new ValidationFailedException("支付订单号不能都为空");
        }
        // 查询支付单
        return this.findAnyOrderNo(param.getOrderNo(), param.getBizOrderNo(), param.getOutOrderNo(), param.getAppId())
                .map(PayOrderConvert.CONVERT::toResult)
                .orElseThrow(() -> new TradeNotExistException("支付订单不存在"));
    }

    /**
     * 查询支付总金额
     */
    public BigDecimal getTotalAmount(PayOrderQuery param) {
        return payOrderManager.getTotalAmount(param);
    }
}
