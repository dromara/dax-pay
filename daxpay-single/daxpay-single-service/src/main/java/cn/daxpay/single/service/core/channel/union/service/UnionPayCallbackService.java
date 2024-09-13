package cn.daxpay.single.service.core.channel.union.service;

import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.daxpay.single.core.code.PayChannelEnum;
import cn.daxpay.single.core.code.PayStatusEnum;
import cn.daxpay.single.core.code.RefundStatusEnum;
import cn.daxpay.single.service.code.PayCallbackStatusEnum;
import cn.daxpay.single.service.code.TradeTypeEnum;
import cn.daxpay.single.service.code.UnionPayCode;
import cn.daxpay.single.service.common.context.CallbackLocal;
import cn.daxpay.single.service.common.local.PaymentContextLocal;
import cn.daxpay.single.service.core.channel.union.entity.UnionPayConfig;
import cn.daxpay.single.service.core.payment.callback.service.PayCallbackService;
import cn.daxpay.single.service.core.payment.callback.service.RefundCallbackService;
import cn.daxpay.single.service.core.record.callback.service.PayCallbackRecordService;
import cn.daxpay.single.service.sdk.union.api.UnionPayKit;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.egzosn.pay.common.bean.NoticeParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

import static cn.daxpay.single.service.code.UnionPayCode.*;

/**
 * 云闪付回调处理
 * @author xxm
 * @since 2024/3/7
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UnionPayCallbackService {

    private final UnionPayConfigService unionPayConfigService;

    private final PayCallbackRecordService callbackService;

    private final PayCallbackService payCallbackService;

    private final RefundCallbackService refundCallbackService;

    /**
     * 回调处理入口
     */
    public String callback(Map<String, String> params){
        CallbackLocal callbackInfo = PaymentContextLocal.get().getCallbackInfo();
        try {
            // 将参数写入到上下文中
            callbackInfo.getCallbackParam().putAll(params);

            // 判断并保存回调类型
            TradeTypeEnum callbackType = this.getCallbackType();
            callbackInfo.setCallbackType(callbackType)
                    .setChannel(PayChannelEnum.UNION_PAY.getCode());

            // 验证消息
            if (!this.verifyNotify()) {
                callbackInfo.setCallbackStatus(PayCallbackStatusEnum.FAIL).setErrorMsg("验证信息格式不通过");
                // 消息有问题, 保存记录并返回
                callbackService.saveCallbackRecord();
                return null;
            }
            if (callbackType == TradeTypeEnum.PAY){
                // 解析支付数据并放处理
                this.resolvePayData();
                payCallbackService.payCallback();
            } else {
                // 解析退款数据并放处理
                this.resolveRefundData();
                refundCallbackService.refundCallback();
            }
            callbackService.saveCallbackRecord();
            return this.getReturnMsg();
        } catch (Exception e) {
            log.error("回调处理失败", e);
            callbackInfo.setCallbackStatus(PayCallbackStatusEnum.FAIL).setErrorMsg("回调处理失败: "+e.getMessage());
            callbackService.saveCallbackRecord();
            throw e;
        }
    }

    /**
     * 验证信息格式
     */
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
     * @see TradeTypeEnum
     */
    public TradeTypeEnum getCallbackType() {
        CallbackLocal callbackInfo = PaymentContextLocal.get().getCallbackInfo();
        Map<String, String> params = callbackInfo.getCallbackParam();
        String txnType = params.get(TXN_TYPE);
        if (UnionPayCode.TXN_TYPE_PAY.equals(txnType)){
            return TradeTypeEnum.PAY;
        } else {
            return TradeTypeEnum.REFUND;
        }
    }

    /**
     * 解析支付回调数据并放到上下文中
     */
    public void resolvePayData() {
        CallbackLocal callbackInfo = PaymentContextLocal.get().getCallbackInfo();
        Map<String, String> callbackParam = callbackInfo.getCallbackParam();

        // 网关支付号
        callbackInfo.setOutTradeNo(callbackParam.get(QUERY_ID));
        // 支付号
        callbackInfo.setTradeNo(callbackParam.get(ORDER_ID));
        // 支付结果
        String resultCode = callbackParam.get(UnionPayCode.RESP_CODE);
        PayStatusEnum payStatus = UnionPayCode.RESP_SUCCESS.equals(resultCode) ? PayStatusEnum.SUCCESS : PayStatusEnum.FAIL;
        // 支付状态
        callbackInfo.setOutStatus(payStatus.getCode());
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
    public void resolveRefundData() {
        // 云闪付需要延迟半秒再进行处理, 不然会出现业务未处理完, 但回调已经到达的情况
        ThreadUtil.sleep(100);

        CallbackLocal callbackInfo = PaymentContextLocal.get().getCallbackInfo();
        Map<String, String> callbackParam = callbackInfo.getCallbackParam();
        // 网关退款号
        callbackInfo.setOutTradeNo(callbackParam.get(QUERY_ID));
        // 退款订单号
        callbackInfo.setTradeNo(callbackParam.get(ORDER_ID));
        // 退款金额
        callbackInfo.setAmount(callbackParam.get(TXN_AMT));
        // 交易状态
        String resultCode = callbackParam.get(UnionPayCode.RESP_CODE);
        RefundStatusEnum refundStatus = UnionPayCode.RESP_SUCCESS.equals(resultCode) ? RefundStatusEnum.SUCCESS : RefundStatusEnum.FAIL;
        callbackInfo.setOutStatus(refundStatus.getCode());

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
    public String getReturnMsg() {
        return "success";
    }

}
