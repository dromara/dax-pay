package cn.bootx.platform.daxpay.core.pay.service;

import cn.bootx.platform.common.core.exception.ErrorCodeRuntimeException;
import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.bootx.platform.daxpay.code.pay.PayChannelEnum;
import cn.bootx.platform.daxpay.code.pay.PayStatusCode;
import cn.bootx.platform.daxpay.core.pay.builder.PayEventBuilder;
import cn.bootx.platform.daxpay.core.pay.builder.PaymentBuilder;
import cn.bootx.platform.daxpay.core.pay.exception.BaseException;
import cn.bootx.platform.daxpay.core.pay.exception.ExceptionInfo;
import cn.bootx.platform.daxpay.core.pay.factory.PayStrategyFactory;
import cn.bootx.platform.daxpay.core.pay.func.AbsPayStrategy;
import cn.bootx.platform.daxpay.core.pay.func.PayStrategyConsumer;
import cn.bootx.platform.daxpay.core.pay.result.PayCallbackResult;
import cn.bootx.platform.daxpay.core.payment.entity.Payment;
import cn.bootx.platform.daxpay.core.payment.service.PaymentService;
import cn.bootx.platform.daxpay.mq.PayEventSender;
import cn.bootx.platform.daxpay.param.pay.PayParam;
import cn.hutool.core.collection.CollectionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 支付回调处理
 *
 * @author xxm
 * @since 2021/2/27
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayCallbackService {

    private final PaymentService paymentService;

    private final PayEventSender eventSender;

    /**
     * 统一回调处理
     * @param tradeStatus 支付状态
     * @see PayStatusCode
     */
    public PayCallbackResult callback(Long paymentId, String tradeStatus, Map<String, String> map) {

        // 获取payment和paymentParam数据
        Payment payment = paymentService.findById(paymentId).orElse(null);

        // 支付单不存在,记录回调记录
        if (Objects.isNull(payment)) {
            return new PayCallbackResult().setCode(PayStatusCode.NOTIFY_PROCESS_FAIL).setMsg("支付单不存在,记录回调记录");
        }

        // 回调时间超出了支付单超时时间, 记录一下, 不做处理
        if (Objects.nonNull(payment.getExpiredTime())
                && LocalDateTimeUtil.ge(LocalDateTime.now(), payment.getExpiredTime())) {
            return new PayCallbackResult().setCode(PayStatusCode.NOTIFY_PROCESS_FAIL).setMsg("回调时间超出了支付单支付有效时间");
        }

        // 成功状态
        if (Objects.equals(PayStatusCode.NOTIFY_TRADE_SUCCESS, tradeStatus)) {
            return this.success(payment, map);
        }
        else {
            // 失败状态
            return this.fail(payment, map);
        }
    }

    /**
     * 成功处理
     */
    private PayCallbackResult success(Payment payment, Map<String, String> map) {
        PayCallbackResult result = new PayCallbackResult().setCode(PayStatusCode.NOTIFY_PROCESS_SUCCESS);

        // payment已经被支付,不需要重复处理
        if (Objects.equals(payment.getPayStatus(), PayStatusCode.TRADE_SUCCESS)) {
            return result.setCode(PayStatusCode.NOTIFY_PROCESS_IGNORE).setMsg("支付单已经是支付成功状态,不进行处理");
        }

        // payment已被取消,记录回调记录
        if (!Objects.equals(payment.getPayStatus(), PayStatusCode.TRADE_PROGRESS)) {
            return result.setCode(PayStatusCode.NOTIFY_PROCESS_FAIL).setMsg("支付单不是待支付状态,记录回调记录");
        }

        // 2.通过工厂生成对应的策略组
        PayParam payParam = PaymentBuilder.buildPayParamByPayment(payment);

        List<AbsPayStrategy> paymentStrategyList = PayStrategyFactory.create(payParam.getPayWayList());
        if (CollectionUtil.isEmpty(paymentStrategyList)) {
            return result.setCode(PayStatusCode.NOTIFY_PROCESS_FAIL).setMsg("支付单数据非法,未找到对应的支付方式");
        }

        // 3.初始化支付的参数
        for (AbsPayStrategy paymentStrategy : paymentStrategyList) {
            paymentStrategy.initPayParam(payment, payParam);
        }
        // 4.处理方法, 支付时只有一种payModel(异步支付), 失败时payment的所有payModel都会生效
        boolean handlerFlag = this.doHandler(payment, paymentStrategyList, (strategyList, paymentObj) -> {
            // 执行异步支付方式的成功回调(不会有同步payModel)
            strategyList.forEach(absPaymentStrategy -> absPaymentStrategy.doAsyncSuccessHandler(map));

            // 修改payment支付状态为成功
            paymentObj.setPayStatus(PayStatusCode.TRADE_SUCCESS);
            paymentObj.setPayTime(LocalDateTime.now());
            paymentService.updateById(paymentObj);
        });

        if (handlerFlag) {
            // 5. 发送成功事件
            eventSender.sendPayComplete(PayEventBuilder.buildPayComplete(payment));
        }
        else {
            return result.setCode(PayStatusCode.NOTIFY_PROCESS_FAIL).setMsg("回调处理过程报错");
        }
        return result;
    }

    /**
     * 失败处理, 关闭并退款 按说这块不会发生
     */
    private PayCallbackResult fail(Payment payment, Map<String, String> map) {
        PayCallbackResult result = new PayCallbackResult().setCode(PayStatusCode.NOTIFY_PROCESS_SUCCESS);

        // payment已被取消,记录回调记录,后期处理
        if (!Objects.equals(payment.getPayStatus(), PayStatusCode.TRADE_PROGRESS)) {
            return result.setCode(PayStatusCode.NOTIFY_PROCESS_IGNORE).setMsg("支付单已经取消,记录回调记录");
        }

        // payment支付成功, 状态非法
        if (!Objects.equals(payment.getPayStatus(), PayStatusCode.TRADE_SUCCESS)) {
            return result.setCode(PayStatusCode.NOTIFY_PROCESS_FAIL).setMsg("支付单状态非法,支付网关状态为失败,但支付单状态为已完成");
        }

        // 2.通过工厂生成对应的策略组
        PayParam payParam = PaymentBuilder.buildPayParamByPayment(payment);
        List<AbsPayStrategy> paymentStrategyList = PayStrategyFactory.create(payParam.getPayWayList());
        if (CollectionUtil.isEmpty(paymentStrategyList)) {
            return result.setCode(PayStatusCode.NOTIFY_PROCESS_FAIL).setMsg("支付单数据非法,未找到对应的支付方式");
        }
        // 3.初始化支付关闭的参数
        for (AbsPayStrategy paymentStrategy : paymentStrategyList) {
            paymentStrategy.initPayParam(payment, payParam);
        }
        // 4.处理方法, 支付时只有一种payModel(异步支付), 失败时payment的所有payModel都会生效
        boolean handlerFlag = this.doHandler(payment, paymentStrategyList, (strategyList, paymentObj) -> {
            // 执行异步支付方式的成功回调(不会有同步payModel)
            strategyList.forEach(AbsPayStrategy::doCancelHandler);

            // 修改payment支付状态为成功
            paymentObj.setPayStatus(PayStatusCode.TRADE_CANCEL);
            paymentService.updateById(paymentObj);
        });

        if (handlerFlag) {
            // 5. 发送退款事件
            eventSender.sendPayRefund(PayEventBuilder.buildPayRefund(payment));
        }
        else {
            return result.setCode(PayStatusCode.NOTIFY_PROCESS_FAIL).setMsg("回调处理过程报错");
        }

        return result;
    }

    /**
     * 处理方法
     * @param payment 支付记录
     * @param strategyList 支付策略
     * @param successCallback 成功操作
     */
    private boolean doHandler(Payment payment, List<AbsPayStrategy> strategyList,
            PayStrategyConsumer<List<AbsPayStrategy>, Payment> successCallback) {

        try {
            // 1.获取异步支付方式，通过工厂生成对应的策略组
            List<AbsPayStrategy> syncPaymentStrategyList = strategyList.stream()
                .filter(paymentStrategy -> PayChannelEnum.ASYNC_TYPE_CODE.contains(paymentStrategy.getType()))
                .collect(Collectors.toList());
            // 执行成功方法
            successCallback.accept(syncPaymentStrategyList, payment);
        }
        catch (Exception e) {
            // error事件的处理
            this.asyncErrorHandler(payment, strategyList, e);
            return false;
        }
        return true;
    }

    /**
     * 对Error的处理
     */
    private void asyncErrorHandler(Payment payment, List<AbsPayStrategy> strategyList, Exception e) {

        // 默认的错误信息
        ExceptionInfo exceptionInfo = new ExceptionInfo(PayStatusCode.TRADE_FAIL, e.getMessage());
        if (e instanceof BaseException) {
            exceptionInfo = ((BaseException) e).getExceptionInfo();
        }
        else if (e instanceof ErrorCodeRuntimeException) {
            ErrorCodeRuntimeException ex = (ErrorCodeRuntimeException) e;
            exceptionInfo = new ExceptionInfo(String.valueOf(ex.getCode()), ex.getMessage());
        }

        // 更新Payment的状态
        payment.setErrorCode(String.valueOf(exceptionInfo.getErrorCode()));
        payment.setErrorMsg(String.valueOf(exceptionInfo.getErrorMsg()));
        payment.setPayStatus(PayStatusCode.TRADE_FAIL);
        paymentService.updateById(payment);

        // 调用失败处理
        for (AbsPayStrategy paymentStrategy : strategyList) {
            paymentStrategy.doAsyncErrorHandler(exceptionInfo);
        }
    }

}
