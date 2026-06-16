package org.dromara.daxpay.payment.pay.service;

import org.dromara.daxpay.platform.core.exception.BizInfoException;
import org.dromara.daxpay.platform.core.code.CommonErrorCode;
import org.dromara.daxpay.platform.core.util.DateTimeUtil;
import org.dromara.daxpay.platform.core.util.TradeNoGenerateUtil;
import org.dromara.daxpay.payment.common.enums.NormalOrderStatusEnum;
import org.dromara.daxpay.payment.common.enums.PayFundStatusEnum;
import org.dromara.daxpay.payment.common.enums.PayTradeTypeEnum;
import org.dromara.daxpay.payment.common.util.PayUtil;
import org.dromara.daxpay.payment.pay.convert.PayTradeConvert;
import org.dromara.daxpay.payment.pay.order.dao.PayNormalOrderManager;
import org.dromara.daxpay.payment.pay.order.entity.PayNormalOrder;
import org.dromara.daxpay.payment.pay.order.dao.PayTradeManager;
import org.dromara.daxpay.payment.pay.order.entity.PayTrade;
import org.dromara.daxpay.payment.unipay.param.trade.pay.PayParam;
import org.dromara.daxpay.payment.unipay.result.trade.pay.PayResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.Optional;

/// # 支付支持服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class PayAssistService {

    private final PayNormalOrderManager payNormalOrderManager;
    private final PayTradeManager payTradeManager;

    /// 创建支付订单（容器 + 资金交易 + 通道信息）
    @Transactional(rollbackFor = Exception.class)
    public PayTrade createOrder(PayParam payParam) {
        OffsetDateTime expiredTime = this.getExpiredTime(payParam.getExpiredTime());
        // 创建容器 PayNormalOrder
        PayNormalOrder normalOrder = new PayNormalOrder();
        normalOrder.setBizOrderNo(payParam.getBizOrderNo());
        normalOrder.setTitle(payParam.getTitle());
        normalOrder.setDescription(payParam.getDescription());
        normalOrder.setStatus(NormalOrderStatusEnum.WAIT_PAY.getCode());
        normalOrder.setNotifyUrl(payParam.getNotifyUrl());
        normalOrder.setReturnUrl(payParam.getReturnUrl());
        normalOrder.setAttach(payParam.getAttach());
        normalOrder.setExpiredTime(expiredTime);
        payNormalOrderManager.save(normalOrder);
        // 创建资金交易 PayTrade
        Long amount = Long.valueOf(PayUtil.convertCentAmount(payParam.getAmount()));
        PayTrade trade = new PayTrade();
        trade.setTradeNo(TradeNoGenerateUtil.pay());
        trade.setTradeType(PayTradeTypeEnum.NORMAL.getCode());
        trade.setContainerId(normalOrder.getId());
        trade.setProduct(payParam.getProduct());
        trade.setChannel(payParam.getChannel());
        trade.setMethod(payParam.getMethod());
        trade.setOtherMethod(payParam.getOtherMethod());
        trade.setLimitPay(payParam.getLimitPay());
        trade.setProvider(payParam.getProvider());
        trade.setAmount(amount);
        trade.setCurrency("CNY");
        trade.setRefundableBalance(amount);
        trade.setStatus(PayFundStatusEnum.PROGRESS.getCode());
        trade.setExpiredTime(expiredTime);
        trade.setSource(payParam.getSource());
        trade.setBarCode(payParam.getAuthCode());
        trade.setOpenid(payParam.getOpenId());
        payTradeManager.save(trade);
        return trade;
    }

    /// 根据业务单号查询并检查支付状态
    public PayTrade getOrderAndCheck(String bizOrderNo, String appId) {
        Optional<PayNormalOrder> normalOrderOpt = payNormalOrderManager.findByBizOrderNo(bizOrderNo, appId);
        if (normalOrderOpt.isEmpty()) {
            return null;
        }
        PayNormalOrder normalOrder = normalOrderOpt.get();
        PayTrade trade = payTradeManager.findByContainerId(normalOrder.getId(), appId).orElse(null);
        if (trade == null) {
            return null;
        }
        this.checkOrder(normalOrder, trade);
        return trade;
    }

    /// 检查订单状态
    public void checkOrder(PayNormalOrder normalOrder, PayTrade trade) {
        // 容器状态检查
        String bizStatus = normalOrder.getStatus();
        if (Objects.equals(bizStatus, NormalOrderStatusEnum.PAID.getCode())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.pay.alreadySuccess");
        }
        if (Objects.equals(bizStatus, NormalOrderStatusEnum.CLOSED.getCode())
                || Objects.equals(bizStatus, NormalOrderStatusEnum.EXPIRED.getCode())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.pay.failedOrClosed");
        }
        // 资金状态检查
        String fundStatus = trade.getStatus();
        if (Objects.equals(fundStatus, PayFundStatusEnum.SUCCESS.getCode())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.pay.alreadySuccess");
        }
        if (Objects.equals(fundStatus, PayFundStatusEnum.CLOSE.getCode())
                || Objects.equals(fundStatus, PayFundStatusEnum.FAIL.getCode())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.pay.failedOrClosed");
        }
        // 超时检查
        if (Objects.nonNull(trade.getExpiredTime())
                && DateTimeUtil.ge(OffsetDateTime.now(ZoneOffset.UTC), trade.getExpiredTime())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.pay.timeoutRetry");
        }
    }

    /// 根据 PayTrade 构建支付结果
    public PayResult buildResult(PayTrade trade) {
        PayNormalOrder normalOrder = payNormalOrderManager.findById(trade.getContainerId()).orElse(null);
        return PayTradeConvert.CONVERT.toResult(trade, normalOrder);
    }

    /// 获取支付超时时间
    public OffsetDateTime getExpiredTime(OffsetDateTime expiredTime) {
        if (Objects.nonNull(expiredTime)) {
            return expiredTime;
        }
        return PayUtil.getPaymentExpiredTime(30);
    }

    /// 校验超时时间
    public void validationExpiredTime(OffsetDateTime expiredTime) {
        if (Objects.nonNull(expiredTime)
                && DateTimeUtil.lt(expiredTime, OffsetDateTime.now(ZoneOffset.UTC))) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.pay.expiredTimeError");
        }
    }
}
