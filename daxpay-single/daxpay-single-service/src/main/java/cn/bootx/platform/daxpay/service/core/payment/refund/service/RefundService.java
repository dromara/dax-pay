package cn.bootx.platform.daxpay.service.core.payment.refund.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.exception.RepetitiveOperationException;
import cn.bootx.platform.common.core.function.CollectorsFunction;
import cn.bootx.platform.common.core.util.ValidationUtil;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.code.RefundStatusEnum;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.param.payment.refund.RefundParam;
import cn.bootx.platform.daxpay.result.pay.RefundResult;
import cn.bootx.platform.daxpay.service.common.context.RefundLocal;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayChannelOrder;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.service.core.order.pay.service.PayOrderService;
import cn.bootx.platform.daxpay.service.core.order.refund.dao.RefundOrderManager;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.RefundChannelOrder;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.RefundOrder;
import cn.bootx.platform.daxpay.service.core.payment.notice.service.ClientNoticeService;
import cn.bootx.platform.daxpay.service.core.payment.refund.factory.RefundStrategyFactory;
import cn.bootx.platform.daxpay.service.func.AbsRefundStrategy;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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

    private final RefundOrderManager refundOrderManager;

    private final LockTemplate lockTemplate;

    /**
     * 分支付通道进行退款
     * 1. 创建退款订单和通道订单并保存(单独事务)
     * 2. 调用API发起退款(异步退款)
     * 3. 根据API返回信息更新退款订单信息
     */
    public RefundResult refund(RefundParam param){
        // 参数校验
        ValidationUtil.validateParam(param);
        // 加锁
        LockInfo lock = lockTemplate.lock("payment:refund:" + param.getBizRefundNo(),10000,200);
        if (Objects.isNull(lock)){
            throw new RepetitiveOperationException("退款处理中，请勿重复操作");
        }
        try {
            // 判断是否是首次发起退款
            Optional<RefundOrder> refund = refundOrderManager.findByBizRefundNo(param.getBizRefundNo());
            if (refund.isPresent()){
                return this.repeatRefund(param,refund.get());
            } else {
                return this.firstRefund(param);
            }
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /**
     * 首次退款
     */
    public RefundResult firstRefund(RefundParam param){

        // 获取支付订单
        PayOrder payOrder = refundAssistService.getPayOrder(param);
        // 检查退款参数
        refundAssistService.checkAndParam(param, payOrder);

        // ----------------------------- 发起退款操作 --------------------------------------------

        // 通过退款参数获取退款策略
        AbsRefundStrategy refundStrategy = RefundStrategyFactory.create(payOrder.getChannel());
        // 设置支付订单数据
        refundStrategy.setPayOrder(payOrder);
        // 进行退款前预处理
        refundStrategy.doBeforeRefundHandler();

        // 退款操作的预处理, 使用独立的新事物进行发起, 返回创建成功的退款订单, 成功后才可以进行下一阶段的操作
        RefundOrder refundOrder = SpringUtil.getBean(this.getClass()).preRefundMethod(param, payOrder);

        try {
            // 3.2 执行退款策略
            refundStrategy.doRefundHandler();
        }
        catch (Exception e) {
            // 5. 失败处理, 所有记录都会回滚, 可以调用重新
            PaymentContextLocal.get().getRefundInfo().setStatus(RefundStatusEnum.FAIL);
            // 记录退款失败的记录
            refundAssistService.updateOrderByError(refundOrder);
        }
        SpringUtil.getBean(this.getClass()).successHandler(refundOrder, payOrder);
        return new RefundResult()
                .setBizRefundNo(refundOrder.getBizRefundNo())
                .setRefundNo(refundOrder.getRefundNo());
    }

    /**
     * 退款一阶段: 进行支付订单退款金额的预扣, 创建退款订单并保存
     */
    @Transactional(rollbackFor = Exception.class )
    public RefundOrder preRefundMethod(RefundParam refundParam, PayOrder payOrder) {
        // --------------------------- 支付订单 -------------------------------------
        // 预扣支付订单要退款的金额并进行更新
        int orderRefundableBalance = payOrder.getRefundableBalance() - refundParam.getAmount();
        payOrder.setRefundableBalance(orderRefundableBalance)
                .setStatus(PayStatusEnum.REFUNDING.getCode());
        payOrderService.updateById(payOrder);
        // -----------------------   退款订单创建   -------------------------
        return refundAssistService.createOrder(refundParam, payOrder);
    }

    /**
     * 重新发起退款处理
     * 1. 查出相关退款订单
     * 2. 构建退款策略, 发起退款
     */
    public RefundResult repeatRefund(RefundParam param, RefundOrder refundOrder){
        // 退款失败才可以重新发起退款, 重新发起退款
        if (!Objects.equals(refundOrder.getStatus(), RefundStatusEnum.FAIL.getCode())){
            throw new PayFailureException("只有失败状态的才可以重新发起退款");
        }

        AbsRefundStrategy refundStrategy = RefundStrategyFactory.create(refundOrder.getRefundNo());

        // 设置退款订单对象
        refundStrategy.setRefundOrder(refundOrder);

        try {
            // 3.1 退款前准备操作
            refundStrategy.forEach(AbsRefundStrategy::doBeforeRefundHandler);
            // 3.2 执行退款策略
            refundStrategy.forEach(AbsRefundStrategy::doRefundHandler);
            // 3.3 执行退款发起成功后操作
            refundStrategy.forEach(AbsRefundStrategy::doSuccessHandler);

            // 4.进行成功处理, 分别处理退款订单, 通道退款订单, 支付订单
            List<RefundChannelOrder> refundChannelOrders = refundStrategy.stream()
                    .map(AbsRefundStrategy::getRefundChannelOrder)
                    .collect(Collectors.toList());
            this.successHandler(refundOrder, refundChannelOrders, payOrder);
        }
        catch (Exception e) {
            // 5. 失败处理, 所有记录都会回滚, 可以调用退款重试
            PaymentContextLocal.get().getRefundInfo().setStatus(RefundStatusEnum.FAIL);
            // 记录退款失败的记录
            refundAssistService.updateOrderByError(refundOrder);
        }
    }

    /**
     * 成功处理, 更新退款订单, 退款通道订单, 支付订单, 支付通道订单
     */
    @Transactional(rollbackFor = Exception.class)
    public void successHandler(RefundOrder refundOrder, PayOrder payOrder) {
        RefundLocal refundInfo = PaymentContextLocal.get().getRefundInfo();
        // 剩余可退款余额
        int refundableBalance = payOrder.getRefundableBalance();
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
        refundAssistService.updateOrder(refundOrder);

        // 发送通知
        List<String> list = Arrays.asList(RefundStatusEnum.SUCCESS.getCode(), RefundStatusEnum.CLOSE.getCode(),  RefundStatusEnum.FAIL.getCode());
        if (list.contains(refundOrder.getStatus())){
            clientNoticeService.registerRefundNotice(refundOrder, refundInfo.getRunOrderExtra());
        }
    }

}
