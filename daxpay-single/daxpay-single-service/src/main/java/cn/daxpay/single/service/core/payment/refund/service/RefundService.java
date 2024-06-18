package cn.daxpay.single.service.core.payment.refund.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.util.ValidationUtil;
import cn.daxpay.single.core.code.PayOrderRefundStatusEnum;
import cn.daxpay.single.core.code.RefundStatusEnum;
import cn.daxpay.single.core.param.payment.refund.RefundParam;
import cn.daxpay.single.core.result.pay.RefundResult;
import cn.daxpay.single.service.common.context.RefundLocal;
import cn.daxpay.single.service.common.local.PaymentContextLocal;
import cn.daxpay.single.service.core.order.pay.entity.PayOrder;
import cn.daxpay.single.service.core.order.pay.service.PayOrderQueryService;
import cn.daxpay.single.service.core.order.pay.service.PayOrderService;
import cn.daxpay.single.service.core.order.refund.dao.RefundOrderManager;
import cn.daxpay.single.service.core.order.refund.entity.RefundOrder;
import cn.daxpay.single.service.core.payment.notice.service.ClientNoticeService;
import cn.daxpay.single.service.core.record.flow.service.TradeFlowRecordService;
import cn.daxpay.single.service.func.AbsRefundStrategy;
import cn.daxpay.single.service.util.PayStrategyFactory;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
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

    private final RefundAssistService refundAssistService;;

    private final PayOrderService payOrderService;

    private final ClientNoticeService clientNoticeService;

    private final RefundOrderManager refundOrderManager;

    private final PayOrderQueryService payOrderQueryService;

    private final TradeFlowRecordService tradeFlowRecordService;

    private final LockTemplate lockTemplate;


    /**
     * 分支付通道进行退款
     * 1. 创建退款订单(单独事务)
     * 2. 调用API发起退款(异步退款)
     * 3. 根据API返回信息更新退款订单信息
     */
    public RefundResult refund(RefundParam param){
        RefundResult result = new RefundResult()
                .setRefundNo(param.getBizRefundNo())
                .setBizRefundNo(param.getBizRefundNo());
        // 参数校验
        ValidationUtil.validateParam(param);
        // 加锁
        LockInfo lock = lockTemplate.lock("payment:refund:" + param.getBizRefundNo(),10000,200);
        if (Objects.isNull(lock)){
            result.setMsg("退款处理中，请勿重复操作");
            return result;
        }
        try {
            // 判断是否是首次发起退款
            Optional<RefundOrder> refund = refundOrderManager.findByBizRefundNo(param.getBizRefundNo());
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
    public RefundResult firstRefund(RefundParam param){

        // 获取支付订单
        PayOrder payOrder = payOrderQueryService.findByBizOrOrderNo(param.getOrderNo(), param.getBizOrderNo())
                .orElseThrow(() -> new DataNotExistException("支付订单不存在"));
        // 检查退款参数
        refundAssistService.checkAndParam(param, payOrder);
        // 通过退款参数获取退款策略
        AbsRefundStrategy refundStrategy = PayStrategyFactory.create(payOrder.getChannel(), AbsRefundStrategy.class);
        // 进行退款前预处理
        refundStrategy.doBeforeRefundHandler();
        // 退款操作的预处理, 对支付订单进行预扣款, 返回创建成功的退款订单, 成功后才可以进行下一阶段的操作
        RefundOrder refundOrder = SpringUtil.getBean(this.getClass()).preRefundMethod(param, payOrder);
        refundStrategy.setRefundOrder(refundOrder);
        try {
            // 执行退款策略
            refundStrategy.doRefundHandler();
        } catch (Exception e) {
            log.error("退款出现错误", e);
            // 记录处理失败状态
            PaymentContextLocal.get().getRefundInfo().setStatus(RefundStatusEnum.FAIL);
            // 更新退款失败的记录
            refundAssistService.updateOrderByError(refundOrder);
            return refundAssistService.buildResult(refundOrder);
        }
        SpringUtil.getBean(this.getClass()).successHandler(refundOrder, payOrder);
        return refundAssistService.buildResult(refundOrder);
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
                .setRefundStatus(PayOrderRefundStatusEnum.REFUNDING.getCode());
        payOrderService.updateById(payOrder);
        // -----------------------   退款订单创建   -------------------------
        return refundAssistService.createOrder(refundParam, payOrder);
    }

    /**
     * 重新发起退款处理
     * 1. 查出相关退款订单
     * 2. 更新退款扩展参数
     * 3. 构建退款策略, 发起退款
     */
    public RefundResult repeatRefund(RefundOrder refundOrder, RefundParam param){
        // 退款失败才可以重新发起退款, 重新发起退款
        if (!Objects.equals(refundOrder.getStatus(), RefundStatusEnum.FAIL.getCode())){
            throw new PayFailureException("只有失败状态的才可以重新发起退款");
        }
        // 获取支付订单
        PayOrder payOrder = payOrderQueryService.findByBizOrOrderNo(refundOrder.getOrderNo(), refundOrder.getBizOrderNo())
                .orElseThrow(() -> new DataNotExistException("支付订单不存在"));
        AbsRefundStrategy refundStrategy = PayStrategyFactory.create(refundOrder.getChannel(), AbsRefundStrategy.class);
        // 设置退款订单对象
        refundStrategy.setRefundOrder(refundOrder);
        // 退款前准备操作
        refundStrategy.doBeforeRefundHandler();
        // 进行发起退款前的操作, 更新扩展记录信息
        this.updateOrder(param,refundOrder);
        try {
            // 执行退款策略
            refundStrategy.doRefundHandler();
        }
        catch (Exception e) {
            log.error("重新退款失败:", e);
            // 记录处理失败状态
            PaymentContextLocal.get().getRefundInfo().setStatus(RefundStatusEnum.FAIL);
            // 记录退款失败的记录
            refundAssistService.updateOrderByError(refundOrder);
            // 返回错误响应对象
            return refundAssistService.buildResult(refundOrder);
        }
        // 退款发起成功处理
        SpringUtil.getBean(this.getClass()).successHandler(refundOrder, payOrder);
        return refundAssistService.buildResult(refundOrder);
    }

    /**
     * 更新退款订单扩展信息
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
    public void successHandler(RefundOrder refundOrder, PayOrder payOrder) {
        RefundLocal refundInfo = PaymentContextLocal.get()
                .getRefundInfo();
        // 剩余可退款余额
        int refundableBalance = payOrder.getRefundableBalance();
        // 退款状态为退款中
        if (refundInfo.getStatus() == RefundStatusEnum.PROGRESS) {
            payOrder.setRefundStatus(PayOrderRefundStatusEnum.REFUNDING.getCode());
        }
        // 退款状态为成功
        else {
            if (refundableBalance == 0) {
                payOrder.setRefundStatus(PayOrderRefundStatusEnum.REFUNDED.getCode());
            } else {
                payOrder.setRefundStatus(PayOrderRefundStatusEnum.PARTIAL_REFUND.getCode());
            }
            // 记录流水
            tradeFlowRecordService.saveRefund(refundOrder);
        }
        payOrderService.updateById(payOrder);

        // 更新退款订单
        refundAssistService.updateOrder(refundOrder);

        // 发送通知
        List<String> list = Arrays.asList(RefundStatusEnum.SUCCESS.getCode(), RefundStatusEnum.CLOSE.getCode(),  RefundStatusEnum.FAIL.getCode());
        if (list.contains(refundOrder.getStatus())){
            clientNoticeService.registerRefundNotice(refundOrder);
        }
    }

}
