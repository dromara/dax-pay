package cn.bootx.platform.daxpay.service.core.payment.pay.service;

import cn.bootx.platform.common.core.exception.RepetitiveOperationException;
import cn.bootx.platform.common.core.function.CollectorsFunction;
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
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    private final PayChannelOrderManager channelOrderManager;

    private final ClientNoticeService clientNoticeService;

    private final LockTemplate lockTemplate;

    /**
     * 支付下单接口(同步/异步/组合支付)
     * 1. 同步支付：都只会在第一次执行中就完成支付，例如钱包、储值卡都是调用完就进行了扣减，完成了支付记录
     * 2. 异步支付：例如支付宝、微信，发起支付后还需要跳转第三方平台进行支付，支付后通常需要进行回调，之后才完成支付记录
     * 3. 组合支付：主要是混合了同步支付和异步支付，同时异步支付只能有一个，在支付时先对同步支付进行扣减，然后异步支付回调结束后完成整个支付单
     * 注意:
     * 组合支付在非第一次支付的时候，只对新传入的异步支付通道进行处理，该通道的价格使用第一次发起的价格，旧的同步支付如果传入后也不做处理，
     * 支付单中支付通道列表将会为 旧有的同步支付+新传入的异步支付方式(在具体支付实现中处理)
     *
     * 订单数据会先进行入库, 才会进行发起支付, 在调用各通道支付之前发生错误, 数据不会入库
     */
    @Transactional(rollbackFor = Exception.class)
    public PayResult pay(PayParam payParam) {
        // 异步支付方式检查
        PayUtil.validationAsyncPay(payParam);

        String businessNo = payParam.getBusinessNo();
        // 加锁
        LockInfo lock = lockTemplate.lock("payment:pay:" + businessNo,10000,200);
        if (Objects.isNull(lock)){
            throw new RepetitiveOperationException("正在支付中，请勿重复支付");
        }
        try {
            // 获取并校验支付订单状态, 如果超时, 触发支付单同步和修复动作
            PayOrder payOrder = payAssistService.getOrderAndCheck(payParam.getBusinessNo());

            // 初始化上下文
            payAssistService.initPayContext(payOrder, payParam);

            // 订单为空, 创建支付订单和通道支付订单
            if (Objects.isNull(payOrder)){
                // 预支付处理, 无论请求成功还是失败, 各种订单对象都会保存
                List<AbsPayStrategy> strategies = SpringUtil.getBean(this.getClass()).prePay(payParam);
                // 执行首次支付逻辑
                return this.onePay(strategies);
            } else {
                // 分为同步通道全部支付成功和所有支付都是支付中状态(上次支付失败)
                return this.towPay(payParam, payOrder);
            }
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /**
     * 组合支付分为同步通道全部支付成功和所有支付都是支付中状态(上次支付失败)
     * 单通道支付不需要区分上次支付发起是否成功
     */
    private PayResult towPay(PayParam payParam, PayOrder payOrder) {
        // 判断是否组合支付且同时包含异步支付
        if (payOrder.isCombinationPay()&&payOrder.isAsyncPay()) {
            // 同步支付通道全部支付成功, 单独走异步支付支付流程
            this.onlyAsyncPay(payParam, payOrder);
            // 通道订单全部支付中, 相当于上次支付异常, 重新走支付流程
            return this.allPay(payParam, payOrder);
        } else {
            // 单通道支付走标准支付流程
            return this.allPay(payParam, payOrder);
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
     * 预支付处理, 无论请求成功还是失败, 各种订单对象都会保存
     * 1. 校验参数
     * 2. 创建支付订单/通道支付订单/扩展信息
     * 3, 返回数据
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public List<AbsPayStrategy> prePay(PayParam payParam){

        // 价格检测
        PayUtil.validationAmount(payParam.getPayChannels());

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

        // 执行支付前处理动作
        strategies.forEach(AbsPayStrategy::doBeforePayHandler);
        // 执行通道支付通道订单的生成
        strategies.forEach(AbsPayStrategy::generateChannelOrder);

        // 保存通道支付订单
        payAssistService.savePayChannelOrder(strategies);
        return strategies;
    }

    /**
     * 发起的第一次支付请求(同步/异步)
     */
    private PayResult firstPay(PayParam payParam, PayOrder payOrder) {

        // 2. 价格检测
        PayUtil.validationAmount(payParam.getPayChannels());

        // 3. 创建支付订单和扩展记录并返回支付订单对象
        payOrder = payAssistService.createPayOrder(payParam);

        // 4. 调用支付方法进行发起支付
        this.allPay(payParam, payOrder);

        // 5. 返回支付结果
        return PayBuilder.buildPayResultByPayOrder(payOrder);
    }


    /**
     * 执行首次支付操作
     */
    public PayResult onePay(List<AbsPayStrategy> strategies){
        PayLocal payInfo = PaymentContextLocal.get().getPayInfo();
        // 支付操作
        try {
            strategies.forEach(AbsPayStrategy::doPayHandler);
        } catch (Exception e) {
            // 记录错误原因
            PayOrderExtra payOrderExtra = payInfo.getPayOrderExtra();
            payOrderExtra.setErrorMsg(e.getMessage());
            throw e;
        }
        PayOrder payOrder = payInfo.getPayOrder();

        // 支付调用成功操作, 进行扣款、创建记录类类似的操作
        strategies.forEach(AbsPayStrategy::doSuccessHandler);

        // 如果没有异步支付, 直接进行订单完成处理
        if (!payOrder.isAsyncPay()) {
            // 修改支付订单状态为成功
            payOrder.setStatus(SUCCESS.getCode())
                    .setPayTime(LocalDateTime.now());
            payOrderService.updateById(payOrder);
        }
        // 如果异步支付完成, 进行订单完成处理, 同时发送回调消息
        PayLocal asyncPayInfo = PaymentContextLocal.get().getPayInfo();
        if (asyncPayInfo.isPayComplete()) {
            payOrder.setGatewayOrderNo(asyncPayInfo.getGatewayOrderNo())
                    .setStatus(SUCCESS.getCode())
                    .setPayTime(LocalDateTime.now());
            payOrderService.updateById(payOrder);
        }

        // 如果支付完成 发送通知
        if (Objects.equals(payOrder.getStatus(), SUCCESS.getCode())){
            clientNoticeService.registerPayNotice(payOrder, payInfo.getPayOrderExtra(), payInfo.getPayChannelOrders());
        }
        // 组装返回参数
        return PayBuilder.buildPayResultByPayOrder(payOrder);
    }


    /**
     * 执行所有的支付方法
     */
    private PayResult allPay(PayParam payParam, PayOrder payOrder) {
        PayLocal payInfo = PaymentContextLocal.get().getPayInfo();

        // 获取支付方式，通过工厂生成对应的策略组
        List<AbsPayStrategy> strategies = PayStrategyFactory.createAsyncLast(payParam.getPayChannels());
        if (CollectionUtil.isEmpty(strategies)) {
            throw new PayUnsupportedMethodException();
        }
        // 查询通道支付订单
        List<PayChannelOrder> orders = channelOrderManager.findAllByPaymentId(payOrder.getId());
        Map<String, PayChannelOrder> channelOrderMap = orders.stream()
                .collect(Collectors.toMap(PayChannelOrder::getChannel, Function.identity(), CollectorsFunction::retainFirst));

        // 初始化支付参数
        for (AbsPayStrategy strategy : strategies) {
            strategy.initPayParam(payOrder, payParam);
            strategy.setChannelOrder(channelOrderMap.get(strategy.getChannel().getCode()));
        }

        // 执行支付前处理动作
        strategies.forEach(AbsPayStrategy::doBeforePayHandler);
        // 支付操作

        try {
            strategies.forEach(AbsPayStrategy::doPayHandler);
        } catch (Exception e) {
            // 记录错误原因
            PayOrderExtra payOrderExtra = payInfo.getPayOrderExtra();
            payOrderExtra.setErrorMsg(e.getMessage());
            throw e;
        }

        // 支付调用成功操作, 进行扣款、创建记录类类似的操作
        strategies.forEach(AbsPayStrategy::doSuccessHandler);

        // 如果没有异步支付, 直接进行订单完成处理
        if (PayUtil.isNotSync(payParam.getPayChannels())) {
            // 修改支付订单状态为成功
            payOrder.setStatus(SUCCESS.getCode())
                    .setPayTime(LocalDateTime.now());
            payOrderService.updateById(payOrder);
        }
        // 如果异步支付完成, 进行订单完成处理, 同时发送回调消息
        PayLocal asyncPayInfo = PaymentContextLocal.get().getPayInfo();
        if (asyncPayInfo.isPayComplete()) {
            payOrder.setGatewayOrderNo(asyncPayInfo.getGatewayOrderNo())
                    .setStatus(SUCCESS.getCode())
                    .setPayTime(LocalDateTime.now());
            payOrderService.updateById(payOrder);
        }

        // 如果支付完成 发送通知
        if (Objects.equals(payOrder.getStatus(), SUCCESS.getCode())){
            clientNoticeService.registerPayNotice(payOrder, payInfo.getPayOrderExtra(), payInfo.getPayChannelOrders());
        }
        return PayBuilder.buildPayResultByPayOrder(payOrder);
    }

    /**
     * 异步支付执行(非第一次请求), 只执行异步支付策略, 因为同步支付已经支付成功. 报错不影响继续发起支付
     */
    private PayResult onlyAsyncPay(PayParam payParam, PayOrder payOrder) {

        // 2.获取 异步支付通道，通过工厂生成对应的策略组(只包含异步支付的策略, 同步支付已经成功不再继续执行)
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

        // 6.1 如果异步支付完成, 进行订单完成处理
        PayLocal asyncPayInfo = PaymentContextLocal.get().getPayInfo();
        if (asyncPayInfo.isPayComplete()) {
            payOrder.setGatewayOrderNo(asyncPayInfo.getGatewayOrderNo())
                    .setStatus(SUCCESS.getCode())
                    .setPayTime(LocalDateTime.now());
        }

        // 6.2 更新支付订单和扩展参数
        payOrderService.updateById(payOrder);
        PayOrderExtra payOrderExtra = payAssistService.updatePayOrderExtra(payParam, payOrder.getId());

        // 订单完成, 触发通知
        if (Objects.equals(payOrder.getStatus(), SUCCESS.getCode())) {
            clientNoticeService.registerPayNotice(payOrder, payOrderExtra,null);
        }

        // 7. 组装返回参数
        return PayBuilder.buildPayResultByPayOrder(payOrder);
    }
}
