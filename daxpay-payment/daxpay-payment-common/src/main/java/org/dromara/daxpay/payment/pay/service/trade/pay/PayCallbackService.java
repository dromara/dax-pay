package org.dromara.daxpay.payment.pay.service.trade.pay;

import org.dromara.daxpay.platform.core.util.DateTimeUtil;
import org.dromara.daxpay.payment.common.context.PaymentContext;
import org.dromara.daxpay.payment.common.context.CallbackInfo;
import org.dromara.daxpay.platform.core.exception.system.DataErrorException;
import org.dromara.daxpay.payment.pay.convert.order.pay.PayOrderConvert;
import org.dromara.daxpay.payment.pay.dao.order.pay.PayOrderExpandManager;
import org.dromara.daxpay.payment.pay.dao.order.pay.PayOrderManager;
import org.dromara.daxpay.platform.common.i18n.util.I18nUtil;
import org.dromara.daxpay.payment.pay.entity.order.pay.PayOrder;
import org.dromara.daxpay.platform.core.enums.pay.notice.CallbackStatusEnum;
import org.dromara.daxpay.platform.core.enums.pay.pay.PayStatusEnum;
import org.dromara.daxpay.payment.pay.service.notice.MerchantNoticeService;
import org.dromara.daxpay.payment.pay.service.order.pay.PayOrderQueryService;
import org.dromara.daxpay.payment.pay.service.record.flow.TradeFlowRecordService;
import org.dromara.daxpay.payment.pay.service.trade.TradeUniHandleService;
import cn.hutool.core.util.StrUtil;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

/// # 支付回调处理
///
@Slf4j
@Service
@RequiredArgsConstructor
public class PayCallbackService {

    private final PayOrderQueryService payOrderQueryService;

    private final LockTemplate lockTemplate;

    private final PayOrderManager payOrderManager;

    private final TradeFlowRecordService tradeFlowRecordService;

    private final MerchantNoticeService merchantNoticeService;

    private final PayOrderExpandManager payOrderExpandManager;

    private final TradeUniHandleService tradeUniHandleService;

    private final PaymentContext apiContext;

    /// 支付统一回调处理, 返回支付产品编码
    @Transactional(rollbackFor = Exception.class)
    public String payCallback() {
        var callbackInfo = apiContext.getCallbackInfo();
        // 加锁
        LockInfo lock = lockTemplate.lock("callback:payment:" + callbackInfo.getTradeNo(),10000, 200);
        if (Objects.isNull(lock)){
            callbackInfo.setCallbackStatus(CallbackStatusEnum.IGNORE).setCallbackErrorMsg(I18nUtil.get("pay.error.callback.processing"));
            log.warn("订单号: {} 回调正在处理中，忽略本次回调请求", callbackInfo.getTradeNo());
            return null;
        }
        try {
            // 首先使用本地订单号进行查询
            PayOrder payOrder = payOrderQueryService.findByOrderNo(callbackInfo.getTradeNo(), apiContext.getTradeInfo().getAppId()).orElse(null);
            if (Objects.isNull(payOrder)){
                // 使用通道订单号获取支付单
                 payOrder = payOrderQueryService.findByOutOrderNo(callbackInfo.getOutTradeNo(), apiContext.getTradeInfo().getAppId()).orElse(null);
            }
            // 本地支付单不存在,记录回调记录, TODO 需要补单或进行退款
            if (Objects.isNull(payOrder)) {
                callbackInfo.setCallbackStatus(CallbackStatusEnum.NOT_FOUND).setCallbackErrorMsg(I18nUtil.get("pay.error.callback.payNotFound"));
                return null;
            }
            // 设置订单关联网关订单号
            if (StrUtil.isNotBlank(callbackInfo.getOutTradeNo())){
                payOrder.setOutOrderNo(callbackInfo.getOutTradeNo());
            }

            // 成功状态
            if (Objects.equals(CallbackStatusEnum.SUCCESS.getCode(), callbackInfo.getTradeStatus())) {
                // 支付成功处理
                this.success(payOrder);
            } else {
                // 失败状态
                this.fail(payOrder);
            }
            return payOrder.getProduct();
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /// 成功处理 将支付订单调整为支付成功状态
    private void success(PayOrder payOrder) {
        CallbackInfo callbackInfo = apiContext.getCallbackInfo();
        // 回调时间超出了支付单超时时间, 记录一下, 不做处理 TODO 考虑不全, 需要做退款or人工处理
        if (Objects.nonNull(payOrder.getExpiredTime())
                && DateTimeUtil.ge(OffsetDateTime.now(ZoneOffset.UTC), payOrder.getExpiredTime())) {
            callbackInfo.setCallbackStatus(CallbackStatusEnum.EXCEPTION).setCallbackErrorMsg(I18nUtil.get("pay.error.callback.timeout"));
            return;
        }
        // 支付单已经被支付,不需要重复处理
        if (Objects.equals(payOrder.getStatus(), PayStatusEnum.SUCCESS.getCode())) {
            callbackInfo.setCallbackStatus(CallbackStatusEnum.IGNORE).setCallbackErrorMsg(I18nUtil.get("pay.error.callback.payAlreadySuccess"));
            return;
        }
        // 支付单已被取消,记录回调记录 TODO 考虑不全, 需要做退款or人工处理
        if (!Objects.equals(payOrder.getStatus(), PayStatusEnum.PROGRESS.getCode())) {
            callbackInfo.setCallbackStatus(CallbackStatusEnum.EXCEPTION).setCallbackErrorMsg(I18nUtil.get("pay.error.callback.payNotWaiting"));
            return;
        }
        // 修改订单支付状态为成功
        payOrder.setStatus(PayStatusEnum.SUCCESS.getCode())
                .setPayTime(callbackInfo.getFinishTime())
                .setOutOrderNo(callbackInfo.getOutTradeNo())
                .setCloseTime(null);
        // 保存附加参数到订单
        var orderExpand = payOrderExpandManager.findById(payOrder.getId()).orElseThrow(() -> new DataErrorException("error.payment.order.payOrderExtNotExist"));
        PayOrderConvert.CONVERT.copy(callbackInfo,orderExpand);
        // 统一处理支付成功逻辑
        tradeUniHandleService.payAfterHandel(payOrder,orderExpand);
    }

    /// 失败处理, 使用调整策略将支付订单调整为关闭状态
    private void fail(PayOrder payOrder) {
        CallbackInfo callbackInfo = apiContext.getCallbackInfo();
        // payment已被取消,记录回调记录,后期处理 TODO 考虑不完善, 后续优化
        if (!Objects.equals(payOrder.getStatus(), PayStatusEnum.PROGRESS.getCode())) {
            callbackInfo.setCallbackStatus(CallbackStatusEnum.IGNORE).setCallbackErrorMsg(I18nUtil.get("pay.error.callback.payCancelled"));
            return;
        }
        // payment支付成功, 状态非法 TODO 考虑不完善, 后续优化
        if (!Objects.equals(payOrder.getStatus(), PayStatusEnum.SUCCESS.getCode())) {
            callbackInfo.setCallbackStatus(CallbackStatusEnum.EXCEPTION).setCallbackErrorMsg(I18nUtil.get("pay.error.callback.payStatusIllegal"));
            return;
        }
        // 执行支付关闭的调整逻辑
        tradeUniHandleService.payClose(payOrder, PayStatusEnum.CLOSE);
    }

}
