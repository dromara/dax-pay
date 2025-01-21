package org.dromara.daxpay.channel.wechat.service.pay.isv;

import cn.bootx.platform.common.spring.exception.RetryableException;
import cn.bootx.platform.core.util.JsonUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.github.binarywang.wxpay.bean.request.WxPayPartnerUnifiedOrderV3Request;
import com.github.binarywang.wxpay.bean.result.WxPayCodepayResult;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderV3Result;
import com.github.binarywang.wxpay.bean.result.enums.TradeTypeEnum;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.constant.WxPayErrorCode;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.channel.wechat.code.WechatPayCode;
import org.dromara.daxpay.channel.wechat.entity.config.WechatPayConfig;
import org.dromara.daxpay.channel.wechat.param.pay.WechatPayParam;
import org.dromara.daxpay.channel.wechat.param.pay.WxPayPartnerCodepayRequest;
import org.dromara.daxpay.channel.wechat.service.config.WechatPayConfigService;
import org.dromara.daxpay.channel.wechat.util.WechatPayUtil;
import org.dromara.daxpay.core.enums.PayMethodEnum;
import org.dromara.daxpay.core.enums.PayStatusEnum;
import org.dromara.daxpay.core.exception.MethodNotExistException;
import org.dromara.daxpay.core.exception.TradeFailException;
import org.dromara.daxpay.core.result.trade.pay.PaySyncResult;
import org.dromara.daxpay.core.util.PayUtil;
import org.dromara.daxpay.service.bo.trade.PayResultBo;
import org.dromara.daxpay.service.entity.order.pay.PayOrder;
import org.dromara.daxpay.service.service.trade.pay.PaySyncService;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 微信支付服务v3版本
 * @author xxm
 * @since 2024/7/17
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatPaySubV3Service {
    private final WechatPayConfigService wechatPayConfigService;

    private static final Gson GSON = new GsonBuilder().create();
    private final PaySyncService paySyncService;

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
            payBody = this.jsPay(payOrder, wechatPayParam, config);
        }
        // 二维码支付
        else if (payMethodEnum == PayMethodEnum.QRCODE) {
            payBody = this.qrCodePay(payOrder, config);
        }
        // 付款码支付
        else if (payMethodEnum == PayMethodEnum.BARCODE) {
            this.barCodePay(payOrder, wechatPayParam.getAuthCode(), config, payResult);
        } else {
            throw new MethodNotExistException("不支持的支付方式");
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
        var sceneInfo = new WxPayPartnerUnifiedOrderV3Request.SceneInfo();
        sceneInfo.setPayerClientIp(payOrder.getClientIp());
        var h5Info = new WxPayPartnerUnifiedOrderV3Request.H5Info();
        h5Info.setType("Wap");
        sceneInfo.setH5Info(h5Info);
        request.setSceneInfo(sceneInfo);
        try {
            // h5Url
            return wxPayService.createPartnerOrderV3(TradeTypeEnum.H5, request);
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
            WxPayUnifiedOrderV3Result.AppResult result = wxPayService.createPartnerOrderV3(TradeTypeEnum.APP, request);
            Map<String, String> map = this.buildAppResult(result);
            return JsonUtil.toJsonStr(map);
        } catch (WxPayException e) {
            log.error("微信V3程序支付失败", e);
            throw new TradeFailException("微信V3程序支付失败: "+e.getMessage());
        }
    }

    /**
     * 微信公众号支付或者小程序支付
     */
    private String jsPay(PayOrder payOrder, WechatPayParam wechatPayParam, WechatPayConfig wechatPayConfig) {
        WxPayService wxPayService = wechatPayConfigService.wxJavaSdk(wechatPayConfig);
        var request = this.buildRequest(payOrder);
        WxPayPartnerUnifiedOrderV3Request.Payer payer = new WxPayPartnerUnifiedOrderV3Request.Payer();
        if (Objects.equals(wechatPayParam.getOpenIdType(), WechatPayCode.SUB_OPENID)){
            payer.setSubOpenid(wechatPayParam.getOpenId());
        } else {
            payer.setSpOpenid(wechatPayParam.getOpenId());
        }
        request.setPayer(payer);
        try {
            WxPayUnifiedOrderV3Result.JsapiResult result = wxPayService.createPartnerOrderV3(TradeTypeEnum.JSAPI, request);
            Map<String, String> map = this.buildJsapiResult(result);
            return JsonUtil.toJsonStr(map);
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
            return wxPayService.createPartnerOrderV3(TradeTypeEnum.NATIVE, request);
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
     * 付款码支付
     */
    private void barCodePay(PayOrder payOrder, String authCode, WechatPayConfig config, PayResultBo payResult) {
        WxPayService wxPayService = wechatPayConfigService.wxJavaSdk(config);
        try {
            var request = new WxPayPartnerCodepayRequest();
            // 设置公共属性
            WxPayConfig wxPayConfig = wxPayService.getConfig();
            request.setSpMchId(wxPayConfig.getMchId())
                    .setSpAppid(wxPayConfig.getAppId())
                    .setSubMchId(config.getSubMchId())
                    .setSubAppid(config.getSubAppId());

            request.setDescription(payOrder.getTitle());
            request.setOutTradeNo(payOrder.getOrderNo());

            // 金额
            var amount = new WxPayPartnerCodepayRequest.Amount();
            amount.setTotal(PayUtil.convertCentAmount(payOrder.getAmount()));
            request.setAmount(amount);

            // 场景信息
            var sceneInfo = new WxPayPartnerCodepayRequest.SceneInfo();
            var storeInfo = new WxPayPartnerCodepayRequest.StoreInfo();
            storeInfo.setOutId("1");
            sceneInfo.setStoreInfo(storeInfo);
            request.setSceneInfo(sceneInfo);

            // 条码参数
            var payer = new WxPayPartnerCodepayRequest.Payer();
            payer.setAuthCode(authCode);
            request.setPayer(payer);

            // 分账参数
            if (payOrder.getAllocation()){
                var settleInfo = new WxPayPartnerCodepayRequest.SettleInfo();
                settleInfo.setProfitSharing(true);
                request.setSettleInfo(settleInfo);
            }
            // 拼接和发送请求
            String url = String.format("%s/v3/pay/partner/transactions/codepay", wxPayService.getPayBaseUrl());
            String body = wxPayService.postV3(url, GSON.toJson(request));
            var result = GSON.fromJson(body, WxPayCodepayResult.class);

            // 支付成功处理, 如果不成功会走异常流
            payResult.setComplete(true)
                    .setFinishTime(WechatPayUtil.parseV3(result.getSuccessTime()))
                    .setOutOrderNo(result.getTransactionId());
        } catch (WxPayException e) {
            String errCode = e.getErrCode();
            String resultCode = e.getResultCode();
            // 支付中, 发起轮训同步
            // 提交支付请求后微信会同步返回支付结果。当返回结果为“系统错误”时，商户系统等待5秒后调用【查询订单API】
            // 查询支付实际交易结果；当返回结果为“USERPAYING”时，商户系统可设置间隔时间(建议10秒)重新查询支付结果，直到支付成功或超时(建议45秒)；
            if (Objects.equals(resultCode, WxPayErrorCode.UnifiedOrder.SYSTEMERROR)
                    || Objects.equals(errCode, WxPayConstants.WxpayTradeStatus.USER_PAYING)) {
                SpringUtil.getBean(this.getClass()).rotationSync(payOrder);
                return;
            }
            log.error("微信付款码支付V3失败", e);
            throw new TradeFailException("微信付款码支付V3失败: "+e.getMessage());
        }
    }

    /**
     * 构建请求参数
     */
    public WxPayPartnerUnifiedOrderV3Request buildRequest(PayOrder payOrder){
        WxPayPartnerUnifiedOrderV3Request request = new WxPayPartnerUnifiedOrderV3Request();
        WxPayPartnerUnifiedOrderV3Request.Amount amount = new WxPayPartnerUnifiedOrderV3Request.Amount();
        amount.setTotal(PayUtil.convertCentAmount(payOrder.getAmount()));
        request.setDescription(payOrder.getTitle());
        request.setOutTradeNo(payOrder.getOrderNo());
        request.setTimeExpire(WechatPayUtil.formatV3(payOrder.getExpiredTime()));
        if (payOrder.getAllocation()){
            var settleInfo = new WxPayPartnerUnifiedOrderV3Request.SettleInfo();
            settleInfo.setProfitSharing(true);
            request.setSettleInfo(settleInfo);
        }
        request.setNotifyUrl(wechatPayConfigService.getPayNotifyUrl(true));
        request.setAmount(amount);
        return request;
    }


    /**
     * 多次重试同步支付状态, 最多10次, 30秒不操作微信会自动关闭
     */
    @Async
    @Retryable(retryFor = RetryableException.class, maxAttempts = 10, backoff = @Backoff(value = 5000L))
    public void rotationSync(PayOrder payOrder) {
        PaySyncResult paySyncResult = paySyncService.syncPayOrder(payOrder);
        // 不为支付中状态后, 调用系统同步更新状态, 支付状态则继续重试
        if (Objects.equals(PayStatusEnum.PROGRESS.getCode(), paySyncResult.getOrderStatus())) {
            throw new RetryableException();
        }
    }
}

