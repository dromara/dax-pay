package cn.daxpay.single.service.core.channel.wechat.service;

import cn.bootx.platform.common.core.util.CollUtil;
import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.bootx.platform.common.jackson.util.JacksonUtil;
import cn.bootx.platform.common.spring.exception.RetryableException;
import cn.daxpay.single.core.code.PayMethodEnum;
import cn.daxpay.single.core.exception.*;
import cn.daxpay.single.core.param.payment.pay.PayParam;
import cn.daxpay.single.core.result.sync.PaySyncResult;
import cn.daxpay.single.core.util.PayUtil;
import cn.daxpay.single.service.code.WeChatPayCode;
import cn.daxpay.single.service.code.WeChatPayWay;
import cn.daxpay.single.service.common.context.PayLocal;
import cn.daxpay.single.service.common.local.PaymentContextLocal;
import cn.daxpay.single.service.core.channel.wechat.entity.WeChatPayConfig;
import cn.daxpay.single.service.core.order.pay.entity.PayOrder;
import cn.daxpay.single.service.core.payment.sync.service.PaySyncService;
import cn.daxpay.single.service.param.channel.wechat.WeChatPayParam;
import cn.daxpay.single.service.sdk.wechat.BarPayModel;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.ijpay.core.enums.SignType;
import com.ijpay.core.enums.TradeType;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.wxpay.WxPayApi;
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

