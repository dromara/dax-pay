package org.dromara.daxpay.service.service.trade.refund;

import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.util.BigDecimalUtil;
import cn.bootx.platform.core.util.ValidationUtil;
import cn.bootx.platform.starter.redis.delay.service.DelayJobService;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.core.enums.PayRefundStatusEnum;
import org.dromara.daxpay.core.enums.RefundStatusEnum;
import org.dromara.daxpay.core.exception.TradeNotExistException;
import org.dromara.daxpay.core.exception.TradeProcessingException;
import org.dromara.daxpay.core.exception.TradeStatusErrorException;
import org.dromara.daxpay.core.param.trade.refund.RefundParam;
import org.dromara.daxpay.core.result.trade.refund.RefundResult;
import org.dromara.daxpay.service.bo.trade.RefundResultBo;
import org.dromara.daxpay.service.code.DaxPayCode;
import org.dromara.daxpay.service.dao.order.pay.PayOrderManager;
import org.dromara.daxpay.service.dao.order.refund.RefundOrderManager;
import org.dromara.daxpay.service.entity.order.pay.PayOrder;
import org.dromara.daxpay.service.entity.order.refund.RefundOrder;
import org.dromara.daxpay.service.service.notice.MerchantNoticeService;
import org.dromara.daxpay.service.service.order.pay.PayOrderQueryService;
import org.dromara.daxpay.service.service.record.flow.TradeFlowRecordService;
import org.dromara.daxpay.service.strategy.AbsRefundStrategy;
import org.dromara.daxpay.service.util.PaymentStrategyFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

