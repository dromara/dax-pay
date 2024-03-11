package cn.bootx.platform.daxpay.service.core.payment.refund.service;

import cn.bootx.platform.common.core.annotation.CountTime;
import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.exception.RepetitiveOperationException;
import cn.bootx.platform.common.core.function.CollectorsFunction;
import cn.bootx.platform.common.core.util.ValidationUtil;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.code.RefundStatusEnum;
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
import cn.bootx.platform.daxpay.service.core.order.refund.dao.RefundChannelOrderManager;
import cn.bootx.platform.daxpay.service.core.order.refund.dao.RefundOrderManager;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.RefundChannelOrder;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.RefundOrder;
import cn.bootx.platform.daxpay.service.core.payment.notice.service.ClientNoticeService;
import cn.bootx.platform.daxpay.service.core.payment.refund.factory.RefundStrategyFactory;
import cn.bootx.platform.daxpay.service.func.AbsRefundStrategy;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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
public class RefundService {

    private final RefundAssistService refundAssistService;;

    private final PayOrderService payOrderService;

    private final ClientNoticeService clientNoticeService;

    private final PayChannelOrderManager payChannelOrderManager;

    private final RefundOrderManager refundOrderManager;

    private final RefundChannelOrderManager refundChannelOrderManager;

    private final LockTemplate lockTemplate;

    /**
     * 支付退款
     */
    @CountTime
    @Transactional(rollbackFor = Exception.class )
    public RefundResult refund(RefundParam param){
        return this.refundAdapter(param,false);
    }

    /**
     * 简单退款
     */
    @CountTime
    @Transactional(rollbackFor = Exception.class )
    public RefundResult simpleRefund(SimpleRefundParam param){
        ValidationUtil.validateParam(param);
        // 构建退款参数
        RefundParam refundParam = new RefundParam();
        BeanUtil.copyProperties(param,refundParam);
        if (!param.isRefundAll()){
            RefundChannelParam channelParam = new RefundChannelParam().setAmount(param.getAmount());
            refundParam.setRefundChannels(Collections.singletonList(channelParam));
        }
        return this.refundAdapter(refundParam,true);
    }

