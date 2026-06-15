package org.dromara.daxpay.payment.pay.service.trade.refund;

import org.dromara.daxpay.platform.common.i18n.util.I18nUtil;
import org.dromara.daxpay.platform.core.exception.DataNotExistException;
import org.dromara.daxpay.platform.core.exception.ValidationFailedException;
import org.dromara.daxpay.platform.core.exception.BizInfoException;
import org.dromara.daxpay.platform.core.code.CommonErrorCode;
import org.dromara.daxpay.platform.core.code.DaxPayErrorCode;
import org.dromara.daxpay.platform.core.util.BigDecimalUtil;
import org.dromara.daxpay.platform.core.util.TradeNoGenerateUtil;
import org.dromara.daxpay.payment.pay.dao.order.pay.PayOrderManager;
import org.dromara.daxpay.payment.pay.dao.order.refund.RefundOrderManager;
import org.dromara.daxpay.payment.pay.entity.order.pay.PayOrder;
import org.dromara.daxpay.payment.pay.entity.order.refund.RefundOrder;
import org.dromara.daxpay.platform.core.enums.pay.pay.PayRefundStatusEnum;
import org.dromara.daxpay.platform.core.enums.pay.pay.PayStatusEnum;
import org.dromara.daxpay.platform.core.enums.pay.refund.RefundStatusEnum;
import org.dromara.daxpay.platform.core.enums.pay.trade.SettleStatusEnum;
import org.dromara.daxpay.payment.pay.exception.TradeStatusErrorException;
import org.dromara.daxpay.payment.pay.service.trade.TradeUniHandleService;
import org.dromara.daxpay.payment.unipay.param.trade.refund.RefundParam;
import org.dromara.daxpay.payment.unipay.result.trade.refund.RefundResult;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

/// # 支付退款支撑服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class RefundAssistService {
    private final RefundOrderManager refundOrderManager;
    private final PayOrderManager payOrderManager;
    private final TradeUniHandleService tradeUniHandleService;

    /// 检查并处理退款参数
    public void checkAndParam(RefundParam param, PayOrder payOrder){
        // 非支付完成的不能进行退款
        if (!Objects.equals(RefundStatusEnum.SUCCESS.getCode(), payOrder.getStatus())) {
            PayStatusEnum statusEnum = PayStatusEnum.findByCode(payOrder.getStatus());
            // 当前支付单状态不允许发起退款操作
            throw new TradeStatusErrorException(DaxPayErrorCode.TRADE_STATUS_ERROR, "pay.error.refund.statusNotAllow",
                    I18nUtil.getEnumName(statusEnum));
        }
        // 退款中和退款完成不能退款
        List<String> tradesStatus = List.of(
                PayRefundStatusEnum.REFUNDED.getCode(),
                PayRefundStatusEnum.REFUNDING.getCode());
        if (tradesStatus.contains(payOrder.getRefundStatus())){
            var statusEnum = PayRefundStatusEnum.findByCode(payOrder.getRefundStatus());
            // 当前支付单退款状态不允许发起退款操作
            throw new TradeStatusErrorException(DaxPayErrorCode.TRADE_STATUS_ERROR, "pay.error.refund.refundStatusNotAllow",
                    I18nUtil.getEnumName(statusEnum));
        }
        // 退款号唯一校验
        if (StrUtil.isNotBlank(param.getBizRefundNo()) && refundOrderManager.existsByRefundNo(param.getBizRefundNo())){
            // 退款单号已存在
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.refund.noDuplicate");
        }

        // 金额判断
        if (BigDecimalUtil.isGreaterThan(param.getAmount(),payOrder.getRefundableBalance())){
            // 退款金额不能大于支付金额
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.refund.amountExceed");
        }
    }

    /// 预先创建退款相关订单并保存, 使用新事务, 防止丢单
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
                .setProduct(payOrder.getProduct())
                .setProvider(payOrder.getProvider())
                .setStatus(RefundStatusEnum.PROGRESS.getCode())
                .setOrderAmount(payOrder.getAmount())
                .setAmount(refundParam.getAmount())
                .setTitle(payOrder.getTitle()+" - 退款")
                .setReason(refundParam.getReason())
                .setClientIp(refundParam.getClientIp())
                .setReqTime(refundParam.getReqTime())
                .setAttach(refundParam.getAttach())
                .setNotifyUrl(refundParam.getNotifyUrl());
        // 判断是否需要退分润, 订单结算状态不为空则进行设置
        if (Objects.nonNull(payOrder.getSettleStatus())){
            refundOrder.setSettleStatus(SettleStatusEnum.NOT_SETTLE.getCode());
        }

        refundOrderManager.save(refundOrder);
        return refundOrder;
    }

    /// 更新退款错误信息
    @Transactional(rollbackFor = Exception.class)
    public void updateOrderByError(RefundOrder refundOrder, String message){
        refundOrder.setErrorMsg(message);
        refundOrder.setStatus(RefundStatusEnum.FAIL.getCode());
        refundOrderManager.updateById(refundOrder);
    }

    /// 关闭退款单并将失败的退款金额归还回订单
    @Transactional(rollbackFor = Exception.class)
    public void close(RefundOrder refundOrder) {
        PayOrder payOrder = payOrderManager.findById(refundOrder.getOrderId())
                // 订单: 退款对应的支付订单不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.order.refundPayOrderNotExist"));
        tradeUniHandleService.refundClose(payOrder,refundOrder);
    }

    /// 退款成功, 更新退款单和支付单
    @Transactional(rollbackFor = Exception.class)
    public void success(RefundOrder refundOrder, OffsetDateTime finishTime) {
        PayOrder payOrder = payOrderManager.findById(refundOrder.getOrderId())
                // 订单: 退款对应的支付订单不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.order.refundPayOrderNotExist"));

        // 完成时间
        refundOrder.setFinishTime(finishTime);
        // 更新订单和退款相关订单
        tradeUniHandleService.refundSuccess(payOrder,refundOrder);
    }

    /// 根据退款订单信息构建出返回结果
    public RefundResult buildResult(RefundOrder refundOrder){
        return new RefundResult()
                .setStatus(refundOrder.getStatus())
                .setRefundNo(refundOrder.getRefundNo())
                .setErrorMsg(refundOrder.getErrorMsg())
                .setBizRefundNo(refundOrder.getBizRefundNo());
    }
}
