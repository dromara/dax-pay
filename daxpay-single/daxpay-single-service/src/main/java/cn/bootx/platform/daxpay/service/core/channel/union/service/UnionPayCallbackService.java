package cn.bootx.platform.daxpay.service.core.channel.union.service;

import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.code.RefundStatusEnum;
import cn.bootx.platform.daxpay.service.code.PaymentTypeEnum;
import cn.bootx.platform.daxpay.service.code.UnionPayCode;
import cn.bootx.platform.daxpay.service.common.context.CallbackLocal;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.channel.union.entity.UnionPayConfig;
import cn.bootx.platform.daxpay.service.func.AbsCallbackStrategy;
import cn.bootx.platform.daxpay.service.sdk.union.api.UnionPayKit;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.egzosn.pay.common.bean.NoticeParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

import static cn.bootx.platform.daxpay.service.code.UnionPayCode.*;

/**
 * 云闪付回调处理
 * @author xxm
 * @since 2024/3/7
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UnionPayCallbackService extends AbsCallbackStrategy {

    @Resource
    private UnionPayConfigService unionPayConfigService;

    /**
     * 验证信息格式
     */
    @Override
    public boolean verifyNotify() {
        CallbackLocal callbackInfo = PaymentContextLocal.get().getCallbackInfo();
        Map<String, String> params = callbackInfo.getCallbackParam();
        String callReq = JSONUtil.toJsonStr(params);
        log.info("云闪付发起回调 报文: {}", callReq);

        // 支付回调信息校验
        UnionPayConfig config = unionPayConfigService.getConfig();
        UnionPayKit unionPayKit = unionPayConfigService.initPayService(config);

        NoticeParams noticeParams = new NoticeParams();
        noticeParams.setBody(Collections.unmodifiableMap(params));
        return unionPayKit.verify(noticeParams);
    }

    /**
     * 判断类型 支付回调/退款回调
     *
     * @see PaymentTypeEnum
     */
    @Override
    public PaymentTypeEnum getCallbackType() {
        CallbackLocal callbackInfo = PaymentContextLocal.get().getCallbackInfo();
        Map<String, String> params = callbackInfo.getCallbackParam();
        String txnType = params.get(TXN_TYPE);
        if (UnionPayCode.TXN_TYPE_PAY.equals(txnType)){
            return PaymentTypeEnum.PAY;
        } else {
            return PaymentTypeEnum.REFUND;
        }
    }

    /**
     * 解析支付回调数据并放到上下文中
     */
    @Override
    public void resolvePayData() {
        CallbackLocal callbackInfo = PaymentContextLocal.get().getCallbackInfo();
        Map<String, String> callbackParam = callbackInfo.getCallbackParam();

        // 网关订单号
        callbackInfo.setGatewayOrderNo(callbackParam.get(QUERY_ID));
        // 支付订单ID
        callbackInfo.setOrderId(Long.valueOf(callbackParam.get(ORDER_ID)));
        // 支付结果
        String resultCode = callbackParam.get(UnionPayCode.RESP_CODE);
        PayStatusEnum payStatus = UnionPayCode.RESP_SUCCESS.equals(resultCode) ? PayStatusEnum.SUCCESS : PayStatusEnum.FAIL;

        callbackInfo.setGatewayStatus(payStatus.getCode());
        // 支付金额
        callbackInfo.setAmount(callbackParam.get(UnionPayCode.TXN_AMT));
        String timeEnd = callbackParam.get(TXN_TIME);
        if (StrUtil.isNotBlank(timeEnd)) {
            LocalDateTime time = LocalDateTimeUtil.parse(timeEnd, DatePattern.PURE_DATETIME_PATTERN);
            callbackInfo.setFinishTime(time);
        } else {
            callbackInfo.setFinishTime(LocalDateTime.now());
        }
    }

    /**
     * 解析退款回调数据并放到上下文中
     */
    @Override
    public void resolveRefundData() {
        // 云闪付需要延迟半秒再进行处理, 不然会出现业务未处理完, 但回调已经到达的情况
        ThreadUtil.sleep(100);

        CallbackLocal callbackInfo = PaymentContextLocal.get().getCallbackInfo();
        Map<String, String> callbackParam = callbackInfo.getCallbackParam();
        // 网关订单号
        callbackInfo.setGatewayOrderNo(callbackParam.get(QUERY_ID));
        // 退款订单Id
        callbackInfo.setOrderId(Long.valueOf(callbackParam.get(ORDER_ID)));
        // 退款金额
        callbackInfo.setAmount(callbackParam.get(TXN_AMT));

        // 交易状态
        String resultCode = callbackParam.get(UnionPayCode.RESP_CODE);
        RefundStatusEnum refundStatus = UnionPayCode.RESP_SUCCESS.equals(resultCode) ? RefundStatusEnum.SUCCESS : RefundStatusEnum.FAIL;
        callbackInfo.setGatewayStatus(refundStatus.getCode());

        // 退款时间
        String timeEnd = callbackParam.get(TXN_TIME);
        if (StrUtil.isNotBlank(timeEnd)) {
            LocalDateTime time = LocalDateTimeUtil.parse(timeEnd, DatePattern.PURE_DATETIME_PATTERN);
            callbackInfo.setFinishTime(time);
        } else {
            callbackInfo.setFinishTime(LocalDateTime.now());
        }

    }

    /**
     * 返回响应结果
     */
    @Override
    public String getReturnMsg() {
        return "success";
    }

    /**
     * 策略标识
     */
    @Override
    public PayChannelEnum getChannel() {
        return PayChannelEnum.UNION_PAY;
    }
}
