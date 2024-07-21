package cn.daxpay.multi.channel.wechat.service.pay;

import cn.daxpay.multi.channel.wechat.entity.config.WechatPayConfig;
import cn.daxpay.multi.channel.wechat.param.pay.WechatPayParam;
import cn.daxpay.multi.channel.wechat.service.config.WechatPayConfigService;
import cn.daxpay.multi.core.enums.PayMethodEnum;
import cn.daxpay.multi.core.exception.TradeFailException;
import cn.daxpay.multi.core.util.PayUtil;
import cn.daxpay.multi.service.common.context.PayLocal;
import cn.daxpay.multi.service.common.local.PaymentContextLocal;
import cn.daxpay.multi.service.entity.order.pay.PayOrder;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.json.JSONUtil;
import com.github.binarywang.wxpay.bean.order.WxPayAppOrderResult;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayMicropayRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayMicropayResult;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信支付服务v2版本
 * @author xxm
 * @since 2024/7/17
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatPayV2Service {
    private final WechatPayConfigService wechatPayConfigService;

    /**
     * 调起支付
     */
    public void pay(PayOrder payOrder, WechatPayParam wechatPayParam, WechatPayConfig config) {

        PayLocal payInfo = PaymentContextLocal.get().getPayInfo();;
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
            this.barCodePay(payOrder, wechatPayParam.getAuthCode(), config);
        }
        payInfo.setPayBody(payBody);
    }

    /**
     * wap支付
     */
    @SneakyThrows
    private String wapPay(PayOrder payOrder, WechatPayConfig wechatPayConfig) {
        WxPayService wxPayService = wechatPayConfigService.wxJavaSdk(wechatPayConfig);
        WxPayUnifiedOrderRequest request = this.buildRequest(payOrder);
        try {
            var result = wxPayService.createOrder(WxPayConstants.TradeType.Specific.MWEB, request);
            return result.getMwebUrl();
        } catch (WxPayException e) {
            log.error("微信V2Wap支付失败", e);
            throw new TradeFailException("微信V2Wap支付失败");
        }
    }

    /**
     * 程序支付
     */
    private String appPay(PayOrder payOrder, WechatPayConfig wechatPayConfig) {
        WxPayService wxPayService = wechatPayConfigService.wxJavaSdk(wechatPayConfig);
        WxPayUnifiedOrderRequest request = this.buildRequest(payOrder);
        try {
            var result = wxPayService.createOrder(WxPayConstants.TradeType.Specific.APP, request);
            Map<String, String> map = this.buildAppResult(result);
            return JSONUtil.toJsonStr(map);
        } catch (WxPayException e) {
            log.error("微信V2App支付失败", e);
            throw new TradeFailException("微信V2App支付失败");
        }

    }

    /**
     * 微信公众号支付或者小程序支付
     */
    private String jsPay(PayOrder payOrder, String openId, WechatPayConfig wechatPayConfig) {
        WxPayService wxPayService = wechatPayConfigService.wxJavaSdk(wechatPayConfig);
        WxPayUnifiedOrderRequest request = this.buildRequest(payOrder);
        request.setOpenid(openId);
        try {
            var result = wxPayService.createOrder(WxPayConstants.TradeType.Specific.JSAPI, request);
            Map<String, String> map = this.buildJsapiResult(result);
            return JSONUtil.toJsonStr(map);
        } catch (WxPayException e) {
            log.error("微信V2Jaspi支付失败", e);
            throw new TradeFailException("微信V2Jaspi支付失败");
        }
    }

    /**
     * 二维码支付
     */
    private String qrCodePay(PayOrder payOrder, WechatPayConfig wechatPayConfig) {
        WxPayService wxPayService = wechatPayConfigService.wxJavaSdk(wechatPayConfig);
        WxPayUnifiedOrderRequest request = this.buildRequest(payOrder);
        try {
            var result = wxPayService.createOrder(WxPayConstants.TradeType.Specific.NATIVE, request);
            return  result.getCodeUrl();
        } catch (WxPayException e) {
            log.error("微信V2扫码支付失败", e);
            throw new TradeFailException("微信V2扫码支付失败");
        }
    }

    /**
     * 付款码支付
     */
    public void barCodePay(PayOrder payOrder, String authCode, WechatPayConfig wechatPayConfig) {
        WxPayService wxPayService = wechatPayConfigService.wxJavaSdk(wechatPayConfig);
        WxPayMicropayRequest request = new WxPayMicropayRequest();
        request.setAuthCode(authCode);
        request.setTotalFee(PayUtil.convertCentAmount(payOrder.getAmount()));
        request.setOutTradeNo(payOrder.getOrderNo());
        request.setSpbillCreateIp(payOrder.getClientIp());
        request.setDetail(payOrder.getDescription());
        request.setBody(payOrder.getTitle());
        request.setProfitSharing(payOrder.getAutoAllocation()?"Y":"N");
        try {
            WxPayMicropayResult micropay = wxPayService.micropay(request);
        } catch (WxPayException e) {
            log.error("微信V2付款码支付失败", e);
            throw new TradeFailException("微信V2付款码支付失败");
        }
    }

    /**
     * 转换APP参数
     */
    private Map<String, String> buildAppResult(WxPayAppOrderResult result){
        Map<String, String> packageParams = new HashMap<>(6);
        packageParams.put("appid", result.getAppId());
        packageParams.put("partnerid", result.getPartnerId());
        packageParams.put("prepayid", result.getPrepayId());
        packageParams.put("package", result.getPackageValue());
        packageParams.put("nonceStr", result.getNonceStr());
        packageParams.put("timeStamp", result.getTimeStamp());
        packageParams.put("sign", result.getSign());
        return packageParams;
    }

    /**
     * 转换Jsapi参数
     */
    private Map<String, String> buildJsapiResult(WxPayMpOrderResult result){
        Map<String, String> packageParams = new HashMap<>(6);
        packageParams.put("appId", result.getAppId());
        packageParams.put("timeStamp", result.getTimeStamp());
        packageParams.put("nonceStr", result.getNonceStr());
        packageParams.put("package", result.getPackageValue());
        packageParams.put("signType", result.getSignType());
        packageParams.put("paySign", result.getPaySign());
        return packageParams;
    }

    /**
     * 构建下单请求参数
     */
    private WxPayUnifiedOrderRequest buildRequest(PayOrder payOrder){
        WxPayUnifiedOrderRequest request = new WxPayUnifiedOrderRequest();
        request.setOutTradeNo(payOrder.getOrderNo());
        request.setTimeExpire(this.getExpiredTime(payOrder.getExpiredTime()));
        request.setNotifyUrl(wechatPayConfigService.getPayNotifyUrl());
        request.setTotalFee(PayUtil.convertCentAmount(payOrder.getAmount()));
        request.setNotifyUrl(wechatPayConfigService.getPayNotifyUrl());
        request.setSpbillCreateIp(payOrder.getClientIp());
        return request;
    }

    /**
     * 获取微信的过期时间 yyyyMMddHHmmss
     */
    private String getExpiredTime(LocalDateTime dateTime) {
        return LocalDateTimeUtil.format(dateTime, DatePattern.PURE_DATETIME_PATTERN);
    }
}
