package cn.daxpay.single.service.core.channel.alipay.service;

import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.daxpay.single.core.code.PayMethodEnum;
import cn.daxpay.single.core.exception.*;
import cn.daxpay.single.core.param.channel.AliPayParam;
import cn.daxpay.single.core.param.payment.pay.PayParam;
import cn.daxpay.single.core.util.PayUtil;
import cn.daxpay.single.service.code.AliPayCode;
import cn.daxpay.single.service.code.AliPayWay;
import cn.daxpay.single.service.common.context.PayLocal;
import cn.daxpay.single.service.common.local.PaymentContextLocal;
import cn.daxpay.single.service.core.channel.alipay.entity.AliPayConfig;
import cn.daxpay.single.service.core.order.pay.entity.PayOrder;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Method;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayResponse;
import com.alipay.api.domain.*;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

import static cn.daxpay.single.service.code.AliPayCode.QUICK_MSECURITY_PAY;


/**
 * 支付宝支付服务
 *
 * @author xxm
 * @since 2021/2/26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AliPayService {

    private final AliPayConfigService aliPayConfigService;

    /**
     * 支付前检查支付方式是否可用
     */
    public void validation(PayParam payParam, AliPayConfig alipayConfig) {

        if (CollUtil.isEmpty(alipayConfig.getPayWays())){
            throw new ConfigErrorException("支付宝未配置可用的支付方式");
        }
        // 发起的支付类型是否在支持的范围内
        PayMethodEnum payMethodEnum = Optional.ofNullable(AliPayWay.findByCode(payParam.getMethod()))
                .orElseThrow(() -> new MethodNotExistException("非法的支付宝支付类型"));
        if (!alipayConfig.getPayWays().contains(payMethodEnum.getCode())) {
            throw new MethodNotEnableException("该支付宝支付方式不可用");
        }
        // 验证订单金额是否超限
        if(payParam.getAmount() > alipayConfig.getLimitAmount()){
            throw new AmountExceedLimitException("支付宝支付金额超过限额");
        }
        // 支付参数开启分账, 配置未开启分账
        if(Objects.equals(payParam.getAllocation(),true) && !Objects.equals(alipayConfig.getAllocation(),true)){
            throw new ConfigErrorException("未开启分账配置");
        }
    }

    /**
     * 调起支付
     */
    public void pay(PayOrder payOrder, AliPayParam aliPayParam, AliPayConfig alipayConfig) {
        String amount = PayUtil.conversionAmount(payOrder.getAmount()).toString();
        String payBody = null;
        // 异步线程存储
        PayLocal payInfo = PaymentContextLocal.get().getPayInfo();
        // wap支付
        if (Objects.equals(payOrder.getMethod(), PayMethodEnum.WAP.getCode())) {
            payBody = this.wapPay(amount, payOrder, alipayConfig);
        }
        // 程序支付
        else if (Objects.equals(payOrder.getMethod(), PayMethodEnum.APP.getCode())) {
            payBody = this.appPay(amount, payOrder, alipayConfig);
        }
        // pc支付
        else if (Objects.equals(payOrder.getMethod(), PayMethodEnum.WEB.getCode())) {
            payBody = this.webPay(amount, payOrder, alipayConfig);
        }
        // jsapi支付
        else if (Objects.equals(payOrder.getMethod(), PayMethodEnum.JSAPI.getCode())) {
            payBody = this.jsapiPay(amount, payOrder, aliPayParam, alipayConfig);
        }
        // 二维码支付
        else if (Objects.equals(payOrder.getMethod(), PayMethodEnum.QRCODE.getCode())) {
            payBody = this.qrCodePay(amount, payOrder, alipayConfig);
        }
        // 付款码支付
        else if (Objects.equals(payOrder.getMethod(), PayMethodEnum.BARCODE.getCode())) {
            this.barCode(amount, payOrder, aliPayParam, alipayConfig);
        }
        // 通常是发起支付的参数
        payInfo.setPayBody(payBody);
    }

    /**
     * wap支付
     */
    @SneakyThrows
    public String wapPay(String amount, PayOrder payOrder, AliPayConfig alipayConfig) {
        // 获取支付宝客户端
        AlipayClient alipayClient = aliPayConfigService.getAlipayClient(alipayConfig);

        AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
        model.setSubject(payOrder.getTitle());
        model.setOutTradeNo(payOrder.getOrderNo());
        model.setTotalAmount(amount);
        // 过期时间
        model.setTimeExpire(PayUtil.getAliTimeExpire(payOrder.getExpiredTime()));
        model.setProductCode(AliPayCode.QUICK_WAP_PAY);
        // 是否分账
        if (payOrder.getAllocation()){
            ExtendParams extendParams = new ExtendParams();
            extendParams.setRoyaltyFreeze(Boolean.TRUE.toString());
            model.setExtendParams(extendParams);
        }

        AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
        request.setBizModel(model);
        // 异步回调必须到当前支付网关系统中, 然后系统负责转发
        request.setNotifyUrl(alipayConfig.getNotifyUrl());
        // 同步回调地址必须到当前支付网关系统中, 然后系统负责跳转
        request.setReturnUrl(alipayConfig.getReturnUrl());

        try {
            // 通过GET方式的请求, 返回URL的响应, 默认是POST方式的请求, 返回的是表单响应
            AlipayTradeWapPayResponse response = alipayClient.pageExecute(request, Method.GET.name());
            return response.getBody();
        }
        catch (AlipayApiException e) {
            log.error("支付宝手机支付失败", e);
            throw new TradeFailException("支付宝手机支付失败");
        }
    }

    /**
     * app支付
     */
    @SneakyThrows
    public String appPay(String amount, PayOrder payOrder, AliPayConfig alipayConfig) {
        // 获取支付宝客户端
        AlipayClient alipayClient = aliPayConfigService.getAlipayClient(alipayConfig);

        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setSubject(payOrder.getTitle());
        model.setProductCode(QUICK_MSECURITY_PAY);
        model.setOutTradeNo(payOrder.getOrderNo());
        // 过期时间
        model.setTimeExpire(PayUtil.getAliTimeExpire(payOrder.getExpiredTime()));
        model.setTotalAmount(amount);
        // 是否分账
        if (payOrder.getAllocation()){
            ExtendParams extendParams = new ExtendParams();
            extendParams.setRoyaltyFreeze(Boolean.TRUE.toString());
            model.setExtendParams(extendParams);
        }
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        request.setBizModel(model);
        request.setNotifyUrl(alipayConfig.getNotifyUrl());
        try {
            // 异步回调必须到当前系统中
            AlipayTradeAppPayResponse response = alipayClient.execute(request);
            return response.getBody();
        }
        catch (AlipayApiException e) {
            log.error("支付宝APP支付失败", e);
            throw new TradeFailException("支付宝APP支付失败");
        }
    }

    /**
     * PC支付
     */
    @SneakyThrows
    public String webPay(String amount, PayOrder payOrder, AliPayConfig alipayConfig) {
        // 获取支付宝客户端
        AlipayClient alipayClient = aliPayConfigService.getAlipayClient(alipayConfig);

        AlipayTradePagePayModel model = new AlipayTradePagePayModel();
        model.setSubject(payOrder.getTitle());
        model.setOutTradeNo(payOrder.getOrderNo());
        // 过期时间
        model.setTimeExpire(PayUtil.getAliTimeExpire(payOrder.getExpiredTime()));
        model.setTotalAmount(amount);
        // 目前仅支持FAST_INSTANT_TRADE_PAY
        model.setProductCode(AliPayCode.FAST_INSTANT_TRADE_PAY);

        // 是否分账
        if (payOrder.getAllocation()){
            ExtendParams extendParams = new ExtendParams();
            extendParams.setRoyaltyFreeze(Boolean.TRUE.toString());
            model.setExtendParams(extendParams);
        }

        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setBizModel(model);
        // 异步回调必须到当前系统中
        request.setNotifyUrl(alipayConfig.getNotifyUrl());
        // 同步回调
        request.setReturnUrl(alipayConfig.getReturnUrl());
        try {
            // 通过GET方式的请求, 返回URL的响应, 默认是POST方式的请求, 返回的是表单响应
            AlipayTradePagePayResponse response = alipayClient.pageExecute(request, Method.GET.name());
            return response.getBody();
        }
        catch (AlipayApiException e) {
            log.error("支付宝PC支付失败", e);
            throw new TradeFailException("支付宝PC支付失败");
        }
    }

    /**
     * jsapi支付
     */
    @SneakyThrows
    public String jsapiPay(String amount, PayOrder payOrder, AliPayParam aliPayParam, AliPayConfig alipayConfig) {
        // 获取支付宝客户端
        AlipayClient alipayClient = aliPayConfigService.getAlipayClient(alipayConfig);

        // 构造请求参数以调用接口
        AlipayTradeCreateRequest request = new AlipayTradeCreateRequest();
        AlipayTradeCreateModel model = new AlipayTradeCreateModel();
        model.setOutTradeNo(payOrder.getOrderNo());
        model.setProductCode(AliPayCode.JSAPI_PAY);
        model.setOpAppId(aliPayParam.getOpAppId());
        model.setTotalAmount(amount);
        model.setSubject(payOrder.getTitle());
        model.setOpBuyerOpenId(aliPayParam.getOpenId());
        model.setTimeExpire(PayUtil.getAliTimeExpire(payOrder.getExpiredTime()));
        // 是否分账
        if (payOrder.getAllocation()){
            ExtendParams extendParams = new ExtendParams();
            extendParams.setRoyaltyFreeze(Boolean.TRUE.toString());
            model.setExtendParams(extendParams);
        }
        request.setBizModel(model);
        try {
            AlipayTradeCreateResponse response = alipayClient.execute(request);
            this.verifyErrorMsg(response);
            // my.tradePay使用支付宝交易号调起支付
            return response.getTradeNo();
        } catch (AlipayApiException e) {
            log.error("支付宝JsApi支付失败", e);
            throw new TradeFailException("支付宝JsApi支付失败");
        }
    }

    /**
     * 二维码支付(扫码支付)
     */
    @SneakyThrows
    public String qrCodePay(String amount, PayOrder payOrder, AliPayConfig alipayConfig) {
        // 获取支付宝客户端
        AlipayClient alipayClient = aliPayConfigService.getAlipayClient(alipayConfig);

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
        model.setTimeExpire(PayUtil.getAliTimeExpire(payOrder.getExpiredTime()));
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        request.setBizModel(model);
        request.setNotifyUrl(alipayConfig.getNotifyUrl());
        try {
            AlipayTradePrecreateResponse response = alipayClient.execute(request);
            this.verifyErrorMsg(response);
            return response.getQrCode();
        }
        catch (AlipayApiException e) {
            log.error("支付宝手机支付失败", e);
            throw new TradeFailException("支付宝手机支付失败");
        }
    }

    /**
     * 付款码支付
     */
    @SneakyThrows
    public void barCode(String amount, PayOrder payOrder, AliPayParam aliPayParam, AliPayConfig alipayConfig) {
        // 获取支付宝客户端
        AlipayClient alipayClient = aliPayConfigService.getAlipayClient(alipayConfig);

        PayLocal payInfo = PaymentContextLocal.get().getPayInfo();
        AlipayTradePayModel model = new AlipayTradePayModel();
        model.setSubject(payOrder.getTitle());
        model.setOutTradeNo(payOrder.getOrderNo());
        model.setScene(AliPayCode.BAR_CODE);
        model.setAuthCode(aliPayParam.getAuthCode());
        // 是否分账
        if (payOrder.getAllocation()){
            ExtendParams extendParams = new ExtendParams();
            extendParams.setRoyaltyFreeze(Boolean.TRUE.toString());
            model.setExtendParams(extendParams);
        }
        // 过期时间
        model.setTimeExpire(PayUtil.getAliTimeExpire(payOrder.getExpiredTime()));
        model.setTotalAmount(amount);
        AlipayTradePayRequest request = new AlipayTradePayRequest();
        request.setBizModel(model);
        request.setNotifyUrl(alipayConfig.getNotifyUrl());
        try {
            AlipayTradePayResponse response = alipayClient.execute(request);
            // 支付成功处理 金额2000以下免密支付, 记录支付完成相关信息
            if (Objects.equals(response.getCode(), AliPayCode.SUCCESS)) {
                Date gmtPayment = response.getGmtPayment();
                payInfo.setOutOrderNo(response.getTradeNo())
                        .setComplete(true)
                        .setFinishTime(LocalDateTimeUtil.of(gmtPayment));
            }
            // 非支付中响应码, 进行错误处理
            if (!Objects.equals(response.getCode(), AliPayCode.INPROCESS)) {
                this.verifyErrorMsg(response);
            }
        }
        catch (AlipayApiException e) {
            log.error("主动扫码支付失败", e);
            throw new TradeFailException("主动扫码支付失败");
        }
    }

    /**
     * 验证错误信息
     */
    private void verifyErrorMsg(AlipayResponse alipayResponse) {
        if (!Objects.equals(alipayResponse.getCode(), AliPayCode.SUCCESS)) {
            String errorMsg = alipayResponse.getSubMsg();
            if (StrUtil.isBlank(errorMsg)) {
                errorMsg = alipayResponse.getMsg();
            }
            log.error("支付失败 {}", errorMsg);
            throw new TradeFailException(errorMsg);
        }
    }

}
