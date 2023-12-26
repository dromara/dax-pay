package cn.bootx.platform.daxpay.core.payment.refund.service;

import cn.bootx.platform.common.core.util.ValidationUtil;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.core.order.pay.dao.PayOrderManager;
import cn.bootx.platform.daxpay.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.core.payment.refund.factory.PayRefundStrategyFactory;
import cn.bootx.platform.daxpay.core.payment.refund.func.AbsPayRefundStrategy;
import cn.bootx.platform.daxpay.core.payment.refund.func.PayRefundStrategyConsumer;
import cn.bootx.platform.daxpay.exception.pay.PayUnsupportedMethodException;
import cn.bootx.platform.daxpay.param.pay.RefundChannelParam;
import cn.bootx.platform.daxpay.param.pay.RefundParam;
import cn.bootx.platform.daxpay.param.pay.SimpleRefundParam;
import cn.bootx.platform.daxpay.result.pay.RefundResult;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 支付退款服务
 * @author xxm
 * @since 2023/12/18
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayRefundService {

    private final PayRefundAssistService payRefundAssistService;;

    private final PayOrderManager payOrderManager;
    /**
     * 支付退款
     */
    @Transactional(rollbackFor = Exception.class )
    public RefundResult refund(RefundParam param){
        return this.refund(param,false);
    }

    /**
     * 简单退款
     */
    @Transactional(rollbackFor = Exception.class )
    public RefundResult simpleRefund(SimpleRefundParam param){
        ValidationUtil.validateParam(param);
        // 构建退款参数
        RefundParam refundParam = new RefundParam();
        BeanUtil.copyProperties(param,refundParam);
        RefundChannelParam channelParam = new RefundChannelParam()
                .setAmount(param.getAmount())
                .setChannel(param.getPayChannel())
                .setChannelExtra(param.getChannelExtra());
        refundParam.setRefundChannels(Collections.singletonList(channelParam));
        return this.refund(refundParam,true);
    }

    /**
     * 支付退款方法
     * @param param 退款参数
     * @param simple 是否简单退款
     */
    private RefundResult refund(RefundParam param, boolean simple){
        // 参数校验
        ValidationUtil.validateParam(param);
        PayOrder payOrder = payRefundAssistService.getPayOrderAndCheckByRefundParam(param, simple);
        // 退款上下文初始化
        payRefundAssistService.initRefundContext(param,payOrder);
        // 是否全部退款
        if (param.isRefundAll()){
            // 全部退款根据支付订单的退款信息构造退款参数
            List<RefundChannelParam> channelParams = payOrder.getRefundableInfos()
                    .stream()
                    .map(o -> new RefundChannelParam().setChannel(o.getChannel())
                            .setAmount(o.getAmount()))
                    .collect(Collectors.toList());
            param.setRefundChannels(channelParams);
        }
        return this.refundByChannel(param,payOrder);
    }

    /**
     * 分支付通道进行退款
     */
    public RefundResult refundByChannel(RefundParam param,PayOrder order){
        List<RefundChannelParam> refundChannels = param.getRefundChannels();
        // 1.获取退款参数方式，通过工厂生成对应的策略组
        List<AbsPayRefundStrategy> payRefundStrategies = PayRefundStrategyFactory.create(refundChannels);
        if (CollectionUtil.isEmpty(payRefundStrategies)) {
            throw new PayUnsupportedMethodException();
        }

        // 2.初始化支付的参数
        for (AbsPayRefundStrategy refundStrategy : payRefundStrategies) {
            refundStrategy.initPayParam(order, param);
        }

        // 3.支付前准备
        this.doHandler(refundChannels, order, payRefundStrategies, AbsPayRefundStrategy::doBeforeRefundHandler, null);

        // 4.执行退款
        this.doHandler(refundChannels,order, payRefundStrategies, AbsPayRefundStrategy::doRefundHandler, (strategyList, payOrder) -> {
            this.paymentHandler(payOrder, refundChannels);
        });
        return new RefundResult();
    }

    /**
     * 处理方法
     * @param channelParams 退款方式参数
     * @param payOrder 支付记录
     * @param strategyList 退款策略
     * @param refundStrategy 执行方法
     * @param successCallback 成功操作
     */
    private void doHandler( List<RefundChannelParam> channelParams,
                            PayOrder payOrder,
                            List<AbsPayRefundStrategy> strategyList,
                            Consumer<AbsPayRefundStrategy> refundStrategy,
                            PayRefundStrategyConsumer<List<AbsPayRefundStrategy>, PayOrder> successCallback) {
        try {
            // 执行策略操作，如退款前/退款时
            // 等同strategyList.forEach(payMethod.accept(PaymentStrategy))
            strategyList.forEach(refundStrategy);

            // 执行操作成功的处
            Optional.ofNullable(successCallback).ifPresent(fun -> fun.accept(strategyList, payOrder));
        }
        catch (Exception e) {
            // 记录退款失败的记录
            Integer i = channelParams.stream()
                    .map(RefundChannelParam::getAmount)
                    .reduce(0,Integer::sum);
            // TODO 保存
//            SpringUtil.getBean(this.getClass()).saveRefund(payOrder, amount, channelParams);
            throw e;
        }

    }

    /**
     * 支付订单处理
     */
    private void paymentHandler(PayOrder payOrder, List<RefundChannelParam> refundModeParams) {
        Integer amount = refundModeParams.stream()
                .map(RefundChannelParam::getAmount)
                .reduce(0, Integer::sum);
        // 剩余可退款余额
        int refundableBalance = payOrder.getRefundableBalance() - amount;

        // 退款完成
        if (refundableBalance == 0) {
            payOrder.setStatus(PayStatusEnum.REFUNDED.getCode());
        }
        else {
            payOrder.setStatus(PayStatusEnum.PARTIAL_REFUND.getCode());
        }
        payOrder.setRefundableBalance(refundableBalance);
        payOrderManager.updateById(payOrder);
        // TODO 记录退款成功的记录
//        SpringUtil.getBean(this.getClass()).saveRefund(payOrder, amount, refundModeParams);
    }
}
