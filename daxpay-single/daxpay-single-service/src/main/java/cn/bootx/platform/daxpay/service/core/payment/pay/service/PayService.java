package cn.bootx.platform.daxpay.service.core.payment.pay.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.daxpay.param.payment.pay.PayParam;
import cn.bootx.platform.daxpay.result.pay.PayResult;
import cn.bootx.platform.daxpay.service.common.context.PayLocal;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.order.pay.dao.PayOrderExtraManager;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrderExtra;
import cn.bootx.platform.daxpay.service.core.order.pay.service.PayOrderService;
import cn.bootx.platform.daxpay.service.core.payment.notice.service.ClientNoticeService;
import cn.bootx.platform.daxpay.service.core.payment.pay.factory.PayStrategyFactory;
import cn.bootx.platform.daxpay.service.func.AbsPayStrategy;
import cn.bootx.platform.daxpay.util.PayUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

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

    private final PayOrderExtraManager payOrderExtraManager;

    private final LockTemplate lockTemplate;

    /**
     * 支付入口
     */
    public PayResult pay(PayParam payParam){
        // 创建返回类
        PayResult payResult = new PayResult();
        // 支付参数检查
        PayUtil.validation(payParam);
        // 校验支付限额
        payAssistService.validationLimitAmount(payParam);
        // 获取商户订单号
        String bizOrderNo = payParam.getBizOrderNo();
        // 加锁
        LockInfo lock = lockTemplate.lock("payment:pay:" + bizOrderNo,10000,200);
        if (Objects.isNull(lock)){
            payResult.setMsg("正在支付中，请勿重复支付");
            return payResult;
        }
        try {
            // 查询并检查订单
            PayOrder payOrder = payAssistService.getOrderAndCheck(payParam.getBizOrderNo());
            // 初始化上下文
            payAssistService.initPayContext(payOrder, payParam);
            // 走首次下单逻辑还是重复下档逻辑
            if (Objects.isNull(payOrder)){
                return this.firstPay(payParam);
            } else {
                return this.repeatPay(payParam,payOrder);
            }
        } catch (Exception e) {
            log.error("支付异常",e);
            payResult.setMsg(e.getMessage());
            return payResult;
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /**
     * 首次支付
     * 拆分为多阶段，1. 保存订单记录信息 2 调起支付 3. 支付成功后操作
     */
    public PayResult firstPay(PayParam payParam){
        // 获取支付策略类
        AbsPayStrategy payStrategy = PayStrategyFactory.create(payParam.getChannel());
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
            payOrder.setErrorMsg(e.getMessage());
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
                    .setStatus(SUCCESS.getCode())
                    .setPayTime(payInfo.getCompleteTime());
        }
        payOrderService.updateById(payOrder);
        // 扩展记录更新
        payOrder.setErrorCode(null);
        payOrder.setErrorMsg(null);
        payOrderService.updateById(payOrder);
        // 如果支付完成 发送通知
        if (Objects.equals(payOrder.getStatus(), SUCCESS.getCode())){
            clientNoticeService.registerPayNotice(payOrder, payInfo.getPayOrderExtra());
        }
        return payAssistService.buildResult(payOrder);
    }

    /**
     * 重复支付
     */
    public PayResult repeatPay(PayParam payParam, PayOrder payOrder){
        // 获取支付策略类
        AbsPayStrategy payStrategy = PayStrategyFactory.create(payParam.getChannel());
        // 初始化支付的参数
        payStrategy.initPayParam(payOrder, payParam);
        // 执行支付前处理动作
        payStrategy.doBeforePayHandler();
        // 查询订单扩展记录
        PayOrderExtra payOrderExtra = payOrderExtraManager.findById(payOrder.getId()).orElseThrow(() -> new DataNotExistException("支付订单不完整"));
        // 执行支付前的更新动作, 更新并保存订单和扩展的数据
        payAssistService.updatePayOrder(payParam, payOrder, payOrderExtra);

        try {
            // 支付操作
            payStrategy.doPayHandler();
        } catch (Exception e) {
            // 记录错误原因
            payOrder.setErrorMsg(e.getMessage());
            payOrderService.updateById(payOrder);
            throw e;
        }
        // 支付调起成功后操作, 使用事务来保证数据一致性
        return SpringUtil.getBean(this.getClass()).repeatPaySuccess(payOrder, payOrderExtra);
    }

    /**
     * 重复支付成功后操作
     */
    @Transactional(rollbackFor = Exception.class)
    public PayResult repeatPaySuccess(PayOrder payOrder, PayOrderExtra payOrderExtra) {
        PayLocal payInfo = PaymentContextLocal.get().getPayInfo();
        // 如果支付完成, 进行订单完成处理, 同时发送回调消息
        if (payInfo.isComplete()) {
            payOrder.setOutOrderNo(payInfo.getOutOrderNo())
                    .setStatus(SUCCESS.getCode())
                    .setPayTime(payInfo.getCompleteTime());
        }
        payOrderService.updateById(payOrder);
        // 扩展记录更新
        payOrder.setErrorMsg(null);
        payOrder.setErrorCode(null);
        payOrderService.updateById(payOrder);
        // 如果支付完成 发送通知
        if (Objects.equals(payOrder.getStatus(), SUCCESS.getCode())){
            // 查询通道订单
            clientNoticeService.registerPayNotice(payOrder, payOrderExtra);
        }
        return payAssistService.buildResult(payOrder);
    }
}
