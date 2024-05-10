package cn.daxpay.single.service.core.payment.notice.service;

import cn.bootx.platform.common.jackson.util.JacksonUtil;
import cn.daxpay.single.service.code.ClientNoticeTypeEnum;
import cn.daxpay.single.service.core.order.pay.entity.PayOrder;
import cn.daxpay.single.service.core.order.pay.entity.PayOrderExtra;
import cn.daxpay.single.service.core.order.refund.entity.RefundOrder;
import cn.daxpay.single.service.core.order.refund.entity.RefundOrderExtra;
import cn.daxpay.single.service.core.payment.common.service.PaymentAssistService;
import cn.daxpay.single.service.core.payment.common.service.PaymentSignService;
import cn.daxpay.single.service.core.payment.notice.result.PayNoticeResult;
import cn.daxpay.single.service.core.payment.notice.result.RefundNoticeResult;
import cn.daxpay.single.service.core.task.notice.entity.ClientNoticeTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 客户系统消息通知任务支撑服务
 * @author xxm
 * @since 2024/2/25
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ClientNoticeAssistService {

    private final PaymentSignService paymentSignService;

    private final PaymentAssistService paymentAssistService;

    /**
     * 构建出支付通知任务对象
     */
    public ClientNoticeTask buildPayTask(PayOrder order, PayOrderExtra orderExtra){
        // 获取系统签名
        paymentAssistService.initPlatform();
        PayNoticeResult payNoticeResult = new PayNoticeResult()
                .setOrderNo(order.getOrderNo())
                .setBizOrderNo(order.getBizOrderNo())
                .setTitle(order.getTitle())
                .setChannel(order.getChannel())
                .setMethod(order.getMethod())
                .setAmount(order.getAmount())
                .setPayTime(order.getPayTime())
                .setCloseTime(order.getCloseTime())
                .setCreateTime(order.getCreateTime())
                .setStatus(order.getStatus())
                .setAttach(orderExtra.getAttach());

        paymentSignService.sign(payNoticeResult);
        return new ClientNoticeTask()
                .setUrl(orderExtra.getNotifyUrl())
                // 时间序列化进行了重写, 所以使用Jackson的序列化工具类
                .setContent(JacksonUtil.toJson(payNoticeResult))
                .setNoticeType(ClientNoticeTypeEnum.PAY.getType())
                .setSendCount(0)
                .setTradeId(order.getId())
                .setTradeNo(order.getOrderNo())
                .setTradeStatus(order.getStatus());
    }

    /**
     * 构建出退款通知任务对象
     */
    public ClientNoticeTask buildRefundTask(RefundOrder order, RefundOrderExtra orderExtra){
        // 获取系统签名
        paymentAssistService.initPlatform();
        // 创建退款通知内容
        RefundNoticeResult payNoticeResult = new RefundNoticeResult()
                .setRefundNo(order.getRefundNo())
                .setBizRefundNo(order.getBizRefundNo())
                .setChannel(order.getChannel())
                .setAmount(order.getAmount())
                .setFinishTime(order.getFinishTime())
                .setCreateTime(order.getCreateTime())
                .setStatus(order.getStatus())
                .setAttach(orderExtra.getAttach());

        // 签名
        paymentSignService.sign(payNoticeResult);
        return new ClientNoticeTask()
                .setUrl(orderExtra.getNotifyUrl())
                // 时间序列化进行了重写
                .setContent(JacksonUtil.toJson(payNoticeResult))
                .setNoticeType(ClientNoticeTypeEnum.REFUND.getType())
                .setSendCount(0)
                .setTradeId(order.getId())
                .setTradeNo(order.getRefundNo())
                .setTradeStatus(order.getStatus());
    }

}
