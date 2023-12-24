package cn.bootx.platform.daxpay.core.payment.pay.service;

import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.bootx.platform.common.core.util.ValidationUtil;
import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.core.order.pay.builder.PaymentBuilder;
import cn.bootx.platform.daxpay.core.order.pay.dao.PayOrderChannelManager;
import cn.bootx.platform.daxpay.core.order.pay.dao.PayOrderExtraManager;
import cn.bootx.platform.daxpay.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.core.order.pay.entity.PayOrderChannel;
import cn.bootx.platform.daxpay.core.order.pay.entity.PayOrderExtra;
import cn.bootx.platform.daxpay.core.order.pay.service.PayOrderService;
import cn.bootx.platform.daxpay.core.payment.pay.factory.PayStrategyFactory;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.exception.pay.PayUnsupportedMethodException;
import cn.bootx.platform.daxpay.func.AbsPayStrategy;
import cn.bootx.platform.daxpay.func.PayStrategyConsumer;
import cn.bootx.platform.daxpay.param.pay.PayParam;
import cn.bootx.platform.daxpay.param.pay.PayWayParam;
import cn.bootx.platform.daxpay.result.pay.PayResult;
import cn.bootx.platform.daxpay.util.PayUtil;
import cn.hutool.core.collection.CollectionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;


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

    private final PayOrderService payOrderService;

    private final PayAssistService payAssistService;;

    private final PayOrderExtraManager payOrderExtraManager;

    private final PayOrderChannelManager payOrderChannelManager;


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
        PayUtil.validationAsyncPayMode(payParam);

        // 获取并校验支付状态
        PayOrder payOrder = this.getAndCheckByBusinessId(payParam.getBusinessNo());

        // 初始化上下文
        payAssistService.initExpiredTime(payOrder, payParam);

        // 异步支付且非第一次支付
        if (Objects.nonNull(payOrder) && payOrder.isAsyncPayMode()) {
            return this.paySyncNotFirst(payParam, payOrder);
        } else {
            // 第一次发起支付或同步支付
            return this.payFirst(payParam, payOrder);
        }
    }

    /**
     * 发起的第一次支付请求(同步/异步)
     */
    private PayResult payFirst(PayParam payParam, PayOrder payOrder) {
        // 1. 已经发起过支付情况直接返回支付结果
        if (Objects.nonNull(payOrder)) {
            return PaymentBuilder.buildPayResultByPayOrder(payOrder);
        }

        // 2. 价格检测
        PayUtil.validationAmount(payParam.getPayWays());

        // 3. 创建支付相关的记录并返回支付订单对象
        payOrder = this.createPayOrder(payParam);

        // 4. 调用支付方法进行发起支付
        this.payFirstMethod(payParam, payOrder);

        // 5. 返回支付结果
        return PaymentBuilder.buildPayResultByPayOrder(payOrder);
    }

    /**
     * 执行支付方法 (第一次支付)
     * @param payParam 支付参数
     * @param payOrder 支付订单
     */
    private void payFirstMethod(PayParam payParam, PayOrder payOrder) {

        // 1.获取支付方式，通过工厂生成对应的策略组
        List<AbsPayStrategy> paymentStrategyList = PayStrategyFactory.create(payParam.getPayWays());
        if (CollectionUtil.isEmpty(paymentStrategyList)) {
            throw new PayUnsupportedMethodException();
        }

        // 2.初始化支付的参数
        for (AbsPayStrategy paymentStrategy : paymentStrategyList) {
            paymentStrategy.initPayParam(payOrder, payParam);
        }

        // 3.支付前准备, 执行支付前处理
        this.doHandler(payOrder, paymentStrategyList, AbsPayStrategy::doBeforePayHandler, null);

        // 4.支付操作
        this.doHandler(payOrder, paymentStrategyList, AbsPayStrategy::doPayHandler, (strategies, payOrderObj) -> {
            // 发起支付成功进行的执行方法
            strategies.forEach(AbsPayStrategy::doSuccessHandler);
            // 所有支付方式都是同步时, 对支付订单进行处理
            if (PayUtil.isNotSync(payParam.getPayWays())) {
                // 修改支付订单状态为成功
                payOrderObj.setStatus(PayStatusEnum.SUCCESS.getCode());
                payOrderObj.setPayTime(LocalDateTime.now());
            }
            payOrderService.updateById(payOrderObj);
        });
    }

    /**
     * 异步支付执行(非第一次请求), 只执行异步支付策略, 报错不影响继续发起支付
     */
    private PayResult paySyncNotFirst(PayParam payParam, PayOrder payOrder) {

        // 0. 处理支付完成情况(完成/退款)
        List<String> trades = Arrays.asList(PayStatusEnum.SUCCESS.getCode(), PayStatusEnum.CANCEL.getCode(),
                PayStatusEnum.PARTIAL_REFUND.getCode(), PayStatusEnum.REFUNDED.getCode());
        if (trades.contains(payOrder.getStatus())) {
            return PaymentBuilder.buildPayResultByPayOrder(payOrder);
        }

        // 1.获取 异步支付 通道道，通过工厂生成对应的策略组(只包含异步支付的策略, 同步支付不再进行执行)
        PayWayParam payWayParam = this.getAsyncPayParam(payParam, payOrder);
        List<AbsPayStrategy> asyncStrategyList = PayStrategyFactory.create(Collections.singletonList(payWayParam));

        // 2.初始化支付的参数
        for (AbsPayStrategy paymentStrategy : asyncStrategyList) {
            paymentStrategy.initPayParam(payOrder, payParam);
        }
        // 3.支付前准备
        this.doHandler(payOrder, asyncStrategyList, AbsPayStrategy::doBeforePayHandler, null);

        // 4. 发起支付
        this.doHandler(payOrder, asyncStrategyList, AbsPayStrategy::doPayHandler, (strategyList, paymentObj) -> {
            // 发起支付成功进行的执行方法
            strategyList.forEach(AbsPayStrategy::doSuccessHandler);
            payOrderService.updateById(paymentObj);
        });

        // 5. 组装返回参数
        return PaymentBuilder.buildPayResultByPayOrder(payOrder);
    }

    /**
     * 执行策略中不同的handler
     * @param payment 主支付对象
     * @param strategyList 策略列表
     * @param payMethod 执行支付的函数或者支付前的函数
     * @param successMethod 执行成功的函数
     */
    private void doHandler(PayOrder payment, List<AbsPayStrategy> strategyList, Consumer<AbsPayStrategy> payMethod,
                           PayStrategyConsumer<List<AbsPayStrategy>, PayOrder> successMethod) {
        // 执行策略操作，如支付前/支付时
        // 等同strategyList.forEach(payMethod.accept(PaymentStrategy))
        strategyList.forEach(payMethod);

        // 执行操作成功的处理
        Optional.ofNullable(successMethod).ifPresent(fun -> fun.accept(strategyList, payment));
    }

    /**
     * 获取异步支付参数
     */
    private PayWayParam getAsyncPayParam(PayParam payParam, PayOrder payOrder) {
        // 查询之前的支付方式
        String asyncPayChannel = payOrder.getAsyncPayChannel();
        PayOrderChannel payOrderChannel = payOrderChannelManager.findByOderIdAndChannel(payOrder.getId(), asyncPayChannel)
                .orElseThrow(() -> new PayFailureException("支付方式数据异常"));

        // 新的异步支付方式
        PayWayParam payWayParam = payParam.getPayWays()
                .stream()
                .filter(payMode -> PayChannelEnum.ASYNC_TYPE_CODE.contains(payMode.getChannel()))
                .findFirst()
                .orElseThrow(() -> new PayFailureException("支付方式数据异常"));
        // 新传入的金额是否一致
        if (!Objects.equals(payOrderChannel.getAmount(), payWayParam.getAmount())){
            throw new PayFailureException("传入的支付金额非法！与订单金额不一致");
        }
        return payWayParam;
    }

    /**
     * 创建支付订单/附加表/支付通道表并保存，返回支付订单
     */
    private PayOrder createPayOrder(PayParam payParam) {
        // 构建支付订单并保存
        PayOrder payOrder = PaymentBuilder.buildPayOrder(payParam);
        payOrderService.saveOder(payOrder);
        // 构建支付订单扩展表并保存
        PayOrderExtra payOrderExtra = PaymentBuilder.buildPayOrderExtra(payParam, payOrder.getId());
        payOrderExtraManager.save(payOrderExtra);
        // 构建支付通道表并保存
        List<PayOrderChannel> payOrderChannels = PaymentBuilder.buildPayChannel(payParam.getPayWays())
                .stream()
                .peek(o -> o.setPaymentId(payOrder.getId()))
                .collect(Collectors.toList());
        payOrderChannelManager.saveAll(payOrderChannels);
        return payOrder;
    }

    /**
     * 校验支付状态，支付成功则返回，支付失败则抛出对应的异常
     */
    private PayOrder getAndCheckByBusinessId(String businessId) {
        // 根据订单查询支付记录
        PayOrder payment = payOrderService.findByBusinessId(businessId).orElse(null);
        if (Objects.nonNull(payment)) {
            // 支付失败类型状态
            List<String> tradesStatus = Arrays.asList(PayStatusEnum.FAIL.getCode(), PayStatusEnum.CANCEL.getCode(),
                    PayStatusEnum.CLOSE.getCode());
            if (tradesStatus.contains(payment.getStatus())) {
                throw new PayFailureException("支付失败或已经被撤销");
            }
            // 退款类型状态
            tradesStatus = Arrays.asList(PayStatusEnum.REFUNDED.getCode(), PayStatusEnum.PARTIAL_REFUND.getCode());
            if (tradesStatus.contains(payment.getStatus())) {
                throw new PayFailureException("支付失败或已经被撤销");
            }
            // 支付超时状态
            if (Objects.nonNull(payment.getExpiredTime())
                    && LocalDateTimeUtil.ge(LocalDateTime.now(), payment.getExpiredTime())) {
                throw new PayFailureException("支付已超时");
            }
        }
        return payment;
    }
}
