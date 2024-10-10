package org.dromara.daxpay.channel.alipay.service.pay;

import cn.bootx.platform.core.util.BigDecimalUtil;
import org.dromara.daxpay.channel.alipay.code.AliPayCode;
import org.dromara.daxpay.channel.alipay.entity.config.AliPayConfig;
import org.dromara.daxpay.channel.alipay.param.pay.AlipayParam;
import org.dromara.daxpay.channel.alipay.service.config.AliPayConfigService;
import org.dromara.daxpay.core.enums.PayMethodEnum;
import org.dromara.daxpay.core.exception.AmountExceedLimitException;
import org.dromara.daxpay.core.exception.TradeFailException;
import org.dromara.daxpay.core.param.trade.pay.PayParam;
import org.dromara.daxpay.core.util.PayUtil;
import org.dromara.daxpay.service.bo.trade.PayResultBo;
import org.dromara.daxpay.service.entity.order.pay.PayOrder;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayResponse;
import com.alipay.api.domain.*;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

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

    private final AliPayConfigService aliPayConfigService;

    /**
     * 支付前检查支付方式是否可用
     */
    public void validation(PayParam payParam) {
        AliPayConfig alipayConfig = aliPayConfigService.getAndCheckConfig();
        // 验证订单金额是否超限
        if(BigDecimalUtil.isGreaterThan(payParam.getAmount(), alipayConfig.getLimitAmount())){
            throw new AmountExceedLimitException("支付宝支付金额超过限额");
        }
    }

    /**
     * 调起支付
     */
    public PayResultBo pay(PayOrder payOrder, AlipayParam aliPayParam) {
        String amount = PayUtil.toDecimal(payOrder.getAmount()).toString();
        String payBody = null;
        // 异步线程存储
        PayResultBo payResult = new PayResultBo();
        // wap支付
        if (Objects.equals(payOrder.getMethod(), PayMethodEnum.WAP.getCode())) {
            payBody = this.wapPay(amount, payOrder);
        }
        // 程序支付
        else if (Objects.equals(payOrder.getMethod(), PayMethodEnum.APP.getCode())) {
            payBody = this.appPay(amount, payOrder);
        }
        // pc支付
        else if (Objects.equals(payOrder.getMethod(), PayMethodEnum.WEB.getCode())) {
            payBody = this.webPay(amount, payOrder);
        }
        // jsapi支付
        else if (Objects.equals(payOrder.getMethod(), PayMethodEnum.JSAPI.getCode())) {
            payBody = this.jsapiPay(amount, payOrder, aliPayParam);
        }
        // 二维码支付
        else if (Objects.equals(payOrder.getMethod(), PayMethodEnum.QRCODE.getCode())) {
            payBody = this.qrCodePay(amount, payOrder);
        }
        // 付款码支付, 付款码存在直接支付成功的情况, 所以返回结果特殊处理
        else if (Objects.equals(payOrder.getMethod(), PayMethodEnum.BARCODE.getCode())) {
            this.barCode(amount, payOrder, aliPayParam, payResult);
            return payResult;
        }
        // 通常是发起支付的参数
        payResult.setPayBody(payBody);
        return payResult;
    }

    /**
     * wap支付
     */
    @SneakyThrows
    public String wapPay(String amount, PayOrder payOrder) {
        // 获取支付宝客户端
        AlipayClient alipayClient = aliPayConfigService.getAlipayClient();

        AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
        model.setSubject(payOrder.getTitle());
        model.setOutTradeNo(payOrder.getOrderNo());
        model.setTotalAmount(amount);
        // 过期时间
        model.setTimeExpire(this.getAliTimeExpire(payOrder.getExpiredTime()));
        model.setProductCode(AliPayCode.Products.QUICK_WAP_PAY);
        // 是否分账
        if (payOrder.getAllocation()){
            ExtendParams extendParams = new ExtendParams();
            extendParams.setRoyaltyFreeze(Boolean.TRUE.toString());
            model.setExtendParams(extendParams);
        }

        AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
        request.setBizModel(model);
        // 异步回调必须到当前支付网关系统中, 然后系统负责转发
        request.setNotifyUrl(aliPayConfigService.getNotifyUrl());
        // 同步回调地址必须到当前支付网关系统中, 然后系统负责跳转
        request.setReturnUrl(aliPayConfigService.getReturnUrl());

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
    @SneakyThrows
    public String appPay(String amount, PayOrder payOrder) {
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setSubject(payOrder.getTitle());
        model.setProductCode(AliPayCode.Products.QUICK_MSECURITY_PAY);
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
        request.setBizModel(model);
        request.setNotifyUrl(aliPayConfigService.getNotifyUrl());
        try {
            // 异步回调必须到当前系统中
            AlipayTradeAppPayResponse response = aliPayConfigService.execute(request);
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
    @SneakyThrows
    public String webPay(String amount, PayOrder payOrder) {
        // 获取支付宝客户端
        AlipayClient alipayClient = aliPayConfigService.getAlipayClient();

        AlipayTradePagePayModel model = new AlipayTradePagePayModel();
        model.setSubject(payOrder.getTitle());
        model.setOutTradeNo(payOrder.getOrderNo());
        // 过期时间
        model.setTimeExpire(this.getAliTimeExpire(payOrder.getExpiredTime()));
        model.setTotalAmount(amount);
        // 目前仅支持FAST_INSTANT_TRADE_PAY
        model.setProductCode(AliPayCode.Products.FAST_INSTANT_TRADE_PAY);

        // 是否分账
        if (payOrder.getAllocation()){
            ExtendParams extendParams = new ExtendParams();
            extendParams.setRoyaltyFreeze(Boolean.TRUE.toString());
            model.setExtendParams(extendParams);
        }

        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setBizModel(model);
        // 异步回调必须到当前系统中
        request.setNotifyUrl(aliPayConfigService.getNotifyUrl());
        // 同步回调
        request.setReturnUrl(aliPayConfigService.getReturnUrl());
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
    @SneakyThrows
    public String jsapiPay(String amount, PayOrder payOrder, AlipayParam aliPayParam) {
        // 构造请求参数以调用接口
        AlipayTradeCreateRequest request = new AlipayTradeCreateRequest();
        AlipayTradeCreateModel model = new AlipayTradeCreateModel();
        model.setOutTradeNo(payOrder.getOrderNo());
        model.setProductCode(AliPayCode.Products.JSAPI_PAY);
        model.setOpAppId(aliPayParam.getOpAppId());
        model.setTotalAmount(amount);
        model.setSubject(payOrder.getTitle());

        // 如果有openId则使用openId，否则使用支付宝用户ID
        if (StrUtil.isNotEmpty(aliPayParam.getOpenId())){
            model.setOpBuyerOpenId(aliPayParam.getOpenId());
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
        request.setBizModel(model);
        try {
            AlipayTradeCreateResponse response = aliPayConfigService.execute(request);
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
    @SneakyThrows
    public String qrCodePay(String amount, PayOrder payOrder) {
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
        request.setBizModel(model);
        request.setNotifyUrl(aliPayConfigService.getNotifyUrl());
        try {
            AlipayTradePrecreateResponse response = aliPayConfigService.execute(request);
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
    @SneakyThrows
    public void barCode(String amount, PayOrder payOrder, AlipayParam aliPayParam, PayResultBo result) {
        AlipayTradePayModel model = new AlipayTradePayModel();
        model.setSubject(payOrder.getTitle());
        model.setOutTradeNo(payOrder.getOrderNo());
        model.setScene(AliPayCode.Products.BAR_CODE);
        model.setAuthCode(aliPayParam.getAuthCode());
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
        request.setBizModel(model);
        request.setNotifyUrl(aliPayConfigService.getNotifyUrl());
        try {
            AlipayTradePayResponse response = aliPayConfigService.execute(request);
            // 支付成功处理 金额2000以下免密支付, 记录支付完成相关信息
            if (Objects.equals(response.getCode(), AliPayCode.ResponseCode.SUCCESS)) {
                Date gmtPayment = response.getGmtPayment();
                result.setOutOrderNo(response.getTradeNo())
                        .setComplete(true)
                        .setFinishTime(LocalDateTimeUtil.of(gmtPayment));
            }
            // 非支付中响应码, 进行错误处理
            if (!Objects.equals(response.getCode(), AliPayCode.ResponseCode.INPROCESS)) {
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
        if (!Objects.equals(alipayResponse.getCode(), AliPayCode.ResponseCode.SUCCESS)) {
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
