package org.dromara.daxpay.service.service.trade.pay;

import cn.bootx.platform.core.exception.ValidationFailedException;
import cn.bootx.platform.core.util.BigDecimalUtil;
import cn.bootx.platform.core.util.DateTimeUtil;
import cn.bootx.platform.starter.redis.delay.service.DelayJobService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.core.enums.PayAllocStatusEnum;
import org.dromara.daxpay.core.enums.PayRefundStatusEnum;
import org.dromara.daxpay.core.enums.PayStatusEnum;
import org.dromara.daxpay.core.exception.AmountExceedLimitException;
import org.dromara.daxpay.core.exception.TradeStatusErrorException;
import org.dromara.daxpay.core.param.trade.pay.PayParam;
import org.dromara.daxpay.core.result.trade.pay.PayResult;
import org.dromara.daxpay.core.util.PayUtil;
import org.dromara.daxpay.core.util.TradeNoGenerateUtil;
import org.dromara.daxpay.service.bo.trade.PayResultBo;
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
 * 支付支持服务
 * @author xxm
 * @since 2023/12/24
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayAssistService {

    private final PayOrderManager payOrderManager;
    private final PayOrderQueryService payOrderQueryService;
    private final PaySyncService paySyncService;
    private final DelayJobService delayJobService;

    /**
     * 创建支付订单并保存, 返回支付订单
     */
    @Transactional(rollbackFor = Exception.class)
    public PayOrder createPayOrder(PayParam payParam) {
        // 订单超时时间
        LocalDateTime expiredTime = this.getExpiredTime(payParam);
        // 构建支付订单对象
        PayOrder order = new PayOrder();
        BeanUtil.copyProperties(payParam, order);
        order.setOrderNo(TradeNoGenerateUtil.pay())
                .setStatus(PayStatusEnum.PROGRESS.getCode())
                .setRefundStatus(PayRefundStatusEnum.NO_REFUND.getCode())
                .setExpiredTime(expiredTime)
                .setRefundableBalance(payParam.getAmount());
        // 如果支持分账, 设置分账状态为待分账
        if (order.getAllocation()) {
            order.setAllocStatus(PayAllocStatusEnum.WAITING.getCode());
        }
        payOrderManager.save(order);
        // 注册支付超时任务
        delayJobService.registerByTransaction(order.getId(), DaxPayCode.Event.MERCHANT_PAY_TIMEOUT, order.getExpiredTime());
        return order;
    }

    /**
     * 根据新传入的支付订单更新订单信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void updatePayOrder(PayParam payParam,PayOrder order) {
        // 订单信息
        order.setAllocation(payParam.getAllocation())
                .setNotifyUrl(payParam.getNotifyUrl())
                .setReturnUrl(payParam.getReturnUrl())
                .setAttach(payParam.getAttach())
                .setClientIp(payParam.getClientIp())
                .setReqTime(payParam.getReqTime())
                .setChannel(payParam.getChannel())
                .setMethod(payParam.getMethod())
                .setExtraParam(payParam.getExtraParam());
        if (!order.getAllocation()) {
            order.setAllocStatus(null);
        }
        payOrderManager.updateById(order);
    }

    /**
     * 校验支付状态，支付成功则返回，支付失败则抛出对应的异常
     */
    public PayOrder getOrderAndCheck(PayParam param) {
        // 根据订单查询支付记录
        PayOrder payOrder = payOrderQueryService.findByBizOrderNo(param.getBizOrderNo(), param.getAppId())
                .orElse(null);
        if (Objects.nonNull(payOrder)) {
            // 待支付
            if (Objects.equals(payOrder.getStatus(), PayStatusEnum.PROGRESS.getCode())) {
                // 如果支付超时, 触发订单同步操作, 同时抛出异常
                if (Objects.nonNull(payOrder.getExpiredTime()) && DateTimeUtil.ge(LocalDateTime.now(), payOrder.getExpiredTime())) {
                    paySyncService.syncPayOrder(payOrder);
                    throw new TradeStatusErrorException("支付已超时，请重新确认支付状态");
                }
                return payOrder;
            }
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
            // 其他状态直接抛出兜底异常
            throw new TradeStatusErrorException("订单不是待支付状态，请重新确认订单状态");
        }
        return null;
    }

    /**
     * 检验订单是否超过限额
     */
    public void validationLimitAmount(PayParam payParam) {
        MchAppLocal mchAppInfo = PaymentContextLocal.get()
                .getMchAppInfo();
        // 总额校验
        if (BigDecimalUtil.isGreaterThan(payParam.getAmount(),mchAppInfo.getLimitAmount())) {
            throw new AmountExceedLimitException("支付金额超过限额");
        }
    }


    /**
     * 根据支付订单构建支付结果
     *
     * @param payOrder 支付订单
     * @param result 支付结果业务类
     * @return PayResult 支付结果
     */
    public PayResult buildResult(PayOrder payOrder, PayResultBo result) {
        PayResult payResult = new PayResult()
                .setBizOrderNo(payOrder.getBizOrderNo())
                .setOrderNo(payOrder.getOrderNo())
                .setStatus(payOrder.getStatus());

        // 设置支付参数
        if (StrUtil.isNotBlank(result.getPayBody())) {
            payResult.setPayBody(result.getPayBody());
        }
        return payResult;
    }

    /**
     * 获取支付订单超时时间
     */
    private LocalDateTime getExpiredTime(PayParam payParam) {
        MchAppLocal mchAppLocal = PaymentContextLocal.get().getMchAppInfo();
        // 支付参数传入
        if (Objects.nonNull(payParam.getExpiredTime())) {
            return payParam.getExpiredTime();
        }
        // 根据商户应用配置计算出时间
        return PayUtil.getPaymentExpiredTime(mchAppLocal.getOrderTimeout());
    }

    /**
     * 校验订单超时时间是否正常
     */
    public void validationExpiredTime(PayParam payParam) {
        LocalDateTime expiredTime = this.getExpiredTime(payParam);
        if (Objects.nonNull(expiredTime) && DateTimeUtil.lt(expiredTime,LocalDateTime.now())) {
            throw new ValidationFailedException("支付超时时间设置有误, 请检查!");
        }
    }
}
