package cn.daxpay.open.payment.trade.runtime.service.callback;

import cn.daxpay.open.payment.trade.runtime.bo.CallbackData;
import cn.daxpay.open.payment.trade.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.trade.enums.PayTradeTypeEnum;
import cn.daxpay.open.payment.trade.order.dao.GatewayPayOrderManager;
import cn.daxpay.open.payment.trade.order.dao.NormalPayOrderManager;
import cn.daxpay.open.payment.trade.order.dao.PayTradeManager;
import cn.daxpay.open.payment.trade.order.entity.GatewayPayOrder;
import cn.daxpay.open.payment.trade.order.entity.NormalPayOrder;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.trade.runtime.service.pay.PayUniHandleService;
import cn.daxpay.open.platform.core.enums.pay.notice.CallbackStatusEnum;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/// # 支付回调处理
///
/// 回调数据通过函数参数显式传递([CallbackData]),不依赖线程上下文。
/// 成功/失败均做终态守卫: 已 SUCCESS 幂等忽略; 非 PROCESSING 不可再流转。
@Slf4j
@Service
@RequiredArgsConstructor
public class PayCallbackService {

    private final PayTradeManager payTradeManager;
    private final NormalPayOrderManager payNormalOrderManager;
    private final GatewayPayOrderManager gatewayPayOrderManager;
    private final PayUniHandleService payUniHandleService;
    private final LockTemplate lockTemplate;

    /// 支付统一回调处理，返回支付产品编码
    @Transactional(rollbackFor = Exception.class)
    public String payCallback(CallbackData callbackData) {
        LockInfo lock = lockTemplate.lock("callback:payment:" + callbackData.getTradeNo(), 10000, 200);
        if (Objects.isNull(lock)) {
            callbackData.setCallbackStatus(CallbackStatusEnum.IGNORE)
                    .setCallbackErrorMsg("回调正在处理中，忽略本次回调请求");
            log.warn("订单号: {} 回调正在处理中，忽略本次回调请求", callbackData.getTradeNo());
            return null;
        }
        try {
            PayTrade trade = payTradeManager.findByTradeNo(callbackData.getTradeNo())
                    .orElse(null);
            if (Objects.isNull(trade)) {
                trade = payTradeManager.findByOutOrderNo(callbackData.getOutTradeNo())
                        .orElse(null);
            }
            if (Objects.isNull(trade)) {
                callbackData.setCallbackStatus(CallbackStatusEnum.NOT_FOUND)
                        .setCallbackErrorMsg("支付订单不存在");
                return null;
            }
            // 持锁后二次读取, 避免与关单/同步竞态使用过期状态
            trade = payTradeManager.findById(trade.getId()).orElse(trade);
            if (Objects.nonNull(callbackData.getOutTradeNo())) {
                trade.setOutOrderNo(callbackData.getOutTradeNo());
            }
            if (Objects.equals(CallbackStatusEnum.SUCCESS.getCode(), callbackData.getTradeStatus())) {
                this.success(trade, callbackData);
            } else {
                this.fail(trade, callbackData);
            }
            return resolveProduct(trade);
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /// 从容器获取 product(回调返回供通道策略识别来源)
    private String resolveProduct(PayTrade trade) {
        if (Objects.equals(trade.getTradeType(), PayTradeTypeEnum.GATEWAY.getCode())) {
            return gatewayPayOrderManager.findById(trade.getContainerId())
                    .map(GatewayPayOrder::getProduct).orElse(null);
        }
        return payNormalOrderManager.findById(trade.getContainerId())
                .map(NormalPayOrder::getProduct).orElse(null);
    }

    /// 支付成功: 仅 PROCESSING/INIT 可转入 SUCCESS; 已 SUCCESS 幂等忽略; 其他终态忽略
    private void success(PayTrade trade, CallbackData callbackData) {
        String status = trade.getStatus();
        if (Objects.equals(status, PayFundStatusEnum.SUCCESS.getCode())) {
            callbackData.setCallbackStatus(CallbackStatusEnum.IGNORE)
                    .setCallbackErrorMsg("支付单已是成功状态，忽略回调");
            log.info("支付回调: 交易 {} 已成功，幂等忽略", trade.getTradeNo());
            return;
        }
        if (!Objects.equals(status, PayFundStatusEnum.PROCESSING.getCode())
                && !Objects.equals(status, PayFundStatusEnum.INIT.getCode())) {
            callbackData.setCallbackStatus(CallbackStatusEnum.IGNORE)
                    .setCallbackErrorMsg("订单非支付中状态，忽略成功回调");
            log.warn("支付回调: 交易 {} 状态为 {}，忽略成功回调", trade.getTradeNo(), status);
            return;
        }
        trade.setStatus(PayFundStatusEnum.SUCCESS.getCode());
        trade.setPayTime(callbackData.getFinishTime());
        trade.setCloseTime(null);
        if (Objects.nonNull(callbackData.getOutTradeNo())) {
            trade.setOutOrderNo(callbackData.getOutTradeNo());
        }
        payUniHandleService.paySuccess(trade);
    }

    /// 支付失败: 仅 PROCESSING 可关失败; 与同步路径一致走 payFail(资金态 FAIL)
    private void fail(PayTrade trade, CallbackData callbackData) {
        if (!Objects.equals(trade.getStatus(), PayFundStatusEnum.PROCESSING.getCode())) {
            callbackData.setCallbackStatus(CallbackStatusEnum.IGNORE)
                    .setCallbackErrorMsg("订单不是支付中状态，忽略");
            return;
        }
        payUniHandleService.payFail(trade, callbackData.getTradeErrorMsg());
    }
}
