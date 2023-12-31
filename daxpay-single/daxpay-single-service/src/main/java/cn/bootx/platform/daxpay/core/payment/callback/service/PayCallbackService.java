package cn.bootx.platform.daxpay.core.payment.callback.service;

import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.PayNotifyStatusEnum;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.common.exception.ExceptionInfo;
import cn.bootx.platform.daxpay.core.order.pay.dao.PayOrderManager;
import cn.bootx.platform.daxpay.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.core.payment.callback.result.PayCallbackResult;
import cn.bootx.platform.daxpay.core.payment.pay.factory.PayStrategyFactory;
import cn.bootx.platform.daxpay.func.AbsPayStrategy;
import cn.bootx.platform.daxpay.func.PayStrategyConsumer;
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

    private final PayOrderManager payOrderService;

    /**
     * 统一回调处理
     * @param tradeStatus 支付状态
     */
    public PayCallbackResult callback(Long paymentId, String tradeStatus, Map<String, String> map) {

        // 获取支付单
        PayOrder payOrder = payOrderService.findById(paymentId).orElse(null);

        // 支付单不存在,记录回调记录
        if (Objects.isNull(payOrder)) {
            return new PayCallbackResult().setStatus(PayNotifyStatusEnum.FAIL.getCode()).setMsg("支付单不存在,记录回调记录");
        }

        // 回调时间超出了支付单超时时间, 记录一下, 不做处理 TODO 这块应该把订单给正常处理了,
        if (Objects.nonNull(payOrder.getExpiredTime())
                && LocalDateTimeUtil.ge(LocalDateTime.now(), payOrder.getExpiredTime())) {
            return new PayCallbackResult().setStatus(PayNotifyStatusEnum.FAIL.getCode()).setMsg("回调时间超出了支付单支付有效时间");
        }

        // 成功状态
        if (Objects.equals(PayNotifyStatusEnum.SUCCESS.getCode(), tradeStatus)) {
            return this.success(payOrder, map);
        }
        else {
            // 失败状态
            return this.fail(payOrder, map);
        }
    }

    /**
     * 成功处理
     */
    private PayCallbackResult success(PayOrder payOrder, Map<String, String> map) {
        PayCallbackResult result = new PayCallbackResult().setStatus(PayNotifyStatusEnum.SUCCESS.getCode());

        // payment已经被支付,不需要重复处理
        if (Objects.equals(payOrder.getStatus(), PayStatusEnum.SUCCESS.getCode())) {
            return result.setStatus(PayNotifyStatusEnum.IGNORE.getCode()).setMsg("支付单已经是支付成功状态,不进行处理");
        }

        // payment已被取消,记录回调记录
        if (!Objects.equals(payOrder.getStatus(), PayStatusEnum.PROGRESS.getCode())) {
            return result.setStatus(PayNotifyStatusEnum.FAIL.getCode()).setMsg("支付单不是待支付状态,记录回调记录");
        }

        // 2.通过工厂生成对应的策略组
        PayParam payParam = null;

        List<AbsPayStrategy> paymentStrategyList = PayStrategyFactory.createAsyncFront(payParam.getPayWays());
        if (CollectionUtil.isEmpty(paymentStrategyList)) {
            return result.setStatus(PayStatusEnum.FAIL.getCode()).setMsg("支付单数据非法,未找到对应的支付方式");
        }

        // 3.初始化支付的参数
        for (AbsPayStrategy paymentStrategy : paymentStrategyList) {
            paymentStrategy.initPayParam(payOrder, payParam);
        }
        // 4.处理方法, 支付时只有一种payModel(异步支付), 失败时payment的所有payModel都会生效
        boolean handlerFlag = this.doHandler(payOrder, paymentStrategyList, (strategyList, paymentObj) -> {
            // 执行异步支付方式的成功回调(不会有同步payModel)
            strategyList.forEach(absPaymentStrategy -> absPaymentStrategy.doAsyncSuccessHandler(map));

            // 修改payment支付状态为成功
            paymentObj.setStatus(PayStatusEnum.SUCCESS.getCode());
            paymentObj.setPayTime(LocalDateTime.now());
            payOrderService.updateById(paymentObj);
        });

        if (handlerFlag) {
            // 5. 发送成功事件
//            eventSender.sendPayComplete(PayEventBuilder.buildPayComplete(payOrder));
        }
        else {
            return result.setStatus(PayStatusEnum.FAIL.getCode()).setMsg("回调处理过程报错");
        }
        return result;
    }

    /**
     * 失败处理, 关闭并退款 按说这块不会发生
     */
    private PayCallbackResult fail(PayOrder payOrder, Map<String, String> map) {
        PayCallbackResult result = new PayCallbackResult().setStatus(PayStatusEnum.SUCCESS.getCode());

        // payment已被取消,记录回调记录,后期处理
        if (!Objects.equals(payOrder.getStatus(), PayStatusEnum.PROGRESS.getCode())) {
            return result.setStatus(PayNotifyStatusEnum.IGNORE.getCode()).setMsg("支付单已经取消,记录回调记录");
        }

        // payment支付成功, 状态非法
        if (!Objects.equals(payOrder.getStatus(), PayStatusEnum.SUCCESS.getCode())) {
            return result.setStatus(PayNotifyStatusEnum.FAIL.getCode()).setMsg("支付单状态非法,支付网关状态为失败,但支付单状态为已完成");
        }

        // 2.通过工厂生成对应的策略组
        PayParam payParam = null;
        List<AbsPayStrategy> paymentStrategyList = PayStrategyFactory.createAsyncFront(payParam.getPayWays());
        if (CollectionUtil.isEmpty(paymentStrategyList)) {
            return result.setStatus(PayNotifyStatusEnum.FAIL.getCode()).setMsg("支付单数据非法,未找到对应的支付方式");
        }
        // 3.初始化支付关闭的参数
        for (AbsPayStrategy paymentStrategy : paymentStrategyList) {
            paymentStrategy.initPayParam(payOrder, payParam);
        }
        // 4.处理方法, 支付时只有一种payModel(异步支付), 失败时payment的所有payModel都会生效
        boolean handlerFlag = this.doHandler(payOrder, paymentStrategyList, (strategyList, paymentObj) -> {
            // 执行异步支付方式的失败回调(不会有同步payModel)
            strategyList.forEach(AbsPayStrategy::doCloseHandler);

            // 修改payment支付状态为撤销
            paymentObj.setStatus(PayStatusEnum.CLOSE.getCode());
            payOrderService.updateById(paymentObj);
        });

        if (handlerFlag) {
            // 5. 发送退款事件
//            eventSender.sendPayRefund(PayEventBuilder.buildPayRefund(payOrder));
        }
        else {
            return result.setStatus(PayNotifyStatusEnum.FAIL.getCode()).setMsg("回调处理过程报错");
        }

        return result;
    }

    /**
     * 处理方法
     * @param payOrder 支付订单
     * @param strategyList 支付策略
     * @param successCallback 成功操作
     */
    private boolean doHandler(PayOrder payOrder, List<AbsPayStrategy> strategyList,
            PayStrategyConsumer<List<AbsPayStrategy>, PayOrder> successCallback) {

        try {
            // 1.获取异步支付方式，通过工厂生成对应的策略组
            List<AbsPayStrategy> syncPaymentStrategyList = strategyList.stream()
                .filter(paymentStrategy -> PayChannelEnum.ASYNC_TYPE.contains(paymentStrategy.getType()))
                .collect(Collectors.toList());
            // 执行成功方法
            successCallback.accept(syncPaymentStrategyList, payOrder);
        }
        catch (Exception e) {
            // error事件的处理
            this.asyncErrorHandler(payOrder, strategyList, e);
            return false;
        }
        return true;
    }

    /**
     * 对Error的处理
     */
    private void asyncErrorHandler(PayOrder payOrder, List<AbsPayStrategy> strategyList, Exception e) {

        // 默认的错误信息
        ExceptionInfo exceptionInfo = new ExceptionInfo(PayStatusEnum.FAIL.getCode(), e.getMessage());
//        if (e instanceof BaseException) {
//            exceptionInfo = ((BaseException) e).getExceptionInfo();
//        }
//        else if (e instanceof ErrorCodeRuntimeException) {
//            ErrorCodeRuntimeException ex = (ErrorCodeRuntimeException) e;
//            exceptionInfo = new ExceptionInfo(String.valueOf(ex.getCode()), ex.getMessage());
//        }

        // 更新Payment的状态
//        payOrder.setErrorCode(String.valueOf(exceptionInfo.getErrorCode()));
//        payOrder.setErrorMsg(String.valueOf(exceptionInfo.getErrorMsg()));
//        payOrder.setPayStatus(PayStatusCode.TRADE_FAIL);
        payOrderService.updateById(payOrder);

        // 调用失败处理
        for (AbsPayStrategy paymentStrategy : strategyList) {
            paymentStrategy.doAsyncErrorHandler(exceptionInfo);
        }
    }

}
