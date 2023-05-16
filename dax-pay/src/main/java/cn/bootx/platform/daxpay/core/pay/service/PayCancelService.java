package cn.bootx.platform.daxpay.core.pay.service;

import cn.bootx.platform.daxpay.code.pay.PayStatusCode;
import cn.bootx.platform.daxpay.core.pay.builder.PayEventBuilder;
import cn.bootx.platform.daxpay.core.pay.builder.PaymentBuilder;
import cn.bootx.platform.daxpay.core.pay.factory.PayStrategyFactory;
import cn.bootx.platform.daxpay.core.pay.func.AbsPayStrategy;
import cn.bootx.platform.daxpay.core.pay.func.PayStrategyConsumer;
import cn.bootx.platform.daxpay.core.payment.entity.Payment;
import cn.bootx.platform.daxpay.core.payment.service.PaymentService;
import cn.bootx.platform.daxpay.exception.payment.PayFailureException;
import cn.bootx.platform.daxpay.exception.payment.PayNotExistedException;
import cn.bootx.platform.daxpay.exception.payment.PayUnsupportedMethodException;
import cn.bootx.platform.daxpay.mq.PaymentEventSender;
import cn.bootx.platform.daxpay.param.pay.PayParam;
import cn.hutool.core.collection.CollectionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static cn.bootx.platform.daxpay.code.pay.PayStatusCode.*;

/**
 * 取消订单处理
 *
 * @author xxm
 * @date 2021/3/2
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayCancelService {

    private final PaymentService paymentService;

    private final PaymentEventSender paymentEventSender;

    /**
     * 根据业务id取消支付记录
     */
    @Transactional(rollbackFor = Exception.class)
    public void cancelByBusinessId(String businessId) {
        Payment payment = paymentService.findByBusinessId(businessId)
            .orElseThrow(() -> new PayFailureException("未找到支付单"));
        this.cancelPayment(payment);
    }

    /**
     * 根据paymentId取消支付记录
     */
    @Transactional(rollbackFor = Exception.class)
    public void cancelByPaymentId(Long paymentId) {
        Payment payment = paymentService.findById(paymentId).orElseThrow(() -> new PayFailureException("未找到支付单"));
        this.cancelPayment(payment);
    }

    /**
     * 取消支付记录
     */
    private void cancelPayment(Payment payment) {
        // 状态检查, 成功/退款/退款中 不处理
        List<Integer> trades = Arrays.asList(TRADE_SUCCESS, TRADE_REFUNDING, TRADE_REFUNDED);
        if (trades.contains(payment.getPayStatus())) {
            throw new PayFailureException("支付已完成, 无法撤销");
        }

        // 获取 paymentParam
        PayParam payParam = PaymentBuilder.buildPayParamByPayment(payment);

        // 1.获取支付方式，通过工厂生成对应的策略组
        List<AbsPayStrategy> paymentStrategyList = PayStrategyFactory.create(payParam.getPayModeList());
        if (CollectionUtil.isEmpty(paymentStrategyList)) {
            throw new PayUnsupportedMethodException();
        }

        // 2.初始化支付的参数
        for (AbsPayStrategy paymentStrategy : paymentStrategyList) {
            paymentStrategy.initPayParam(payment, payParam);
        }

        // 3.执行取消订单
        this.doHandler(payment, paymentStrategyList, (strategyList, paymentObj) -> {
            // 发起取消进行的执行方法
            strategyList.forEach(AbsPayStrategy::doCancelHandler);
            // 取消订单
            paymentObj.setPayStatus(PayStatusCode.TRADE_CANCEL);
            paymentService.updateById(paymentObj);
        });

        // 4. 获取支付记录信息
        payment = paymentService.findById(payment.getId()).orElseThrow(PayNotExistedException::new);

        // 5. 发布撤销事件
        paymentEventSender.sendPayCancel(PayEventBuilder.buildPayCancel(payment));
    }

    /**
     * 处理方法
     * @param payment 支付记录
     * @param strategyList 支付策略
     * @param successCallback 成功操作
     */
    private void doHandler(Payment payment, List<AbsPayStrategy> strategyList,
                           PayStrategyConsumer<List<AbsPayStrategy>, Payment> successCallback) {

        try {
            // 执行
            successCallback.accept(strategyList, payment);
        }
        catch (Exception e) {
            // error事件的处理
            log.warn("取消订单失败");
            throw e;
        }
    }

}
