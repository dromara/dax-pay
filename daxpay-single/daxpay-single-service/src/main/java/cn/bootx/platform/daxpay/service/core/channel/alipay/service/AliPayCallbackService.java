package cn.bootx.platform.daxpay.service.core.channel.alipay.service;

import cn.bootx.platform.common.core.util.CertUtil;
import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.service.code.PayCallbackTypeEnum;
import cn.bootx.platform.daxpay.service.common.context.CallbackLocal;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.channel.alipay.entity.AliPayConfig;
import cn.bootx.platform.daxpay.service.func.AbsCallbackStrategy;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayConstants;
import com.alipay.api.internal.util.AlipaySignature;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

import static cn.bootx.platform.daxpay.service.code.AliPayCode.*;

/**
 * 支付宝回调处理
 *
 * @author xxm
 * @since 2021/2/28
 */
@Slf4j
@Service
public class AliPayCallbackService extends AbsCallbackStrategy {

    @Resource
    private AliPayConfigService aliasConfigService;


    /**
     * 策略标识
     */
    @Override
    public PayChannelEnum getChannel() {
        return PayChannelEnum.ALI;
    }

    /**
     * 验证信息格式是否合法
     */
    @SneakyThrows
    @Override
    public boolean verifyNotify() {
        Map<String, String> params =PaymentContextLocal.get().getCallbackInfo().getCallbackParam();
        String callReq = JSONUtil.toJsonStr(params);
        log.info("支付宝发起回调 报文: {}", callReq);
        String appId = params.get(APP_ID);
        if (StrUtil.isBlank(appId)) {
            log.error("支付宝回调报文appId为空");
            return false;
        }
        AliPayConfig alipayConfig = aliasConfigService.getConfig();
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
    @Override
    public void resolvePayData() {
        CallbackLocal callback = PaymentContextLocal.get().getCallbackInfo();
        Map<String, String> callbackParam = callback.getCallbackParam();
        // 网关订单号
        callback.setGatewayOrderNo(callbackParam.get(TRADE_NO));
        // 支付订单ID
        callback.setOrderId(Long.valueOf(callbackParam.get(OUT_TRADE_NO)));
        // 支付状态
        PayStatusEnum payStatus = Objects.equals(callbackParam.get(TRADE_STATUS), NOTIFY_TRADE_SUCCESS) ? PayStatusEnum.SUCCESS : PayStatusEnum.FAIL;
        callback.setGatewayStatus(payStatus.getCode());
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
     */
    @Override
    public void resolveRefundData() {
        CallbackLocal callback = PaymentContextLocal.get().getCallbackInfo();
        Map<String, String> callbackParam = callback.getCallbackParam();
        // 网关订单号
        callback.setGatewayOrderNo(callbackParam.get(OUT_BIZ_NO));
        // 退款订单Id
        callback.setOrderId(Long.valueOf(callbackParam.get(OUT_TRADE_NO)));
        // 退款状态
        callback.setGatewayStatus(callbackParam.get(TRADE_STATUS));
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
     * @see PayCallbackTypeEnum
     */
    @Override
    public PayCallbackTypeEnum getCallbackType() {
        CallbackLocal callback = PaymentContextLocal.get().getCallbackInfo();
        Map<String, String> callbackParam = callback.getCallbackParam();
        String refundFee = callbackParam.get("refund_fee");
        // 如果有退款金额，说明是退款回调
        if (StrUtil.isNotBlank(refundFee)){
            return PayCallbackTypeEnum.REFUND;
        } else {
            return PayCallbackTypeEnum.PAY;
        }
    }

    /**
     * 返回响应结果
     */
    @Override
    public String getReturnMsg() {
        return "success";
    }
}
