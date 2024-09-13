package cn.daxpay.single.service.core.channel.wechat.service;

import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.daxpay.single.core.code.PayChannelEnum;
import cn.daxpay.single.core.code.PayStatusEnum;
import cn.daxpay.single.core.code.RefundStatusEnum;
import cn.daxpay.single.service.code.PayCallbackStatusEnum;
import cn.daxpay.single.service.code.TradeTypeEnum;
import cn.daxpay.single.service.common.context.CallbackLocal;
import cn.daxpay.single.service.common.local.PaymentContextLocal;
import cn.daxpay.single.service.core.channel.wechat.entity.WeChatPayConfig;
import cn.daxpay.single.service.core.payment.callback.service.PayCallbackService;
import cn.daxpay.single.service.core.payment.callback.service.RefundCallbackService;
import cn.daxpay.single.service.core.record.callback.service.PayCallbackRecordService;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.ijpay.core.enums.SignType;
import com.ijpay.core.kit.WxPayKit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static cn.daxpay.single.service.code.WeChatPayCode.*;


/**
 * 微信支付回调
 *
 * @author xxm
 * @since 2021/6/21
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WeChatPayCallbackService {
    private final WeChatPayConfigService weChatPayConfigService;

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
                    .setChannel(PayChannelEnum.WECHAT.getCode());

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

    public boolean verifyNotify() {
        CallbackLocal callbackInfo = PaymentContextLocal.get().getCallbackInfo();
        Map<String, String> params = callbackInfo.getCallbackParam();
        String callReq = JSONUtil.toJsonStr(params);
        log.info("微信发起回调 报文: {}", callReq);
        String appId = params.get(APPID);

        if (StrUtil.isBlank(appId)) {
            log.warn("微信回调报文 appId 为空");
            return false;
        }

        // 退款回调不用进行校验
        TradeTypeEnum callbackType = callbackInfo.getCallbackType();
        if (callbackType == TradeTypeEnum.REFUND){
            return true;
        }
        // 支付回调信息校验
        WeChatPayConfig weChatPayConfig = weChatPayConfigService.getConfig();
        if (Objects.isNull(weChatPayConfig)) {
            log.warn("微信支付配置不存在");
            return false;
        }
        return WxPayKit.verifyNotify(params, weChatPayConfig.getApiKeyV2(), SignType.HMACSHA256, null);
    }

    /**
     * 解析支付数据放到上下文中
     */
    public void resolvePayData() {
        CallbackLocal callbackInfo = PaymentContextLocal.get().getCallbackInfo();
        Map<String, String> callbackParam = callbackInfo.getCallbackParam();

        // 网关支付号
        callbackInfo.setOutTradeNo(callbackParam.get(TRANSACTION_ID));
        // 支付号
        callbackInfo.setTradeNo(callbackParam.get(OUT_TRADE_NO));
        // 支付状态
        PayStatusEnum payStatus = WxPayKit.codeIsOk(callbackParam.get(RESULT_CODE)) ? PayStatusEnum.SUCCESS : PayStatusEnum.FAIL;
        callbackInfo.setOutStatus(payStatus.getCode());
        // 支付金额
        callbackInfo.setAmount(callbackParam.get(TOTAL_FEE));
        String timeEnd = callbackParam.get(TIME_END);
        if (StrUtil.isNotBlank(timeEnd)) {
            LocalDateTime time = LocalDateTimeUtil.parse(timeEnd, DatePattern.PURE_DATETIME_PATTERN);
            callbackInfo.setFinishTime(time);
        } else {
            callbackInfo.setFinishTime(LocalDateTime.now());
        }
    }

    /**
     * 解析退款回调数据并放到上下文中, 微信退款通知返回的数据需要进行解密
     */
    public void resolveRefundData() {
        CallbackLocal callbackInfo = PaymentContextLocal.get().getCallbackInfo();
        Map<String, String> callbackParam = callbackInfo.getCallbackParam();

        // 解密返回数据
        WeChatPayConfig weChatPayConfig = weChatPayConfigService.getConfig();
        String reqInfo = callbackParam.get(REQ_INFO);
        String decryptData = WxPayKit.decryptData(reqInfo,weChatPayConfig.getApiKeyV2());

        // 同时覆盖上下文原有的的回调内容
        callbackParam = WxPayKit.xmlToMap(decryptData);
        callbackInfo.setCallbackParam(callbackParam);
        // 网关订单号
        callbackInfo.setOutTradeNo(callbackParam.get(CALLBACK_REFUND_ID));
        // 退款订单Id
        callbackInfo.setTradeNo(callbackParam.get(CALLBACK_OUT_REFUND_NO));
        // 退款金额
        callbackInfo.setAmount(callbackParam.get(CALLBACK_REFUND_FEE));

        // 交易状态
        RefundStatusEnum refundStatus = Objects.equals(callbackParam.get(CALLBACK_REFUND_STATUS), REFUND_SUCCESS)
                ? RefundStatusEnum.SUCCESS : RefundStatusEnum.FAIL;
        callbackInfo.setOutStatus(refundStatus.getCode());

        // 退款时间
        String timeEnd = callbackParam.get(CALLBACK_SUCCESS_TIME);
        if (StrUtil.isNotBlank(timeEnd)) {
            LocalDateTime time = LocalDateTimeUtil.parse(timeEnd, DatePattern.NORM_DATETIME_PATTERN);
            callbackInfo.setFinishTime(time);
        } else {
            callbackInfo.setFinishTime(LocalDateTime.now());
        }
    }

    /**
     * 判断类型 支付回调/退款回调
     *
     * @see TradeTypeEnum
     */
    public TradeTypeEnum getCallbackType() {
        CallbackLocal callbackInfo = PaymentContextLocal.get().getCallbackInfo();
        Map<String, String> callbackParam = callbackInfo.getCallbackParam();
        if (StrUtil.isNotBlank(callbackParam.get(REQ_INFO))){
            return TradeTypeEnum.REFUND;
        } else {
            return TradeTypeEnum.PAY;
        }
    }

    /**
     * 返回响应结果
     */
    public String getReturnMsg() {
        Map<String, String> xml = new HashMap<>(4);
        xml.put(RETURN_CODE, "SUCCESS");
        xml.put(RETURN_MSG, "OK");
        return WxPayKit.toXml(xml);
    }
}
