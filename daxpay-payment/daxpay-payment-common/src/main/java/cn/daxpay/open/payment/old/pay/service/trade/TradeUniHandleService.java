package cn.daxpay.open.payment.old.pay.service.trade;

import cn.daxpay.open.platform.core.annotation.IgnoreTenant;
import cn.daxpay.open.payment.old.pay.dao.order.pay.PayOrderExpandManager;
import cn.daxpay.open.payment.old.pay.dao.order.pay.PayOrderManager;
import cn.daxpay.open.payment.old.pay.entity.order.pay.PayOrder;
import cn.daxpay.open.payment.old.pay.entity.order.pay.PayOrderExpand;
import cn.daxpay.open.platform.core.enums.pay.pay.PayStatusEnum;
import cn.daxpay.open.payment.old.pay.service.notice.MerchantNoticeService;
import cn.daxpay.open.payment.old.pay.service.record.flow.TradeFlowRecordService;
import cn.daxpay.open.payment.old.pay.service.assist.PayPluginAssistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

/// # 交易统一处理服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class TradeUniHandleService {

    private final PayOrderManager payOrderManager;
    private final PayOrderExpandManager payOrderExpandManager;
    private final TradeFlowRecordService tradeFlowRecordService;
    private final MerchantNoticeService merchantNoticeService;
    private final PayPluginAssistService payPluginAssistService;

    /// 支付成功发起后处理
    public void payAfterHandel(PayOrder payOrder, PayOrderExpand orderExpand){
        // 订单更新
        payOrderManager.updateById(payOrder);
        payOrderExpandManager.updateById(orderExpand);
        // 如果支付完成进行统一处理相关逻辑
        if (Objects.equals(payOrder.getStatus(), PayStatusEnum.SUCCESS.getCode())){
            // 相关操作
            tradeFlowRecordService.savePay(payOrder);
            merchantNoticeService.registerPayNotice(payOrder);
            // 处理插件策略
            payPluginAssistService.paySuccess(payOrder, orderExpand);
        }

    }

    /// 支付失败处理
    @IgnoreTenant
    public void payFail(PayOrder payOrder, String errMsg){
        payOrder.setStatus(PayStatusEnum.FAIL.getCode())
                .setErrorMsg(errMsg)
                .setCloseTime(OffsetDateTime.now(ZoneOffset.UTC));
        payOrderManager.updateById(payOrder);
        merchantNoticeService.registerPayNotice(payOrder);
        // 处理插件策略
        payPluginAssistService.payFail(payOrder);
    }

    /// 支付关闭处理
    @IgnoreTenant
    public void payClose(PayOrder order, PayStatusEnum payStatusEnum){
        order.setStatus(payStatusEnum.getCode())
                .setCloseTime(OffsetDateTime.now(ZoneOffset.UTC));
        payOrderManager.updateById(order);
        // 发送通知
        merchantNoticeService.registerPayNotice(order);
        // 处理插件策略
        payPluginAssistService.payClose(order);
    }

}
