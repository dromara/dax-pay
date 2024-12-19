package org.dromara.daxpay.service.service.notice.notify;

import cn.bootx.platform.common.jackson.util.JacksonUtil;
import cn.bootx.platform.starter.redis.delay.service.DelayJobService;
import org.dromara.daxpay.core.enums.MerchantNotifyTypeEnum;
import org.dromara.daxpay.core.result.allocation.order.AllocDetailResult;
import org.dromara.daxpay.service.code.DaxPayCode;
import org.dromara.daxpay.service.common.context.MchAppLocal;
import org.dromara.daxpay.service.common.local.PaymentContextLocal;
import org.dromara.daxpay.service.convert.allocation.AllocOrderConvert;
import org.dromara.daxpay.service.convert.order.pay.PayOrderConvert;
import org.dromara.daxpay.service.convert.order.refund.RefundOrderConvert;
import org.dromara.daxpay.service.convert.order.transfer.TransferOrderConvert;
import org.dromara.daxpay.service.dao.notice.notify.MerchantNotifyTaskManager;
import org.dromara.daxpay.service.entity.allocation.order.AllocDetail;
import org.dromara.daxpay.service.entity.allocation.order.AllocOrder;
import org.dromara.daxpay.service.entity.notice.notify.MerchantNotifyTask;
import org.dromara.daxpay.service.entity.order.pay.PayOrder;
import org.dromara.daxpay.service.entity.order.refund.RefundOrder;
import org.dromara.daxpay.service.entity.order.transfer.TransferOrder;
import org.dromara.daxpay.service.enums.NotifyContentTypeEnum;
import org.dromara.daxpay.service.service.config.MerchantNotifyConfigService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 商户订阅消息通知服务类
 * @author xxm
 * @since 2024/7/30
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MerchantNotifyTaskService {

    private final MerchantNotifyTaskManager taskManager;

    private final MerchantNotifyConfigService notifyConfigService;

    private final DelayJobService delayJobService;

    /**
     * 注册支付通知
     */
    public void registerPayNotice(PayOrder order) {
        if (this.nonRegister(NotifyContentTypeEnum.PAY)){
            log.info("支付通知无需回调，订单号：{}",order.getOrderNo());
            return;
        }
        var noticeResult = PayOrderConvert.CONVERT.toResult(order);
        var task = new MerchantNotifyTask()
                // 时间序列化进行了重写, 所以使用Jackson的序列化工具类
                .setContent(JacksonUtil.toJson(noticeResult))
                .setNotifyType(NotifyContentTypeEnum.PAY.getCode())
                .setSendCount(0)
                .setTradeId(order.getId())
                .setTradeNo(order.getOrderNo());
        taskManager.save(task);
        delayJobService.registerByTransaction(task.getId(), DaxPayCode.Event.MERCHANT_NOTIFY_SENDER, 0);
        log.info("注册支付通知");
    }

    /**
     * 注册退款通知
     */
    public void registerRefundNotice(RefundOrder order) {
        if (this.nonRegister(NotifyContentTypeEnum.REFUND)){
            log.info("支付退款无需回调，订单号：{}",order.getRefundNo());
            return;
        }
        var noticeResult = RefundOrderConvert.CONVERT.toResult(order);
        var task = new MerchantNotifyTask()
                // 时间序列化进行了重写, 所以使用Jackson的序列化工具类
                .setContent(JacksonUtil.toJson(noticeResult))
                .setNotifyType(NotifyContentTypeEnum.REFUND.getCode())
                .setSendCount(0)
                .setTradeId(order.getId())
                .setTradeNo(order.getRefundNo());
        taskManager.save(task);
        delayJobService.registerByTransaction(task.getId(), DaxPayCode.Event.MERCHANT_NOTIFY_SENDER, 0);
        log.info("注册退款通知");
    }

    /**
     * 注册转账通知
     */
    public void registerTransferNotice(TransferOrder order) {
        if (this.nonRegister(NotifyContentTypeEnum.TRANSFER)){
            log.info("转账无需回调，订单号：{}",order.getTransferNo());
            return;
        }
        var noticeResult = TransferOrderConvert.CONVERT.toResult(order);
        var task = new MerchantNotifyTask()
                // 时间序列化进行了重写, 所以使用Jackson的序列化工具类
                .setContent(JacksonUtil.toJson(noticeResult))
                .setNotifyType(NotifyContentTypeEnum.TRANSFER.getCode())
                .setSendCount(0)
                .setTradeId(order.getId())
                .setTradeNo(order.getTransferNo());
        taskManager.save(task);
        delayJobService.registerByTransaction(task.getId(), DaxPayCode.Event.MERCHANT_NOTIFY_SENDER, 0);
        log.info("注册转账通知");
    }

    /**
     * 注册分账通知
     */
    public void registerAllocNotice(AllocOrder order, List<AllocDetail> details) {
        if (this.nonRegister(NotifyContentTypeEnum.ALLOCATION)){
            log.info("分账无需回调，订单号：{}",order.getAllocNo());
            return;
        }
        var noticeResult = AllocOrderConvert.CONVERT.toResult(order);
        List<AllocDetailResult> detailResults = AllocOrderConvert.CONVERT.toList(details);
        noticeResult.setDetails(detailResults);
        var task = new MerchantNotifyTask()
                // 时间序列化进行了重写, 所以使用Jackson的序列化工具类
                .setContent(JacksonUtil.toJson(noticeResult))
                .setNotifyType(NotifyContentTypeEnum.ALLOCATION.getCode())
                .setSendCount(0)
                .setTradeId(order.getId())
                .setTradeNo(order.getAllocNo());
        taskManager.save(task);
        delayJobService.registerByTransaction(task.getId(), DaxPayCode.Event.MERCHANT_NOTIFY_SENDER, 0);
        log.info("注册分账通知");
    }

    /**
     * 判断是否 不需要注册通知
     * true 不需要
     * false 需要
     */
    private boolean nonRegister(NotifyContentTypeEnum notifyType) {
        MchAppLocal mchAppInfo = PaymentContextLocal.get().getMchAppInfo();

        // 判断是否开启消息通知功能, 不需要注册通知
        if (!Objects.equals(mchAppInfo.getNotifyType(), MerchantNotifyTypeEnum.HTTP.getCode())){
            return true;
        }

        // http方式且地址
        if (StrUtil.isBlank(mchAppInfo.getNotifyUrl())){
            return true;
        }
        // 判断是否订阅该类型的通知
        return !notifyConfigService.getSubscribeByAppIdAndType(mchAppInfo.getAppId(), notifyType.getCode());
    }

}