/**
 * 支付退款服务
 * @author xxm
 * @since 2023/12/18
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RefundService {

    private final RefundOrderManager refundOrderManager;

    private final PayOrderQueryService payOrderQueryService;

    private final LockTemplate lockTemplate;

    private final RefundAssistService refundAssistService;

    private final PayOrderManager payOrderManager;

    private final TradeFlowRecordService tradeFlowRecordService;

    private final MerchantNoticeService merchantNoticeService;

    private final DelayJobService delayJobService;

    /**
     * 退款
     * 1. 创建退款订单(单独事务)
     * 2. 调用API发起退款(异步退款)
     * 3. 根据API返回信息更新退款订单信息
     */
    public RefundResult refund(RefundParam param){
        // 参数校验
        ValidationUtil.validateParam(param);
        // 加锁
        LockInfo lock = lockTemplate.lock("payment:refund:" + param.getBizRefundNo(),10000,200);
        if (Objects.isNull(lock)){
            throw new TradeProcessingException("退款处理中，请勿重复操作");
        }
        try {
            // 判断是否是首次发起退款
            Optional<RefundOrder> refund = refundOrderManager.findByBizRefundNo(param.getBizRefundNo(), param.getAppId());
            if (refund.isPresent()){
                return this.repeatRefund(refund.get(),param);
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
    private RefundResult firstRefund(RefundParam param) {
        // 获取支付订单
        PayOrder payOrder = payOrderQueryService.findByBizOrOrderNo(param.getOrderNo(), param.getBizOrderNo(), param.getAppId())
                .orElseThrow(() -> new DataNotExistException("支付订单不存在"));
        // 检查退款参数和支付订单
        refundAssistService.checkAndParam(param, payOrder);
        // 通过退款参数获取退款策略
        AbsRefundStrategy refundStrategy = PaymentStrategyFactory.create(payOrder.getChannel(), AbsRefundStrategy.class);
        // 进行退款前预处理
        refundStrategy.doBeforeRefundHandler();
        // 退款操作的预处理, 对支付订单进行预扣款, 返回创建成功的退款订单, 成功后才可以进行下一阶段的操作
        RefundOrder refundOrder = SpringUtil.getBean(this.getClass())
                .preRefundMethod(param, payOrder);
        refundStrategy.setRefundOrder(refundOrder);
        RefundResultBo refundResultBo;
        try {
            // 执行退款策略
            refundResultBo = refundStrategy.doRefundHandler();
        } catch (Exception e) {
            log.error("退款出现错误", e);
            // 更新退款失败的记录
            refundAssistService.updateOrderByError(refundOrder, e.getMessage());
            return refundAssistService.buildResult(refundOrder);
        }
        SpringUtil.getBean(this.getClass()).successHandler(refundOrder, payOrder, refundResultBo);
        return refundAssistService.buildResult(refundOrder);
    }

    /**
     * 退款一阶段: 进行支付订单退款金额的预扣, 创建退款订单并保存
     */
    @Transactional(rollbackFor = Exception.class )
    public RefundOrder preRefundMethod(RefundParam refundParam, PayOrder payOrder) {
        // --------------------------- 支付订单 -------------------------------------
        // 预扣支付订单要退款的金额并进行更新
        var orderRefundableBalance = payOrder.getRefundableBalance().subtract(refundParam.getAmount());
        payOrder.setRefundableBalance(orderRefundableBalance)
                .setRefundStatus(PayRefundStatusEnum.REFUNDING.getCode());
        payOrderManager.updateById(payOrder);
        // -----------------------   退款订单创建   -------------------------
        return refundAssistService.createOrder(refundParam, payOrder);
    }

    /**
     * 重新发起退款处理
     * 1. 查出相关退款订单
     * 2. 更新退款扩展参数
     * 3. 构建退款策略, 发起退款
     */
    private RefundResult repeatRefund(RefundOrder refundOrder, RefundParam param) {
        // 退款失败才可以重新发起退款
        if (!Objects.equals(refundOrder.getStatus(), RefundStatusEnum.FAIL.getCode())) {
            throw new TradeStatusErrorException("只有失败状态的才可以重新发起退款");
        }
        // 获取支付订单
        PayOrder payOrder = payOrderQueryService.findByBizOrOrderNo(refundOrder.getOrderNo(), refundOrder.getBizOrderNo(), refundOrder.getAppId())
                .orElseThrow(() -> new TradeNotExistException("支付订单不存在"));
        AbsRefundStrategy refundStrategy = PaymentStrategyFactory.create(refundOrder.getChannel(), AbsRefundStrategy.class);
        // 设置退款订单对象
        refundStrategy.setRefundOrder(refundOrder);
        // 退款前准备操作
        refundStrategy.doBeforeRefundHandler();
        // 进行发起退款前的操作, 更新扩展记录信息
        this.updateOrder(param, refundOrder);
        RefundResultBo refundResultBo;
        try {
            // 执行退款策略
            refundResultBo = refundStrategy.doRefundHandler();
            // 注册一个两分钟后执行的同步任务, 作为接不到回调任务的兜底
            delayJobService.registerByTransaction(refundOrder.getId(), DaxPayCode.Event.ORDER_PAY_TIMEOUT, 2*60*1000L);
        } catch (Exception e) {
            log.error("重新退款失败:", e);
            // 记录退款失败的记录
            refundAssistService.updateOrderByError(refundOrder, e.getMessage());
            // 返回错误响应对象
            return refundAssistService.buildResult(refundOrder);
        }
        // 退款发起成功处理
        SpringUtil.getBean(this.getClass()).successHandler(refundOrder, payOrder, refundResultBo);
        return refundAssistService.buildResult(refundOrder);
    }

    /**
     * 更新退款订单信息
     */
    private void updateOrder(RefundParam param, RefundOrder order){
        order.setAttach(param.getAttach())
                .setClientIp(param.getClientIp())
                .setNotifyUrl(param.getNotifyUrl())
                .setReqTime(param.getReqTime())
                .setExtraParam(param.getExtraParam());
        refundOrderManager.updateById(order);
    }

    /**
     * 成功处理, 更新退款订单, 支付订单,
     */
    @Transactional(rollbackFor = Exception.class)
    public void successHandler(RefundOrder refundOrder, PayOrder payOrder, RefundResultBo refundInfo) {
        // 剩余可退款余额
        BigDecimal refundableBalance = payOrder.getRefundableBalance();
        // 退款状态为退款中
        if (refundInfo.getStatus() == RefundStatusEnum.PROGRESS) {
            payOrder.setRefundStatus(PayRefundStatusEnum.REFUNDING.getCode());
        }
        // 退款状态为成功
        else {
            if (BigDecimalUtil.isEqual(refundableBalance,BigDecimal.ZERO)) {
                payOrder.setRefundStatus(PayRefundStatusEnum.REFUNDED.getCode());
            } else {
                payOrder.setRefundStatus(PayRefundStatusEnum.PARTIAL_REFUND.getCode());
            }
            // 记录流水
            tradeFlowRecordService.saveRefund(refundOrder);
        }
        payOrderManager.updateById(payOrder);

        // 更新退款订单
        refundOrder.setStatus(refundInfo.getStatus().getCode())
                .setOutRefundNo(refundInfo.getOutRefundNo())
                .setFinishTime(refundInfo.getFinishTime());
        // 是否直接返回了退款成功
        if (Objects.equals(refundOrder.getStatus(), RefundStatusEnum.SUCCESS.getCode())){
            // 发送通知
            merchantNoticeService.registerRefundNotice(refundOrder);
        } else {
            // 注册延时同步事件
            delayJobService.registerByTransaction(refundOrder.getId(), DaxPayCode.Event.ORDER_REFUND_SYNC, 2*60*1000L);
        }
        refundOrderManager.updateById(refundOrder);
    }
}
