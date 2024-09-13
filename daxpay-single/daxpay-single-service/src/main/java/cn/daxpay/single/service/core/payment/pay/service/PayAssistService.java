package cn.daxpay.single.service.core.payment.pay.service;

import cn.bootx.platform.common.core.util.CollUtil;
import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.daxpay.single.core.code.PayChannelEnum;
import cn.daxpay.single.core.code.PayOrderAllocStatusEnum;
import cn.daxpay.single.core.code.PayOrderRefundStatusEnum;
import cn.daxpay.single.core.code.PayStatusEnum;
import cn.daxpay.single.core.exception.AmountExceedLimitException;
import cn.daxpay.single.core.exception.TradeStatusErrorException;
import cn.daxpay.single.core.param.payment.pay.PayParam;
import cn.daxpay.single.core.result.pay.PayResult;
import cn.daxpay.single.core.util.TradeNoGenerateUtil;
import cn.daxpay.single.core.util.PayUtil;
import cn.daxpay.single.service.common.context.PayLocal;
import cn.daxpay.single.service.common.context.PlatformLocal;
import cn.daxpay.single.service.common.local.PaymentContextLocal;
import cn.daxpay.single.service.core.order.pay.entity.PayOrder;
import cn.daxpay.single.service.core.order.pay.service.PayOrderQueryService;
import cn.daxpay.single.service.core.order.pay.service.PayOrderService;
import cn.daxpay.single.service.core.payment.sync.service.PaySyncService;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static cn.daxpay.single.core.code.PayStatusEnum.*;

/**
 * 支付支持服务
 * @author xxm
 * @since 2023/12/24
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayAssistService {

    private final PaySyncService paySyncService;

    private final PayOrderService payOrderService;

    private final PayOrderQueryService payOrderQueryService;


    /**
     * 创建支付订单并保存, 返回支付订单
     */
    @Transactional(rollbackFor = Exception.class)
    public PayOrder createPayOrder(PayParam payParam) {
        // 订单超时时间
        LocalDateTime expiredTime = this.getExpiredTime(payParam);
        // 构建支付订单对象
        PayOrder order = new PayOrder()
                .setBizOrderNo(payParam.getBizOrderNo())
                .setOrderNo(TradeNoGenerateUtil.pay())
                .setTitle(payParam.getTitle())
                .setDescription(payParam.getDescription())
                .setStatus(PayStatusEnum.PROGRESS.getCode())
                .setRefundStatus(PayOrderRefundStatusEnum.NO_REFUND.getCode())
                .setAllocation(payParam.getAllocation())
                .setAmount(payParam.getAmount())
                .setChannel(payParam.getChannel())
                .setMethod(payParam.getMethod())
                .setExpiredTime(expiredTime)
                .setRefundableBalance(payParam.getAmount())
                .setClientIp(payParam.getClientIp())
                .setNotifyUrl(payParam.getNotifyUrl())
                .setReturnUrl(payParam.getReturnUrl())
                .setAttach(payParam.getAttach())
                .setReqTime(payParam.getReqTime());
        // 如果支持分账, 设置分账状态为待分账
        if (order.getAllocation()) {
            order.setAllocStatus(PayOrderAllocStatusEnum.WAITING.getCode());
        }
        payOrderService.save(order);
        return order;
    }

    /**
     * 根据新传入的支付订单更新订单信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void updatePayOrder(PayParam payParam,PayOrder order) {
        // 订单信息
        order.setAllocation(payParam.getAllocation())
                .setClientIp(payParam.getClientIp())
                .setNotifyUrl(payParam.getNotifyUrl())
                .setReturnUrl(payParam.getReturnUrl())
                .setAttach(payParam.getAttach())
                .setClientIp(payParam.getClientIp())
                .setReqTime(payParam.getReqTime())
                .setChannel(payParam.getChannel())
                .setMethod(payParam.getMethod());
        if (!order.getAllocation()) {
            order.setAllocStatus(null);
        }
        if (CollUtil.isNotEmpty(payParam.getExtraParam())){
            order.setExtraParam(JSONUtil.toJsonStr(payParam.getExtraParam()));
        } else {
            order.setExtraParam(null);
        }

        payOrderService.updateById(order);
    }

    /**
     * 校验支付状态，支付成功则返回，支付失败则抛出对应的异常
     */
    public PayOrder getOrderAndCheck(String bizOrderNo) {
        // 根据订单查询支付记录
        PayOrder payOrder = payOrderQueryService.findByBizOrderNo(bizOrderNo)
                .orElse(null);
        if (Objects.nonNull(payOrder)) {
            // 待支付
            if (Objects.equals(payOrder.getStatus(), PROGRESS.getCode())) {
                // 如果支付超时, 触发订单同步操作, 同时抛出异常
                if (Objects.nonNull(payOrder.getExpiredTime()) && LocalDateTimeUtil.ge(LocalDateTime.now(), payOrder.getExpiredTime())) {
                    paySyncService.syncPayOrder(payOrder);
                    throw new TradeStatusErrorException("支付已超时，请重新确认支付状态");
                }
                return payOrder;
            }
            // 已经支付状态
            if (SUCCESS.getCode()
                    .equals(payOrder.getStatus())) {
                throw new TradeStatusErrorException("已经支付成功，请勿重新支付");
            }
            // 支付失败类型状态
            List<String> tradesStatus = Arrays.asList(FAIL.getCode(), CLOSE.getCode(), CANCEL.getCode());
            if (tradesStatus.contains(payOrder.getStatus())) {
                throw new TradeStatusErrorException("支付失败或已经被关闭");
            }
            // 退款类型状态
            if (Objects.equals(payOrder.getRefundStatus(), PayOrderRefundStatusEnum.REFUNDING.getCode())) {
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
        // 总额校验
        PlatformLocal platformInfo = PaymentContextLocal.get().getPlatformInfo();
        if (payParam.getAmount() > platformInfo.getLimitAmount()) {
            throw new AmountExceedLimitException("支付金额超过限额");
        }
    }


    /**
     * 根据支付订单构建支付结果
     * @param payOrder 支付订单
     * @return PayResult 支付结果
     */
    public PayResult buildResult(PayOrder payOrder) {
        PayResult payResult;
        payResult = new PayResult();
        payResult.setBizOrderNo(payOrder.getBizOrderNo());
        payResult.setOrderNo(payOrder.getOrderNo());
        payResult.setStatus(payOrder.getStatus());

        // 设置支付参数
        PayLocal payInfo = PaymentContextLocal.get().getPayInfo();
        if (StrUtil.isNotBlank(payInfo.getPayBody())) {
            payResult.setPayBody(payInfo.getPayBody());
        }
        return payResult;
    }

    /**
     * 获取支付订单超时时间
     */
    private LocalDateTime getExpiredTime(PayParam payParam) {
        // 钱包没有超时时间
        if (PayChannelEnum.WALLET.getCode()
                .equals(payParam.getChannel())) {
            return null;
        }
        PlatformLocal platform = PaymentContextLocal.get().getPlatformInfo();
        // 支付参数传入
        if (Objects.nonNull(payParam.getExpiredTime())) {
            return payParam.getExpiredTime();
        }
        // 根据平台配置计算出
        return PayUtil.getPaymentExpiredTime(platform.getOrderTimeout());
    }
}
