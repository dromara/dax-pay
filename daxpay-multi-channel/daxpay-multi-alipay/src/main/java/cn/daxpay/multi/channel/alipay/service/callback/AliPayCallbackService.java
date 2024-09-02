package cn.daxpay.multi.channel.alipay.service.callback;

import cn.daxpay.multi.channel.alipay.service.config.AliPayConfigService;
import cn.daxpay.multi.core.enums.CallbackStatusEnum;
import cn.daxpay.multi.core.enums.ChannelEnum;
import cn.daxpay.multi.core.enums.PayStatusEnum;
import cn.daxpay.multi.core.enums.TradeTypeEnum;
import cn.daxpay.multi.core.util.PayUtil;
import cn.daxpay.multi.service.common.context.CallbackLocal;
import cn.daxpay.multi.service.common.local.PaymentContextLocal;
import cn.daxpay.multi.service.service.record.callback.TradeCallbackRecordService;
import cn.daxpay.multi.service.service.trade.pay.PayCallbackService;
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

import static cn.daxpay.multi.channel.alipay.code.AliPayCode.*;

/**
 * 支付宝回调服务
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

    /**
     * 支付回调处理
     */
    public String payHandle(HttpServletRequest request) {
        // 解析数据
        if (this.resolve(request)){
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
    }

    /**
     * 支付回调处理, 解析数据
     */
    public boolean resolve(HttpServletRequest request) {
        CallbackLocal callback = PaymentContextLocal.get().getCallbackInfo();
        Map<String, String> callbackParam = PayUtil.toMap(request);
        callback.setCallbackData(callbackParam);

        // 通道和回调类型
        callback.setChannel(ChannelEnum.ALI.getCode()).setCallbackType(TradeTypeEnum.PAY);

        // 验签
        if (!aliPayConfigService.verifyNotify(callbackParam)) {
            log.error("支付宝回调报文验签失败");
            callback.setCallbackStatus(CallbackStatusEnum.FAIL).setCallbackErrorMsg("支付宝回调报文验签失败");
            return false;
        }

        // 网关订单号
        callback.setOutTradeNo(callbackParam.get(TRADE_NO));
        // 支付订单ID
        callback.setTradeNo(callbackParam.get(OUT_TRADE_NO));
        // 支付状态
        PayStatusEnum payStatus = Objects.equals(callbackParam.get(TRADE_STATUS), PayStatus.TRADE_SUCCESS) ? PayStatusEnum.SUCCESS : PayStatusEnum.FAIL;
        callback.setTradeStatus(payStatus.getCode());
        // 支付金额
        String amountStr = callbackParam.get(TOTAL_AMOUNT);
        callback.setAmount(new BigDecimal(amountStr));

        // 支付时间
        String gmpTime = callbackParam.get(GMT_PAYMENT);
        if (StrUtil.isNotBlank(gmpTime)) {
            LocalDateTime time = LocalDateTimeUtil.parse(gmpTime, DatePattern.NORM_DATETIME_PATTERN);
            callback.setFinishTime(time);
        } else {
            callback.setFinishTime(LocalDateTime.now());
        }
        return true;
    }
}
