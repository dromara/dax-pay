package org.dromara.daxpay.service.service.checkout;

import cn.bootx.platform.core.exception.ValidationFailedException;
import cn.bootx.platform.core.util.BigDecimalUtil;
import cn.bootx.platform.core.util.DateTimeUtil;
import cn.bootx.platform.starter.redis.delay.service.DelayJobService;
import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.core.enums.PayAllocStatusEnum;
import org.dromara.daxpay.core.enums.PayRefundStatusEnum;
import org.dromara.daxpay.core.enums.PayStatusEnum;
import org.dromara.daxpay.core.exception.AmountExceedLimitException;
import org.dromara.daxpay.core.exception.TradeStatusErrorException;
import org.dromara.daxpay.core.param.checkout.CheckoutCreatParam;
import org.dromara.daxpay.core.util.PayUtil;
import org.dromara.daxpay.core.util.TradeNoGenerateUtil;
import org.dromara.daxpay.service.code.DaxPayCode;
import org.dromara.daxpay.service.common.context.MchAppLocal;
import org.dromara.daxpay.service.common.local.PaymentContextLocal;
import org.dromara.daxpay.service.dao.order.pay.PayOrderManager;
import org.dromara.daxpay.service.entity.order.pay.PayOrder;
import org.dromara.daxpay.service.service.order.pay.PayOrderQueryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 收银台支撑服务
 * @author xxm
 * @since 2024/11/26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CheckoutAssistService {
    private final DelayJobService delayJobService;
    private final PayOrderManager payOrderManager;
    private final PayOrderQueryService payOrderQueryService;

    /**
     * 校验支付状态，支付成功则返回，支付失败则抛出对应的异常
     */
    public PayOrder getOrderAndCheck(String orderNo) {
        PayOrder payOrder = payOrderQueryService.findByOrderNo(orderNo).orElse(null);
        return getOrderAndCheck(payOrder);
    }

    /**
     * 校验支付状态，支付成功则返回，支付失败则抛出对应的异常
     */
    public PayOrder getOrderAndCheck(CheckoutCreatParam param) {
        // 根据订单查询支付记录
        PayOrder payOrder = payOrderQueryService.findByBizOrderNo(param.getBizOrderNo(), param.getAppId()).orElse(null);
        return getOrderAndCheck(payOrder);
    }/**
     * 校验支付状态，支付成功则返回，支付失败则抛出对应的异常
     */
    public PayOrder getOrderAndCheck(PayOrder payOrder) {
        if (Objects.nonNull(payOrder)) {
            // 已经支付状态
            if (PayStatusEnum.SUCCESS.getCode()
                    .equals(payOrder.getStatus())) {
                throw new TradeStatusErrorException("已经支付成功，请勿重新支付");
            }
            // 支付失败类型状态
            List<String> tradesStatus = List.of(
                    PayStatusEnum.FAIL.getCode(),
                    PayStatusEnum.CLOSE.getCode(),
                    PayStatusEnum.CANCEL.getCode());
            if (tradesStatus.contains(payOrder.getStatus())) {
                throw new TradeStatusErrorException("支付失败或已经被关闭");
            }
            // 退款类型状态
            if (Objects.equals(payOrder.getRefundStatus(), PayRefundStatusEnum.REFUNDING.getCode())) {
                throw new TradeStatusErrorException("该订单处于退款状态");
            }
            // 待支付和支付中返回订单对象
            if (List.of(PayStatusEnum.WAIT.getCode(), PayStatusEnum.PROGRESS.getCode()).contains(payOrder.getStatus())){
                return payOrder;
            }
            // 其他状态直接抛出兜底异常
            throw new TradeStatusErrorException("订单不是待支付状态，请重新确认订单状态");
        }
        return null;
    }

    /**
     * 检验订单是否超过限额
     */
    public void validationLimitAmount(CheckoutCreatParam checkoutParam) {
        MchAppLocal mchAppInfo = PaymentContextLocal.get()
                .getMchAppInfo();
        // 总额校验
        if (BigDecimalUtil.isGreaterThan(checkoutParam.getAmount(),mchAppInfo.getLimitAmount())) {
            throw new AmountExceedLimitException("支付金额超过限额");
        }
    }


    /**
     * 校验订单超时时间是否正常
     */
    public void validationExpiredTime(CheckoutCreatParam payParam) {
        LocalDateTime expiredTime = this.getExpiredTime(payParam);
        if (Objects.nonNull(expiredTime) && DateTimeUtil.lt(expiredTime,LocalDateTime.now())) {
            throw new ValidationFailedException("支付超时时间设置有误, 请检查!");
        }
    }



    /**
     * 创建支付订单并保存, 返回支付订单
     */
    @Transactional(rollbackFor = Exception.class)
    public PayOrder createPayOrder(CheckoutCreatParam checkoutParam) {
        // 订单超时时间
        LocalDateTime expiredTime = this.getExpiredTime(checkoutParam);
        // 构建支付订单对象
        PayOrder order = new PayOrder();
        BeanUtil.copyProperties(checkoutParam, order);
        order.setOrderNo(TradeNoGenerateUtil.pay())
                .setStatus(PayStatusEnum.WAIT.getCode())
                .setRefundStatus(PayRefundStatusEnum.NO_REFUND.getCode())
                .setExpiredTime(expiredTime)
                .setAttach(checkoutParam.getAttach())
                .setReqTime(checkoutParam.getReqTime())
                .setNotifyUrl(checkoutParam.getNotifyUrl())
                .setReturnUrl(checkoutParam.getReturnUrl())
                .setRefundableBalance(checkoutParam.getAmount());
        // 如果支持分账, 设置分账状态为待分账
        if (order.getAllocation()) {
            order.setAllocStatus(PayAllocStatusEnum.WAITING.getCode());
        }
        payOrderManager.save(order);
        // 注册支付超时任务
        delayJobService.registerByTransaction(order.getId(), DaxPayCode.Event.ORDER_PAY_TIMEOUT, order.getExpiredTime());
        return order;
    }


    /**
     * 获取支付订单超时时间
     */
    private LocalDateTime getExpiredTime(CheckoutCreatParam payParam) {
        MchAppLocal mchAppLocal = PaymentContextLocal.get().getMchAppInfo();
        // 支付参数传入
        if (Objects.nonNull(payParam.getExpiredTime())) {
            return payParam.getExpiredTime();
        }
        // 根据商户应用配置计算出时间
        return PayUtil.getPaymentExpiredTime(mchAppLocal.getOrderTimeout());
    }

}
