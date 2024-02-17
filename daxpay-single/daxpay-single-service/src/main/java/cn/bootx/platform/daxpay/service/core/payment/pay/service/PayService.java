package cn.bootx.platform.daxpay.service.core.payment.pay.service;

import cn.bootx.platform.common.core.exception.RepetitiveOperationException;
import cn.bootx.platform.daxpay.exception.pay.PayUnsupportedMethodException;
import cn.bootx.platform.daxpay.param.pay.PayChannelParam;
import cn.bootx.platform.daxpay.param.pay.PayParam;
import cn.bootx.platform.daxpay.param.pay.SimplePayParam;
import cn.bootx.platform.daxpay.result.pay.PayResult;
import cn.bootx.platform.daxpay.service.common.context.AsyncPayLocal;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.order.pay.builder.PayBuilder;
import cn.bootx.platform.daxpay.service.core.order.pay.dao.PayChannelOrderManager;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayChannelOrder;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.service.core.order.pay.service.PayOrderService;
import cn.bootx.platform.daxpay.service.core.payment.pay.factory.PayStrategyFactory;
import cn.bootx.platform.daxpay.service.func.AbsPayStrategy;
import cn.bootx.platform.daxpay.util.PayUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.bootx.platform.daxpay.code.PayStatusEnum.*;


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

    private final PayAssistService payAssistService;

    private final PayChannelOrderManager payChannelOrderManager;

    private final LockTemplate lockTemplate;

    /**
     * 支付下单接口(同步/异步/组合支付)
     * 1. 同步支付：都只会在第一次执行中就完成支付，例如钱包、储值卡都是调用完就进行了扣减，完成了支付记录
     * 2. 异步支付：例如支付宝、微信，发起支付后还需要跳转第三方平台进行支付，支付后通常需要进行回调，之后才完成支付记录
     * 3. 组合支付：主要是混合了同步支付和异步支付，同时异步支付只能有一个，在支付时先对同步支付进行扣减，然后异步支付回调结束后完成整个支付单
     * 注意:
     * 组合支付在非第一次支付的时候，只对新传入的异步支付通道进行处理，该通道的价格使用第一次发起的价格，旧的同步支付如果传入后也不做处理，
     * 支付单中支付通道列表将会为 旧有的同步支付+新传入的异步支付方式(在具体支付实现中处理)
     */
    @Transactional(rollbackFor = Exception.class)
    public PayResult pay(PayParam payParam) {
        // 异步支付方式检查
        PayUtil.validationAsyncPay(payParam);

        String businessNo = payParam.getBusinessNo();
        // 加锁
        LockInfo lock = lockTemplate.lock("payment:pay:" + businessNo);
        if (Objects.isNull(lock)){
            throw new RepetitiveOperationException("正在支付中，请勿重复支付");
        }
        try {
            // 获取并校验支付订单状态, 如果超时, 触发支付单同步和修复动作
            PayOrder payOrder = payAssistService.getOrderAndCheck(payParam.getBusinessNo());

            // 初始化上下文
            payAssistService.initPayContext(payOrder, payParam);

            // 异步支付且非第一次支付
            if (Objects.nonNull(payOrder) && payOrder.isAsyncPay()) {
                return this.paySyncNotFirst(payParam, payOrder);
            } else {
                // 第一次发起支付或同步支付
                return this.firstPay(payParam, payOrder);
            }
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /**
     * 简单下单, 可以视为不支持组合支付的下单接口
     */
    @Transactional(rollbackFor = Exception.class)
    public PayResult simplePay(SimplePayParam simplePayParam) {
        // 组装支付参数
        PayParam payParam = new PayParam();
        PayChannelParam payChannelParam = new PayChannelParam();
        payChannelParam.setChannel(simplePayParam.getChannel());
        payChannelParam.setWay(simplePayParam.getPayWay());
        payChannelParam.setAmount(simplePayParam.getAmount());
        payChannelParam.setChannelParam(simplePayParam.getChannelParam());
        BeanUtil.copyProperties(simplePayParam,payParam, CopyOptions.create().ignoreNullValue());
        payParam.setPayChannels(Collections.singletonList(payChannelParam));
        // 复用支付下单接口
        return this.pay(payParam);
    }

    /**
     * 发起的第一次支付请求(同步/异步)
     */
    private PayResult firstPay(PayParam payParam, PayOrder payOrder) {
        // 1. 已经发起过支付情况直接返回支付结果
        if (Objects.nonNull(payOrder)) {
            return PayBuilder.buildPayResultByPayOrder(payOrder);
        }

        // 2. 价格检测
        PayUtil.validationAmount(payParam.getPayChannels());

        // 3. 创建支付订单和扩展记录并返回支付订单对象
        payOrder = payAssistService.createPayOrder(payParam);

        // 4. 调用支付方法进行发起支付
        this.firstPayHandler(payParam, payOrder);

        // 5. 返回支付结果
        return PayBuilder.buildPayResultByPayOrder(payOrder);
    }

    /**
     * 执行第一次支付的方法
     */
    private void firstPayHandler(PayParam payParam, PayOrder payOrder) {

        // 1.获取支付方式，通过工厂生成对应的策略组
        List<AbsPayStrategy> payStrategyList = PayStrategyFactory.createAsyncLast(payParam.getPayChannels());
        if (CollectionUtil.isEmpty(payStrategyList)) {
            throw new PayUnsupportedMethodException();
        }

        // 2.初始化支付的参数
        for (AbsPayStrategy paymentStrategy : payStrategyList) {
            paymentStrategy.initPayParam(payOrder, payParam);
        }

        // 3.1 执行支付前处理动作
        payStrategyList.forEach(AbsPayStrategy::doBeforePayHandler);
        // 3.2 执行通道支付单的生成动作
        payStrategyList.forEach(AbsPayStrategy::generateChannelOrder);

        // 4.1 支付操作
        payStrategyList.forEach(AbsPayStrategy::doPayHandler);
        // 4.2 支付调用成功操作, 进行扣款、创建记录类类似的操作
        payStrategyList.forEach(AbsPayStrategy::doSuccessHandler);
        // 4.3 获取通道支付订单进行保存, 异步支付通道的订单单独处理
        List<PayChannelOrder> channelOrders = payStrategyList.stream()
                .map(AbsPayStrategy::getChannelOrder)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        payChannelOrderManager.saveAll(channelOrders);

        // 5.1 如果没有异步支付, 直接进行订单完成处理
        if (PayUtil.isNotSync(payParam.getPayChannels())) {
            // 修改支付订单状态为成功
            payOrder.setStatus(SUCCESS.getCode())
                    .setPayTime(LocalDateTime.now());
            payOrderService.updateById(payOrder);
        }
        // 5.2 如果异步支付完成, 进行订单完成处理
        AsyncPayLocal asyncPayInfo = PaymentContextLocal.get().getAsyncPayInfo();
        if (asyncPayInfo.isPayComplete()) {
            payOrder.setGatewayOrderNo(asyncPayInfo.getGatewayOrderNo())
                    .setStatus(SUCCESS.getCode())
                    .setPayTime(LocalDateTime.now());
            payOrderService.updateById(payOrder);
        }

    }

    /**
     * 异步支付执行(非第一次请求), 只执行异步支付策略, 报错不影响继续发起支付
     */
    private PayResult paySyncNotFirst(PayParam payParam, PayOrder payOrder) {

        // 1. 处理支付结束情况(完成/退款/关闭/错误)
        List<String> trades = Arrays.asList(SUCCESS.getCode(), CLOSE.getCode(), PARTIAL_REFUND.getCode(),
                REFUNDED.getCode(), REFUNDING.getCode(), FAIL.getCode());
        if (trades.contains(payOrder.getStatus())) {
            return PayBuilder.buildPayResultByPayOrder(payOrder);
        }

        // 2.获取 异步支付通道，通过工厂生成对应的策略组(只包含异步支付的策略, 同步支付相关逻辑不再进行执行)
        PayChannelParam payChannelParam = payAssistService.getAsyncPayParam(payParam, payOrder);
        List<AbsPayStrategy> payStrategyList = PayStrategyFactory.createAsyncLast(Collections.singletonList(payChannelParam));

        // 3.初始化支付的参数
        for (AbsPayStrategy payStrategy : payStrategyList) {
            payStrategy.initPayParam(payOrder, payParam);
        }
        // 4.支付前准备
        payStrategyList.forEach(AbsPayStrategy::doBeforePayHandler);

        // 5.1发起支付
        payStrategyList.forEach(AbsPayStrategy::doPayHandler);
        // 5.2支付发起成功处理
        payStrategyList.forEach(AbsPayStrategy::doSuccessHandler);

        // 6. 更新支付订单和扩展参数
        payOrderService.updateById(payOrder);
        payAssistService.updatePayOrderExtra(payParam,payOrder.getId());

        // 7. 组装返回参数
        return PayBuilder.buildPayResultByPayOrder(payOrder);
    }
}
