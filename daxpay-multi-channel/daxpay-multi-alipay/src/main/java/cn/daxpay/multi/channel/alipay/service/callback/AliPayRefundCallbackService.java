package cn.daxpay.multi.channel.alipay.service.callback;

import cn.daxpay.multi.core.enums.ChannelEnum;
import cn.daxpay.multi.core.enums.TradeTypeEnum;
import cn.daxpay.multi.core.util.PayUtil;
import cn.daxpay.multi.service.common.context.CallbackLocal;
import cn.daxpay.multi.service.common.local.PaymentContextLocal;
import cn.daxpay.multi.service.service.notice.callback.RefundCallbackService;
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

import static cn.daxpay.multi.channel.alipay.code.AliPayCode.*;

/**
 * 支付宝退款回调
 * 退款存在退到银行卡场景时，收单会根据银行回执消息发送退款完成信息。仅当退款发起时，在query_options中传入：deposit_back_info时会发送。
 * 通常不会触发这个, 触发了也没什么需要处理的, 所以不进行任务处理, 仅做记录
 * @author xxm
 * @since 2024/7/23
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AliPayRefundCallbackService {
    private final RefundCallbackService refundCallbackService;


    /**
     * 支付回调处理
     */
    public String refund(HttpServletRequest request) {
        CallbackLocal callback = PaymentContextLocal.get().getCallbackInfo();
        Map<String, String> callbackParam = PayUtil.toMap(request);
        callback.setCallbackData(callbackParam);
        // 设置类型和通道
        callback.setCallbackType(TradeTypeEnum.REFUND)
                .setChannel(ChannelEnum.ALI.getCode());
        // 退款订单号
        callback.setTradeNo(callbackParam.get(OUT_BIZ_NO));
        // 退款状态
        callback.setOutStatus(callbackParam.get(TRADE_STATUS));
        // 退款金额
        callback.setAmount(new BigDecimal(callbackParam.get(REFUND_FEE)));

        // 退款时间
        String gmpTime = callbackParam.get(GMT_REFUND);
        if (StrUtil.isNotBlank(gmpTime)) {
            LocalDateTime time = LocalDateTimeUtil.parse(gmpTime, DatePattern.NORM_DATETIME_PATTERN);
            callback.setFinishTime(time);
        } else {
            callback.setFinishTime(LocalDateTime.now());
        }
        // 进行退款的处理
        refundCallbackService.refundCallback();
        return "success";
    }
}
