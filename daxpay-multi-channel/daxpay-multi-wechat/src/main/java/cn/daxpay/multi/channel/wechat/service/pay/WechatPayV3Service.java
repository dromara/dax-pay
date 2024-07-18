package cn.daxpay.multi.channel.wechat.service.pay;

import cn.daxpay.multi.channel.wechat.entity.config.WechatPayConfig;
import cn.daxpay.multi.channel.wechat.param.pay.WechatPayParam;
import cn.daxpay.multi.channel.wechat.service.config.WechatPayConfigService;
import cn.daxpay.multi.core.enums.PayMethodEnum;
import cn.daxpay.multi.core.exception.TradeFailException;
import cn.daxpay.multi.core.param.payment.pay.PayParam;
import cn.daxpay.multi.core.util.PayUtil;
import cn.daxpay.multi.service.common.context.PayLocal;
import cn.daxpay.multi.service.common.local.PaymentContextLocal;
import cn.daxpay.multi.service.entity.order.pay.PayOrder;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderV3Request;
import com.github.binarywang.wxpay.bean.result.enums.TradeTypeEnum;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * 微信支付服务v3版本
 * @author xxm
 * @since 2024/7/17
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatPayV3Service {
    private final WechatPayConfigService wechatPayConfigService;

    /**
     * 支付前检查支付方式是否可用
     */
    public void validation(PayParam payParam) {

    }

    /**
     * 调起支付
     */
    public void pay(PayOrder payOrder, WechatPayParam wechatPayParam, WechatPayConfig config) {

        BigDecimal amount = payOrder.getAmount();
        String totalFee = String.valueOf(PayUtil.convertCentAmount(amount));
        PayLocal payInfo = PaymentContextLocal.get().getPayInfo();;
        String payBody = null;
        PayMethodEnum payMethodEnum = PayMethodEnum.findByCode(payOrder.getMethod());


        // wap支付
        if (payMethodEnum == PayMethodEnum.WAP) {
            payBody = this.wapPay(totalFee, payOrder, config);
        }
        // APP支付
        else if (payMethodEnum == PayMethodEnum.APP) {
            payBody = this.appPay(totalFee, payOrder, config);
        }
        // 微信公众号支付或者小程序支付
        else if (payMethodEnum == PayMethodEnum.JSAPI) {
            payBody = this.jsPay(totalFee, payOrder, wechatPayParam.getOpenId(), config);
        }
        // 二维码支付
        else if (payMethodEnum == PayMethodEnum.QRCODE) {
            payBody = this.qrCodePay(payOrder, config);
        }
        // 付款码支付
        else if (payMethodEnum == PayMethodEnum.BARCODE) {
            this.barCodePay(totalFee, payOrder, wechatPayParam.getAuthCode(), config);
        }
        payInfo.setPayBody(payBody);
    }

    /**
     * wap支付
     */
    private String wapPay(String amount, PayOrder payOrder, WechatPayConfig wechatPayConfig) {
        return null;
    }

    /**
     * 程序支付
     */
    private String appPay(String amount, PayOrder payOrder, WechatPayConfig wechatPayConfig) {
        return null;

    }

    /**
     * 微信公众号支付或者小程序支付
     */
    private String jsPay(String amount, PayOrder payOrder, String openId, WechatPayConfig wechatPayConfig) {
        return null;
    }

    /**
     * 二维码支付
     */
    @SneakyThrows
    private String qrCodePay(PayOrder payOrder, WechatPayConfig wechatPayConfig) {
        WxPayService wxPayService = wechatPayConfigService.wxJavaSdk(wechatPayConfig);
        WxPayUnifiedOrderV3Request request = new WxPayUnifiedOrderV3Request();
        WxPayUnifiedOrderV3Request.Amount amount = new WxPayUnifiedOrderV3Request.Amount();
        amount.setTotal(PayUtil.convertCentAmount(payOrder.getAmount()));
        request.setDescription(payOrder.getDescription());
        request.setOutTradeNo(payOrder.getOrderNo());
        request.setTimeExpire(this.getWxExpiredTime(payOrder.getExpiredTime()));
        request.setNotifyUrl(wechatPayConfigService.getNotifyUrl());
        request.setAmount(amount);
        String qrUrl;
        try {
            qrUrl = wxPayService.createOrderV3(TradeTypeEnum.NATIVE, request);
        } catch (WxPayException e) {
            log.error("微信V3扫码支付失败", e);
            throw new TradeFailException("微信V3扫码支付失败");
        }
        return qrUrl;
    }

    /**
     * 付款码支付
     */
    private void barCodePay(String amount, PayOrder payOrder, String authCode, WechatPayConfig wechatPayConfig) {
    }


    /**
     * 获取微信的过期时间 yyyyMMddHHmmss
     */
    private String getWxExpiredTime(LocalDateTime dateTime) {
        String format = LocalDateTimeUtil.format(dateTime, DatePattern.NORM_DATETIME_PATTERN);
        dateTime = LocalDateTimeUtil.parse(format, DatePattern.NORM_DATETIME_PATTERN);
        return dateTime.atOffset(ZoneOffset.ofHours(8)).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

}

