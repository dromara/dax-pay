package cn.daxpay.open.payment.old.pay.service.trade.pay;

import cn.daxpay.open.platform.core.exception.ValidationFailedException;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.util.DateTimeUtil;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.exception.business.AmountExceedLimitException;
import cn.daxpay.open.payment.common.util.PayUtil;
import cn.daxpay.open.platform.core.util.TradeNoGenerateUtil;
import cn.daxpay.open.payment.old.pay.convert.order.pay.PayOrderConvert;
import cn.daxpay.open.payment.old.pay.dao.order.pay.PayOrderExpandManager;
import cn.daxpay.open.payment.old.pay.dao.order.pay.PayOrderManager;
import cn.daxpay.open.payment.old.pay.entity.order.pay.PayOrder;
import cn.daxpay.open.payment.old.pay.entity.order.pay.PayOrderExpand;
import cn.daxpay.open.platform.core.enums.pay.channel.*;
import cn.daxpay.open.platform.core.enums.pay.pay.*;
import cn.daxpay.open.platform.core.enums.pay.trade.*;
import cn.daxpay.open.platform.core.enums.pay.notice.*;
import cn.hutool.core.util.StrUtil;
import cn.daxpay.open.payment.old.pay.exception.TradeStatusErrorException;
import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.old.pay.service.order.pay.PayOrderQueryService;
import cn.daxpay.open.payment.unipay.param.trade.pay.PayParam;
import cn.daxpay.open.payment.unipay.result.trade.pay.PayResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;

/// # 支付支持服务
///
@Slf4j
@Service("oldPayAssistService")
@RequiredArgsConstructor
public class PayAssistService {

    private final PayOrderManager payOrderManager;
    private final PayOrderQueryService payOrderQueryService;
    private final PaymentContext apiContext;
    private final PaySyncService paySyncService;
//    private final DelayJobService delayJobService;
    private final PayCloseService payCloseService;
    private final PayOrderExpandManager payOrderExpandManager;

    /// 创建支付订单并保存, 返回支付订单
    @Transactional(rollbackFor = Exception.class)
    public PayOrder createPayOrder(PayParam payParam) {
        // 订单超时时间
        OffsetDateTime expiredTime = this.getExpiredTime(payParam.getExpiredTime());
        // 构建支付订单对象
        PayOrder order = new PayOrder();
        PayOrderConvert.CONVERT.copy(payParam, order);
        // 从产品编码派生通道编码
        if (StrUtil.isNotBlank(order.getProduct()) && StrUtil.isBlank(order.getChannel())) {
            ProductEnum productEnum = ProductEnum.findByCode(order.getProduct());
            if (productEnum != null) {
                order.setChannel(productEnum.getChannel());
            }
        }
        order.setOrderNo(TradeNoGenerateUtil.pay())
                .setStatus(PayStatusEnum.PROGRESS.getCode())
                .setRefundStatus(PayRefundStatusEnum.NO_REFUND.getCode())
                .setExpiredTime(expiredTime)
                .setRefundableBalance(payParam.getAmount());
        // 支付渠道：付款码场景由 authCode 推导
        if (PayUtil.isBarcodePayMethod(payParam.getMethod())) {
            var barInstrument = PayUtil.getBarCodeType(payParam.getAuthCode());
            order.setProvider(barInstrument.getCode());
        }
        // 初始化处理
        this.initPayOrder(order);
        payOrderManager.save(order);
        // 保存订单扩展信息
        var orderExpand = new PayOrderExpand();
        orderExpand.setBarCode(payParam.getAuthCode())
                .setJsapiOpenId(payParam.getOpenId())
                .setNotifyUrl(payParam.getNotifyUrl())
                .setReturnUrl(payParam.getReturnUrl())
                .setAttach(payParam.getAttach())
                .setId(order.getId());
        payOrderExpandManager.save(orderExpand);
        return order;
    }

    /// 订单初始化处理
    public void initPayOrder(PayOrder order){
        // 注册支付超时任务
//        delayJobService.registerByTransaction(order.getId(), DaxPayCode.Event.ORDER_PAY_TIMEOUT, order.getExpiredTime());
    }

    /// 更新, 通常只有网关支付方式创建的订单才需要进行更新, 用来设置支付方式等信息
    @Transactional(rollbackFor = Exception.class)
    public void updatePayOrder(PayParam payParam, PayOrder payOrder) {
        payOrder.setProduct(payParam.getProduct())
                .setMethod(payParam.getMethod())
                .setStatus(PayStatusEnum.PROGRESS.getCode());
        // 从产品编码派生通道编码
        if (StrUtil.isNotBlank(payParam.getProduct())) {
            ProductEnum productEnum = ProductEnum.findByCode(payParam.getProduct());
            if (productEnum != null) {
                payOrder.setChannel(productEnum.getChannel());
            }
        }
        payOrderManager.updateById(payOrder);
    }

