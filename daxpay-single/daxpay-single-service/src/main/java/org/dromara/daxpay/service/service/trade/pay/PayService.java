package org.dromara.daxpay.service.service.trade.pay;

import org.dromara.daxpay.core.enums.PayStatusEnum;
import org.dromara.daxpay.core.exception.PayFailureException;
import org.dromara.daxpay.core.exception.TradeProcessingException;
import org.dromara.daxpay.core.param.trade.pay.PayParam;
import org.dromara.daxpay.core.result.trade.pay.PayResult;
import org.dromara.daxpay.service.bo.trade.PayResultBo;
import org.dromara.daxpay.service.dao.order.pay.PayOrderManager;
import org.dromara.daxpay.service.entity.order.pay.PayOrder;
import org.dromara.daxpay.service.service.allocation.AllocationService;
import org.dromara.daxpay.service.service.notice.MerchantNoticeService;
import org.dromara.daxpay.service.service.record.flow.TradeFlowRecordService;
import org.dromara.daxpay.service.strategy.AbsPayStrategy;
import org.dromara.daxpay.service.util.PaymentStrategyFactory;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * 支付服务类
 * @author xxm
 * @since 2024/6/27
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayService {

    private final PayAssistService payAssistService;

    private final LockTemplate lockTemplate;
    private final PayOrderManager payOrderManager;
    private final TradeFlowRecordService tradeFlowRecordService;
    private final MerchantNoticeService merchantNoticeService;
    private final AllocationService allocationService;

    /**
     * 支付入口
     */
    public PayResult pay(PayParam payParam){
        // 校验支付限额
        payAssistService.validationLimitAmount(payParam);
        // 校验超时时间, 不可早于当前
        payAssistService.validationExpiredTime(payParam);
        // 获取商户订单号
        String bizOrderNo = payParam.getBizOrderNo();
        // 加锁
        LockInfo lock = lockTemplate.lock("payment:pay:" + bizOrderNo,10000,200);
        if (Objects.isNull(lock)){
            log.warn("正在支付中，请勿重复支付");
            throw new TradeProcessingException("正在支付中，请勿重复支付");
        }
        try {
            // 查询并检查订单
            PayOrder payOrder = payAssistService.getOrderAndCheck(payParam);
            // 走首次下单逻辑还是重复下档逻辑
            if (Objects.isNull(payOrder)){
                return this.firstPay(payParam);
            } else {
                return this.repeatPay(payParam,payOrder);
            }
        } catch (Exception e) {
            log.error("支付异常",e);
            throw e;
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /**
     * 支付入口, 内部调用时使用
     */
    public PayResult pay(PayParam payParam, PayOrder payOrder){
        // 获取商户订单号
        String bizOrderNo = payOrder.getBizOrderNo();
        // 加锁
        LockInfo lock = lockTemplate.lock("payment:pay:" + bizOrderNo,10000,200);
        if (Objects.isNull(lock)){
            log.warn("正在支付中，请勿重复支付");
            throw new TradeProcessingException("正在支付中，请勿重复支付");
        }
        try {
            return this.repeatPay(payParam,payOrder);
        } catch (Exception e) {
            log.error("支付异常",e);
            throw e;
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /**
     * 首次支付 无事务
     * 拆分为多阶段，1. 保存订单记录信息 2 调起支付 3. 支付成功后操作
     */
    public PayResult firstPay(PayParam payParam) {
        // 获取支付策略类
        AbsPayStrategy payStrategy = PaymentStrategyFactory.create(payParam.getChannel(), AbsPayStrategy.class);
        // 初始化支付的参数
        payStrategy.setPayParam(payParam);
        // 执行支付前处理动作, 进行各种校验, 校验通过才会进行下面的操作
        payStrategy.doBeforePayHandler();
        // 执行支付前的保存动作, 保存支付订单和扩展记录
        PayOrder payOrder = payAssistService.createPayOrder(payParam);
        payStrategy.setOrder(payOrder);
        PayResultBo result;
        try {
            // 支付操作
            result = payStrategy.doPayHandler();
        } catch (Exception e) {
            if (e instanceof PayFailureException) {
                payOrder.setErrorMsg(e.getMessage());
            } else {
                payOrder.setErrorCode("支付出现异常");
            }
            // 这个方法没有事务, 所以可以正常更新
            payOrderManager.updateById(payOrder);
            throw e;
        }
        // 支付调起成功后操作, 使用事务来保证数据一致性
        return SpringUtil.getBean(this.getClass()).firstPaySuccess(payOrder, result);
    }

    /**
     * 首次成功后操作, 更新订单信息
     */
    @Transactional(rollbackFor = Exception.class)
    public PayResult firstPaySuccess(PayOrder payOrder, PayResultBo result){
        // 如果支付完成, 进行订单完成处理, 同时发送回调消息
        if (result.isComplete()) {
            payOrder.setOutOrderNo(result.getOutOrderNo())
                    .setStatus(PayStatusEnum.SUCCESS.getCode())
                    .setPayTime(result.getFinishTime());
        }
        payOrder.setErrorCode(null);
        payOrder.setErrorMsg(null);
        payOrderManager.updateById(payOrder);
        // 如果支付完成 发送通知, 记录流水
        if (Objects.equals(payOrder.getStatus(), PayStatusEnum.SUCCESS.getCode())){
            tradeFlowRecordService.savePay(payOrder);
            merchantNoticeService.registerPayNotice(payOrder);
            allocationService.registerAutoAlloc(payOrder);
        }
        return payAssistService.buildResult(payOrder,result);
    }

    /**
     * 重复支付
     */
    public PayResult repeatPay(PayParam payParam, PayOrder payOrder) {
        // 获取支付策略类
        AbsPayStrategy payStrategy = PaymentStrategyFactory.create(payParam.getChannel(), AbsPayStrategy.class);
        // 初始化支付的参数
        payStrategy.initPayParam(payOrder, payParam);
        // 执行支付前处理动作
        payStrategy.doBeforePayHandler();
        // 执行支付前的更新动作, 更新并保存订单数据
        payAssistService.updatePayOrder(payParam, payOrder);

        PayResultBo payResultBo;
        try {
            // 支付操作
            payResultBo = payStrategy.doPayHandler();
        } catch (Exception e) {
            // 记录错误原因, 此处没有事务, 所以可以正常更新
            payOrder.setErrorMsg(e.getMessage());
            payOrderManager.updateById(payOrder);
            throw e;
        }
        // 支付调起成功后操作, 使用事务来保证数据一致性
        return SpringUtil.getBean(this.getClass())
                .repeatPaySuccess(payOrder, payResultBo);
    }

    /**
     * 重复支付成功后操作
     */
    @Transactional(rollbackFor = Exception.class)
    public PayResult repeatPaySuccess(PayOrder payOrder, PayResultBo payResultBo) {
        // 如果支付完成, 进行订单完成处理, 同时发送回调消息
        if (payResultBo.isComplete()) {
            payOrder.setOutOrderNo(payResultBo.getOutOrderNo())
                    .setStatus(PayStatusEnum.SUCCESS.getCode())
                    .setPayTime(payResultBo.getFinishTime());
        }
        // 扩展记录更新
        payOrder.setErrorMsg(null);
        payOrder.setErrorCode(null);
        payOrderManager.updateById(payOrder);
        // 如果支付完成 发送通知, 记录流水
        if (Objects.equals(payOrder.getStatus(), PayStatusEnum.SUCCESS.getCode())){
            tradeFlowRecordService.savePay(payOrder);
            merchantNoticeService.registerPayNotice(payOrder);
            allocationService.registerAutoAlloc(payOrder);
        }
        return payAssistService.buildResult(payOrder, payResultBo);
    }
}
