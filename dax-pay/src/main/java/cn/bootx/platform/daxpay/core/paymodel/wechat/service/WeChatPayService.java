package cn.bootx.platform.daxpay.core.paymodel.wechat.service;

import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.bootx.platform.common.spring.exception.RetryableException;
import cn.bootx.platform.daxpay.code.pay.PayStatusCode;
import cn.bootx.platform.daxpay.code.pay.PayWayCode;
import cn.bootx.platform.daxpay.code.pay.PayWayEnum;
import cn.bootx.platform.daxpay.code.paymodel.WeChatPayCode;
import cn.bootx.platform.daxpay.code.paymodel.WeChatPayWay;
import cn.bootx.platform.daxpay.core.pay.local.AsyncPayInfoLocal;
import cn.bootx.platform.daxpay.core.pay.result.PaySyncResult;
import cn.bootx.platform.daxpay.core.pay.service.PaySyncService;
import cn.bootx.platform.daxpay.core.payment.entity.Payment;
import cn.bootx.platform.daxpay.core.paymodel.wechat.entity.WeChatPayConfig;
import cn.bootx.platform.daxpay.dto.pay.AsyncPayInfo;
import cn.bootx.platform.daxpay.exception.payment.PayFailureException;
import cn.bootx.platform.daxpay.param.pay.PayModeParam;
import cn.bootx.platform.daxpay.param.paymodel.wechat.WeChatPayParam;
import cn.bootx.platform.daxpay.util.PayModelUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.ijpay.core.enums.SignType;
import com.ijpay.core.enums.TradeType;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.wxpay.WxPayApi;
import com.ijpay.wxpay.model.MicroPayModel;
import com.ijpay.wxpay.model.UnifiedOrderModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static cn.bootx.platform.daxpay.code.pay.PaySyncStatus.WAIT_BUYER_PAY;

