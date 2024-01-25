package cn.bootx.platform.daxpay.service.core.payment.refund.service;

import cn.bootx.platform.common.core.exception.RepetitiveOperationException;
import cn.bootx.platform.common.core.function.CollectorsFunction;
import cn.bootx.platform.common.core.util.ValidationUtil;
import cn.bootx.platform.daxpay.code.PayRefundStatusEnum;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.exception.pay.PayUnsupportedMethodException;
import cn.bootx.platform.daxpay.param.pay.RefundChannelParam;
import cn.bootx.platform.daxpay.param.pay.RefundParam;
import cn.bootx.platform.daxpay.param.pay.SimpleRefundParam;
import cn.bootx.platform.daxpay.result.pay.RefundResult;
import cn.bootx.platform.daxpay.service.common.context.RefundLocal;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.order.pay.dao.PayChannelOrderManager;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayChannelOrder;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.service.core.order.pay.service.PayOrderService;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.PayRefundChannelOrder;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.PayRefundOrder;
import cn.bootx.platform.daxpay.service.core.payment.refund.factory.PayRefundStrategyFactory;
import cn.bootx.platform.daxpay.service.func.AbsRefundStrategy;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
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

    private final PayOrderService payOrderService;

    private final PayChannelOrderManager payChannelOrderManager;

    private final LockTemplate lockTemplate;

    /**
     * 支付退款
     */
    @Transactional(rollbackFor = Exception.class )
    public RefundResult refund(RefundParam param){
        return this.refundAdapter(param,false);
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
        RefundChannelParam channelParam = new RefundChannelParam().setAmount(param.getAmount());
        refundParam.setRefundChannels(Collections.singletonList(channelParam));
        return this.refundAdapter(refundParam,true);
    }

    /**
     * 支付退款适配方法, 处理简单退款与普通退款, 全部退款和部分退款的参数转换
     * @param param 退款参数
     * @param simple 是否简单退款
     */
    private RefundResult refundAdapter(RefundParam param, boolean simple){
        // 获取支付订单
        PayOrder payOrder = payRefundAssistService.getPayOrder(param);
        // 第一次检查退款参数, 校验一些特殊情况
        payRefundAssistService.checkByRefundParam(param, payOrder);

        // 组装退款参数, 处理全部退款和简单退款情况
        List<PayChannelOrder> payChannelOrders = payChannelOrderManager.findAllByPaymentId(payOrder.getId());
        // 是否全部退款
        if (param.isRefundAll()){
            // 全部退款根据支付订单的退款信息构造退款参数
            List<RefundChannelParam> channelParams = payChannelOrders
                    .stream()
                    .map(o -> new RefundChannelParam()
                            .setChannel(o.getChannel())
                            .setAmount(o.getAmount()))
                    .collect(Collectors.toList());
            param.setRefundChannels(channelParams);
        } else if (simple) {
            // 如果不是全部退款且是简单退款的情况下, 设置要退款金额
            String channel = payChannelOrders.get(0).getChannel();
            param.getRefundChannels().get(0).setChannel(channel);
        }

        // 参数校验
        ValidationUtil.validateParam(param);
        // ----------------------------- 发起退款操作 --------------------------------------------
        // 加锁
        LockInfo lock = lockTemplate.lock("payment:refund:" + payOrder.getId());
        if (Objects.isNull(lock)){
            throw new RepetitiveOperationException("退款处理中，请勿重复操作");
        }
        try {
            // 退款上下文初始化
            payRefundAssistService.initRefundContext(param);
            // 分支付通道进行退款
            return this.refundByChannel(param,payOrder,payChannelOrders);
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /**
     * 分支付通道进行退款
     */
    public RefundResult refundByChannel(RefundParam refundParam, PayOrder payOrder, List<PayChannelOrder> payChannelOrders){
        // 0.基础数据准备, 并比对通道支付单是否与可退款记录数量一致
        Map<String, PayChannelOrder> orderChannelMap = payChannelOrders.stream()
                .collect(Collectors.toMap(PayChannelOrder::getChannel, Function.identity(), CollectorsFunction::retainLatest));
        List<RefundChannelParam> refundChannels = refundParam.getRefundChannels();

        try {
            // 1.获取退款参数方式，通过工厂生成对应的策略组
            List<AbsRefundStrategy> payRefundStrategies = PayRefundStrategyFactory.createAsyncLast(refundChannels);
            if (CollectionUtil.isEmpty(payRefundStrategies)) {
                throw new PayUnsupportedMethodException();
            }

            // 2.初始化退款策略的参数
            for (AbsRefundStrategy refundStrategy : payRefundStrategies) {
                PayChannelOrder payChannelOrder = orderChannelMap.get(refundStrategy.getType().getCode());
                if (Objects.isNull(payChannelOrder)){
                    throw new PayFailureException("[数据异常]进行退款的通道没有对应的支付单, 无法退款");
                }
                refundStrategy.initRefundParam(payOrder, refundParam, payChannelOrder);
            }

            // 3.1 退款前准备操作
            payRefundStrategies.forEach(AbsRefundStrategy::doBeforeRefundHandler);
            // 3.2 生成各通道退款订单
            payRefundStrategies.forEach(AbsRefundStrategy::generateChannelOrder);
            // 3.3 执行退款策略
            payRefundStrategies.forEach(AbsRefundStrategy::doRefundHandler);
            // 3.4 执行退款发起成功后操作
            payRefundStrategies.forEach(AbsRefundStrategy::doSuccessHandler);

            // 4 更新各支付通道订单的信息
            List<PayChannelOrder> channelOrders = payRefundStrategies.stream()
                    .map(AbsRefundStrategy::getPayChannelOrder)
                    .collect(Collectors.toList());
            payChannelOrderManager.updateAllById(channelOrders);

            // 5 获取退款通道订单, 进行保存
            List<PayRefundChannelOrder> refundChannelOrders = payRefundStrategies.stream()
                    .map(AbsRefundStrategy::getRefundChannelOrder)
                    .collect(Collectors.toList());

            // 6.进行成功处理, 分别处理退款订单, 通道退款订单, 支付订单
            PayRefundOrder refundOrder = this.successHandler(refundParam, refundChannelOrders, payOrder);
            return new RefundResult()
                    .setRefundId(refundOrder.getId())
                    .setRefundNo(refundParam.getRefundNo());
        }
        catch (Exception e) {
            // 失败处理
            PaymentContextLocal.get().getRefundInfo().setStatus(PayRefundStatusEnum.FAIL);
            this.errorHandler(refundParam, payOrder);
            throw e;
        }
    }

    /**
     * 退款订单成功处理, 保存退款订单, 通道退款订单, 更新支付订单
     */
    private PayRefundOrder successHandler(RefundParam refundParam, List<PayRefundChannelOrder> refundChannelOrders, PayOrder payOrder) {
        RefundLocal asyncRefundInfo = PaymentContextLocal.get().getRefundInfo();
        // ----------------------- 支付订单处理  ---------------------------------------
        // 此次的总退款金额
        Integer amount = refundParam.getRefundChannels().stream()
                .map(RefundChannelParam::getAmount)
                .reduce(0, Integer::sum);
        // 剩余可退款余额
        int refundableBalance = payOrder.getRefundableBalance() - amount;
        payOrder.setRefundableBalance(refundableBalance);

        // 设置支付订单状态
        if (asyncRefundInfo.getStatus() == PayRefundStatusEnum.PROGRESS) {
            // 设置为退款中
            payOrder.setStatus(PayStatusEnum.REFUNDING.getCode());
        } else if (refundableBalance == 0) {
            // 退款完成
            payOrder.setStatus(PayStatusEnum.REFUNDED.getCode());
        } else {
            // 部分退款
            payOrder.setStatus(PayStatusEnum.PARTIAL_REFUND.getCode());
        }

        // ----------------------- 退款订单处理  ---------------------------------------
        // 生成退款订单
        PayRefundOrder refundOrder = payRefundAssistService.generateRefundOrder(refundParam, payOrder);
        // 更新或保存相关订单
        payRefundAssistService.saveOrderAndChannels(refundOrder,refundChannelOrders);
        payOrderService.updateById(payOrder);
        return refundOrder;
    }

    /**
     * 失败处理
     */
    private void errorHandler(RefundParam refundParam, PayOrder payOrder) {
        // 记录退款失败的记录
        PayRefundOrder refundOrder = payRefundAssistService.generateRefundOrder(refundParam, payOrder);
        payRefundAssistService.saveOrder(refundOrder);
    }
}
