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
        payAssistService.validationLimitAmount(payParam.getAmount());
        // 校验超时时间, 不可早于当前
        payAssistService.validationExpiredTime(payParam.getExpiredTime());
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
            PayOrder payOrder = payAssistService.getOrderAndCheck(payParam.getBizOrderNo(), payParam.getAppId());
            // 调用支付流程
            return this.payHandle(payParam,payOrder);
        } catch (Exception e) {
            log.error("支付异常",e);
            throw e;
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /**
     * 支付操作 无事务
     * 拆分为多阶段，1. 保存订单记录信息 2. 调起支付 3. 支付成功后操作
     */
    public PayResult payHandle(PayParam payParam, PayOrder payOrder) {
        // 获取支付策略类
        var payStrategy = PaymentStrategyFactory.create(payParam.getChannel(), AbsPayStrategy.class);
        // 初始化支付的参数
        payStrategy.setPayParam(payParam);
        // 执行支付前处理动作, 进行各种校验, 校验通过才会进行下面的操作
        payStrategy.doBeforePayHandler();
        // 订单不存在执行支付前的保存动作, 保存支付订单和扩展记录, 默认状态为支付中
        if (Objects.isNull(payOrder)){
            payOrder = payAssistService.createPayOrder(payParam);
        }
        // 订单待支付状态, 设置支付方式和对应状态
        if (Objects.equals(payOrder.getStatus(), PayStatusEnum.WAIT.getCode())){
            payAssistService.updatePayOrder(payParam,payOrder);
        }
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
        return SpringUtil.getBean(this.getClass()).paySuccess(payOrder, result);
    }

    /**
     * 支付成功后操作, 更新订单信息
     */
    @Transactional(rollbackFor = Exception.class)
    public PayResult paySuccess(PayOrder payOrder, PayResultBo result){
        // 如果支付完成, 进行订单完成处理, 同时发送回调消息
        if (result.isComplete()) {
            payOrder.setStatus(PayStatusEnum.SUCCESS.getCode())
                    .setPayTime(result.getFinishTime());
        }
        payOrder.setOutOrderNo(result.getOutOrderNo());
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
}
