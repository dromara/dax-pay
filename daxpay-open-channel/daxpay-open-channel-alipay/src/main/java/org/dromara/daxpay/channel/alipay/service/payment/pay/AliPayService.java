package org.dromara.daxpay.channel.alipay.service.payment.pay;

import org.dromara.daxpay.channel.alipay.code.AlipayCode;
import org.dromara.daxpay.channel.alipay.entity.config.AliPayConfig;
import org.dromara.daxpay.channel.alipay.param.pay.AlipayParam;
import org.dromara.daxpay.channel.alipay.service.payment.config.AlipayConfigService;
import org.dromara.daxpay.core.enums.PayMethodEnum;
import org.dromara.daxpay.core.exception.TradeFailException;
import org.dromara.daxpay.core.param.trade.pay.PayParam;
import org.dromara.daxpay.core.util.PayUtil;
import org.dromara.daxpay.service.pay.bo.trade.PayResultBo;
import org.dromara.daxpay.service.pay.entity.order.pay.PayOrder;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConstants;
import com.alipay.api.AlipayResponse;
import com.alipay.api.domain.*;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

/**
 * 支付宝支付服务
 * @author xxm
 * @since 2024/6/28
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AliPayService {

    private final AlipayConfigService aliPayConfigService;

    /**
     * 支付前检查支付方式是否可用
     */
    public void validation(PayParam payParam, AliPayConfig aliPayConfig) {
        // 验证订单金额是否超限
    }

    /**
     * 调起支付
     */
    public PayResultBo pay(PayOrder payOrder, PayParam payParam, AlipayParam aliPayParam, AliPayConfig aliPayConfig) {
        String amount = PayUtil.toDecimal(payOrder.getAmount()).toPlainString();
        String payBody = null;
        // 异步线程存储
        PayResultBo payResult = new PayResultBo();
        // wap支付
        if (Objects.equals(payOrder.getMethod(), PayMethodEnum.WAP.getCode())) {
            payBody = this.wapPay(amount, payOrder, aliPayConfig);
        }
        // 程序支付
        else if (Objects.equals(payOrder.getMethod(), PayMethodEnum.APP.getCode())) {
            payBody = this.appPay(amount, payOrder, aliPayConfig);
        }
        // pc支付
        else if (Objects.equals(payOrder.getMethod(), PayMethodEnum.WEB.getCode())) {
            payBody = this.webPay(amount, payOrder, aliPayConfig);
        }
        // jsapi支付
        else if (Objects.equals(payOrder.getMethod(), PayMethodEnum.JSAPI.getCode())) {
            payBody = this.jsapiPay(amount, payOrder, payParam, aliPayParam, aliPayConfig);
        }
        // 二维码支付
        else if (Objects.equals(payOrder.getMethod(), PayMethodEnum.QRCODE.getCode())) {
            payBody = this.qrCodePay(amount, payOrder, aliPayConfig);
        }
        // 付款码支付, 付款码存在直接支付成功的情况, 所以返回结果特殊处理
        else if (Objects.equals(payOrder.getMethod(), PayMethodEnum.BARCODE.getCode())) {
            this.barCode(amount, payOrder, payParam.getAuthCode(), payResult, aliPayConfig);
            return payResult;
        }
        // 通常是发起支付的参数
        payResult.setPayBody(payBody);
        return payResult;
    }

    /**
     * wap支付
     */
    public String wapPay(String amount, PayOrder payOrder, AliPayConfig aliPayConfig) {
        // 获取支付宝客户端
        AlipayClient alipayClient = aliPayConfigService.getAlipayClient(aliPayConfig);

        var model = new AlipayTradeWapPayModel();
        model.setSubject(payOrder.getTitle());
        model.setOutTradeNo(payOrder.getOrderNo());
        model.setTotalAmount(amount);
        // 过期时间
        model.setTimeExpire(this.getAliTimeExpire(payOrder.getExpiredTime()));
        model.setProductCode(AlipayCode.Products.QUICK_WAP_PAY);
        // 是否分账
        if (payOrder.getAllocation()){
            ExtendParams extendParams = new ExtendParams();
            extendParams.setRoyaltyFreeze(Boolean.TRUE.toString());
            model.setExtendParams(extendParams);
        }
        AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
        // 特约商户调用
        if (aliPayConfig.isIsv()){
            request.putOtherTextParam(AlipayConstants.APP_AUTH_TOKEN, aliPayConfig.getAppAuthToken());
        }
        request.setBizModel(model);
        // 异步回调必须到当前支付网关系统中, 然后系统负责转发
        request.setNotifyUrl(aliPayConfigService.getNotifyUrl(aliPayConfig.isIsv()));
        // 同步回调地址必须到当前支付网关系统中, 然后系统负责跳转
        request.setReturnUrl(aliPayConfigService.getReturnUrl(aliPayConfig.isIsv()));

        try {
            // 通过GET方式的请求, 返回URL的响应, 默认是POST方式的请求, 返回的是表单响应
            AlipayTradeWapPayResponse response = alipayClient.pageExecute(request, HttpMethod.GET.name());
            return response.getBody();
        }
        catch (AlipayApiException e) {
            log.error("支付宝手机支付失败", e);
            throw new TradeFailException("支付宝手机支付失败: "+e.getMessage());
        }
    }

    /**
     * app支付
     */
    public String appPay(String amount, PayOrder payOrder, AliPayConfig aliPayConfig) {
        AlipayClient alipayClient = aliPayConfigService.getAlipayClient(aliPayConfig);
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setSubject(payOrder.getTitle());
        model.setProductCode(AlipayCode.Products.QUICK_MSECURITY_PAY);
        model.setOutTradeNo(payOrder.getOrderNo());
        // 过期时间
        model.setTimeExpire(this.getAliTimeExpire(payOrder.getExpiredTime()));
        model.setTotalAmount(amount);
        // 是否分账
        if (payOrder.getAllocation()){
            ExtendParams extendParams = new ExtendParams();
            extendParams.setRoyaltyFreeze(Boolean.TRUE.toString());
            model.setExtendParams(extendParams);
        }
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        // 特约商户调用
        if (aliPayConfig.isIsv()){
            request.putOtherTextParam(AlipayConstants.APP_AUTH_TOKEN, aliPayConfig.getAppAuthToken());
        }
        request.setBizModel(model);
        request.setNotifyUrl(aliPayConfigService.getNotifyUrl(aliPayConfig.isIsv()));
        try {
            // 异步回调必须到当前系统中
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            return response.getBody();
        }
        catch (AlipayApiException e) {
            log.error("支付宝APP支付失败", e);
            throw new TradeFailException("支付宝APP支付失败: "+e.getMessage());
        }
    }

    /**
     * PC支付
     */
    public String webPay(String amount, PayOrder payOrder, AliPayConfig aliPayConfig) {
        // 获取支付宝客户端
        AlipayClient alipayClient = aliPayConfigService.getAlipayClient(aliPayConfig);
        AlipayTradePagePayModel model = new AlipayTradePagePayModel();
        model.setSubject(payOrder.getTitle());
        model.setOutTradeNo(payOrder.getOrderNo());
        // 过期时间
        model.setTimeExpire(this.getAliTimeExpire(payOrder.getExpiredTime()));
        model.setTotalAmount(amount);
        // 目前仅支持FAST_INSTANT_TRADE_PAY
        model.setProductCode(AlipayCode.Products.FAST_INSTANT_TRADE_PAY);

        // 是否分账
        if (payOrder.getAllocation()){
            ExtendParams extendParams = new ExtendParams();
            extendParams.setRoyaltyFreeze(Boolean.TRUE.toString());
            model.setExtendParams(extendParams);
        }
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        // 特约商户调用
        if (aliPayConfig.isIsv()){
            request.putOtherTextParam(AlipayConstants.APP_AUTH_TOKEN, aliPayConfig.getAppAuthToken());
        }
        request.setBizModel(model);
        // 异步回调必须到当前系统中
        request.setNotifyUrl(aliPayConfigService.getNotifyUrl(aliPayConfig.isIsv()));
        // 同步回调
        request.setReturnUrl(aliPayConfigService.getReturnUrl(aliPayConfig.isIsv()));
        try {
            // 通过GET方式的请求, 返回URL的响应, 默认是POST方式的请求, 返回的是表单响应
            AlipayTradePagePayResponse response = alipayClient.pageExecute(request, HttpMethod.GET.name());
            return response.getBody();
        }
        catch (AlipayApiException e) {
            log.error("支付宝PC支付失败", e);
            throw new TradeFailException("支付宝PC支付失败: "+e.getMessage());
        }
    }

    /**
     * jsapi支付
     */
    public String jsapiPay(String amount, PayOrder payOrder, PayParam payParam, AlipayParam aliPayParam, AliPayConfig aliPayConfig) {
        AlipayTradeCreateModel model = new AlipayTradeCreateModel();
        model.setOutTradeNo(payOrder.getOrderNo());
        model.setProductCode(AlipayCode.Products.JSAPI_PAY);
        model.setOpAppId(aliPayParam.getOpAppId());
        model.setTotalAmount(amount);
        model.setSubject(payOrder.getTitle());

        // 如果有openId则使用openId，否则使用支付宝用户ID
        if (StrUtil.isNotEmpty(payParam.getOpenId())){
            model.setOpBuyerOpenId(payParam.getOpenId());
        } else {
            model.setBuyerId(aliPayParam.getBuyerId());
        }

        model.setTimeExpire(this.getAliTimeExpire(payOrder.getExpiredTime()));
        // 是否分账
        if (payOrder.getAllocation()){
            ExtendParams extendParams = new ExtendParams();
            extendParams.setRoyaltyFreeze(Boolean.TRUE.toString());
            model.setExtendParams(extendParams);
        }
        // 构造请求参数以调用接口
        AlipayTradeCreateRequest request = new AlipayTradeCreateRequest();
        // 特约商户调用
        if (aliPayConfig.isIsv()){
            request.putOtherTextParam(AlipayConstants.APP_AUTH_TOKEN, aliPayConfig.getAppAuthToken());
        }
        request.setBizModel(model);
        // 异步回调必须到当前系统中
        request.setNotifyUrl(aliPayConfigService.getNotifyUrl(aliPayConfig.isIsv()));
        try {
            AlipayTradeCreateResponse response = aliPayConfigService.execute(request,aliPayConfig);
            this.verifyErrorMsg(response);
            // my.tradePay使用支付宝交易号调起支付
            return response.getTradeNo();
        } catch (AlipayApiException e) {
            log.error("支付宝JsApi支付失败", e);
            throw new TradeFailException("支付宝JsApi支付失败: "+e.getMessage());
        }
    }

    /**
     * 二维码支付(扫码支付)
     */
    public String qrCodePay(String amount, PayOrder payOrder, AliPayConfig aliPayConfig) {
        AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
        model.setSubject(payOrder.getTitle());
        model.setOutTradeNo(payOrder.getOrderNo());
        model.setTotalAmount(amount);
        // 是否分账
        if (payOrder.getAllocation()){
            ExtendParams extendParams = new ExtendParams();
            extendParams.setRoyaltyFreeze(Boolean.TRUE.toString());
            model.setExtendParams(extendParams);
        }
        // 过期时间
        model.setTimeExpire(this.getAliTimeExpire(payOrder.getExpiredTime()));
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        // 特约商户调用
        if (aliPayConfig.isIsv()){
            request.putOtherTextParam(AlipayConstants.APP_AUTH_TOKEN, aliPayConfig.getAppAuthToken());
        }
        request.setBizModel(model);
        request.setNotifyUrl(aliPayConfigService.getNotifyUrl(aliPayConfig.isIsv()));
        try {
            AlipayTradePrecreateResponse response = aliPayConfigService.execute(request,aliPayConfig);
            this.verifyErrorMsg(response);
            return response.getQrCode();
        }
        catch (AlipayApiException e) {
            log.error("支付宝扫码支付失败", e);
            throw new TradeFailException("支付宝扫码支付失败: "+e.getMessage());
        }
    }

    /**
     * 付款码支付
     */
    public void barCode(String amount, PayOrder payOrder, String authCode, PayResultBo result, AliPayConfig aliPayConfig) {
        AlipayTradePayModel model = new AlipayTradePayModel();
        model.setSubject(payOrder.getTitle());
        model.setOutTradeNo(payOrder.getOrderNo());
        model.setScene(AlipayCode.Products.BAR_CODE);
        model.setAuthCode(authCode);
        // 是否分账
        if (payOrder.getAllocation()){
            ExtendParams extendParams = new ExtendParams();
            extendParams.setRoyaltyFreeze(Boolean.TRUE.toString());
            model.setExtendParams(extendParams);
        }
        // 过期时间
        model.setTimeExpire(this.getAliTimeExpire(payOrder.getExpiredTime()));
        model.setTotalAmount(amount);
        AlipayTradePayRequest request = new AlipayTradePayRequest();
        // 特约商户调用
        if (aliPayConfig.isIsv()){
            request.putOtherTextParam(AlipayConstants.APP_AUTH_TOKEN, aliPayConfig.getAppAuthToken());
        }
        request.setBizModel(model);
        request.setNotifyUrl(aliPayConfigService.getNotifyUrl(aliPayConfig.isIsv()));
        try {
            AlipayTradePayResponse response = aliPayConfigService.execute(request,aliPayConfig);
            // 支付成功处理 金额2000以下免密支付, 记录支付完成相关信息
            if (Objects.equals(response.getCode(), AlipayCode.ResponseCode.SUCCESS)) {
                Date gmtPayment = response.getGmtPayment();
                result.setOutOrderNo(response.getTradeNo())
                        .setComplete(true)
                        .setFinishTime(LocalDateTimeUtil.of(gmtPayment))
                        .setRealAmount(new BigDecimal(response.getBuyerPayAmount()))
                        .setBuyerId(response.getBuyerOpenId());
            }
            // 非支付中响应码, 进行错误处理
            if (!Objects.equals(response.getCode(), AlipayCode.ResponseCode.INPROCESS)) {
                this.verifyErrorMsg(response);
            }
        }
        catch (AlipayApiException e) {
            log.error("主动扫码支付失败", e);
            throw new TradeFailException("主动扫码支付失败: "+e.getMessage());
        }
    }

    /**
     * 验证错误信息
     */
    private void verifyErrorMsg(AlipayResponse alipayResponse) {
        if (!alipayResponse.isSuccess()) {
            String errorMsg = alipayResponse.getSubMsg();
            if (StrUtil.isBlank(errorMsg)) {
                errorMsg = alipayResponse.getMsg();
            }
            log.error("支付失败 {}", errorMsg);
            throw new TradeFailException("支付失败: "+errorMsg);
        }
    }

    /**
     * 获取支付宝的过期时间 yyyy-MM-dd HH:mm:ss
     */
    public String getAliTimeExpire(LocalDateTime dateTime) {
        return LocalDateTimeUtil.format(dateTime, DatePattern.NORM_DATETIME_PATTERN);
    }
}
