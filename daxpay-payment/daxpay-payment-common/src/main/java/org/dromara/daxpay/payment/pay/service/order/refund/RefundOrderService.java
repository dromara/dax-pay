package org.dromara.daxpay.payment.pay.service.order.refund;

import org.dromara.daxpay.platform.common.spring.util.WebServletUtil;
import org.dromara.daxpay.platform.core.exception.operation.OperationFailException;
import org.dromara.daxpay.platform.core.code.CommonCode;
import org.dromara.daxpay.platform.core.code.DaxPayErrorCode;
import org.dromara.daxpay.platform.core.code.CommonCode;
import org.dromara.daxpay.platform.core.exception.operation.OperationUnsupportedException;
import org.dromara.daxpay.payment.common.util.PayUtil;
import org.dromara.daxpay.platform.core.util.TradeNoGenerateUtil;
import org.dromara.daxpay.payment.common.service.MerchantPermissionService;

import org.dromara.daxpay.payment.pay.dao.order.pay.PayOrderManager;
import org.dromara.daxpay.payment.pay.dao.order.refund.RefundOrderManager;
import org.dromara.daxpay.payment.pay.entity.order.refund.RefundOrder;
import org.dromara.daxpay.platform.core.enums.pay.refund.RefundStatusEnum;
import org.dromara.daxpay.platform.core.enums.pay.trade.TradeSourceEnum;
import org.dromara.daxpay.payment.pay.exception.TradeNotExistException;
import org.dromara.daxpay.payment.pay.exception.TradeStatusErrorException;
import org.dromara.daxpay.payment.pay.param.order.refund.RefundCreateParam;
import org.dromara.daxpay.payment.pay.service.assist.PaymentAssistService;
import org.dromara.daxpay.payment.pay.service.trade.refund.RefundAssistService;
import org.dromara.daxpay.payment.pay.service.trade.refund.RefundService;
import org.dromara.daxpay.payment.pay.service.trade.refund.RefundSyncService;
import org.dromara.daxpay.payment.unipay.param.trade.refund.RefundParam;
import org.dromara.daxpay.payment.unipay.result.trade.refund.RefundResult;
import cn.hutool.extra.servlet.JakartaServletUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.Optional;

/// # 退款
///
@Slf4j
@Service
@RequiredArgsConstructor
public class RefundOrderService {

    private final PaymentAssistService paymentAssistService;

    private final PayOrderManager payOrderManager;

    private final RefundOrderManager refundOrderManager;

    private final RefundService refundService;

    private final RefundAssistService refundAssistService;

    private final RefundSyncService refundSyncService;


    private final MerchantPermissionService merchantPermissionService;

    /// 创建退款订单
    public RefundResult create(RefundCreateParam param) {
        // 检查是否有权限
        var payOrder = payOrderManager.findByOrderNo(param.getOrderNo())
                // 订单: 支付订单不存在
                .orElseThrow(() -> new TradeNotExistException("error.payment.order.payOrderNotExist"));

        // 初始化商户和应用
        paymentAssistService.initMchAndApp(payOrder.getMchNo(),payOrder.getAppId());

        String ip = Optional.ofNullable(WebServletUtil.getRequest())
                .map(JakartaServletUtil::getClientIP)
                .orElse("127.0.0.1");

        // 构建退款参数并发起
        var refundParam = new RefundParam();
        refundParam.setMchNo(payOrder.getMchNo());
        refundParam.setAppId(payOrder.getAppId());
        refundParam.setClientIp(ip);
        refundParam.setReqTime(OffsetDateTime.now(ZoneOffset.UTC));
        refundParam.setOrderNo(payOrder.getOrderNo());
        refundParam.setBizRefundNo("MANUAL_"+TradeNoGenerateUtil.refund());
        refundParam.setAmount(PayUtil.toDecimal(param.getAmount()));
        refundParam.setReason(param.getReason());
        refundParam.setSource(TradeSourceEnum.USER.getCode());
        return refundService.refund(refundParam);
    }

    /// 同步
    public void sync(Long id) {
        RefundOrder refundOrder = refundOrderManager.findById(id)
                // 订单: 退款订单不存在
                .orElseThrow(() -> new TradeNotExistException("error.payment.order.refundOrderNotExist"));
        // 初始化商户和应用
        paymentAssistService.initMchAndApp(refundOrder.getMchNo(),refundOrder.getAppId());
        // 同步退款订单状态
        refundSyncService.syncRefundOrder(refundOrder);
    }

    /// 退款重试
    public void retry(Long id) {
        RefundOrder refundOrder = refundOrderManager.findById(id)
                // 订单: 退款订单不存在
                .orElseThrow(() -> new TradeNotExistException("error.payment.order.refundOrderNotExist"));
        // 初始化商户和应用
        paymentAssistService.initMchAndApp(refundOrder.getMchNo(),refundOrder.getAppId());

        String ip = Optional.ofNullable(WebServletUtil.getRequest())
                .map(JakartaServletUtil::getClientIP)
                .orElse("127.0.0.1");

        // 构建退款参数并发起
        var refundParam = new RefundParam();
        refundParam.setMchNo(refundOrder.getMchNo());
        refundParam.setAppId(refundOrder.getAppId());
        refundParam.setClientIp(ip);
        refundParam.setReqTime(OffsetDateTime.now(ZoneOffset.UTC));
        refundParam.setOrderNo(refundOrder.getOrderNo());
        refundParam.setBizRefundNo(refundOrder.getBizRefundNo());
        refundParam.setAmount(PayUtil.toDecimal(refundOrder.getAmount()));
        // 发起退款
        refundParam.setSource(TradeSourceEnum.USER.getCode());
        refundService.refund(refundParam);
    }

    /// 退款关闭
    public void close(Long id) {
        RefundOrder refundOrder = refundOrderManager.findById(id)
                // 订单: 退款订单不存在
                .orElseThrow(() -> new TradeNotExistException("error.payment.order.refundOrderNotExist"));
        // 初始化商户和应用
        paymentAssistService.initMchAndApp(refundOrder.getMchNo(),refundOrder.getAppId());
        if (!Objects.equals(refundOrder.getStatus(), RefundStatusEnum.FAIL.getCode())) {
            // 只有失败状态的才可以关闭退款
            throw new TradeStatusErrorException(DaxPayErrorCode.TRADE_STATUS_ERROR, "pay.error.refund.onlyFailRetry");
        }
        // 关闭
        refundAssistService.close(refundOrder);
    }


}
