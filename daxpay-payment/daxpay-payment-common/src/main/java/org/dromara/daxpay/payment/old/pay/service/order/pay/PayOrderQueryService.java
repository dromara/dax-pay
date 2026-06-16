package org.dromara.daxpay.payment.old.pay.service.order.pay;

import org.dromara.daxpay.platform.common.mybatisplus.util.MpUtil;
import org.dromara.daxpay.platform.core.exception.ValidationFailedException;
import org.dromara.daxpay.platform.core.exception.BizInfoException;
import org.dromara.daxpay.platform.core.code.CommonErrorCode;
import org.dromara.daxpay.platform.core.code.CommonCode;
import org.dromara.daxpay.platform.core.rest.param.PageParam;
import org.dromara.daxpay.platform.core.rest.result.PageResult;
import org.dromara.daxpay.platform.core.exception.operation.OperationFailException;
import org.dromara.daxpay.platform.core.code.CommonCode;

import org.dromara.daxpay.payment.old.pay.convert.order.pay.PayOrderConvert;
import org.dromara.daxpay.payment.old.pay.dao.order.pay.PayOrderExpandManager;
import org.dromara.daxpay.payment.old.pay.dao.order.pay.PayOrderManager;
import org.dromara.daxpay.payment.old.pay.entity.order.pay.PayOrder;
import org.dromara.daxpay.payment.old.pay.entity.order.pay.PayOrderExpand;
import org.dromara.daxpay.payment.old.pay.exception.TradeNotExistException;
import org.dromara.daxpay.payment.old.pay.param.order.pay.PayOrderQuery;
import org.dromara.daxpay.payment.old.pay.result.order.pay.PayOrderExpandResult;
import org.dromara.daxpay.payment.old.pay.result.order.pay.PayOrderVo;
import org.dromara.daxpay.payment.unipay.param.trade.pay.QueryPayParam;
import org.dromara.daxpay.payment.unipay.result.trade.pay.PayOrderResult;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

/// # 支付查询服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class PayOrderQueryService {
    private final PayOrderManager payOrderManager;
    private final PayOrderExpandManager payOrderExpandManager;

    /// 分页
    public PageResult<PayOrderVo> page(PageParam pageParam, PayOrderQuery param) {
        Page<PayOrder> page = payOrderManager.page(pageParam, param);
        return MpUtil.toPageResult(page);
    }

    /// 根据id查询
    public Optional<PayOrder> findById(Long orderId) {
        return payOrderManager.findById(orderId);
    }

    /// 根据ID查询订单扩展参数
    public PayOrderExpandResult findExpandByById(Long id){
        return payOrderExpandManager.findById(id)
                .map(PayOrderExpand::toResult)
                .orElse(new PayOrderExpandResult());
    }

    /// 根据订单号查询
    public Optional<PayOrder> findByOrderNo(String orderNo) {
        return payOrderManager.findByOrderNo(orderNo);
    }
    /// 根据订单号查询
    public Optional<PayOrder> findByOrderNo(String orderNo, String appId) {
        return payOrderManager.findByOrderNo(orderNo,appId);
    }

    /// 根据商户订单号查询
    public Optional<PayOrder> findByBizOrderNo(String bizOrderNo, String appId) {
        return payOrderManager.findByBizOrderNo(bizOrderNo, appId);
    }

    /// 根据通道订单号查询
    public Optional<PayOrder> findByOutOrderNo(String outOrderNo, String appId) {
        return payOrderManager.findByOutOrderNo(outOrderNo, appId);
    }

    /// 根据订单号或商户订单号查询 平台 > 商户 > 通道
    public Optional<PayOrder> findAnyOrderNo(String orderNo, String bizOrderNo, String appId) {
        if (Objects.nonNull(orderNo)){
            return payOrderManager.findByOrderNo(orderNo,appId);
        }
        if (Objects.nonNull(bizOrderNo)){
            return payOrderManager.findByBizOrderNo(bizOrderNo,appId);
        }
        return Optional.empty();
    }

    /// 根据订单号或商户订单号查询 平台 > 商户 > 通道 如果不存在使用下一种查询方式
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

    /// 查询支付记录
    public PayOrderResult queryPayOrder(QueryPayParam param) {
        // 校验参数
        if (StrUtil.isBlank(param.getOrderNo()) && Objects.isNull(param.getBizOrderNo())){
            // 支付订单号不能都为空
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.orderNoRequired");
        }
        // 查询支付单和扩展信息
        PayOrder payOrder = this.findAnyOrderNo(param.getOrderNo(), param.getBizOrderNo(), param.getAppId())
                .orElseThrow(() -> new TradeNotExistException("error.payment.order.payOrderNotExist"));
        PayOrderExpand payOrderExpand = payOrderExpandManager.findById(payOrder.getId())
                .orElseThrow(() -> new TradeNotExistException("error.payment.order.payOrderNotExist"));
        var payOrderResult = PayOrderConvert.CONVERT.toResult(payOrder);
        PayOrderConvert.CONVERT.copy(payOrderExpand,payOrderResult);
        return payOrderResult;
    }

    /// 查询支付总金额
    public BigDecimal getTotalAmount(PayOrderQuery param) {
        return payOrderManager.getTotalAmount(param);
    }

    
    

}
