package cn.bootx.platform.daxpay.core.channel.wechat.service;

import cn.bootx.platform.common.core.util.CollUtil;
import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.bootx.platform.common.jackson.util.JacksonUtil;
import cn.bootx.platform.common.spring.exception.RetryableException;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.code.PayWayEnum;
import cn.bootx.platform.daxpay.code.WeChatPayCode;
import cn.bootx.platform.daxpay.code.WeChatPayWay;
import cn.bootx.platform.daxpay.common.context.AsyncPayLocal;
import cn.bootx.platform.daxpay.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.core.channel.wechat.entity.WeChatPayConfig;
import cn.bootx.platform.daxpay.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.core.payment.sync.result.PaySyncResult;
import cn.bootx.platform.daxpay.core.payment.sync.service.PaySyncService;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.param.channel.wechat.WeChatPayParam;
import cn.bootx.platform.daxpay.param.pay.PayWayParam;
import cn.bootx.platform.daxpay.util.PayUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.ijpay.core.enums.SignType;
import com.ijpay.core.enums.TradeType;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.wxpay.WxPayApi;
import com.ijpay.wxpay.model.MicroPayModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static cn.bootx.platform.daxpay.code.PaySyncStatus.WAIT_BUYER_PAY;
import static com.ijpay.wxpay.model.UnifiedOrderModel.UnifiedOrderModelBuilder;
import static com.ijpay.wxpay.model.UnifiedOrderModel.builder;

/**
 * 微信支付
 *
 * @author xxm
 * @since 2021/3/2
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
    public void validation(PayWayParam payWayParam, WeChatPayConfig weChatPayConfig) {
        List<String> payWays = weChatPayConfig.getPayWays();
        if (CollUtil.isEmpty(payWays)){
            throw new PayFailureException("未配置微信支付方式");
        }

        PayWayEnum payWayEnum = Optional.ofNullable(WeChatPayWay.findByCode(payWayParam.getWay()))
            .orElseThrow(() -> new PayFailureException("非法的微信支付类型"));
        if (!payWays.contains(payWayEnum.getCode())) {
            throw new PayFailureException("该微信支付方式不可用");
        }
    }

    /**
     * 支付
     */
    public void pay(int amount, PayOrder payOrder, WeChatPayParam weChatPayParam, PayWayParam payWayParam,
                    WeChatPayConfig weChatPayConfig) {
        // 微信传入的是分, 将元转换为分
        String totalFee = String.valueOf(amount);
        AsyncPayLocal asyncPayInfo = PaymentContextLocal.get().getAsyncPayInfo();;
        String payBody = null;
        PayWayEnum payWayEnum = PayWayEnum.findByCode(payWayParam.getWay());

        // wap支付
        if (payWayEnum == PayWayEnum.WAP) {
            payBody = this.wapPay(totalFee, payOrder, weChatPayConfig);
        }
        // APP支付
        else if (payWayEnum == PayWayEnum.APP) {
            payBody = this.appPay(totalFee, payOrder, weChatPayConfig);
        }
        // 微信公众号支付或者小程序支付
        else if (payWayEnum == PayWayEnum.JSAPI) {
            payBody = this.jsPay(totalFee, payOrder, weChatPayParam.getOpenId(), weChatPayConfig);
        }
        // 二维码支付
        else if (payWayEnum == PayWayEnum.QRCODE) {
            payBody = this.qrCodePay(totalFee, payOrder, weChatPayConfig);
        }
        // 付款码支付
        else if (payWayEnum == PayWayEnum.BARCODE) {
            String tradeNo = this.barCode(totalFee, payOrder, weChatPayParam.getAuthCode(), weChatPayConfig);
            asyncPayInfo.setTradeNo(tradeNo).setExpiredTime(false);
        }
        asyncPayInfo.setPayBody(payBody);
    }

    /**
     * wap支付
     */
    private String wapPay(String amount, PayOrder payment, WeChatPayConfig weChatPayConfig) {
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
    private String appPay(String amount, PayOrder payment, WeChatPayConfig weChatPayConfig) {
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
    private String jsPay(String amount, PayOrder payment, String openId, WeChatPayConfig weChatPayConfig) {
        Map<String, String> params = this.buildParams(amount, payment, weChatPayConfig, TradeType.JSAPI.getTradeType())
            .openid(openId)
            .build()
            .createSign(weChatPayConfig.getApiKeyV2(), SignType.HMACSHA256);

        String xmlResult = WxPayApi.pushOrder(false, params);
        Map<String, String> result = WxPayKit.xmlToMap(xmlResult);
        this.verifyErrorMsg(result);
        String prepayId = result.get(WeChatPayCode.PREPAY_ID);
        Map<String, String> packageParams = WxPayKit.miniAppPrepayIdCreateSign(weChatPayConfig.getWxAppId(), prepayId,
                weChatPayConfig.getApiKeyV2(), SignType.HMACSHA256);
        String jsonStr = JacksonUtil.toJson(packageParams);
        log.info("小程序支付的参数:" + jsonStr);
        return jsonStr ;
    }

    /**
     * 二维码支付
     */
    private String qrCodePay(String amount, PayOrder payment, WeChatPayConfig weChatPayConfig) {

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
    private String barCode(String amount, PayOrder payment, String authCode, WeChatPayConfig weChatPayConfig) {
        Map<String, String> params = MicroPayModel.builder()
            .appid(weChatPayConfig.getWxAppId())
            .mch_id(weChatPayConfig.getWxMchId())
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
            payment.setStatus(PayStatusEnum.SUCCESS.getCode())
                    .setPayTime(LocalDateTime.now());
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
    private UnifiedOrderModelBuilder buildParams(String amount, PayOrder payment, WeChatPayConfig weChatPayConfig, String tradeType) {
        LocalDateTime expiredTime = PaymentContextLocal.get()
                .getAsyncPayInfo()
                .getExpiredTime();
        return builder()
            .appid(weChatPayConfig.getWxAppId())
            .mch_id(weChatPayConfig.getWxMchId())
            .nonce_str(WxPayKit.generateStr())
            .time_start(LocalDateTimeUtil.format(LocalDateTime.now(), DatePattern.PURE_DATETIME_PATTERN))
            // 反正v2版本的超时时间无效
            .time_expire(PayUtil.getWxExpiredTime(expiredTime))
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
    public void rotationSync(PayOrder payment, WeChatPayConfig weChatPayConfig) {
        PaySyncResult paySyncResult = weChatPaySyncService.syncPayStatus(payment.getId(), weChatPayConfig);
        // 不为支付中状态后, 调用系统同步更新状态, 支付状态则继续重试
        if (Objects.equals(WAIT_BUYER_PAY, paySyncResult.getPaySyncStatus())) {
            throw new RetryableException();
        }
        else {
            paySyncService.sync(payment);
        }
    }

}
