package cn.daxpay.single.service.core.channel.alipay.service;

import cn.bootx.platform.common.core.util.CertUtil;
import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.daxpay.single.core.code.PayChannelEnum;
import cn.daxpay.single.core.code.PayStatusEnum;
import cn.daxpay.single.service.code.PayCallbackStatusEnum;
import cn.daxpay.single.service.code.TradeTypeEnum;
import cn.daxpay.single.service.common.context.CallbackLocal;
import cn.daxpay.single.service.common.local.PaymentContextLocal;
import cn.daxpay.single.service.core.channel.alipay.entity.AliPayConfig;
import cn.daxpay.single.service.core.payment.callback.service.PayCallbackService;
import cn.daxpay.single.service.core.payment.callback.service.RefundCallbackService;
import cn.daxpay.single.service.core.record.callback.service.PayCallbackRecordService;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayConstants;
import com.alipay.api.internal.util.AlipaySignature;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

import static cn.daxpay.single.service.code.AliPayCode.*;

/**
 * 支付宝回调处理
 *
 * @author xxm
 * @since 2021/2/28
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AliPayCallbackService {

    private final AliPayConfigService aliPayConfigService;

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
                    .setChannel(PayChannelEnum.ALI.getCode());

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
     * 验证信息格式是否合法
     */
    public boolean verifyNotify() {
        Map<String, String> params =PaymentContextLocal.get().getCallbackInfo().getCallbackParam();
        String callReq = JSONUtil.toJsonStr(params);
        log.info("支付宝发起回调 报文: {}", callReq);
        String appId = params.get(APP_ID);
        if (StrUtil.isBlank(appId)) {
            log.error("支付宝回调报文appId为空");
            return false;
        }
        AliPayConfig alipayConfig = aliPayConfigService.getConfig();
        if (Objects.isNull(alipayConfig)) {
            log.error("支付宝支付配置不存在");
            return false;
        }
        try {
            if (Objects.equals(alipayConfig.getAuthType(), AUTH_TYPE_KEY)) {
                return AlipaySignature.rsaCheckV1(params, alipayConfig.getAlipayPublicKey(), CharsetUtil.UTF_8, AlipayConstants.SIGN_TYPE_RSA2);
            }
            else {
                return AlipaySignature.verifyV1(params, CertUtil.getCertByContent(alipayConfig.getAlipayCert()), CharsetUtil.UTF_8, AlipayConstants.SIGN_TYPE_RSA2);
            }
        } catch (AlipayApiException e) {
            log.error("支付宝验签失败", e);
            return false;
        }
    }

    /**
     * 解析支付数据并放到上下文中
     */
    public void resolvePayData() {
        CallbackLocal callback = PaymentContextLocal.get().getCallbackInfo();
        Map<String, String> callbackParam = callback.getCallbackParam();
        // 网关订单号
        callback.setOutTradeNo(callbackParam.get(TRADE_NO));
        // 支付订单ID
        callback.setTradeNo(callbackParam.get(OUT_TRADE_NO));
        // 支付状态
        PayStatusEnum payStatus = Objects.equals(callbackParam.get(TRADE_STATUS), NOTIFY_TRADE_SUCCESS) ? PayStatusEnum.SUCCESS : PayStatusEnum.FAIL;
        callback.setOutStatus(payStatus.getCode());
        // 支付金额
        callback.setAmount(callbackParam.get(TOTAL_AMOUNT));

        // 支付时间
        String gmpTime = callbackParam.get(GMT_PAYMENT);
        if (StrUtil.isNotBlank(gmpTime)) {
            LocalDateTime time = LocalDateTimeUtil.parse(gmpTime, DatePattern.NORM_DATETIME_PATTERN);
            callback.setFinishTime(time);
        } else {
            callback.setFinishTime(LocalDateTime.now());
        }
    }

    /**
     * 解析退款回调数据并放到上下文中
     * 注意: 支付宝退款没有网关订单号, 网关订单号是支付单的
     */
    public void resolveRefundData() {
        CallbackLocal callback = PaymentContextLocal.get().getCallbackInfo();
        Map<String, String> callbackParam = callback.getCallbackParam();
        // 退款订单号
        callback.setTradeNo(callbackParam.get(OUT_BIZ_NO));
        // 退款状态
        callback.setOutStatus(callbackParam.get(TRADE_STATUS));
        // 退款金额
        callback.setAmount(callbackParam.get(REFUND_FEE));

        // 退款时间
        String gmpTime = callbackParam.get(GMT_REFUND);
        if (StrUtil.isNotBlank(gmpTime)) {
            LocalDateTime time = LocalDateTimeUtil.parse(gmpTime, DatePattern.NORM_DATETIME_PATTERN);
            callback.setFinishTime(time);
        } else {
            callback.setFinishTime(LocalDateTime.now());
        }
    }

    /**
     * 判断类型 支付回调/退款回调
     *
     * @see TradeTypeEnum
     */
    public TradeTypeEnum getCallbackType() {
        CallbackLocal callback = PaymentContextLocal.get().getCallbackInfo();
        Map<String, String> callbackParam = callback.getCallbackParam();
        String refundFee = callbackParam.get(REFUND_FEE);
        // 如果有退款金额，说明是退款回调
        if (StrUtil.isNotBlank(refundFee)){
            return TradeTypeEnum.REFUND;
        } else {
            return TradeTypeEnum.PAY;
        }
    }

    /**
     * 返回值
     */
    public String getReturnMsg() {
        return "success";
    }
}