    /// 校验支付状态，支付成功则返回，支付失败则抛出对应的异常
    public PayOrder getOrderAndCheck(String bizOrderNo, String appId) {
        // 根据订单查询支付记录
        PayOrder payOrder = payOrderQueryService.findByBizOrderNo(bizOrderNo, appId).orElse(null);
        if (Objects.nonNull(payOrder)) {
            this.checkOrder(payOrder);
            return payOrder;
        }
        return null;
    }

    /// 检查支付订单状态
    public void checkOrder(PayOrder payOrder){
        // 待支付
        if (Objects.equals(payOrder.getStatus(), PayStatusEnum.WAIT.getCode())){
            // 如果支付超时, 触发订单同步操作, 同时抛出异常
            if (Objects.nonNull(payOrder.getExpiredTime()) && DateTimeUtil.ge(OffsetDateTime.now(ZoneOffset.UTC), payOrder.getExpiredTime())) {
                payCloseService.closeOrder(payOrder,false);
                // 支付已超时，请重新确认支付状态
            throw new TradeStatusErrorException(DaxPayErrorCode.TRADE_STATUS_ERROR, "pay.error.pay.timeoutRetry");
            }
            return;
        }
        // 支付中
        if (Objects.equals(payOrder.getStatus(), PayStatusEnum.PROGRESS.getCode())) {
            // 如果支付超时, 触发订单同步操作, 同时抛出异常
            if (Objects.nonNull(payOrder.getExpiredTime()) && DateTimeUtil.ge(OffsetDateTime.now(ZoneOffset.UTC), payOrder.getExpiredTime())) {
                paySyncService.syncPayOrder(payOrder);
                // 支付已超时，请重新确认支付状态
            throw new TradeStatusErrorException(DaxPayErrorCode.TRADE_STATUS_ERROR, "pay.error.pay.timeoutRetry");
            }
            return;
        }
        // 已经支付状态
        if (PayStatusEnum.SUCCESS.getCode()
                .equals(payOrder.getStatus())) {
            // 已经支付成功，请勿重新支付
            throw new TradeStatusErrorException(DaxPayErrorCode.TRADE_STATUS_ERROR, "pay.error.pay.alreadySuccess");
        }
        // 支付失败类型状态
        List<String> tradesStatus = List.of(
                PayStatusEnum.FAIL.getCode(),
                PayStatusEnum.CLOSE.getCode(),
                PayStatusEnum.CANCEL.getCode());
        if (tradesStatus.contains(payOrder.getStatus())) {
            // 该订单支付失败或已经被关闭
            throw new TradeStatusErrorException(DaxPayErrorCode.TRADE_STATUS_ERROR, "pay.error.pay.failedOrClosed");
        }
        // 退款类型状态
        if (Objects.equals(payOrder.getRefundStatus(), PayRefundStatusEnum.REFUNDING.getCode())) {
            // 该订单处于退款状态
            throw new TradeStatusErrorException(DaxPayErrorCode.TRADE_STATUS_ERROR, "pay.error.pay.refunding");
        }
        // 其他状态直接抛出兜底异常
        // 订单支付状态异常，请重新确认订单状态
            throw new TradeStatusErrorException(DaxPayErrorCode.TRADE_STATUS_ERROR, "pay.error.pay.statusAbnormal");
    }

    /// 根据支付订单构建支付结果
    ///
    /// @param payOrder 支付订单
    /// @param orderExpand 支付订单扩展信息
    /// @return PayResult 支付结果
    public PayResult buildResult(PayOrder payOrder, PayOrderExpand orderExpand) {
        return new PayResult()
                .setBizOrderNo(payOrder.getBizOrderNo())
                .setOrderNo(payOrder.getOrderNo())
                .setStatus(payOrder.getStatus())
                .setOrderId(payOrder.getId())
                .setPayBody(orderExpand.getPayBody())
                .setPayBodyType(orderExpand.getPayBodyType());
    }

    /// 获取支付订单超时时间, 后续会根据各通道调整订单的超时时间
    public OffsetDateTime getExpiredTime(OffsetDateTime expiredTime) {
        // 参数传入
        if (Objects.nonNull(expiredTime)) {
            return expiredTime;
        }
        // 默认30分钟超时
        return PayUtil.getPaymentExpiredTime(30);
    }

    /// 校验订单超时时间是否正常
    public void validationExpiredTime(OffsetDateTime expiredTime) {
        if (Objects.nonNull(expiredTime) && DateTimeUtil.lt(expiredTime,OffsetDateTime.now(ZoneOffset.UTC))) {
            // 支付超时时间设置有误
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.pay.expiredTimeError");
        }
    }

}


