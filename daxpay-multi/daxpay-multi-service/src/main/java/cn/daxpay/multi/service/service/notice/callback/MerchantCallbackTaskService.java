package cn.daxpay.multi.service.service.notice.callback;

import cn.bootx.platform.common.jackson.util.JacksonUtil;
import cn.bootx.platform.common.redis.delay.service.DelayJobService;
import cn.daxpay.multi.core.enums.TradeTypeEnum;
import cn.daxpay.multi.service.code.DaxPayCode;
import cn.daxpay.multi.service.convert.order.pay.PayOrderConvert;
import cn.daxpay.multi.service.convert.order.refund.RefundOrderConvert;
import cn.daxpay.multi.service.convert.order.transfer.TransferOrderConvert;
import cn.daxpay.multi.service.dao.notice.callback.MerchantCallbackTaskManager;
import cn.daxpay.multi.service.entity.notice.callback.MerchantCallbackTask;
import cn.daxpay.multi.service.entity.order.pay.PayOrder;
import cn.daxpay.multi.service.entity.order.refund.RefundOrder;
import cn.daxpay.multi.service.entity.order.transfer.TransferOrder;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 商户回调消息服务类
 * @author xxm
 * @since 2024/7/30
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MerchantCallbackTaskService {
    private final MerchantCallbackTaskManager taskManager;

    private final DelayJobService delayJobService;

    /**
     * 注册支付通知
     */
    public void registerPayNotice(PayOrder order) {
        // 判断是否需要进行通知
        if (StrUtil.isBlank(order.getNotifyUrl())){
            log.info("支付订单无需通知，订单号：{}",order.getOrderNo());
            return;
        }
        var noticeResult = PayOrderConvert.CONVERT.toResult(order);
        var task = new MerchantCallbackTask()
                // 时间序列化进行了重写, 所以使用Jackson的序列化工具类
                .setContent(JacksonUtil.toJson(noticeResult))
                .setTradeType(TradeTypeEnum.PAY.getCode())
                .setUrl(order.getNotifyUrl())
                .setSendCount(0)
                .setTradeId(order.getId())
                .setTradeNo(order.getOrderNo());
        taskManager.save(task);
        delayJobService.registerByTransaction(task, DaxPayCode.Event.MERCHANT_NOTIFY_SENDER, 0);
        log.info("注册支付通知");
    }

    /**
     * 注册退款通知
     */
    public void registerRefundNotice(RefundOrder order) {
        // 判断是否需要进行通知
        if (StrUtil.isBlank(order.getNotifyUrl())){
            log.info("支付退款无需通知，订单号：{}",order.getRefundNo());
            return;
        }
        var noticeResult = RefundOrderConvert.CONVERT.toResult(order);
        var task = new MerchantCallbackTask()
                // 时间序列化进行了重写, 所以使用Jackson的序列化工具类
                .setContent(JacksonUtil.toJson(noticeResult))
                .setTradeType(TradeTypeEnum.REFUND.getCode())
                .setUrl(order.getNotifyUrl())
                .setSendCount(0)
                .setTradeId(order.getId())
                .setTradeNo(order.getRefundNo());
        taskManager.save(task);
        delayJobService.registerByTransaction(task, DaxPayCode.Event.MERCHANT_NOTIFY_SENDER, 0);
        log.info("注册退款通知");
    }

    /**
     * 注册转账通知
     */
    public void registerTransferNotice(TransferOrder order) {
        // 判断是否需要进行通知
        if (StrUtil.isBlank(order.getNotifyUrl())){
            log.info("转账订单无需通知，订单号：{}",order.getTransferNo());
            return;
        }
        var noticeResult = TransferOrderConvert.CONVERT.toResult(order);
        var task = new MerchantCallbackTask()
                // 时间序列化进行了重写, 所以使用Jackson的序列化工具类
                .setContent(JacksonUtil.toJson(noticeResult))
                .setTradeType(TradeTypeEnum.TRANSFER.getCode())
                .setUrl(order.getNotifyUrl())
                .setSendCount(0)
                .setTradeId(order.getId())
                .setTradeNo(order.getTransferNo());
        taskManager.save(task);
        delayJobService.registerByTransaction(task, DaxPayCode.Event.MERCHANT_NOTIFY_SENDER, 0);
        log.info("注册转账通知");
    }
}
