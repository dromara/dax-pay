package cn.daxpay.multi.channel.wechat.service.pay;

import cn.daxpay.multi.channel.wechat.entity.config.WechatPayConfig;
import cn.daxpay.multi.channel.wechat.param.pay.WechatPayParam;
import cn.daxpay.multi.channel.wechat.service.config.WechatPayConfigService;
import cn.daxpay.multi.channel.wechat.util.WechatPayUtil;
import cn.daxpay.multi.core.enums.PayMethodEnum;
import cn.daxpay.multi.core.exception.TradeFailException;
import cn.daxpay.multi.core.util.PayUtil;
import cn.daxpay.multi.service.bo.trade.PayResultBo;
import cn.daxpay.multi.service.entity.order.pay.PayOrder;
import cn.hutool.json.JSONUtil;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderV3Request;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderV3Result;
import com.github.binarywang.wxpay.bean.result.enums.TradeTypeEnum;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

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

    private final WechatPayV2Service wechatPayV2Service;

    /**
     * 调起支付
     */
    public PayResultBo pay(PayOrder payOrder, WechatPayParam wechatPayParam, WechatPayConfig config) {
        PayResultBo payResult = new PayResultBo();
        String payBody = null;
        PayMethodEnum payMethodEnum = PayMethodEnum.findByCode(payOrder.getMethod());

        // wap支付
        if (payMethodEnum == PayMethodEnum.WAP) {
            payBody = this.wapPay(payOrder, config);
        }
        // APP支付
        else if (payMethodEnum == PayMethodEnum.APP) {
            payBody = this.appPay(payOrder, config);
        }
        // 微信公众号支付或者小程序支付
        else if (payMethodEnum == PayMethodEnum.JSAPI) {
            payBody = this.jsPay(payOrder, wechatPayParam.getOpenId(), config);
        }
        // 二维码支付
        else if (payMethodEnum == PayMethodEnum.QRCODE) {
            payBody = this.qrCodePay(payOrder, config);
        }
        // 付款码支付
        else if (payMethodEnum == PayMethodEnum.BARCODE) {
            wechatPayV2Service.barCodePay(payOrder, wechatPayParam.getAuthCode(), config, payResult);
        }
        payResult.setPayBody(payBody);
        return payResult;
    }

    /**
     * wap支付
     */
    private String wapPay(PayOrder payOrder, WechatPayConfig wechatPayConfig) {
        WxPayService wxPayService = wechatPayConfigService.wxJavaSdk(wechatPayConfig);
        var request = this.buildRequest(payOrder);
        try {
            // h5Url
            return wxPayService.createOrderV3(TradeTypeEnum.H5, request);
        } catch (WxPayException e) {
            log.error("微信V3手机网站支付失败", e);
            throw new TradeFailException("微信V3手机网站支付失败: "+e.getMessage());
        }
    }

    /**
     * 程序支付
     */
    private String appPay(PayOrder payOrder, WechatPayConfig wechatPayConfig) {
        WxPayService wxPayService = wechatPayConfigService.wxJavaSdk(wechatPayConfig);
        var request = this.buildRequest(payOrder);
        try {
            WxPayUnifiedOrderV3Result.AppResult result = wxPayService.createOrderV3(TradeTypeEnum.APP, request);
            Map<String, String> map = this.buildAppResult(result);
            return JSONUtil.toJsonStr(map);
        } catch (WxPayException e) {
            log.error("微信V3程序支付失败", e);
            throw new TradeFailException("微信V3程序支付失败: "+e.getMessage());
        }
    }

    /**
     * 微信公众号支付或者小程序支付
     */
    private String jsPay(PayOrder payOrder, String openId, WechatPayConfig wechatPayConfig) {
        WxPayService wxPayService = wechatPayConfigService.wxJavaSdk(wechatPayConfig);
        var request = this.buildRequest(payOrder);
        WxPayUnifiedOrderV3Request.Payer payer = new WxPayUnifiedOrderV3Request.Payer();
        payer.setOpenid(openId);
        request.setPayer(payer);
        try {
            // h5Url
            WxPayUnifiedOrderV3Result.JsapiResult result = wxPayService.createOrderV3(TradeTypeEnum.JSAPI, request);
            Map<String, String> map = this.buildJsapiResult(result);
            return JSONUtil.toJsonStr(map);
        } catch (WxPayException e) {
            log.error("微信V3JsApi支付失败", e);
            throw new TradeFailException("微信V3JsApi支付失败: "+e.getMessage());
        }
    }

    /**
     * 二维码支付
     */
    private String qrCodePay(PayOrder payOrder, WechatPayConfig wechatPayConfig) {
        WxPayService wxPayService = wechatPayConfigService.wxJavaSdk(wechatPayConfig);
        var request = this.buildRequest(payOrder);
        try {
            return wxPayService.createOrderV3(TradeTypeEnum.NATIVE, request);
        } catch (WxPayException e) {
            log.error("微信V3扫码支付失败", e);
            throw new TradeFailException("微信V3扫码支付失败: "+e.getMessage());
        }
    }

    /**
     * 转换APP参数
     */
    private Map<String, String> buildAppResult(WxPayUnifiedOrderV3Result.AppResult result){
        Map<String, String> packageParams = new HashMap<>(6);
        packageParams.put("appid", result.getAppid());
        packageParams.put("partnerid", result.getPartnerId());
        packageParams.put("prepayid", result.getPrepayId());
        packageParams.put("package", result.getPackageValue());
        packageParams.put("nonceStr", result.getNoncestr());
        packageParams.put("timeStamp", result.getTimestamp());
        packageParams.put("sign", result.getSign());
        return packageParams;
    }

    /**
     * 转换Jsapi参数
     */
    private Map<String, String> buildJsapiResult(WxPayUnifiedOrderV3Result.JsapiResult result){
        Map<String, String> packageParams = new HashMap<>(6);
        packageParams.put("appId", result.getAppId());
        packageParams.put("timeStamp", result.getTimeStamp());
        packageParams.put("nonceStr", result.getNonceStr());
        packageParams.put("package", result.getPackageValue());
        String signType = result.getSignType();
        if (result.getSignType() == null) {
            signType = "MD5";
        }
        packageParams.put("signType", signType);
        packageParams.put("paySign", result.getPaySign());
        return packageParams;
    }


    /**
     * 构建请求参数
     */
    public WxPayUnifiedOrderV3Request buildRequest(PayOrder payOrder){
        WxPayUnifiedOrderV3Request request = new WxPayUnifiedOrderV3Request();
        WxPayUnifiedOrderV3Request.Amount amount = new WxPayUnifiedOrderV3Request.Amount();
        amount.setTotal(PayUtil.convertCentAmount(payOrder.getAmount()));
        request.setDescription(payOrder.getDescription());
        request.setOutTradeNo(payOrder.getOrderNo());
        request.setTimeExpire(WechatPayUtil.formatV3(payOrder.getExpiredTime()));
        if (payOrder.getAllocation()){
            var settleInfo = new WxPayUnifiedOrderV3Request.SettleInfo();
            settleInfo.setProfitSharing(true);
            request.setSettleInfo(settleInfo);
        }
        request.setNotifyUrl(wechatPayConfigService.getPayNotifyUrl());
        request.setAmount(amount);
        return request;
    }

}

