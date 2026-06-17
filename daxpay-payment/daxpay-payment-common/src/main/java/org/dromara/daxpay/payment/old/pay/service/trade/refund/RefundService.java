package org.dromara.daxpay.payment.old.pay.service.trade.refund;

import org.dromara.daxpay.platform.core.exception.DataNotExistException;
import org.dromara.daxpay.platform.core.exception.ValidationFailedException;
import org.dromara.daxpay.platform.core.exception.BizInfoException;
import org.dromara.daxpay.platform.core.code.CommonErrorCode;
import org.dromara.daxpay.platform.core.util.ValidationUtil;
import org.dromara.daxpay.platform.core.code.DaxPayErrorCode;
import org.dromara.daxpay.platform.core.exception.operation.OperationFailException;
import org.dromara.daxpay.payment.common.util.PaymentStrategyFactory;
import org.dromara.daxpay.payment.old.pay.bo.trade.RefundResultBo;
import org.dromara.daxpay.payment.old.pay.dao.order.pay.PayOrderManager;
import org.dromara.daxpay.payment.old.pay.dao.order.refund.RefundOrderManager;
import org.dromara.daxpay.payment.old.pay.entity.order.pay.PayOrder;
import org.dromara.daxpay.payment.old.pay.entity.order.refund.RefundOrder;
import org.dromara.daxpay.platform.core.enums.pay.pay.PayRefundStatusEnum;
import org.dromara.daxpay.platform.core.enums.pay.refund.RefundStatusEnum;
import org.dromara.daxpay.payment.old.pay.exception.TradeNotExistException;
import org.dromara.daxpay.payment.old.pay.exception.TradeProcessingException;
import org.dromara.daxpay.payment.old.pay.exception.TradeStatusErrorException;
import org.dromara.daxpay.payment.old.pay.service.order.pay.PayOrderQueryService;
import org.dromara.daxpay.payment.old.pay.service.trade.TradeUniHandleService;
import org.dromara.daxpay.payment.strategy.refund.AbsRefundStrategy;
import org.dromara.daxpay.payment.unipay.param.trade.refund.RefundParam;
import org.dromara.daxpay.payment.unipay.result.trade.refund.RefundResult;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import org.dromara.daxpay.platform.core.code.CommonCode;

/// # 支付退款服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class RefundService {

    private final RefundOrderManager refundOrderManager;

    private final PayOrderQueryService payOrderQueryService;

    private final LockTemplate lockTemplate;

    private final RefundAssistService refundAssistService;

    private final PayOrderManager payOrderManager;

