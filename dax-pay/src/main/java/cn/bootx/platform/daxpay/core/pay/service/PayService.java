package cn.bootx.platform.daxpay.core.pay.service;

import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.bootx.platform.common.core.util.ValidationUtil;
import cn.bootx.platform.daxpay.code.MchAndAppCode;
import cn.bootx.platform.daxpay.code.pay.PayChannelEnum;
import cn.bootx.platform.daxpay.core.merchant.dao.MchAppManager;
import cn.bootx.platform.daxpay.core.merchant.dao.MerchantInfoManager;
import cn.bootx.platform.daxpay.core.merchant.entity.MchApplication;
import cn.bootx.platform.daxpay.core.merchant.entity.MerchantInfo;
import cn.bootx.platform.daxpay.core.pay.builder.PayEventBuilder;
import cn.bootx.platform.daxpay.core.pay.builder.PaymentBuilder;
import cn.bootx.platform.daxpay.core.pay.factory.PayStrategyFactory;
import cn.bootx.platform.daxpay.core.pay.func.AbsPayStrategy;
import cn.bootx.platform.daxpay.core.pay.func.PayStrategyConsumer;
import cn.bootx.platform.daxpay.core.payment.entity.Payment;
import cn.bootx.platform.daxpay.core.payment.service.PaymentService;
import cn.bootx.platform.daxpay.dto.pay.PayResult;
import cn.bootx.platform.daxpay.exception.payment.PayFailureException;
import cn.bootx.platform.daxpay.exception.payment.PayNotExistedException;
import cn.bootx.platform.daxpay.exception.payment.PayUnsupportedMethodException;
import cn.bootx.platform.daxpay.mq.PayEventSender;
import cn.bootx.platform.daxpay.param.pay.PayParam;
import cn.bootx.platform.daxpay.param.pay.PayWayParam;
import cn.bootx.platform.daxpay.util.PayWaylUtil;
import cn.hutool.core.collection.CollectionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;

import static cn.bootx.platform.daxpay.code.pay.PayStatusCode.*;

