package org.dromara.daxpay.service.pay.service.trade.pay;

import cn.bootx.platform.core.annotation.IgnoreTenant;
import cn.bootx.platform.core.exception.ValidationFailedException;
import cn.bootx.platform.core.util.BigDecimalUtil;
import cn.bootx.platform.core.util.DateTimeUtil;
import cn.bootx.platform.starter.redis.delay.service.DelayJobService;
import org.dromara.daxpay.core.context.PaymentReqInfoLocal;
import org.dromara.daxpay.core.enums.*;
import org.dromara.daxpay.core.exception.AmountExceedLimitException;
import org.dromara.daxpay.core.exception.TradeStatusErrorException;
import org.dromara.daxpay.core.param.trade.pay.PayParam;
import org.dromara.daxpay.core.result.trade.pay.PayResult;
import org.dromara.daxpay.core.util.PayUtil;
import org.dromara.daxpay.core.util.TradeNoGenerateUtil;
import org.dromara.daxpay.service.pay.bo.trade.PayResultBo;
import org.dromara.daxpay.service.common.code.DaxPayCode;
import org.dromara.daxpay.service.pay.common.local.PaymentContextLocal;
import org.dromara.daxpay.service.pay.dao.order.pay.PayOrderExpandManager;
import org.dromara.daxpay.service.pay.dao.order.pay.PayOrderManager;
import org.dromara.daxpay.service.pay.entity.order.pay.PayOrder;
import org.dromara.daxpay.service.pay.entity.order.pay.PayOrderExpand;
import org.dromara.daxpay.service.pay.service.notice.MerchantNoticeService;
import org.dromara.daxpay.service.pay.service.order.pay.PayOrderQueryService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    private final MerchantNoticeService merchantNoticeService;
    private final PayCloseService payCloseService;
    private final PayOrderExpandManager payOrderExpandManager;

    /**
     * 创建支付订单并保存, 返回支付订单
     */
    @Transactional(rollbackFor = Exception.class)
    public PayOrder createPayOrder(PayParam payParam) {
        // 订单超时时间
        LocalDateTime expiredTime = this.getExpiredTime(payParam.getExpiredTime());
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
        // 保存订单扩展信息
        var orderExpand = new PayOrderExpand();
        orderExpand.setId(order.getId());
        payOrderExpandManager.save(orderExpand);
        // 注册支付超时任务
        delayJobService.registerByTransaction(order.getId(), DaxPayCode.Event.ORDER_PAY_TIMEOUT, order.getExpiredTime());
        return order;
    }

    /**
     * 更新
     */
    @Transactional(rollbackFor = Exception.class)
    public void updatePayOrder(PayParam payParam, PayOrder payOrder) {
        payOrder.setChannel(payParam.getChannel())
                .setMethod(payParam.getMethod())
                .setOtherMethod(payParam.getOtherMethod())
                .setStatus(PayStatusEnum.PROGRESS.getCode());
        payOrderManager.updateById(payOrder);
    }

    /**
     * 失败处理
     */
    @IgnoreTenant
    public void fail(PayOrder payOrder, String errMsg){
        // 执行策略的关闭方法
        payOrder.setStatus(PayStatusEnum.FAIL.getCode())
                .setErrorMsg(errMsg)
                .setCloseTime(LocalDateTime.now());
        payOrderManager.updateById(payOrder);
        merchantNoticeService.registerPayNotice(payOrder);
    }

    /**
     * 校验支付状态，支付成功则返回，支付失败则抛出对应的异常
     */
    public PayOrder getOrderAndCheck(String bizOrderNo, String appId) {
        // 根据订单查询支付记录
        PayOrder payOrder = payOrderQueryService.findByBizOrderNo(bizOrderNo, appId).orElse(null);
        if (Objects.nonNull(payOrder)) {
            this.checkOrder(payOrder);
            return payOrder;
        }
        return null;
    }

    /**
     * 检查支付订单状态
     */
    public void checkOrder(PayOrder payOrder){
        // 待支付
        if (Objects.equals(payOrder.getStatus(), PayStatusEnum.WAIT.getCode())){
            // 如果支付超时, 触发订单同步操作, 同时抛出异常
            if (Objects.nonNull(payOrder.getExpiredTime()) && DateTimeUtil.ge(LocalDateTime.now(), payOrder.getExpiredTime())) {
                payCloseService.closeOrder(payOrder,false);
                throw new TradeStatusErrorException("支付已超时，请重新确认支付状态");
            }
            return;
        }
        // 支付中
        if (Objects.equals(payOrder.getStatus(), PayStatusEnum.PROGRESS.getCode())) {
            // 如果支付超时, 触发订单同步操作, 同时抛出异常
            if (Objects.nonNull(payOrder.getExpiredTime()) && DateTimeUtil.ge(LocalDateTime.now(), payOrder.getExpiredTime())) {
                paySyncService.syncPayOrder(payOrder);
                throw new TradeStatusErrorException("支付已超时，请重新确认支付状态");
            }
            throw new TradeStatusErrorException("订单号重复，请重新发起支付");
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
            throw new TradeStatusErrorException("该订单支付失败或已经被关闭");
        }
        // 退款类型状态
        if (Objects.equals(payOrder.getRefundStatus(), PayRefundStatusEnum.REFUNDING.getCode())) {
            throw new TradeStatusErrorException("该订单处于退款状态");
        }
        // 其他状态直接抛出兜底异常
        throw new TradeStatusErrorException("订单不是待支付状态，请重新确认订单状态");
    }

    /**
     * 检验订单是否超过限额
     */
    public void validationLimitAmount(BigDecimal amount) {
        PaymentReqInfoLocal reqInfo = PaymentContextLocal.get().getReqInfo();
        // 总额校验
        if (BigDecimalUtil.isGreaterThan(amount,reqInfo.getLimitAmount())) {
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
     * 获取支付订单超时时间, 后续会根据各通道调整订单的超时时间
     */
    public LocalDateTime getExpiredTime(LocalDateTime expiredTime) {
        PaymentReqInfoLocal reqInfoLocal = PaymentContextLocal.get().getReqInfo();
        // 参数传入
        if (Objects.nonNull(expiredTime)) {
            return expiredTime;
        }
        // 根据商户应用配置计算出时间
        return PayUtil.getPaymentExpiredTime(reqInfoLocal.getOrderTimeout());
    }

    /**
     * 校验订单超时时间是否正常
     */
    public void validationExpiredTime(LocalDateTime expiredTime) {
        if (Objects.nonNull(expiredTime) && DateTimeUtil.lt(expiredTime,LocalDateTime.now())) {
            throw new ValidationFailedException("支付超时时间设置有误, 请检查!");
        }
    }

}
