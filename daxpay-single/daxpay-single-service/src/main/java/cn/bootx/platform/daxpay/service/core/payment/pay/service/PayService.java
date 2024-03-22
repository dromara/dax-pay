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
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrderExtra;
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
import org.springframework.transaction.annotation.Propagation;
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

    private final PayAssistService payAssistService;

    private final ClientNoticeService clientNoticeService;

    private final LockTemplate lockTemplate;


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
     * 支付入口
     * 支付下单接口(同步/异步/组合支付)
     * 1. 同步支付：包含一个或多个同步支付通道进行支付
     * 2. 异步支付：例如支付宝、微信，发起支付后还需要跳转第三方平台进行支付，支付后通常需要进行回调，之后才完成支付记录
     * 3. 组合支付：主要是混合了同步支付和异步支付，同时异步支付只能有一个，在支付时先对同步支付进行扣减，然后异步支付回调结束后完成整个支付单
     * 注意:
     * 组合支付在非第一次支付的时候，只对新传入的异步支付通道进行处理，该通道的价格使用第一次发起的价格，旧的同步支付如果传入后也不做处理，
     * 支付单中支付通道列表将会为 旧有的同步支付+新传入的异步支付方式(在具体支付实现中处理)
     *
     * 订单数据会先进行入库, 才会进行发起支付, 在调用各通道支付之前发生错误, 数据不会入库
     */
    public PayResult pay(PayParam payParam){

        // 异步支付方式检查
        PayUtil.validationAsyncPay(payParam);
        // 检查支付金额
        PayUtil.validationAmount(payParam.getPayChannels());

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
                    return this.firstSyncPay(payParam);
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
                // 单个异步通道支付

                // 包含异步通道的组合支付

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * 首次同步支付, 可以为一个或多个同步通道进行支付
     */
    private PayResult firstSyncPay(PayParam payParam){
        // 创建支付订单和扩展记录并返回支付订单对象
        PayOrder payOrder = payAssistService.createPayOrder(payParam);
        PayLocal payInfo = PaymentContextLocal.get().getPayInfo();

        // 获取支付方式，通过工厂生成对应的策略组
        List<AbsPayStrategy> strategies = PayStrategyFactory.createAsyncLast(payParam.getPayChannels());
        if (CollectionUtil.isEmpty(strategies)) {
            throw new PayUnsupportedMethodException();
        }

        // 初始化支付参数
        for (AbsPayStrategy strategy : strategies) {
            strategy.initPayParam(payOrder, payParam);
        }

        // 执行支付前处理动作
        strategies.forEach(AbsPayStrategy::doBeforePayHandler);
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
     */
    private PayResult firstAsyncPay(PayParam payParam){
        PayLocal payInfo = PaymentContextLocal.get().getPayInfo();
        // 开启新事务执行订单预保存操作, 并返回对应的支付策略组
        AbsPayStrategy asyncPayStrategy = SpringUtil.getBean(this.getClass()).asyncPayPreSave(payParam);

        // 支付操作
        try {
            asyncPayStrategy.doPayHandler();
        } catch (Exception e) {
            // 记录错误原因
            PayOrderExtra payOrderExtra = payInfo.getPayOrderExtra();
            payOrderExtra.setErrorMsg(e.getMessage());
            throw e;
        }
        // 支付调用成功操作,
        asyncPayStrategy.doSuccessHandler();
        PayOrder payOrder = payInfo.getPayOrder();
        // 如果支付完成, 进行订单完成处理, 同时发送回调消息
        if (payInfo.isPayComplete()) {
            payOrder.setGatewayOrderNo(payInfo.getGatewayOrderNo())
                    .setStatus(SUCCESS.getCode())
                    .setPayTime(LocalDateTime.now());
            // TODO 使用网关返回的时间
            payOrderService.updateById(payOrder);
        }
        // 如果支付完成 发送通知
        if (Objects.equals(payOrder.getStatus(), SUCCESS.getCode())){
            clientNoticeService.registerPayNotice(payOrder, payInfo.getPayOrderExtra(), payInfo.getPayChannelOrders());
        }
        return PayBuilder.buildPayResultByPayOrder(payOrder);
    }

    /**
     * 首次组合支付, 包含异步支付通道
     */
    private PayResult firstCombinationPay(PayParam payParam){
        PayLocal payInfo = PaymentContextLocal.get().getPayInfo();
        // 创建支付订单和扩展记录并返回支付订单对象
        PayOrder payOrder = payAssistService.createPayOrder(payParam);

        // 获取支付方式，通过工厂生成对应的策略组
        List<AbsPayStrategy> strategies = PayStrategyFactory.createAsyncLast(payParam.getPayChannels());
        if (CollectionUtil.isEmpty(strategies)) {
            throw new PayUnsupportedMethodException();
        }
        // 初始化支付的参数
        for (AbsPayStrategy strategy : strategies) {
            strategy.initPayParam(payOrder, payParam);
        }

        // 取出同步通道 然后进行支付
        List<AbsPayStrategy> syncStrategies = strategies.stream()
                .filter(strategy -> !ASYNC_TYPE.contains(strategy.getChannel()))
                .collect(Collectors.toList());

        // 初始化支付参数
        for (AbsPayStrategy strategy : syncStrategies) {
            strategy.initPayParam(payOrder, payParam);
        }

        // 执行支付前处理动作
        syncStrategies.forEach(AbsPayStrategy::doBeforePayHandler);
        // 生成支付通道订单
        syncStrategies.forEach(AbsPayStrategy::generateChannelOrder);
        // 支付操作
        syncStrategies.forEach(AbsPayStrategy::doPayHandler);
        // 支付成功操作, 进行扣款、创建记录类类似的操作
        syncStrategies.forEach(AbsPayStrategy::doSuccessHandler);
        payOrder.setStatus(SUCCESS.getCode())
                .setPayTime(LocalDateTime.now());
        // 保存通道支付订单
        payAssistService.savePayChannelOrder(strategies);
        // ------------------------- 进行异步支付 -------------------------------
        // 筛选出异步通道策略类
        AbsPayStrategy asyncPayStrategy = strategies.stream()
                .filter(strategy -> !ASYNC_TYPE.contains(strategy.getChannel()))
                .findFirst()
                .orElseThrow(() -> new PayFailureException("数据和代码异常, 请排查代码"));

        // 支付前准备
        asyncPayStrategy.doBeforePayHandler();
        // 设置异步支付通道订单信息
        asyncPayStrategy.generateChannelOrder();
        try {
            // 异步支付操作
            asyncPayStrategy.doPayHandler();
        } catch (Exception e) {
            // 记录错误原因
            PayOrderExtra payOrderExtra = payInfo.getPayOrderExtra();
            payOrderExtra.setErrorMsg(e.getMessage());
            throw e;
        }
        // 支付调用成功操作, 进行扣款、创建记录类类似的操作
        asyncPayStrategy.doSuccessHandler();
        // 如果支付完成, 进行订单完成处理, 同时发送回调消息
        if (payInfo.isPayComplete()) {
            payOrder.setGatewayOrderNo(payInfo.getGatewayOrderNo())
                    .setStatus(SUCCESS.getCode())
                    .setPayTime(LocalDateTime.now());
            // TODO 使用网关返回的时间
            payOrderService.updateById(payOrder);
        }
        // 如果支付完成 发送通知
        if (Objects.equals(payOrder.getStatus(), SUCCESS.getCode())){
            clientNoticeService.registerPayNotice(payOrder, payInfo.getPayOrderExtra(), payInfo.getPayChannelOrders());
        }
        return PayBuilder.buildPayResultByPayOrder(payOrder);

    }

    /**
     * 异步支付预报保存处理, 无论请求成功还是失败, 各种订单对象都会保存
     * 1. 创建支付订单/通道支付订单/扩展信息
     * 2, 返回支付列表记录
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public AbsPayStrategy asyncPayPreSave(PayParam payParam){
        // 创建支付订单和扩展记录并返回支付订单对象
        PayOrder payOrder = payAssistService.createPayOrder(payParam);

        // 获取支付方式，通过工厂生成对应的策略组
        List<AbsPayStrategy> strategies = PayStrategyFactory.createAsyncLast(payParam.getPayChannels());
        if (CollectionUtil.isEmpty(strategies)) {
            throw new PayUnsupportedMethodException();
        }

        AbsPayStrategy absPayStrategy = Optional.ofNullable(strategies.get(0))
                .orElseThrow(() -> new PayFailureException("数据和代码异常, 请排查代码"));

        // 初始化支付的参数
        absPayStrategy.initPayParam(payOrder, payParam);

        // 执行支付前处理动作
        absPayStrategy.doBeforePayHandler();
        // 执行支付通道订单的生成和保存
        absPayStrategy.generateChannelOrder();
        return absPayStrategy;
    }

    /**
     * 重复支付
     */
    public void repeatAsyncPay(){

    }

    /**
     *
     */
    public void repeatCombinationPay(){

    }
}
