package org.dromara.daxpay.channel.union.service.callback;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.channel.union.code.UnionPayCode;
import org.dromara.daxpay.channel.union.sdk.api.UnionPayKit;
import org.dromara.daxpay.channel.union.sdk.bean.SDKConstants;
import org.dromara.daxpay.channel.union.service.config.UnionPayConfigService;
import org.dromara.daxpay.core.enums.CallbackStatusEnum;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.core.enums.RefundStatusEnum;
import org.dromara.daxpay.core.enums.TradeTypeEnum;
import org.dromara.daxpay.core.util.PayUtil;
import org.dromara.daxpay.service.common.context.CallbackLocal;
import org.dromara.daxpay.service.common.local.PaymentContextLocal;
import org.dromara.daxpay.service.service.record.callback.TradeCallbackRecordService;
import org.dromara.daxpay.service.service.trade.refund.RefundCallbackService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 云闪付退款回调
 * @author xxm
 * @since 2024/10/9
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UnionRefundCallbackService {

    private final RefundCallbackService refundCallbackService;
    private final TradeCallbackRecordService tradeCallbackRecordService;
    private final UnionPayConfigService unionPayConfigService;

    /**
     * 退款回调处理
     */
    public String refundHandle(HttpServletRequest request){
        // 解析数据
        if (this.resolve(request)){
            // 执行回调业务处理
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

    @SneakyThrows
    public boolean resolve(HttpServletRequest request) {
        Map<String, String> callbackParam = PayUtil.toMap(request);
        CallbackLocal callbackInfo = PaymentContextLocal.get().getCallbackInfo();
        // 设置类型和通道
        callbackInfo.setCallbackType(TradeTypeEnum.REFUND)
                .setChannel(ChannelEnum.UNION_PAY.getCode())
                .setCallbackData(callbackParam);

        // 签名校验
        UnionPayKit unionPayKit = unionPayConfigService.initPayKit();
        boolean verify = unionPayKit.signVerify(callbackParam, callbackParam.get(SDKConstants.param_signature));
        if (!verify){
            callbackInfo.setCallbackStatus(CallbackStatusEnum.FAIL);
            return false;
        }
        // 网关退款号
        callbackInfo.setOutTradeNo(callbackParam.get(UnionPayCode.QUERY_ID));
        // 退款订单号
        callbackInfo.setTradeNo(callbackParam.get(UnionPayCode.ORDER_ID));
        // 退款金额
        String amount = callbackParam.get(UnionPayCode.TXN_AMT);
        callbackInfo.setAmount(PayUtil.conversionAmount(Integer.parseInt(amount)));
        // 交易状态
        String resultCode = callbackParam.get(UnionPayCode.RESP_CODE);
        RefundStatusEnum refundStatus = UnionPayCode.RESP_SUCCESS.equals(resultCode) ? RefundStatusEnum.SUCCESS : RefundStatusEnum.FAIL;
        callbackInfo.setTradeStatus(refundStatus.getCode());

        // 退款时间
        String timeEnd = callbackParam.get(UnionPayCode.TXN_TIME);
        if (StrUtil.isNotBlank(timeEnd)) {
            LocalDateTime time = LocalDateTimeUtil.parse(timeEnd, DatePattern.PURE_DATETIME_PATTERN);
            callbackInfo.setFinishTime(time);
        } else {
            callbackInfo.setFinishTime(LocalDateTime.now());
        }
        return true;
    }
}