import static cn.daxpay.single.core.code.PaySyncStatusEnum.PROGRESS;
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

    /**
     * 校验
     */
    public void validation(PayParam payParam, WeChatPayConfig weChatPayConfig) {
        List<String> payWays = weChatPayConfig.getPayWays();
        if (CollUtil.isEmpty(payWays)){
            throw new ConfigNotEnableException("未配置微信支付方式");
        }

        PayMethodEnum payMethodEnum = Optional.ofNullable(WeChatPayWay.findByCode(payParam.getMethod()))
                .orElseThrow(() -> new MethodNotExistException("非法的微信支付类型"));
        if (!payWays.contains(payMethodEnum.getCode())) {
            throw new MethodNotEnableException("该微信支付方式不可用");
        }
        // 支付金额是否超限
        if (payParam.getAmount() > weChatPayConfig.getLimitAmount()) {
            throw new AmountExceedLimitException("微信支付金额超限");
        }
        // 是否支持分账
        if (Objects.equals(payParam.getAllocation(),true) && !Objects.equals(weChatPayConfig.getAllocation(),true)) {
            throw new ConfigNotEnableException("未开启分账配置");
        }
    }

    /**
     * 支付
     */
    public void pay(PayOrder payOrder, WeChatPayParam weChatPayParam, WeChatPayConfig weChatPayConfig) {

        Integer amount = payOrder.getAmount();
        String totalFee = String.valueOf(amount);
        PayLocal payInfo = PaymentContextLocal.get().getPayInfo();;
        String payBody = null;
        PayMethodEnum payMethodEnum = PayMethodEnum.findByCode(payOrder.getMethod());

        // wap支付
        if (payMethodEnum == PayMethodEnum.WAP) {
            payBody = this.wapPay(totalFee, payOrder, weChatPayConfig);
        }
        // APP支付
        else if (payMethodEnum == PayMethodEnum.APP) {
            payBody = this.appPay(totalFee, payOrder, weChatPayConfig);
        }
        // 微信公众号支付或者小程序支付
        else if (payMethodEnum == PayMethodEnum.JSAPI) {
            payBody = this.jsPay(totalFee, payOrder, weChatPayParam.getOpenId(), weChatPayConfig);
        }
        // 二维码支付
        else if (payMethodEnum == PayMethodEnum.QRCODE) {
            payBody = this.qrCodePay(totalFee, payOrder, weChatPayConfig);
        }
        // 付款码支付
        else if (payMethodEnum == PayMethodEnum.BARCODE) {
            this.barCodePay(totalFee, payOrder, weChatPayParam.getAuthCode(), weChatPayConfig);
        }
        payInfo.setPayBody(payBody);
    }

    /**
     * wap支付
     */
    private String wapPay(String amount, PayOrder payOrder, WeChatPayConfig weChatPayConfig) {
        Map<String, String> params = this.buildParams(amount, payOrder, weChatPayConfig, TradeType.MWEB.getTradeType())
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
    private String appPay(String amount, PayOrder payOrder, WeChatPayConfig weChatPayConfig) {
        Map<String, String> params = this.buildParams(amount, payOrder, weChatPayConfig, TradeType.APP.getTradeType())
                .build()
                .createSign(weChatPayConfig.getApiKeyV2(), SignType.HMACSHA256);

        String xmlResult = WxPayApi.pushOrder(false, params);
        Map<String, String> result = WxPayKit.xmlToMap(xmlResult);
        this.verifyErrorMsg(result);
        String prepayId = result.get(WeChatPayCode.PREPAY_ID);
        Map<String, String> packageParams = WxPayKit.miniAppPrepayIdCreateSign(weChatPayConfig.getWxAppId(), prepayId,
                weChatPayConfig.getApiKeyV2(), SignType.HMACSHA256);
        String jsonStr = JacksonUtil.toJson(packageParams);
        log.info("Jsapi支付的参数:" + jsonStr);
        return jsonStr;
    }

    /**
     * 微信公众号支付或者小程序支付
     */
    private String jsPay(String amount, PayOrder payOrder, String openId, WeChatPayConfig weChatPayConfig) {
        Map<String, String> params = this.buildParams(amount, payOrder, weChatPayConfig, TradeType.JSAPI.getTradeType())
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
        log.info("Jsapi支付的参数:" + jsonStr);
        return jsonStr ;
    }

    /**
     * 二维码支付
     */
    private String qrCodePay(String amount, PayOrder payOrder, WeChatPayConfig weChatPayConfig) {

        Map<String, String> params = this.buildParams(amount, payOrder, weChatPayConfig, TradeType.NATIVE.getTradeType())
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
    private void barCodePay(String amount, PayOrder payOrder, String authCode, WeChatPayConfig weChatPayConfig) {
        PayLocal payInfo = PaymentContextLocal.get().getPayInfo();

        Map<String, String> params = BarPayModel.builder()
                .appid(weChatPayConfig.getWxAppId())
                .mch_id(weChatPayConfig.getWxMchId())
                .nonce_str(WxPayKit.generateStr())
                .profit_sharing(payOrder.getAllocation()?"Y":"N")
                .body(payOrder.getTitle())
                .auth_code(authCode)
                .out_trade_no(String.valueOf(payOrder.getOrderNo()))
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
            throw new TradeFailException(errorMsg);
        }

        String resultCode = result.get(WeChatPayCode.RESULT_CODE);
        String errCode = result.get(WeChatPayCode.ERR_CODE);
        // 支付成功处理,
        if (Objects.equals(resultCode, WeChatPayCode.PAY_SUCCESS)) {
            String timeEnd = result.get(WeChatPayCode.TIME_END);
            LocalDateTime time = LocalDateTimeUtil.parse(timeEnd, DatePattern.PURE_DATETIME_PATTERN);
            payInfo.setOutOrderNo(result.get(WeChatPayCode.TRANSACTION_ID))
                    .setFinishTime(time)
                    .setComplete(true);
            return;
        }
        // 支付中, 发起轮训同步
        if (Objects.equals(resultCode, WeChatPayCode.PAY_FAIL)
                && Objects.equals(errCode, WeChatPayCode.PAY_USERPAYING)) {
            SpringUtil.getBean(this.getClass()).rotationSync(payOrder);
            payInfo.setOutOrderNo(result.get(WeChatPayCode.TRANSACTION_ID));
            return;
        }
        // 支付撤销
        if (Objects.equals(resultCode, WeChatPayCode.PAY_REVOKED)) {
            throw new TradeStatusErrorException("用户已撤销支付");
        }
        // 支付失败
        if (Objects.equals(resultCode, WeChatPayCode.TRADE_PAYERROR)
                || Objects.equals(resultCode, WeChatPayCode.PAY_FAIL)) {
            String errorMsg = result.get(WeChatPayCode.ERR_CODE_DES);
            throw new TradeFailException(errorMsg);
        }
    }

    /**
     * 构建参数
     */
    private UnifiedOrderModelBuilder buildParams(String amount, PayOrder payOrder, WeChatPayConfig weChatPayConfig, String tradeType) {

        return builder()
                .appid(weChatPayConfig.getWxAppId())
                .mch_id(weChatPayConfig.getWxMchId())
                .nonce_str(WxPayKit.generateStr())
                .time_start(LocalDateTimeUtil.format(LocalDateTime.now(), DatePattern.PURE_DATETIME_PATTERN))
                // 反正v2版本的超时时间无效
                .time_expire(PayUtil.getWxExpiredTime(payOrder.getExpiredTime()))
                .body(payOrder.getTitle())
                .profit_sharing(payOrder.getAllocation()?"Y":"N")
                .out_trade_no(payOrder.getOrderNo())
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
            throw new TradeFailException(errorMsg);
        }
    }

    /**
     * 多次重试同步支付状态, 最多10次, 30秒不操作微信会自动关闭
     */
    @Async("bigExecutor")
    @Retryable(value = RetryableException.class, maxAttempts = 10, backoff = @Backoff(value = 5000L))
    public void rotationSync(PayOrder payOrder) {
        PaySyncResult paySyncResult = paySyncService.syncPayOrder(payOrder);
        // 不为支付中状态后, 调用系统同步更新状态, 支付状态则继续重试
        if (Objects.equals(PROGRESS.getCode(), paySyncResult.getStatus())) {
            throw new RetryableException();
        }
    }

}