/**
 * 微信支付
 *
 * @author xxm
 * @date 2021/3/2
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WeChatPayService {

    private final PaySyncService paySyncService;

    private final WeChatPaySyncService weChatPaySyncService;

    /**
     * 校验
     */
    public void validation(PayModeParam payModeParam, WeChatPayConfig weChatPayConfig) {
        List<String> payWays = Optional.ofNullable(weChatPayConfig.getPayWays())
            .filter(StrUtil::isNotBlank)
            .map(s -> StrUtil.split(s, ','))
            .orElse(new ArrayList<>(1));

        PayWayEnum payWayEnum = Optional.ofNullable(WeChatPayWay.findByNo(payModeParam.getPayWay()))
            .orElseThrow(() -> new PayFailureException("非法的微信支付类型"));
        if (!payWays.contains(payWayEnum.getCode())) {
            throw new PayFailureException("该微信支付方式不可用");
        }
    }

    /**
     * 支付
     */
    public void pay(BigDecimal amount, Payment payment, WeChatPayParam weChatPayParam, PayModeParam payModeParam,
                    WeChatPayConfig weChatPayConfig) {
        // 微信传入的是分, 将元转换为分
        String totalFee = String.valueOf(amount.multiply(new BigDecimal(100)).longValue());
        AsyncPayInfo asyncPayInfo = Optional.ofNullable(AsyncPayInfoLocal.get()).orElse(new AsyncPayInfo());
        String payBody = null;

        // wap支付
        if (payModeParam.getPayWay() == PayWayCode.WAP) {
            payBody = this.wapPay(totalFee, payment, weChatPayConfig);
        }
        // APP支付
        else if (payModeParam.getPayWay() == PayWayCode.APP) {
            payBody = this.appPay(totalFee, payment, weChatPayConfig);
        }
        // 微信公众号支付或者小程序支付
        else if (payModeParam.getPayWay() == PayWayCode.JSAPI) {
            payBody = this.jsPay(totalFee, payment, weChatPayParam.getOpenId(), weChatPayConfig);
        }
        // 二维码支付
        else if (payModeParam.getPayWay() == PayWayCode.QRCODE) {
            payBody = this.qrCodePay(totalFee, payment, weChatPayConfig);
        }
        // 付款码支付
        else if (payModeParam.getPayWay() == PayWayCode.BARCODE) {
            String tradeNo = this.barCode(totalFee, payment, weChatPayParam.getAuthCode(), weChatPayConfig);
            asyncPayInfo.setTradeNo(tradeNo).setExpiredTime(false);
        }
        asyncPayInfo.setPayBody(payBody);
        AsyncPayInfoLocal.set(asyncPayInfo);
    }

    /**
     * wap支付
     */
    private String wapPay(String amount, Payment payment, WeChatPayConfig weChatPayConfig) {
        Map<String, String> params = this.buildParams(amount, payment, weChatPayConfig, TradeType.MWEB.getTradeType())
            .build()
            .createSign(weChatPayConfig.getApiKeyV2(), SignType.HMACSHA256);

        String xmlResult = WxPayApi.pushOrder(false, params);
        Map<String, String> result = WxPayKit.xmlToMap(xmlResult);
        this.verifyErrorMsg(result);
        return result.get(WeChatPayCode.MWEB_URL);
    }

    /**
     * 程序支付
     */
    private String appPay(String amount, Payment payment, WeChatPayConfig weChatPayConfig) {
        Map<String, String> params = this.buildParams(amount, payment, weChatPayConfig, TradeType.APP.getTradeType())
            .build()
            .createSign(weChatPayConfig.getApiKeyV2(), SignType.HMACSHA256);

        String xmlResult = WxPayApi.pushOrder(false, params);
        Map<String, String> result = WxPayKit.xmlToMap(xmlResult);
        this.verifyErrorMsg(result);
        return result.get(WeChatPayCode.PREPAY_ID);
    }

    /**
     * 微信公众号支付或者小程序支付
     */
    private String jsPay(String amount, Payment payment, String openId, WeChatPayConfig weChatPayConfig) {
        Map<String, String> params = this.buildParams(amount, payment, weChatPayConfig, TradeType.JSAPI.getTradeType())
            .openid(openId)
            .build()
            .createSign(weChatPayConfig.getApiKeyV2(), SignType.HMACSHA256);

        String xmlResult = WxPayApi.pushOrder(false, params);
        Map<String, String> result = WxPayKit.xmlToMap(xmlResult);
        this.verifyErrorMsg(result);
        return result.get(WeChatPayCode.PREPAY_ID);
    }

    /**
     * 二维码支付
     */
    private String qrCodePay(String amount, Payment payment, WeChatPayConfig weChatPayConfig) {

        Map<String, String> params = this.buildParams(amount, payment, weChatPayConfig, TradeType.NATIVE.getTradeType())
            .build()
            .createSign(weChatPayConfig.getApiKeyV2(), SignType.HMACSHA256);

        String xmlResult = WxPayApi.pushOrder(false, params);
        Map<String, String> result = WxPayKit.xmlToMap(xmlResult);
        this.verifyErrorMsg(result);
        return result.get(WeChatPayCode.CODE_URL);
    }

    /**
     * 付款码支付
     */
    private String barCode(String amount, Payment payment, String authCode, WeChatPayConfig weChatPayConfig) {
        Map<String, String> params = MicroPayModel.builder()
            .appid(weChatPayConfig.getAppId())
            .mch_id(weChatPayConfig.getMchId())
            .nonce_str(WxPayKit.generateStr())
            .body(payment.getTitle())
            .auth_code(authCode)
            .out_trade_no(String.valueOf(payment.getId()))
            .total_fee(amount)
            .spbill_create_ip(NetUtil.getLocalhostStr())
            .build()
            .createSign(weChatPayConfig.getApiKeyV2(), SignType.HMACSHA256);

        String xmlResult = WxPayApi.microPay(false, params);
        Map<String, String> result = WxPayKit.xmlToMap(xmlResult);

        String returnCode = result.get(WeChatPayCode.RETURN_CODE);
        // 支付失败
        if (!WxPayKit.codeIsOk(returnCode)) {
            String errorMsg = result.get(WeChatPayCode.ERR_CODE_DES);
            throw new PayFailureException(errorMsg);
        }

        String resultCode = result.get(WeChatPayCode.RESULT_CODE);
        String errCode = result.get(WeChatPayCode.ERR_CODE);
        // 支付成功处理
        if (Objects.equals(resultCode, WeChatPayCode.TRADE_SUCCESS)) {
            payment.setPayStatus(PayStatusCode.TRADE_SUCCESS).setPayTime(LocalDateTime.now());
            return result.get(WeChatPayCode.TRANSACTION_ID);
        }
        // 支付中, 发起轮训同步
        if (Objects.equals(resultCode, WeChatPayCode.TRADE_FAIL)
                && Objects.equals(errCode, WeChatPayCode.TRADE_USERPAYING)) {
            SpringUtil.getBean(this.getClass()).rotationSync(payment, weChatPayConfig);
            return result.get(WeChatPayCode.TRANSACTION_ID);
        }

        // 支付撤销
        if (Objects.equals(resultCode, WeChatPayCode.TRADE_REVOKED)) {
            throw new PayFailureException("用户已撤销支付");
        }

        // 支付失败
        if (Objects.equals(resultCode, WeChatPayCode.TRADE_PAYERROR)
                || Objects.equals(resultCode, WeChatPayCode.TRADE_FAIL)) {
            String errorMsg = result.get(WeChatPayCode.ERR_CODE_DES);
            throw new PayFailureException(errorMsg);
        }
        return null;
    }

    /**
     * 构建参数
     */
    private UnifiedOrderModel.UnifiedOrderModelBuilder buildParams(String amount, Payment payment,
                                                                   WeChatPayConfig weChatPayConfig, String tradeType) {
        // 过期时间
        payment.setExpiredTime(PayModelUtil.getPaymentExpiredTime(weChatPayConfig.getExpireTime()));
        return UnifiedOrderModel.builder()
            .appid(weChatPayConfig.getAppId())
            .mch_id(weChatPayConfig.getMchId())
            .nonce_str(WxPayKit.generateStr())
            .time_start(LocalDateTimeUtil.format(LocalDateTime.now(), DatePattern.PURE_DATETIME_PATTERN))
            // 反正v2版本的超时时间无效
            .time_expire(PayModelUtil.getWxExpiredTime(weChatPayConfig.getExpireTime()))
            .body(payment.getTitle())
            .out_trade_no(String.valueOf(payment.getId()))
            .total_fee(amount)
            .spbill_create_ip(NetUtil.getLocalhostStr())
            .notify_url(weChatPayConfig.getNotifyUrl())
            .trade_type(tradeType);
    }

    /**
     * 验证错误信息
     */
    private void verifyErrorMsg(Map<String, String> result) {
        String returnCode = result.get(WeChatPayCode.RETURN_CODE);
        String resultCode = result.get(WeChatPayCode.RESULT_CODE);
        if (!WxPayKit.codeIsOk(returnCode) || !WxPayKit.codeIsOk(resultCode)) {
            String errorMsg = result.get(WeChatPayCode.ERR_CODE_DES);
            if (StrUtil.isBlank(errorMsg)) {
                errorMsg = result.get(WeChatPayCode.RETURN_MSG);
            }
            log.error("支付失败 {}", errorMsg);
            throw new PayFailureException(errorMsg);
        }
    }

    /**
     * 重试同步支付状态, 最多10次, 30秒不操作微信会自动关闭
     */
    @Async("bigExecutor")
    @Retryable(value = RetryableException.class, maxAttempts = 10, backoff = @Backoff(value = 5000L))
    public void rotationSync(Payment payment, WeChatPayConfig weChatPayConfig) {
        PaySyncResult paySyncResult = weChatPaySyncService.syncPayStatus(payment.getId(), weChatPayConfig);
        // 不为支付中状态后, 调用系统同步更新状态, 支付状态则继续重试
        if (Objects.equals(WAIT_BUYER_PAY, paySyncResult.getPaySyncStatus())) {
            throw new RetryableException();
        }
        else {
            paySyncService.syncPayment(payment);
        }
    }

}
