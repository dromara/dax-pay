package cn.bootx.platform.daxpay.service.core.payment.pay.service;

import cn.bootx.platform.common.core.exception.RepetitiveOperationException;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.exception.pay.PayUnsupportedMethodException;
import cn.bootx.platform.daxpay.param.pay.PayChannelParam;
import cn.bootx.platform.daxpay.param.pay.PayParam;
import cn.bootx.platform.daxpay.param.pay.SimplePayParam;
import cn.bootx.platform.daxpay.result.pay.PayResult;
import cn.bootx.platform.daxpay.service.common.context.PayLocal;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.order.pay.builder.PayBuilder;
import cn.bootx.platform.daxpay.service.core.order.pay.dao.PayChannelOrderManager;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayChannelOrder;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrderExtra;
import cn.bootx.platform.daxpay.service.core.order.pay.service.PayOrderExtraService;
import cn.bootx.platform.daxpay.service.core.order.pay.service.PayOrderService;
import cn.bootx.platform.daxpay.service.core.payment.notice.service.ClientNoticeService;
import cn.bootx.platform.daxpay.service.core.payment.pay.factory.PayStrategyFactory;
import cn.bootx.platform.daxpay.service.func.AbsPayStrategy;
import cn.bootx.platform.daxpay.util.PayUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static cn.bootx.platform.daxpay.code.PayChannelEnum.ASYNC_TYPE;
import static cn.bootx.platform.daxpay.code.PayStatusEnum.SUCCESS;


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

    private final PayOrderExtraService payOrderExtraManager;

    private final PayAssistService payAssistService;

    private final ClientNoticeService clientNoticeService;

    private final PayChannelOrderManager payChannelOrderManager;

    private final LockTemplate lockTemplate;


    /**
     * 简单下单, 可以视为不支持组合支付的下单接口
     */
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
     * 支付入口
     * 支付下单接口(同步/异步/组合支付)
     * 1. 同步支付：包含一个或多个同步支付通道进行支付, 使用一个事务进行包裹，要么成功要么失败
     * 2. 异步支付：会首先创建订单信息，然后再发起支付，支付成功后更新订单和通道订单信息，支付失败也会存在订单信息，预防丢单
     * 3. 组合支付(包含异步支付)：会首先进行同步通道的支付，支付完成后才会调用异步支付，如果同步支付失败会回滚信息，不会进行存库
     *  执行异步通道支付的逻辑与上面异步支付的逻辑一致
     * 4. 同步支付一次支付完成，不允许重复发起支付
     * 5. 重复支付时，不允许中途将异步支付换为同步支付，也不允许更改出支付通道和支付方式之外的支付参数（请求时间、IP、签名等可以更改）
     *
     */
    public PayResult pay(PayParam payParam){

        // 异步支付方式检查
        PayUtil.validationAsyncPay(payParam);
        // 检查支付金额
        PayUtil.validationAmount(payParam.getPayChannels());
        // 校验支付限额
        payAssistService.validationLimitAmount(payParam);

        String businessNo = payParam.getBusinessNo();
        // 加锁
        LockInfo lock = lockTemplate.lock("payment:pay:" + businessNo,10000,200);
        if (Objects.isNull(lock)){
            throw new RepetitiveOperationException("正在支付中，请勿重复支付");
        }
        try {
            // 查询并检查订单
            PayOrder payOrder = payAssistService.getOrderAndCheck(payParam.getBusinessNo());
            // 初始化上下文
            payAssistService.initPayContext(payOrder, payParam);
            // 走首次下单逻辑还是重复发起逻辑
            if (Objects.isNull(payOrder)){
                // 判断不同的支付请求类型, 然后走不同的逻辑
                List<PayChannelParam> payChannels = payParam.getPayChannels();
                // 不包含异步支付通道
                if (PayUtil.isNotSync(payChannels)){
                    return SpringUtil.getBean(this.getClass()).firstSyncPay(payParam);
                }
                // 单个异步通道支付
                else if (payChannels.size() == 1 && !PayUtil.isNotSync(payChannels)) {
                    return this.firstAsyncPay(payParam);
                }
                // 包含异步通道的组合支付
                else if (!PayUtil.isNotSync(payChannels)) {
                    return this.firstCombinationPay(payParam);
                } else {
                    throw new PayFailureException("支付参数错误");
                }
            } else {
                List<PayChannelParam> payChannels = payParam.getPayChannels();
                // 不包含异步支付通道
                if (PayUtil.isNotSync(payChannels)){
                    throw new PayFailureException("支付参数错误, 不可以中途切换为同步支付通道");
                }
                // 单个异步通道支付
                if (payOrder.isAsyncPay() && !payOrder.isCombinationPay()){
                    return this.repeatAsyncPay(payParam,payOrder);
                }
                // 包含异步通道的组合支付
                else if (payOrder.isAsyncPay()){
                    return this.repeatCombinationPay(payParam, payOrder);
                } else {
                    throw new PayFailureException("支付参数错误");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /**
     * 首次同步支付, 可以为一个或多个同步通道进行支付
     * 使用事务，保证支付只有成功或失败两种状态
     */
    @Transactional(rollbackFor = Exception.class)
    public PayResult firstSyncPay(PayParam payParam){
        PayLocal payInfo = PaymentContextLocal.get().getPayInfo();

        // 获取支付方式，通过工厂生成对应的策略组
        List<AbsPayStrategy> strategies = PayStrategyFactory.createAsyncLast(payParam.getPayChannels());
        if (CollectionUtil.isEmpty(strategies)) {
            throw new PayUnsupportedMethodException();
        }

        // 初始化支付参数
        strategies.forEach(strategy -> strategy.setPayParam(payParam));

        // 执行支付前处理动作, 进行校验
        strategies.forEach(AbsPayStrategy::doBeforePayHandler);

        // 创建支付订单和扩展记录并返回支付订单对象
        PayOrder payOrder = payAssistService.createPayOrder(payParam);
        strategies.forEach(strategy -> strategy.setOrder(payOrder));

        // 生成支付通道订单
        strategies.forEach(AbsPayStrategy::generateChannelOrder);
        // 支付操作
        strategies.forEach(AbsPayStrategy::doPayHandler);
        // 支付成功操作, 进行扣款、创建记录类类似的操作
        strategies.forEach(AbsPayStrategy::doSuccessHandler);
        payOrder.setStatus(SUCCESS.getCode())
                .setPayTime(LocalDateTime.now());
        // 保存通道支付订单
        payAssistService.savePayChannelOrder(strategies);
        // 更新订单信息
        payOrderService.updateById(payOrder);
        // 支付完成 发送通知
        clientNoticeService.registerPayNotice(payOrder, payInfo.getPayOrderExtra(), payInfo.getPayChannelOrders());
        return PayBuilder.buildPayResultByPayOrder(payOrder);
    }

    /**
     * 首次单个异步通道支付
     * 拆分为多阶段，1. 保存订单记录信息 2 调起支付 3. 支付成功后操作
     */
    private PayResult firstAsyncPay(PayParam payParam){
        PayLocal payInfo = PaymentContextLocal.get().getPayInfo();

        // 获取支付方式，通过工厂生成对应的策略组
         List<AbsPayStrategy> strategies = PayStrategyFactory.createAsyncLast(payParam.getPayChannels());
        if (CollectionUtil.isEmpty(strategies)) {
            throw new PayUnsupportedMethodException();
        }
        // 获取异步通道
        AbsPayStrategy asyncPayStrategy = Optional.ofNullable(strategies.get(0))
                .orElseThrow(() -> new PayFailureException("数据和代码异常, 请排查代码"));

        // 初始化支付的参数
        asyncPayStrategy.setPayParam(payParam);

        // 执行支付前处理动作, 进行各种校验
        asyncPayStrategy.doBeforePayHandler();
        // 执行支付前的保存动作, 保持订单和通道订单的原子性
        PayOrder payOrder = SpringUtil.getBean(this.getClass()).firstPreAsyncPay(asyncPayStrategy, payParam);

        // 支付操作
        try {
            asyncPayStrategy.doPayHandler();
        } catch (Exception e) {
            // 记录错误原因
            PayOrderExtra payOrderExtra = payInfo.getPayOrderExtra();
            payOrderExtra.setErrorMsg(e.getMessage());
            payOrderExtraManager.update(payOrderExtra);
            throw e;
        }
        // 支付调起成功后操作, 使用事务来保证数据一致性
        return SpringUtil.getBean(this.getClass()).firstAsyncPaySuccess(asyncPayStrategy,payOrder);

    }

    /**
     * 异步单通道支付的订单预保存, 保存订单和通道订单
     */
    @Transactional(rollbackFor = Exception.class)
    public PayOrder firstPreAsyncPay(AbsPayStrategy asyncPayStrategy, PayParam payParam){
        // 创建支付订单和扩展记录并返回支付订单对象
        PayOrder payOrder = payAssistService.createPayOrder(payParam);
        asyncPayStrategy.setOrder(payOrder);
        // 生成通道支付订单
        asyncPayStrategy.generateChannelOrder();
        return payOrder;
    }

    /**
     * 首次单通道异步支付成功后操作, 更新订单和扥爱松
     */
    @Transactional(rollbackFor = Exception.class)
    public PayResult firstAsyncPaySuccess(AbsPayStrategy asyncPayStrategy, PayOrder payOrder){
        PayLocal payInfo = PaymentContextLocal.get().getPayInfo();
        // 支付调用成功操作,
        asyncPayStrategy.doSuccessHandler();
        // 如果支付完成, 进行订单完成处理, 同时发送回调消息
        if (payInfo.isPayComplete()) {
            // TODO 使用网关返回的时间
            payOrder.setGatewayOrderNo(payInfo.getGatewayOrderNo())
                    .setStatus(SUCCESS.getCode())
                    .setPayTime(LocalDateTime.now());
        }
        payOrderService.updateById(payOrder);
        // 扩展记录更新
        PayOrderExtra payOrderExtra = payInfo.getPayOrderExtra();
        payOrderExtra.setErrorMsg(null);
        payOrderExtraManager.update(payOrderExtra);
        // 如果支付完成 发送通知
        if (Objects.equals(payOrder.getStatus(), SUCCESS.getCode())){
            clientNoticeService.registerPayNotice(payOrder, payOrderExtra, payInfo.getPayChannelOrders());
        }
        return PayBuilder.buildPayResultByPayOrder(payOrder);
    }

    /**
     * 首次组合支付, 包含异步支付通道
     */
    private PayResult firstCombinationPay(PayParam payParam){
        PayLocal payInfo = PaymentContextLocal.get().getPayInfo();
        // ------------------------- 进行同步支付, 成功后返回异步通道策略类 -------------------------------
        AbsPayStrategy asyncPayStrategy = SpringUtil.getBean(this.getClass()).firstCombinationSyncPay(payParam);
        // ------------------------- 进行异步支付 -------------------------------
        try {
            // 异步支付操作
            asyncPayStrategy.doPayHandler();
        } catch (Exception e) {
            // 记录错误原因
            PayOrderExtra payOrderExtra = payInfo.getPayOrderExtra();
            payOrderExtra.setErrorMsg(e.getMessage());
            payOrderExtraManager.update(payOrderExtra);
            throw e;
        }
        // 支付调起成功后操作, 使用事务来保证数据一致性
        return SpringUtil.getBean(this.getClass()).firstCombinationPaySuccess(asyncPayStrategy);
    }

    /**
     * 首次组合支付, 先进行同步支付, 如果成功返回异步支付策略
     * 注意: 同时也对异步支付通道进行校验和订单的保存, 保证整个订单操作的原子性
     */
    @Transactional(rollbackFor = Exception.class)
    public AbsPayStrategy firstCombinationSyncPay(PayParam payParam){

        // 获取支付方式，通过工厂生成对应的策略组
        List<AbsPayStrategy> strategies = PayStrategyFactory.createAsyncLast(payParam.getPayChannels());
        if (CollectionUtil.isEmpty(strategies)) {
            throw new PayUnsupportedMethodException();
        }
        // 初始化支付的参数
        for (AbsPayStrategy strategy : strategies) {
            strategy.setPayParam(payParam);
        }

        // 初始化支付参数
        strategies.forEach(strategy -> strategy.setPayParam(payParam));

        // 执行支付前处理动作, 对各通道进行检验
        strategies.forEach(AbsPayStrategy::doBeforePayHandler);
        // 创建支付订单和扩展记录并返回支付订单对象
        PayOrder payOrder = payAssistService.createPayOrder(payParam);
        strategies.forEach(strategy -> strategy.setOrder(payOrder));

        // 生成支付通道订单, 同时也执行异步支付通道订单的保存, 保证订单操作的原子性
        strategies.forEach(AbsPayStrategy::generateChannelOrder);

        // 取出同步通道 然后进行同步通道的支付操作
        List<AbsPayStrategy> syncStrategies = strategies.stream()
                .filter(strategy -> !ASYNC_TYPE.contains(strategy.getChannel()))
                .collect(Collectors.toList());
        syncStrategies.forEach(strategy -> strategy.setPayParam(payParam));

        // 支付操作
        syncStrategies.forEach(AbsPayStrategy::doPayHandler);
        // 支付调起成功操作, 进行扣款、创建记录类类似的操作
        syncStrategies.forEach(AbsPayStrategy::doSuccessHandler);
        // 保存通道支付订单
        payAssistService.savePayChannelOrder(strategies);

        return strategies.stream()
                .filter(o-> ASYNC_TYPE.contains(o.getChannel()))
                .findFirst()
                .orElseThrow(()->new PayFailureException("支付代码异常,请检查"));
    }

    /**
     * 首次组合支付成功后操作
     */
    @Transactional(rollbackFor = Exception.class)
    public PayResult firstCombinationPaySuccess(AbsPayStrategy asyncPayStrategy){
        PayLocal payInfo = PaymentContextLocal.get().getPayInfo();
        PayOrder payOrder = payInfo.getPayOrder();
        // 支付调起成功的处理
        asyncPayStrategy.doSuccessHandler();
        // 如果支付完成, 进行订单完成处理, 同时发送回调消息
        if (payInfo.isPayComplete()) {
            // TODO 使用网关返回的时间
            payOrder.setGatewayOrderNo(payInfo.getGatewayOrderNo())
                    .setStatus(SUCCESS.getCode())
                    .setPayTime(LocalDateTime.now());
        }
        payOrderService.updateById(payOrder);
        // 扩展记录更新
        PayOrderExtra payOrderExtra = payInfo.getPayOrderExtra();
        payOrderExtra.setErrorMsg(null);
        payOrderExtraManager.update(payOrderExtra);
        // 如果支付完成 发送通知
        if (Objects.equals(payOrder.getStatus(), SUCCESS.getCode())){
            clientNoticeService.registerPayNotice(payOrder, payOrderExtra, payInfo.getPayChannelOrders());
        }
        return PayBuilder.buildPayResultByPayOrder(payOrder);
    }

    /**
     * 重复支付-单异步通道支付
     */
    public PayResult repeatAsyncPay(PayParam payParam, PayOrder payOrder){
        // 查询支付扩展订单信息
        PayOrderExtra payOrderExtra = payOrderExtraManager.findById(payOrder.getId());
        //获取异步支付通道参数并构建支付策略
        PayChannelParam payChannelParam = payAssistService.switchAsyncPayParam(payParam, payOrder);
        AbsPayStrategy asyncPayStrategy = PayStrategyFactory.create(payChannelParam);
        // 初始化支付的参数
        asyncPayStrategy.initPayParam(payOrder, payParam);
        // 执行支付前处理动作
        asyncPayStrategy.doBeforePayHandler();
        // 更新支付通道订单的信息
        asyncPayStrategy.generateChannelOrder();
        try {
            // 支付操作
            asyncPayStrategy.doPayHandler();
        } catch (Exception e) {
            // 记录错误原因
            payOrderExtra.setErrorMsg(e.getMessage());
            payOrderExtraManager.update(payOrderExtra);
            throw e;
        }
        // 支付调起成功后操作, 使用事务来保证数据一致性
        return SpringUtil.getBean(this.getClass()).repeatPaySuccess(asyncPayStrategy, payOrder, payOrderExtra);
    }

    /**
     * 重复发起组合支付(包含异步支付)
     */
    public PayResult repeatCombinationPay(PayParam payParam, PayOrder payOrder){
        PayLocal payInfo = PaymentContextLocal.get().getPayInfo();
        PayOrderExtra payOrderExtra = payInfo.getPayOrderExtra();
        PayChannelParam payChannelParam = payAssistService.switchAsyncPayParam(payParam, payOrder);
        // 取出异步通道 然后进行支付
        AbsPayStrategy asyncPayStrategy = PayStrategyFactory.create(payChannelParam);
        // 初始化参数
        asyncPayStrategy.initPayParam(payOrder, payParam);
        // 支付前准备
        asyncPayStrategy.doBeforePayHandler();
        // 更新异步支付通道订单信息
        asyncPayStrategy.generateChannelOrder();
        try {
            // 异步支付操作
            asyncPayStrategy.doPayHandler();
        } catch (Exception e) {
            // 记录错误原因
            payOrderExtra.setErrorMsg(e.getMessage());
            payOrderExtraManager.update(payOrderExtra);
            throw e;
        }
        // 支付调起成功后操作, 使用事务来保证数据一致性
        return SpringUtil.getBean(this.getClass()).repeatPaySuccess(asyncPayStrategy, payOrder,payOrderExtra);

    }

    /**
     * 重复支付成功后操作
     */
    @Transactional(rollbackFor = Exception.class)
    public PayResult repeatPaySuccess(AbsPayStrategy asyncPayStrategy, PayOrder payOrder, PayOrderExtra payOrderExtra) {
        PayLocal payInfo = PaymentContextLocal.get().getPayInfo();
        asyncPayStrategy.doSuccessHandler();
        // 如果支付完成, 进行订单完成处理, 同时发送回调消息
        if (payInfo.isPayComplete()) {
            // TODO 使用网关返回的时间
            payOrder.setGatewayOrderNo(payInfo.getGatewayOrderNo())
                    .setStatus(SUCCESS.getCode())
                    .setPayTime(LocalDateTime.now());
        }
        payOrderService.updateById(payOrder);
        // 扩展记录更新
        payOrderExtra.setErrorMsg(null);
        payOrderExtraManager.update(payOrderExtra);
        // 如果支付完成 发送通知
        if (Objects.equals(payOrder.getStatus(), SUCCESS.getCode())){
            // 查询通道订单
            List<PayChannelOrder> payChannelOrders = payChannelOrderManager.findAllByPaymentId(payOrder.getId());
            clientNoticeService.registerPayNotice(payOrder, payOrderExtra, payChannelOrders);
        }
        return PayBuilder.buildPayResultByPayOrder(payOrder);
    }
}
