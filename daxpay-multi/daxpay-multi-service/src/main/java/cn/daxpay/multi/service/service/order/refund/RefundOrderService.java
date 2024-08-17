package cn.daxpay.multi.service.service.order.refund;

import cn.bootx.platform.common.spring.util.WebServletUtil;
import cn.daxpay.multi.core.exception.TradeNotExistException;
import cn.daxpay.multi.core.param.trade.refund.RefundParam;
import cn.daxpay.multi.core.util.TradeNoGenerateUtil;
import cn.daxpay.multi.service.dao.order.pay.PayOrderManager;
import cn.daxpay.multi.service.dao.order.refund.RefundOrderManager;
import cn.daxpay.multi.service.entity.order.refund.RefundOrder;
import cn.daxpay.multi.service.param.order.refund.RefundCreateParam;
import cn.daxpay.multi.service.service.assist.PaymentAssistService;
import cn.daxpay.multi.service.service.trade.refund.RefundAssistService;
import cn.daxpay.multi.service.service.trade.refund.RefundService;
import cn.daxpay.multi.service.service.trade.refund.RefundSyncService;
import cn.hutool.extra.servlet.JakartaServletUtil;
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

    private final PaymentAssistService paymentAssistService;

    private final PayOrderManager payOrderManager;

    private final RefundOrderManager refundOrderManager;

    private final RefundService refundService;

    private final RefundAssistService refundAssistService;

    private final RefundSyncService refundSyncService;

    /**
     * 创建退款订单
     */
    public void create(RefundCreateParam param) {

        var payOrder = payOrderManager.findByOrderNo(param.getOrderNo())
                .orElseThrow(() -> new TradeNotExistException("支付订单不存在"));

        // 初始化商户和应用
        paymentAssistService.initMchAndApp(payOrder.getMchNo(),payOrder.getAppId());

        String ip = Optional.ofNullable(WebServletUtil.getRequest())
                .map(JakartaServletUtil::getClientIP)
                .orElse("未知");

        // 构建退款参数并发起
        var refundParam = new RefundParam();
        refundParam.setMchNo(payOrder.getMchNo());
        refundParam.setAppId(payOrder.getAppId());
        refundParam.setClientIp(ip);
        refundParam.setReqTime(LocalDateTime.now());
        refundParam.setOrderNo(payOrder.getOrderNo());
        refundParam.setBizRefundNo("MANUAL_"+TradeNoGenerateUtil.refund());
        refundParam.setAmount(param.getAmount());
        refundService.refund(refundParam);
    }

    /**
     * 同步
     */
    public void sync(Long id) {
        RefundOrder refundOrder = refundOrderManager.findById(id)
                .orElseThrow(() -> new TradeNotExistException("退款订单不存在"));
        // 初始化商户和应用
        paymentAssistService.initMchAndApp(refundOrder.getMchNo(),refundOrder.getAppId());
        // 同步退款订单状态
        refundSyncService.syncRefundOrder(refundOrder);
    }

    /**
     * 退款重试
     */
    public void retry(Long id) {
        RefundOrder refundOrder = refundOrderManager.findById(id)
                .orElseThrow(() -> new TradeNotExistException("退款订单不存在"));
        // 初始化商户和应用
        paymentAssistService.initMchAndApp(refundOrder.getMchNo(),refundOrder.getAppId());

        String ip = Optional.ofNullable(WebServletUtil.getRequest())
                .map(JakartaServletUtil::getClientIP)
                .orElse("未知");

        // 构建退款参数并发起
        var refundParam = new RefundParam();
        refundParam.setMchNo(refundOrder.getMchNo());
        refundParam.setAppId(refundOrder.getAppId());
        refundParam.setClientIp(ip);
        refundParam.setReqTime(LocalDateTime.now());
        refundParam.setOrderNo(refundOrder.getOrderNo());
        refundParam.setBizRefundNo(refundOrder.getBizRefundNo());
        refundParam.setAmount(refundOrder.getAmount());
        // 发起退款
        refundService.refund(refundParam);
    }

    /**
     * 退款关闭
     */
    public void close(Long id) {
        RefundOrder refundOrder = refundOrderManager.findById(id)
                .orElseThrow(() -> new TradeNotExistException("退款订单不存在"));
        // 初始化商户和应用
        paymentAssistService.initMchAndApp(refundOrder.getMchNo(),refundOrder.getAppId());
        // 更新订单状态
        refundAssistService.close(refundOrder);
    }
}
