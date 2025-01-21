package org.dromara.daxpay.service.service.trade.refund;

import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.exception.ValidationFailedException;
import cn.bootx.platform.core.util.BigDecimalUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.core.enums.PayRefundStatusEnum;
import org.dromara.daxpay.core.enums.PayStatusEnum;
import org.dromara.daxpay.core.enums.RefundStatusEnum;
import org.dromara.daxpay.core.exception.TradeStatusErrorException;
import org.dromara.daxpay.core.param.trade.refund.RefundParam;
import org.dromara.daxpay.core.result.trade.refund.RefundResult;
import org.dromara.daxpay.core.util.TradeNoGenerateUtil;
import org.dromara.daxpay.service.bo.trade.RefundResultBo;
import org.dromara.daxpay.service.dao.order.pay.PayOrderManager;
import org.dromara.daxpay.service.dao.order.refund.RefundOrderManager;
import org.dromara.daxpay.service.entity.order.pay.PayOrder;
import org.dromara.daxpay.service.entity.order.refund.RefundOrder;
import org.dromara.daxpay.service.service.notice.MerchantNoticeService;
import org.dromara.daxpay.service.service.record.flow.TradeFlowRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 支付退款支撑服务
 * @author xxm
 * @since 2023/12/26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RefundAssistService {
    private final RefundOrderManager refundOrderManager;
    private final PayOrderManager payOrderManager;
    private final MerchantNoticeService merchantNoticeService;
    private final TradeFlowRecordService tradeFlowRecordService;

    /**
     * 检查并处理退款参数
     */
    public void checkAndParam(RefundParam param, PayOrder payOrder){
        // 非支付完成的不能进行退款
        if (!Objects.equals(RefundStatusEnum.SUCCESS.getCode(), payOrder.getStatus())) {
            PayStatusEnum statusEnum = PayStatusEnum.findByCode(payOrder.getStatus());
            throw new TradeStatusErrorException("当前支付单订状态["+statusEnum.getName()+"]不允许发起退款操作");
        }
        // 退款中和退款完成不能退款
        List<String> tradesStatus = List.of(
                PayRefundStatusEnum.REFUNDED.getCode(),
                PayRefundStatusEnum.REFUNDING.getCode());
        if (tradesStatus.contains(payOrder.getRefundStatus())){
            var statusEnum = PayRefundStatusEnum.findByCode(payOrder.getRefundStatus());
            throw new TradeStatusErrorException("当前支付单退款状态["+statusEnum.getName()+"]不允许发起退款操作");
        }
        // 退款号唯一校验
        if (StrUtil.isNotBlank(param.getBizRefundNo()) && refundOrderManager.existsByRefundNo(param.getBizRefundNo())){
            throw new ValidationFailedException("退款单号已存在");
        }

        // 金额判断
        if (BigDecimalUtil.isGreaterThan(param.getAmount(),payOrder.getRefundableBalance())){
            throw new ValidationFailedException("退款金额不能大于支付金额");
        }
    }

    /**
     * 预先创建退款相关订单并保存, 使用新事务, 防止丢单
     */
    @Transactional(rollbackFor = Exception.class)
    public RefundOrder createOrder(RefundParam refundParam, PayOrder payOrder) {
        // 生成退款订单
        RefundOrder refundOrder = new RefundOrder()
                .setOrderId(payOrder.getId())
                .setOrderNo(payOrder.getOrderNo())
                .setBizOrderNo(payOrder.getBizOrderNo())
                .setOutOrderNo(payOrder.getOutOrderNo())
                .setRefundNo(TradeNoGenerateUtil.refund())
                .setBizRefundNo(refundParam.getBizRefundNo())
                .setChannel(payOrder.getChannel())
                .setStatus(RefundStatusEnum.PROGRESS.getCode())
                .setOrderAmount(payOrder.getAmount())
                .setAmount(refundParam.getAmount())
                .setTitle(payOrder.getTitle())
                .setReason(refundParam.getReason())
                .setClientIp(refundParam.getClientIp())
                .setReqTime(refundParam.getReqTime())
                .setAttach(refundParam.getAttach())
                .setNotifyUrl(refundParam.getNotifyUrl());
        refundOrderManager.save(refundOrder);
        return refundOrder;
    }

    /**
     * 更新退款成功信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateOrder(RefundOrder refundOrder, RefundResultBo resultBo){
        refundOrder.setStatus(resultBo.getStatus().getCode())
                .setOutRefundNo(resultBo.getOutRefundNo());
        // 是否直接返回了退款成功
        if (Objects.equals(refundOrder.getStatus(), RefundStatusEnum.SUCCESS.getCode())){
            // 读取网关返回的退款时间和完成时间
            refundOrder.setFinishTime(resultBo.getFinishTime());
        }
        refundOrderManager.updateById(refundOrder);
    }

    /**
     * 更新退款错误信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateOrderByError(RefundOrder refundOrder,  String message){
        refundOrder.setErrorMsg(message);
        refundOrder.setStatus(RefundStatusEnum.FAIL.getCode());
        refundOrderManager.updateById(refundOrder);
    }

    /**
     * 退款失败, 关闭退款单并将失败的退款金额归还回订单
     */
    @Transactional(rollbackFor = Exception.class)
    public void close(RefundOrder refundOrder) {
        PayOrder payOrder = payOrderManager.findById(refundOrder.getOrderId())
                .orElseThrow(() -> new DataNotExistException("退款订单关联支付订单不存在"));
        // 退款失败返还后的余额
        var payOrderAmount =  refundOrder
                .getAmount()
                .add(payOrder.getRefundableBalance());
        // 退款失败返还后的余额+可退余额 == 订单金额 支付订单回退为为未退款状态
        if (BigDecimalUtil.isEqual(payOrderAmount, payOrder.getAmount())) {
            payOrder.setRefundStatus(PayRefundStatusEnum.NO_REFUND.getCode());
        } else {
            // 回归部分退款状态
            payOrder.setRefundStatus(PayRefundStatusEnum.PARTIAL_REFUND.getCode());
        }

        // 更新支付订单相关的可退款金额
        payOrder.setRefundableBalance(payOrderAmount);
        refundOrder.setStatus(RefundStatusEnum.CLOSE.getCode());

        // 更新订单和退款相关订单
        payOrderManager.updateById(payOrder);
        refundOrderManager.updateById(refundOrder);
        // 发送通知
        merchantNoticeService.registerRefundNotice(refundOrder);
    }


    /**
     * 退款成功, 更新退款单和支付单
     */
    @Transactional(rollbackFor = Exception.class)
    public void success(RefundOrder refundOrder, LocalDateTime finishTime) {
        PayOrder payOrder = payOrderManager.findById(refundOrder.getOrderId())
                .orElseThrow(() -> new DataNotExistException("退款订单关联支付订单不存在"));

        // 订单相关状态
        PayRefundStatusEnum afterPayRefundStatus;

        // 判断订单全部退款还是部分退款
        if (BigDecimalUtil.isEqual(payOrder.getRefundableBalance(), BigDecimal.ZERO)) {
            afterPayRefundStatus = PayRefundStatusEnum.REFUNDED;
        } else {
            afterPayRefundStatus = PayRefundStatusEnum.PARTIAL_REFUND;
        }
        // 设置退款为完成状态和完成时间
        refundOrder.setStatus(RefundStatusEnum.SUCCESS.getCode())
                .setErrorMsg(null)
                .setFinishTime(finishTime);
        payOrder.setRefundStatus(afterPayRefundStatus.getCode());

        // 更新订单和退款相关订单
        payOrderManager.updateById(payOrder);
        refundOrderManager.updateById(refundOrder);

        // 记录流水
        tradeFlowRecordService.saveRefund(refundOrder);
        // 发送通知
        merchantNoticeService.registerRefundNotice(refundOrder);
    }

    /**
     * 根据退款订单信息构建出返回结果
     */
    public RefundResult buildResult(RefundOrder refundOrder){
        return new RefundResult()
                .setStatus(refundOrder.getStatus())
                .setRefundNo(refundOrder.getRefundNo())
                .setErrorMsg(refundOrder.getErrorMsg())
                .setBizRefundNo(refundOrder.getBizRefundNo());
    }
}