/**
 * 支付流程
 *
 * @author xxm
 * @since 2020/12/9
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayService {

    private final PaymentService paymentService;

    private final PayEventSender eventSender;

    private final MchAppManager mchAppManager;

    private final MerchantInfoManager mchManager;

    /**
     * 支付方法(同步/异步/组合支付) 同步支付：都只会在第一次执行中就完成支付，例如钱包、积分都是调用完就进行了扣减，完成了支付记录
     * 异步支付：例如支付宝、微信，发起支付后还需要跳转第三方平台进行支付，支付后通常需要进行回调，之后才完成支付记录
     * 组合支付：主要是混合了同步支付和异步支付，同时异步支付只能有一个，在支付时先对同步支付进行扣减，然后异步支付回调结束后完成整个支付单
     * 组合支付在非第一次支付的时候，只对新传入的异步支付PayMode进行处理，PayMode的价格使用第一次发起的价格，旧的同步支付如果传入后也不做处理，
     * Payment中PayModeList将会为 旧有的同步支付+新传入的异步支付方式(在具体支付实现中处理)
     */
    @Transactional(rollbackFor = Exception.class)
    public PayResult pay(PayParam payParam) {
        // 检验参数
        ValidationUtil.validateParam(payParam);
        // 异步支付方式检查
        PayWaylUtil.validationAsyncPayMode(payParam);
        // 商户和应用信息检测
        this.checkMchAndApp(payParam);
        // 获取并校验支付状态
        Payment payment = this.getAndCheckPaymentByBusinessId(payParam.getBusinessId());

        // 异步支付且非第一次支付
        if (Objects.nonNull(payment) && payment.isAsyncPayMode()) {
            return this.paySyncNotFirst(payParam, payment);
        }
        else {
            // 第一次发起支付或同步支付
            return this.payFirst(payParam, payment);
        }
    }

    /**
     * 发起的第一次支付请求(同步/异步)
     */
    private PayResult payFirst(PayParam payParam, Payment payment) {
        // 0. 支付成功直接返回
        if (Objects.nonNull(payment)) {
            return PaymentBuilder.buildResultByPayment(payment);
        }

        // 1. 价格检测
        PayWaylUtil.validationAmount(payParam.getPayWayList());

        // 2. 创建支付记录
        payment = this.createPayment(payParam);

        // 3. 调用支付方法进行发起支付
        this.payFirstMethod(payParam, payment);

        // 4. 获取支付记录信息
        payment = paymentService.findById(payment.getId()).orElseThrow(PayNotExistedException::new);

        // 5. 返回支付结果
        PayResult payResult = PaymentBuilder.buildResultByPayment(payment);

        // 如果是支付成功, 发送事件
        if (Objects.equals(payResult.getPayStatus(), TRADE_SUCCESS)) {
            eventSender.sendPayComplete(PayEventBuilder.buildPayComplete(payment));
        }
        return payResult;
    }

    /**
     * 执行支付方法 (第一次支付)
     */
    private void payFirstMethod(PayParam payParam, Payment payment) {

        // 1.获取支付方式，通过工厂生成对应的策略组
        List<AbsPayStrategy> paymentStrategyList = PayStrategyFactory.create(payParam.getPayWayList());
        if (CollectionUtil.isEmpty(paymentStrategyList)) {
            throw new PayUnsupportedMethodException();
        }

        // 2.初始化支付的参数
        for (AbsPayStrategy paymentStrategy : paymentStrategyList) {
            paymentStrategy.initPayParam(payment, payParam);
        }

        // 3.支付前准备
        this.doHandler(payment, paymentStrategyList, AbsPayStrategy::doBeforePayHandler, null);

        // 4.支付
        this.doHandler(payment, paymentStrategyList, AbsPayStrategy::doPayHandler, (strategyList, paymentObj) -> {
            // 发起支付成功进行的执行方法
            strategyList.forEach(AbsPayStrategy::doSuccessHandler);
            // 所有支付方式都是同步时进行Payment处理
            if (PayWaylUtil.isNotSync(payParam.getPayWayList())) {
                // 修改payment支付状态为成功
                paymentObj.setPayStatus(TRADE_SUCCESS);
                paymentObj.setPayTime(LocalDateTime.now());
            }
            paymentService.updateById(paymentObj);
        });
    }

    /**
     * 异步支付执行(非第一次请求), 只执行异步支付策略, 报错不影响继续发起支付
     */
    private PayResult paySyncNotFirst(PayParam payParam, Payment payment) {

        // 0. 处理支付完成情况(完成/退款)
        List<String> trades = Arrays.asList(TRADE_SUCCESS, TRADE_CANCEL, TRADE_REFUNDING, TRADE_REFUNDED);
        if (trades.contains(payment.getPayStatus())) {
            return PaymentBuilder.buildResultByPayment(payment);
        }

        // 1.获取 异步支付 通道，通过工厂生成对应的策略组
        PayParam oldPayParam = PaymentBuilder.buildPayParamByPayment(payment);
        PayWayParam payWayParam = this.getAsyncPayModeParam(payParam, oldPayParam);
        List<AbsPayStrategy> paymentStrategyList = PayStrategyFactory.create(Collections.singletonList(payWayParam));

        // 2.初始化支付的参数
        for (AbsPayStrategy paymentStrategy : paymentStrategyList) {
            paymentStrategy.initPayParam(payment, payParam);
        }
        // 3.支付前准备
        this.doHandler(payment, paymentStrategyList, AbsPayStrategy::doBeforePayHandler, null);

        // 4. 发起支付
        this.doHandler(payment, paymentStrategyList, AbsPayStrategy::doPayHandler, (strategyList, paymentObj) -> {
            // 发起支付成功进行的执行方法
            strategyList.forEach(AbsPayStrategy::doSuccessHandler);
            paymentService.updateById(paymentObj);
        });

        // 5. 获取支付记录信息
        payment = paymentService.findById(payment.getId()).orElseThrow(PayNotExistedException::new);

        // 6. 组装返回参数
        return PaymentBuilder.buildResultByPayment(payment);
    }

    /**
     * 执行策略中不同的handler
     * @param payment 主支付对象
     * @param strategyList 策略列表
     * @param payMethod 执行支付/支付前的函数
     * @param successMethod 执行成功的函数
     */
    private void doHandler(Payment payment, List<AbsPayStrategy> strategyList, Consumer<AbsPayStrategy> payMethod,
            PayStrategyConsumer<List<AbsPayStrategy>, Payment> successMethod) {
        // 执行策略操作，如支付前/支付时
        // 等同strategyList.forEach(payMethod.accept(PaymentStrategy))
        strategyList.forEach(payMethod);

        // 执行操作成功的处理
        Optional.ofNullable(successMethod).ifPresent(fun -> fun.accept(strategyList, payment));
    }

    /**
     * 获取异步支付参数
     */
    private PayWayParam getAsyncPayModeParam(PayParam payParam, PayParam oldPaymentParam) {

        List<PayWayParam> oldPayModes = oldPaymentParam.getPayWayList();
        // 旧的异步支付方式
        PayWayParam oldModeParam = oldPayModes.stream()
            .filter(payMode -> PayChannelEnum.ASYNC_TYPE_CODE.contains(payMode.getPayChannel()))
            .findFirst()
            .orElseThrow(() -> new PayFailureException("支付方式数据异常"));

        // 新的异步支付方式
        PayWayParam payWayParam = payParam.getPayWayList()
            .stream()
            .filter(payMode -> PayChannelEnum.ASYNC_TYPE_CODE.contains(payMode.getPayChannel()))
            .findFirst()
            .orElseThrow(() -> new PayFailureException("支付方式数据异常"));
        payWayParam.setAmount(oldModeParam.getAmount());

        return payWayParam;
    }

    /**
     * 创建支付记录
     */
    private Payment createPayment(PayParam payParam) {
        // 构建payment记录 并保存
        Payment payment = PaymentBuilder.buildPayment(payParam);
        return paymentService.save(payment);
    }

    /**
     * 商户和应用信息检测
     */
    private void checkMchAndApp(PayParam payParam) {
        MerchantInfo merchantInfo = mchManager.findByCode(payParam.getMchCode())
            .orElseThrow(DataNotExistException::new);
        MchApplication mchApp = mchAppManager.findByCode(payParam.getMchAppCode()).orElseThrow(DataNotExistException::new);

        // 商户与应用是否有关联关系
        if (!Objects.equals(mchApp.getMchCode(), merchantInfo.getCode())) {
            throw new BizException("商户应用编码与商户不匹配");
        }

        // 商户是否可用状态
        if (!Objects.equals(MchAndAppCode.MCH_STATE_NORMAL, merchantInfo.getState())) {
            throw new BizException("商户状态不可用");
        }

        // 应用是否可用状态
        if (!Objects.equals(MchAndAppCode.MCH_APP_STATE_NORMAL, mchApp.getState())) {
            throw new BizException("商户应用状态不可用");
        }
    }

    /**
     * 校验支付状态，支付成功则返回，支付失败则抛出对应的异常
     */
    private Payment getAndCheckPaymentByBusinessId(String businessId) {
        // 根据订单查询支付记录
        Payment payment = paymentService.findByBusinessId(businessId).orElse(null);
        if (Objects.nonNull(payment)) {
            // 支付失败类型状态
            List<String> tradesStatus = Arrays.asList(TRADE_FAIL, TRADE_CANCEL);
            if (tradesStatus.contains(payment.getPayStatus())) {
                throw new PayFailureException("支付失败或已经被撤销");
            }
            // 退款类型状态
            tradesStatus = Arrays.asList(TRADE_REFUNDING, TRADE_REFUNDED);
            if (tradesStatus.contains(payment.getPayStatus())) {
                throw new PayFailureException("支付失败或已经被撤销");
            }
            // 支付超时状态
            if (Objects.nonNull(payment.getExpiredTime())
                    && LocalDateTimeUtil.ge(LocalDateTime.now(), payment.getExpiredTime())) {
                throw new PayFailureException("支付已超时");
            }
            return payment;
        }
        return null;
    }

}
