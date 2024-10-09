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
import org.dromara.daxpay.core.enums.PayStatusEnum;
import org.dromara.daxpay.core.enums.TradeTypeEnum;
import org.dromara.daxpay.core.util.PayUtil;
import org.dromara.daxpay.service.common.context.CallbackLocal;
import org.dromara.daxpay.service.common.local.PaymentContextLocal;
import org.dromara.daxpay.service.service.record.callback.TradeCallbackRecordService;
import org.dromara.daxpay.service.service.trade.pay.PayCallbackService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 云闪付支付回调
 * @author xxm
 * @since 2024/10/9
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UnionPayCallbackService {

    private final PayCallbackService payCallbackService;
    private final TradeCallbackRecordService tradeCallbackRecordService;
    private final UnionPayConfigService unionPayConfigService;

    /**
     * 支付回调处理
     */
    public String payHandle(HttpServletRequest request){
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
     * 解析支付回调数据并放到上下文中
     */
    @SneakyThrows
    public boolean resolve(HttpServletRequest request) {
        Map<String, String> callbackParam = PayUtil.toMap(request);
        CallbackLocal callbackInfo = PaymentContextLocal.get().getCallbackInfo();
        // 设置类型和通道
        callbackInfo.setCallbackType(TradeTypeEnum.PAY)
                .setChannel(ChannelEnum.UNION_PAY.getCode())
                .setCallbackData(callbackParam);
        // 签名校验
        UnionPayKit unionPayKit = unionPayConfigService.initPayKit();
        boolean verify = unionPayKit.signVerify(callbackParam, callbackParam.get(SDKConstants.param_signature));
        if (!verify){
            callbackInfo.setCallbackStatus(CallbackStatusEnum.FAIL);
            return false;
        }
        // 网关支付号
        callbackInfo.setOutTradeNo(callbackParam.get(UnionPayCode.QUERY_ID));
        // 支付号
        callbackInfo.setTradeNo(callbackParam.get(UnionPayCode.ORDER_ID));
        // 支付结果
        String resultCode = callbackParam.get(UnionPayCode.RESP_CODE);
        PayStatusEnum payStatus = UnionPayCode.RESP_SUCCESS.equals(resultCode) ? PayStatusEnum.SUCCESS : PayStatusEnum.FAIL;
        // 支付状态
        callbackInfo.setTradeStatus(payStatus.getCode());
        // 支付金额
        String amount = callbackParam.get(UnionPayCode.TXN_AMT);
        callbackInfo.setAmount(PayUtil.conversionAmount(Integer.parseInt(amount)));
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