    /**
     * 支付退款适配方法, 处理简单退款与普通退款, 全部退款和部分退款的参数转换
     * @param param 退款参数
     * @param simple 是否简单退款
     */
    private RefundResult refundAdapter(RefundParam param, boolean simple){
        // 获取支付订单
        PayOrder payOrder = refundAssistService.getPayOrder(param);
        // 第一次检查退款参数, 校验一些特殊情况
        refundAssistService.checkAndDisposeParam(param, payOrder);

        // 组装退款参数, 处理全部退款和简单退款情况
        List<PayChannelOrder> payChannelOrders = payChannelOrderManager.findAllByPaymentId(payOrder.getId());
        // 是否全部退款
        if (param.isRefundAll()){
            // 全部退款根据支付订单的退款信息构造退款参数
            List<RefundChannelParam> channelParams = payChannelOrders
                    .stream()
                    .map(o -> new RefundChannelParam()
                            .setChannel(o.getChannel())
                            .setAmount(o.getRefundableBalance()))
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
        LockInfo lock = lockTemplate.lock("payment:refund:" + payOrder.getId(),10000,200);
        if (Objects.isNull(lock)){
            throw new RepetitiveOperationException("退款处理中，请勿重复操作");
        }
        try {
            // 退款上下文初始化
            refundAssistService.initRefundContext(param);
            // 分支付通道进行退款
            return this.refundByChannel(param,payOrder,payChannelOrders);
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /**
     * 分支付通道进行退款
     * 1. 创建退款订单和通道订单并保存(单独事务)
     * 2. 调用API发起退款(异步退款)
     * 3. 根据API返回信息更新退款订单信息
     */
    public RefundResult refundByChannel(RefundParam refundParam, PayOrder payOrder, List<PayChannelOrder> payChannelOrders){
        // 1.1 基础数据准备
        Map<String, PayChannelOrder> orderChannelMap = payChannelOrders.stream()
                .collect(Collectors.toMap(PayChannelOrder::getChannel, Function.identity(), CollectorsFunction::retainLatest));
        List<RefundChannelParam> refundChannels = refundParam.getRefundChannels();

        // 1.2获取退款参数方式，通过工厂生成对应的策略组
        List<AbsRefundStrategy> payRefundStrategies = RefundStrategyFactory.createAsyncLast(refundChannels);
        if (CollectionUtil.isEmpty(payRefundStrategies)) {
            throw new PayUnsupportedMethodException();
        }
        // 1.3初始化退款策略的参数
        for (AbsRefundStrategy refundStrategy : payRefundStrategies) {
            PayChannelOrder payChannelOrder = orderChannelMap.get(refundStrategy.getChannel().getCode());
            if (Objects.isNull(payChannelOrder)){
                throw new PayFailureException("[数据异常]进行退款的通道没有对应的支付单, 无法退款");
            }
            refundStrategy.initRefundParam(payOrder, payChannelOrder);
        }

        // 2.1 退款前校验操作
        payRefundStrategies.forEach(AbsRefundStrategy::doBeforeCheckHandler);

        // 2.2 生成通道退款订单对象
        payRefundStrategies.forEach(AbsRefundStrategy::generateChannelOrder);

        // 2.3 退款操作的预处理, 使用独立的新事物进行发起, 返回创建成功的退款订单, 成功后才可以进行下一阶段的操作
        RefundOrder refundOrder = SpringUtil.getBean(this.getClass()).preRefundMethod(refundParam, payOrder, payRefundStrategies);

        // 2.4 设置退款订单对象
        payRefundStrategies.forEach(r->r.setRefundOrder(refundOrder));

        try {
            // 3.1 退款前准备操作
            payRefundStrategies.forEach(AbsRefundStrategy::doBeforeRefundHandler);
            // 3.2 执行退款策略
            payRefundStrategies.forEach(AbsRefundStrategy::doRefundHandler);
            // 3.3 执行退款发起成功后操作
            payRefundStrategies.forEach(AbsRefundStrategy::doSuccessHandler);

            // 4.进行成功处理, 分别处理退款订单, 通道退款订单, 支付订单
            List<RefundChannelOrder> refundChannelOrders = payRefundStrategies.stream()
                    .map(AbsRefundStrategy::getRefundChannelOrder)
                    .collect(Collectors.toList());
            this.successHandler(refundOrder, refundChannelOrders, payOrder);
            return new RefundResult()
                    .setRefundId(refundOrder.getId())
                    .setRefundNo(refundParam.getRefundNo());
        }
        catch (Exception e) {
            // 5. 失败处理, 所有记录都会回滚, 可以调用重新
            PaymentContextLocal.get().getRefundInfo().setStatus(RefundStatusEnum.FAIL);
            this.errorHandler(refundOrder);
            throw e;
        }
    }

    /**
     * 退款一阶段: 进行支付订单和支付通道订单的预扣, 预创建退款订单并保存, 使用独立的新事物进行发起
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class )
    public RefundOrder preRefundMethod(RefundParam refundParam, PayOrder payOrder, List<AbsRefundStrategy> payRefundStrategies) {
        // --------------------------- 支付订单 -------------------------------------
        // 预扣支付相关订单要退款的金额并进行更新
        payRefundStrategies.forEach(AbsRefundStrategy::doPreDeductOrderHandler);
        List<PayChannelOrder> channelOrders = payRefundStrategies.stream()
                .map(AbsRefundStrategy::getPayChannelOrder)
                .collect(Collectors.toList());
        payChannelOrderManager.updateAllById(channelOrders);
        // 此次的总退款金额
        Integer amount = refundParam.getRefundChannels()
                .stream()
                .map(RefundChannelParam::getAmount)
                .reduce(0, Integer::sum);
        int orderRefundableBalance = payOrder.getRefundableBalance() - amount;
        payOrder.setRefundableBalance(orderRefundableBalance)
                .setStatus(PayStatusEnum.REFUNDING.getCode());
        payOrderService.updateById(payOrder);
        // -----------------------   退款订单   -------------------------
        List<RefundChannelOrder> refundChannelOrders = payRefundStrategies.stream()
                .map(AbsRefundStrategy::getRefundChannelOrder)
                .collect(Collectors.toList());
        return refundAssistService.createOrderAndChannel(refundParam, payOrder,refundChannelOrders);
    }

    /**
     * 重新发退款处理
     * 1. 查出相关退款订单
     * 2. 构建退款策略, 发起退款
     */
    public void resetRefund(Long id){
        // 查询
        RefundOrder refundOrder = refundOrderManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("未查找到退款订单"));

        // 退款失败才可以重新发起退款, 重新发起退款
        if (!Objects.equals(refundOrder.getStatus(), RefundStatusEnum.FAIL.getCode())){
            throw new PayFailureException("退款状态不正确, 无法重新发起退款");
        }

        // 构建策略
        List<RefundChannelOrder> refundChannels = refundChannelOrderManager
                .findAllByRefundId(refundOrder.getId());
        PayOrder payOrder = payOrderService.findById(refundOrder.getPaymentId())
                .orElseThrow(() -> new DataNotExistException("未查找到支付订单"));
        List<PayChannelOrder> payChannelOrders = payChannelOrderManager.findAllByPaymentId(payOrder.getId());
        Map<String, PayChannelOrder> orderChannelMap = payChannelOrders.stream()
                .collect(Collectors.toMap(PayChannelOrder::getChannel, Function.identity(), CollectorsFunction::retainLatest));

        List<AbsRefundStrategy> strategies = RefundStrategyFactory.createAsyncFrontByOrder(refundChannels);
        for (AbsRefundStrategy strategy : strategies) {
            strategy.initRefundParam(payOrder, orderChannelMap.get(strategy.getChannel().getCode()));
        }

        // 设置退款订单对象
        strategies.forEach(r->r.setRefundOrder(refundOrder));

        try {
            // 3.1 退款前准备操作
            strategies.forEach(AbsRefundStrategy::doBeforeRefundHandler);
            // 3.2 执行退款策略
            strategies.forEach(AbsRefundStrategy::doRefundHandler);
            // 3.3 执行退款发起成功后操作
            strategies.forEach(AbsRefundStrategy::doSuccessHandler);

            // 4.进行成功处理, 分别处理退款订单, 通道退款订单, 支付订单
            List<RefundChannelOrder> refundChannelOrders = strategies.stream()
                    .map(AbsRefundStrategy::getRefundChannelOrder)
                    .collect(Collectors.toList());
            this.successHandler(refundOrder, refundChannelOrders, payOrder);
        }
        catch (Exception e) {
            // 5. 失败处理, 所有记录都会回滚, 可以调用退款重试
            PaymentContextLocal.get().getRefundInfo().setStatus(RefundStatusEnum.FAIL);
            this.errorHandler(refundOrder);
            throw e;
        }
    }

    /**
     * 成功处理, 更新退款订单, 退款通道订单, 支付订单, 支付通道订单
     */
    private void successHandler(RefundOrder refundOrder, List<RefundChannelOrder> refundChannelOrders, PayOrder payOrder) {
        RefundLocal refundInfo = PaymentContextLocal.get().getRefundInfo();
        // 剩余可退款余额
        int refundableBalance = refundOrder.getRefundableBalance();
        // 设置支付订单状态
        if (refundInfo.getStatus() == RefundStatusEnum.PROGRESS) {
            payOrder.setStatus(PayStatusEnum.REFUNDING.getCode());
        } else if (refundableBalance == 0) {
            payOrder.setStatus(PayStatusEnum.REFUNDED.getCode());
        } else {
            payOrder.setStatus(PayStatusEnum.PARTIAL_REFUND.getCode());
        }
        payOrderService.updateById(payOrder);

        // 更新退款订单和相关通道订单
        refundAssistService.updateOrderAndChannel(refundOrder,refundChannelOrders);

        // 发送通知
        List<String> list = Arrays.asList(RefundStatusEnum.SUCCESS.getCode(), RefundStatusEnum.CLOSE.getCode(),  RefundStatusEnum.FAIL.getCode());
        if (list.contains(refundOrder.getStatus())){
            clientNoticeService.registerRefundNotice(refundOrder, refundInfo.getRunOrderExtra(), refundChannelOrders);
        }
    }

    /**
     * 失败处理, 只更新退款订单, 通道订单不进行错误更新
     */
    private void errorHandler(RefundOrder refundOrder) {
        // 记录退款失败的记录
        refundAssistService.updateOrderByError(refundOrder);
    }
}
