package cn.bootx.platform.daxpay.service.core.payment.notice.service;

import cn.bootx.platform.common.jackson.util.JacksonUtil;
import cn.bootx.platform.daxpay.code.PaySignTypeEnum;
import cn.bootx.platform.daxpay.service.code.ClientNoticeTypeEnum;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrderExtra;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.RefundChannelOrder;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.RefundOrder;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.RefundOrderExtra;
import cn.bootx.platform.daxpay.service.core.payment.notice.result.PayNoticeResult;
import cn.bootx.platform.daxpay.service.core.payment.notice.result.RefundChannelResult;
import cn.bootx.platform.daxpay.service.core.payment.notice.result.RefundNoticeResult;
import cn.bootx.platform.daxpay.service.core.system.config.entity.PlatformConfig;
import cn.bootx.platform.daxpay.service.core.system.config.service.PlatformConfigService;
import cn.bootx.platform.daxpay.service.core.task.notice.entity.ClientNoticeTask;
import cn.bootx.platform.daxpay.util.PaySignUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 客户系统消息通知任务支撑服务
 * @author xxm
 * @since 2024/2/25
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ClientNoticeAssistService {

    private final PlatformConfigService configService;


    /**
     * 构建出支付通知任务对象
     */
    public ClientNoticeTask buildPayTask(PayOrder order, PayOrderExtra orderExtra){

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

        PlatformConfig config = configService.getConfig();
        // 签名
        if (Objects.equals(config.getSignType(), PaySignTypeEnum.MD5.getCode())){
            payNoticeResult.setSign(PaySignUtil.md5Sign(payNoticeResult,config.getSignSecret()));
        } else {
            payNoticeResult.setSign(PaySignUtil.hmacSha256Sign(payNoticeResult,config.getSignSecret()));
        }
        return new ClientNoticeTask()
                .setUrl(orderExtra.getNotifyUrl())
                // 时间序列化进行了重写, 所以使用Jackson的序列化工具类
                .setContent(JacksonUtil.toJson(payNoticeResult))
                .setNoticeType(ClientNoticeTypeEnum.PAY.getType())
                .setSendCount(0)
                .setOrderId(order.getId())
                .setOrderStatus(order.getStatus());
    }

    /**
     * 构建出退款通知任务对象
     */
    public ClientNoticeTask buildRefundTask(RefundOrder order, RefundOrderExtra orderExtra, List<RefundChannelOrder> channelOrders){
        // 组装内容
        List<RefundChannelResult> channels = channelOrders.stream()
                .map(o->new RefundChannelResult().setChannel(o.getChannel()).setAmount(o.getAmount()))
                .collect(Collectors.toList());

        RefundNoticeResult payNoticeResult = new RefundNoticeResult()
                .setRefundId(order.getId())
                .setAsyncPay(order.isAsyncPay())
                .setAsyncChannel(order.getAsyncChannel())
                .setRefundNo(order.getRefundNo())
                .setAmount(order.getAmount())
                .setRefundTime(order.getRefundTime())
                .setCreateTime(order.getCreateTime())
                .setStatus(order.getStatus())
                .setAttach(orderExtra.getAttach())
                .setRefundChannels(channels);

        PlatformConfig config = configService.getConfig();
        // 是否需要签名
        if (orderExtra.isNoticeSign()){
            // 签名
            if (Objects.equals(config.getSignType(), PaySignTypeEnum.MD5.getCode())){
                payNoticeResult.setSign(PaySignUtil.md5Sign(payNoticeResult,config.getSignSecret()));
            } else {
                payNoticeResult.setSign(PaySignUtil.hmacSha256Sign(payNoticeResult,config.getSignSecret()));
            }
        }
        return new ClientNoticeTask()
                .setUrl(orderExtra.getNotifyUrl())
                // 时间序列化进行了重写
                .setContent(JacksonUtil.toJson(payNoticeResult))
                .setNoticeType(ClientNoticeTypeEnum.REFUND.getType())
                .setSendCount(0)
                .setOrderId(order.getId())
                .setOrderStatus(order.getStatus());
    }

}