//    private final DelayJobService delayJobService;

    private final TradeUniHandleService tradeUniHandleService;

    /// 退款
    /// 1. 创建退款订单(单独事务)
    /// 2. 调用API发起退款(异步退款)
    /// 3. 根据API返回信息更新退款订单信息
    public RefundResult refund(RefundParam param){
        // 绕过退款来源的校验
        String source = param.getSource();
        param.setSource(null);
        // 参数校验
        ValidationUtil.validateParam(param);
        param.setSource(source);
        // 校验参数
        if (StrUtil.isBlank(param.getOrderNo()) && Objects.isNull(param.getBizOrderNo())){
            // 支付订单号不能都为空
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.orderNoRequired");
        }
        // 判断是否是首次发起退款
        Optional<RefundOrder> refund = refundOrderManager.findByBizRefundNo(param.getBizRefundNo(),param.getAppId());
        if (refund.isPresent()){
            return this.repeatRefund(refund.get(),param);
        } else {
            return this.firstRefund(param);
        }
    }

    /// 首次退款
    private RefundResult firstRefund(RefundParam param) {
        // 获取支付订单
        PayOrder payOrder = payOrderQueryService.findAnyOrderNo(param.getOrderNo(), param.getBizOrderNo(), param.getAppId())
                // 订单: 支付订单不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.order.payOrderNotExist"));
        // 加锁, 使用支付订单id, 防止同时多个退款被发起
        LockInfo lock = lockTemplate.lock("payment:refund:" + payOrder.getId(),10000,200);
        if (Objects.isNull(lock)){
            // 退款处理中，请勿重复操作
            throw new TradeProcessingException();
        }
        try {
            // 检查退款参数
            refundAssistService.checkAndParam(param, payOrder);
            // 通过退款参数获取退款策略
            AbsRefundStrategy refundStrategy = PaymentStrategyFactory.createByProduct(payOrder.getProduct(), AbsRefundStrategy.class);
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
            SpringUtil.getBean(this.getClass())
                    .successHandler(refundOrder, payOrder, refundResultBo);
            return refundAssistService.buildResult(refundOrder);
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /// 退款一阶段: 进行支付订单退款金额的预扣, 创建退款订单并保存
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

    /// 重新发起退款处理
    /// 1. 查出相关退款订单
    /// 2. 更新退款扩展参数
    /// 3. 构建退款策略, 发起退款
    private RefundResult repeatRefund(RefundOrder refundOrder, RefundParam param) {
        // 加锁
        LockInfo lock = lockTemplate.lock("payment:refund:" + refundOrder.getId(),10000,200);
        if (Objects.isNull(lock)){
            // 退款处理中，请勿重复操作
            throw new TradeProcessingException();
        }
        try {
            // 退款失败才可以重新发起退款
            if (!Objects.equals(refundOrder.getStatus(), RefundStatusEnum.FAIL.getCode())) {
                // 只有失败状态的才可以重新发起退款
                throw new TradeStatusErrorException(DaxPayErrorCode.TRADE_STATUS_ERROR, "pay.error.refund.onlyFailRetry");
            }
            // 获取支付订单
            PayOrder payOrder = payOrderQueryService.findAnyOrderNo(refundOrder.getOrderNo(), refundOrder.getBizOrderNo(), refundOrder.getAppId())
                    // 订单: 支付订单不存在
                    .orElseThrow(() -> new TradeNotExistException("error.payment.order.payOrderNotExist"));
            AbsRefundStrategy refundStrategy = PaymentStrategyFactory.createByProduct(refundOrder.getProduct(), AbsRefundStrategy.class);
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
//                delayJobService.registerByTransaction(refundOrder.getId(), DaxPayCode.Event.ORDER_REFUND_SYNC, 2*60*1000L);
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
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /// 更新退款订单信息
    private void updateOrder(RefundParam param, RefundOrder order){
        order.setAttach(param.getAttach())
                .setClientIp(param.getClientIp())
                .setNotifyUrl(param.getNotifyUrl())
                .setReqTime(param.getReqTime())
                .setExtraParam(param.getExtraParam());
        refundOrderManager.updateById(order);
    }

    /// 成功处理, 更新退款订单, 支付订单,
    @Transactional(rollbackFor = Exception.class)
    public void successHandler(RefundOrder refundOrder, PayOrder payOrder, RefundResultBo refundInfo) {
        // 更新退款订单
        refundOrder.setStatus(refundInfo.getStatus().getCode())
                .setOutRefundNo(refundInfo.getOutRefundNo())
                .setRelationOrderNo(refundInfo.getRelationOrderNo())
                .setFinishTime(refundInfo.getFinishTime());
        // 退款状态为退款中
        if (refundInfo.getStatus() == RefundStatusEnum.PROGRESS) {
            payOrder.setRefundStatus(PayRefundStatusEnum.REFUNDING.getCode());
            payOrderManager.updateById(payOrder);
            refundOrderManager.updateById(refundOrder);
            // 注册延时同步事件
//            delayJobService.registerByTransaction(refundOrder.getId(), DaxPayCode.Event.ORDER_REFUND_SYNC, 2*60*1000L);
        }
        // 退款状态为成功
        else {
            // 退款成功处理
            tradeUniHandleService.refundSuccess(payOrder,refundOrder);
        }
    }

}

