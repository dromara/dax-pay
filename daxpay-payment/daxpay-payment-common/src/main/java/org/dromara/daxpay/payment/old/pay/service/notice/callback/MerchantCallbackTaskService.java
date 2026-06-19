package org.dromara.daxpay.payment.old.pay.service.notice.callback;

import org.dromara.daxpay.platform.common.json.util.JacksonUtil;
import org.dromara.daxpay.platform.core.exception.DataNotExistException;
import org.dromara.daxpay.payment.old.pay.convert.order.pay.PayOrderConvert;
import org.dromara.daxpay.payment.old.pay.dao.notice.callback.MerchantCallbackTaskManager;
import org.dromara.daxpay.payment.old.pay.dao.order.pay.PayOrderExpandManager;
import org.dromara.daxpay.payment.old.pay.entity.notice.callback.MerchantCallbackTask;
import org.dromara.daxpay.payment.old.pay.entity.order.pay.PayOrder;
import org.dromara.daxpay.platform.core.enums.pay.notice.CallbackNoticeTypeEnum;
import org.dromara.daxpay.platform.core.enums.pay.trade.TradeTypeEnum;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 商户回调消息服务类
///
@Slf4j
@Service
@RequiredArgsConstructor
public class MerchantCallbackTaskService {
    private final MerchantCallbackTaskManager taskManager;
    private final PayOrderExpandManager payOrderExpandManager;
//    private final DelayJobService delayJobService;

    /// 注册支付回调通知
    public void registerPayNotice(PayOrder order) {
        var orderExpand = payOrderExpandManager.findById(order.getId())
                .orElseThrow(DataNotExistException::new);
        // 判断是否需要进行通知
        if (StrUtil.isBlank(orderExpand.getNotifyUrl())){
            log.info("支付订单无需回调，订单号：{}",order.getOrderNo());
            return;
        }
        var noticeResult = PayOrderConvert.CONVERT.toResult(order);
        PayOrderConvert.CONVERT.copy(orderExpand, noticeResult);
        var task = new MerchantCallbackTask()
                // 时间序列化进行了重写, 所以使用Jackson的序列化工具类
                .setContent(JacksonUtil.toJson(noticeResult))
                .setNoticeType(CallbackNoticeTypeEnum.SYSTEM.getCode())
                .setTradeType(TradeTypeEnum.PAY.getCode())
                .setUrl(orderExpand.getNotifyUrl())
                .setSendCount(0)
                .setDelayCount(0)
                .setTradeId(order.getId())
                .setTradeNo(order.getOrderNo());
        taskManager.save(task);
//        delayJobService.registerByTransaction(task.getId(), DaxPayCode.Event.MERCHANT_CALLBACK_SENDER, 0);
        log.info("注册支付回调通知");
    }

}
