package org.dromara.daxpay.payment.pay.service;

import org.dromara.daxpay.platform.core.code.CommonErrorCode;
import org.dromara.daxpay.platform.core.exception.BizInfoException;
import org.dromara.daxpay.platform.core.exception.PayFailureException;
import org.dromara.daxpay.payment.common.enums.PayFundStatusEnum;
import org.dromara.daxpay.payment.common.util.PaymentStrategyFactory;
import org.dromara.daxpay.payment.pay.bo.PayTradeResultBo;
import org.dromara.daxpay.payment.pay.order.dao.PayTradeManager;
import org.dromara.daxpay.payment.pay.order.entity.PayTrade;
import org.dromara.daxpay.payment.pay.service.route.PayRouteFacade;
import cn.hutool.core.util.StrUtil;
import org.dromara.daxpay.payment.strategy.pay.AbsPayStrategy;
import org.dromara.daxpay.payment.unipay.param.trade.pay.PayParam;
import org.dromara.daxpay.payment.unipay.result.trade.pay.PayResult;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/// # 支付服务类
///
@Slf4j
@Service
@RequiredArgsConstructor
public class PayService {

    private final PayAssistService payAssistService;
    private final PayUniHandleService payUniHandleService;
    private final LockTemplate lockTemplate;
    private final PayTradeManager payTradeManager;
    private final PayRouteFacade payRouteFacade;

    /// 支付入口
    public PayResult pay(PayParam payParam) {
        payAssistService.validationExpiredTime(payParam.getExpiredTime());
        String bizOrderNo = payParam.getBizOrderNo();
        LockInfo lock = lockTemplate.lock("payment:pay:" + bizOrderNo, 10000, 200);
        if (Objects.isNull(lock)) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.pay.processing");
        }
        try {
            PayTrade trade = payAssistService.getOrderAndCheck(bizOrderNo);
            return this.payHandle(payParam, trade);
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /// 支付操作
    public PayResult payHandle(PayParam payParam, PayTrade trade) {
        // 解析应用号：空则取商户默认应用
        payAssistService.resolveApp(payParam);
        // 重付场景：已有订单且 param 未传产品时，复用首次产品，不重复路由
        if (Objects.nonNull(trade) && StrUtil.isBlank(payParam.getProduct())
                && StrUtil.isNotBlank(trade.getProduct())) {
            payParam.setProduct(trade.getProduct());
        }
        // 路由解析：已指定 product 则跳过，否则按 appId+method 策略匹配
        payRouteFacade.resolve(payParam);
        var payStrategy = PaymentStrategyFactory.createByProduct(payParam.getProduct(), AbsPayStrategy.class);
        payStrategy.setPayParam(payParam);
        // 订单不存在，新建支付订单
        if (Objects.isNull(trade)) {
            trade = payAssistService.createOrder(payParam);
        } else {
            // 判断是否已经拉起了支付，如果拉起返回保存的支付参数
            if (StrUtil.isNotBlank(trade.getPayBody())) {
                return payAssistService.buildResult(trade);
            }
        }
        payStrategy.setTrade(trade);
        payStrategy.initPayParam(trade, payParam);
        payStrategy.doBeforePayHandler();
        PayTradeResultBo result;
        try {
            result = payStrategy.doPayHandler();
        } catch (Exception e) {
            log.error("支付出现异常", e);
            trade.setStatus(PayFundStatusEnum.FAIL.getCode());
            if (e instanceof PayFailureException) {
                trade.setErrorMsg(e.getMessage());
            } else {
                trade.setErrorMsg("支付出现异常: " + e.getMessage());
            }
            payTradeManager.updateById(trade);
            throw e;
        }
        return SpringUtil.getBean(this.getClass()).paySuccess(trade, result);
    }

    /// 支付成功后操作
    @Transactional(rollbackFor = Exception.class)
    public PayResult paySuccess(PayTrade trade, PayTradeResultBo result) {
        if (result.isComplete()) {
            trade.setStatus(PayFundStatusEnum.SUCCESS.getCode());
            trade.setPayTime(result.getFinishTime());
        }
        trade.setOutOrderNo(result.getOutOrderNo());
        trade.setTransOrderNo(result.getTransOrderNo());
        trade.setRelationOrderNo(result.getRelationOrderNo());
        trade.setBuyerId(result.getBuyerId());
        trade.setTradeProduct(result.getTradeProduct());
        trade.setTradeWay(result.getTradeWay());
        trade.setBankType(result.getBankType());
        trade.setPromotionType(result.getPromotionType());
        trade.setPayBody(result.getPayBody());
        trade.setPayBodyType(Objects.nonNull(result.getPayBodyType())
                ? result.getPayBodyType().getCode() : null);
        trade.setErrorMsg(null);
        payUniHandleService.paySuccess(trade);
        return payAssistService.buildResult(trade);
    }
}
