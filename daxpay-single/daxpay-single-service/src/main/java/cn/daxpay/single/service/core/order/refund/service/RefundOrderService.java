package cn.daxpay.single.service.core.order.refund.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.spring.util.WebServletUtil;
import cn.daxpay.single.param.payment.refund.RefundParam;
import cn.daxpay.single.service.core.order.refund.dao.RefundOrderExtraManager;
import cn.daxpay.single.service.core.order.refund.dao.RefundOrderManager;
import cn.daxpay.single.service.core.order.refund.entity.RefundOrder;
import cn.daxpay.single.service.core.order.refund.entity.RefundOrderExtra;
import cn.daxpay.single.service.core.payment.common.service.PaymentAssistService;
import cn.daxpay.single.service.core.payment.refund.service.RefundService;
import cn.daxpay.single.service.param.order.PayOrderRefundParam;
import cn.daxpay.single.util.OrderNoGenerateUtil;
import cn.hutool.extra.servlet.ServletUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 退款
 *
 * @author xxm
 * @since 2022/3/2
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RefundOrderService {

    private final RefundService refundService;

    private final PaymentAssistService paymentAssistService;

    private final RefundOrderExtraManager refundOrderExtraManager;

    private final RefundOrderManager refundOrderManager;

    /**
     * 手动发起退款
     * 退款涉及到回调通知, 索所以需要手动初始化一下上下文
     */
    public void refund(PayOrderRefundParam param) {

        String ip = Optional.ofNullable(WebServletUtil.getRequest())
                .map(ServletUtil::getClientIP)
                .orElse("未知");

        RefundParam refundParam = new RefundParam();
        refundParam.setOrderNo(param.getOrderNo());
        refundParam.setBizRefundNo(OrderNoGenerateUtil.refund());
        refundParam.setAmount(param.getAmount());
        refundParam.setReason(param.getReason());
        refundParam.setReqTime(LocalDateTime.now());
        refundParam.setClientIp(ip);
        // 手动初始化上下文
        paymentAssistService.initRequest(refundParam);
        // 调用统一退款接口
        refundService.refund(refundParam);
    }

    /**
     * 重新退款
     */
    public void resetRefund(Long id){

        // 查询扩展信息
        RefundOrderExtra refundOrderExtra = refundOrderExtraManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("未找到退款订单"));

        // 查询扩展信息
        RefundOrder refundOrder = refundOrderManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("未找到退款订单"));

        String ip = Optional.ofNullable(WebServletUtil.getRequest())
                .map(ServletUtil::getClientIP)
                .orElse("未知");

        RefundParam refundParam = new RefundParam();
        refundParam.setBizRefundNo(refundOrder.getBizRefundNo());
        // 回调通知
        refundParam.setNotifyUrl(refundOrderExtra.getNotifyUrl());
        refundParam.setAttach(refundOrderExtra.getAttach());
        refundParam.setReqTime(LocalDateTime.now());
        refundParam.setClientIp(ip);

        // 手动初始化请求上下文
        paymentAssistService.initRequest(refundParam);
    }
}
