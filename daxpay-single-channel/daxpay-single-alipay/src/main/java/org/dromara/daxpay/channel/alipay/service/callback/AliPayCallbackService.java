package org.dromara.daxpay.channel.alipay.service.callback;

import org.dromara.daxpay.channel.alipay.service.config.AliPayConfigService;
import org.dromara.daxpay.core.enums.CallbackStatusEnum;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.core.enums.PayStatusEnum;
import org.dromara.daxpay.core.enums.TradeTypeEnum;
import org.dromara.daxpay.core.util.PayUtil;
import org.dromara.daxpay.service.common.context.CallbackLocal;
import org.dromara.daxpay.service.common.local.PaymentContextLocal;
import org.dromara.daxpay.service.service.record.callback.TradeCallbackRecordService;
import org.dromara.daxpay.service.service.trade.pay.PayCallbackService;
import org.dromara.daxpay.service.service.trade.refund.RefundCallbackService;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

import static org.dromara.daxpay.channel.alipay.code.AliPayCode.*;

/**
 * 支付宝回调服务
 *
 * @author xxm
 * @since 2024/7/22
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AliPayCallbackService {

    private final AliPayConfigService aliPayConfigService;
    private final PayCallbackService payCallbackService;
    private final TradeCallbackRecordService tradeCallbackRecordService;
    private final RefundCallbackService refundCallbackService;

    /**
     * 回调处理
     */
    public String callbackHandle(HttpServletRequest request) {
        Map<String, String> callbackParam = PayUtil.toMap(request);

        // 判断为支付还是退款
        TradeTypeEnum tradeTypeEnum = this.resolveAndGetType(callbackParam);
        if (tradeTypeEnum == TradeTypeEnum.PAY){
            // 支付回调处理
            if (this.payHandler(callbackParam)){
                // 执行回调业务处理
                payCallbackService.payCallback();
                // 保存记录
                tradeCallbackRecordService.saveCallbackRecord();
                return "success";
            } else {
                // 保存记录
                tradeCallbackRecordService.saveCallbackRecord();
                return "fail";
            }
        } else {
            // 解析数据
            if (this.refundHandle(callbackParam)){
                // 执行退款回调处理
                refundCallbackService.refundCallback();
                // 保存记录
                tradeCallbackRecordService.saveCallbackRecord();
                return "success";
            } else {
                // 保存记录
                tradeCallbackRecordService.saveCallbackRecord();
                return "fail";
            }
        }
    }

    /**
     * 解析回调内容并返回类型
     */
    public TradeTypeEnum resolveAndGetType(Map<String, String> callbackParam){
        CallbackLocal callback = PaymentContextLocal.get().getCallbackInfo();
        callback.setCallbackData(callbackParam);
        // 通道和回调类型
        callback.setChannel(ChannelEnum.ALI.getCode());
        String refundFee = callbackParam.get(ResponseParams.REFUND_FEE);
        // 如果有退款金额，说明是退款回调
        if (StrUtil.isNotBlank(refundFee)){
            callback.setCallbackType(TradeTypeEnum.REFUND);
            return TradeTypeEnum.REFUND;
        } else {
            callback.setCallbackType(TradeTypeEnum.PAY);
            return TradeTypeEnum.PAY;
        }

    }

    /**
     * 支付回调处理, 解析数据
     */
    public boolean payHandler(Map<String, String> callbackParam) {
        CallbackLocal callback = PaymentContextLocal.get().getCallbackInfo();

        if (!aliPayConfigService.verifyNotify(callbackParam)) {
            log.error("支付宝回调报文验签失败");
            callback.setCallbackStatus(CallbackStatusEnum.FAIL).setCallbackErrorMsg("支付宝回调报文验签失败");
            return false;
        }

        // 网关订单号
        callback.setOutTradeNo(callbackParam.get(ResponseParams.TRADE_NO));
        // 支付订单ID
        callback.setTradeNo(callbackParam.get(ResponseParams.OUT_TRADE_NO));
        // 支付状态
        PayStatusEnum payStatus = Objects.equals(callbackParam.get(ResponseParams.TRADE_STATUS), PayStatus.TRADE_SUCCESS) ? PayStatusEnum.SUCCESS : PayStatusEnum.FAIL;
        callback.setTradeStatus(payStatus.getCode());
        // 支付金额
        String amountStr = callbackParam.get(ResponseParams.TOTAL_AMOUNT);
        callback.setAmount(new BigDecimal(amountStr));

        // 支付时间
        String gmpTime = callbackParam.get(ResponseParams.GMT_PAYMENT);
        if (StrUtil.isNotBlank(gmpTime)) {
            LocalDateTime time = LocalDateTimeUtil.parse(gmpTime, DatePattern.NORM_DATETIME_PATTERN);
            callback.setFinishTime(time);
        } else {
            callback.setFinishTime(LocalDateTime.now());
        }
        return true;
    }

    /**
     * 退款回调处理
     */
    public boolean refundHandle(Map<String, String> callbackParam ) {
        CallbackLocal callback = PaymentContextLocal.get().getCallbackInfo();
        // 验签
        if (!aliPayConfigService.verifyNotify(callbackParam)) {
            log.error("支付宝回调报文验签失败");
            callback.setCallbackStatus(CallbackStatusEnum.FAIL).setCallbackErrorMsg("支付宝回调报文验签失败");
            return false;
        }
        // 退款订单号
        callback.setTradeNo(callbackParam.get(ResponseParams.OUT_BIZ_NO));
        // 退款状态
        callback.setTradeStatus(callbackParam.get(ResponseParams.TRADE_STATUS));
        // 退款金额
        callback.setAmount(new BigDecimal(callbackParam.get(ResponseParams.REFUND_FEE)));

        // 退款时间
        String gmpTime = callbackParam.get(ResponseParams.GMT_REFUND);
        if (StrUtil.isNotBlank(gmpTime)) {
            LocalDateTime time = LocalDateTimeUtil.parse(gmpTime, DatePattern.NORM_DATETIME_PATTERN);
            callback.setFinishTime(time);
        } else {
            callback.setFinishTime(LocalDateTime.now());
        }
        return true;
    }
}
