package cn.daxpay.multi.service.service.payment.pay;

import cn.daxpay.multi.core.enums.PayStatusEnum;
import cn.daxpay.multi.core.exception.PayFailureException;
import cn.daxpay.multi.core.exception.TradeProcessingException;
import cn.daxpay.multi.core.param.payment.pay.PayParam;
import cn.daxpay.multi.core.result.PayResult;
import cn.daxpay.multi.service.common.context.PayLocal;
import cn.daxpay.multi.service.common.local.PaymentContextLocal;
import cn.daxpay.multi.service.entity.order.pay.PayOrder;
import cn.daxpay.multi.service.service.order.pay.PayOrderService;
import cn.daxpay.multi.service.strategy.AbsPayStrategy;
import cn.daxpay.multi.service.util.PayStrategyFactory;
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
    private final PayOrderService payOrderService;

    /**
     * 支付入口
     */
    public PayResult pay(PayParam payParam){
        // 创建返回类
        // 校验支付限额
        payAssistService.validationLimitAmount(payParam);
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
            PayOrder payOrder = payAssistService.getOrderAndCheck(payParam.getBizOrderNo());
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
     * 首次支付 无事务
     * 拆分为多阶段，1. 保存订单记录信息 2 调起支付 3. 支付成功后操作
     */
    public PayResult firstPay(PayParam payParam){
        // 获取支付策略类
        AbsPayStrategy payStrategy = PayStrategyFactory.create(payParam.getChannel(), AbsPayStrategy.class);
        // 初始化支付的参数
        payStrategy.setPayParam(payParam);
        // 执行支付前处理动作, 进行各种校验, 校验通过才会进行下面的操作
        payStrategy.doBeforePayHandler();
        // 执行支付前的保存动作, 保存支付订单和扩展记录
        PayOrder payOrder = payAssistService.createPayOrder(payParam);
        payStrategy.setOrder(payOrder);
        try {
            // 支付操作
            payStrategy.doPayHandler();
        } catch (Exception e) {
            if (e instanceof PayFailureException){
                payOrder.setErrorMsg(e.getMessage());
            } else {
                payOrder.setErrorCode("支付出现异常");
            }
            // 这个方法没有事务, 所以可以正常更新
            payOrderService.updateById(payOrder);
            throw e;
        }
        // 支付调起成功后操作, 使用事务来保证数据一致性
        return SpringUtil.getBean(this.getClass()).firstPaySuccess(payOrder);
    }

    /**
     * 首次成功后操作, 更新订单信息
     */
    @Transactional(rollbackFor = Exception.class)
    public PayResult firstPaySuccess(PayOrder payOrder){
        PayLocal payInfo = PaymentContextLocal.get().getPayInfo();
        // 如果支付完成, 进行订单完成处理, 同时发送回调消息
        if (payInfo.isComplete()) {
            payOrder.setOutOrderNo(payInfo.getOutOrderNo())
                    .setStatus(PayStatusEnum.SUCCESS.getCode())
                    .setPayTime(payInfo.getCompleteTime());
        }
        payOrderService.updateById(payOrder);
        // 扩展记录更新
        payOrder.setErrorCode(null);
        payOrder.setErrorMsg(null);
        payOrderService.updateById(payOrder);
        // 如果支付完成 发送通知, 记录流水
        if (Objects.equals(payOrder.getStatus(), PayStatusEnum.SUCCESS.getCode())){
//            tradeFlowRecordService.savePay(payOrder);
//            clientNoticeService.registerPayNotice(payOrder);
        }
        return payAssistService.buildResult(payOrder);
    }

    /**
     * 重复支付
     */
    public PayResult repeatPay(PayParam payParam, PayOrder payOrder){
        // 获取支付策略类
        AbsPayStrategy payStrategy = PayStrategyFactory.create(payParam.getChannel(),AbsPayStrategy.class);
        // 初始化支付的参数
        payStrategy.initPayParam(payOrder, payParam);
        // 执行支付前处理动作
        payStrategy.doBeforePayHandler();
        // 执行支付前的更新动作, 更新并保存订单数据
        payAssistService.updatePayOrder(payParam, payOrder);

        try {
            // 支付操作
            payStrategy.doPayHandler();
        } catch (Exception e) {
            // 记录错误原因, 此处没有事务, 所以可以正常更新
            payOrder.setErrorMsg(e.getMessage());
            payOrderService.updateById(payOrder);
            throw e;
        }
        // 支付调起成功后操作, 使用事务来保证数据一致性
        return SpringUtil.getBean(this.getClass()).repeatPaySuccess(payOrder);
    }

    /**
     * 重复支付成功后操作
     */
    @Transactional(rollbackFor = Exception.class)
    public PayResult repeatPaySuccess(PayOrder payOrder) {
        PayLocal payInfo = PaymentContextLocal.get().getPayInfo();
        // 如果支付完成, 进行订单完成处理, 同时发送回调消息
        if (payInfo.isComplete()) {
            payOrder.setOutOrderNo(payInfo.getOutOrderNo())
                    .setStatus(PayStatusEnum.SUCCESS.getCode())
                    .setPayTime(payInfo.getCompleteTime());
        }
        payOrderService.updateById(payOrder);
        // 扩展记录更新
        payOrder.setErrorMsg(null);
        payOrder.setErrorCode(null);
        payOrderService.updateById(payOrder);
        // 如果支付完成 发送通知, 记录流水
        if (Objects.equals(payOrder.getStatus(), PayStatusEnum.SUCCESS.getCode())){
//            tradeFlowRecordService.savePay(payOrder);
//            clientNoticeService.registerPayNotice(payOrder);
        }
        return payAssistService.buildResult(payOrder);
    }
}
