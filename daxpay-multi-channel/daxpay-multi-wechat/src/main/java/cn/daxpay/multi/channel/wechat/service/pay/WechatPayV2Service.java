package cn.daxpay.multi.channel.wechat.service.pay;

import cn.bootx.platform.common.spring.exception.RetryableException;
import cn.daxpay.multi.channel.wechat.entity.config.WechatPayConfig;
import cn.daxpay.multi.channel.wechat.param.pay.WechatPayParam;
import cn.daxpay.multi.channel.wechat.service.config.WechatPayConfigService;
import cn.daxpay.multi.channel.wechat.util.WechatPayUtil;
import cn.daxpay.multi.core.enums.PayMethodEnum;
import cn.daxpay.multi.core.exception.TradeFailException;
import cn.daxpay.multi.core.util.PayUtil;
import cn.daxpay.multi.service.bo.trade.PayResultBo;
import cn.daxpay.multi.service.entity.order.pay.PayOrder;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.github.binarywang.wxpay.bean.order.WxPayAppOrderResult;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayMicropayRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayMicropayResult;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.constant.WxPayConstants.WxpayTradeStatus;
import com.github.binarywang.wxpay.constant.WxPayErrorCode;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
    public PayResultBo pay(PayOrder payOrder, WechatPayParam wechatPayParam, WechatPayConfig config) {
        PayResultBo payInfo = new PayResultBo();
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
            this.barCodePay(payOrder, wechatPayParam.getAuthCode(), config, payInfo);
            return payInfo;
        }
        payInfo.setPayBody(payBody);
        return payInfo;
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
            throw new TradeFailException("微信V2手机网站支付失败: "+e.getMessage());
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
            throw new TradeFailException("微信V2App程序支付失败: "+e.getMessage());
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
            throw new TradeFailException("微信V2Jaspi支付失败: "+e.getMessage());
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
            throw new TradeFailException("微信V2扫码支付失败: "+e.getMessage());
        }
    }

    /**
     * 付款码支付
     */
    public void barCodePay(PayOrder payOrder, String authCode, WechatPayConfig wechatPayConfig, PayResultBo payResult) {
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
            WxPayMicropayResult result = wxPayService.micropay(request);
            // 支付成功处理,
            String timeEnd = result.getTimeEnd();
            LocalDateTime time = WechatPayUtil.parseV2(timeEnd);
            payResult.setOutOrderNo(result.getTransactionId())
                    .setFinishTime(time)
                    .setComplete(true);
        } catch (WxPayException e) {
            String errCode = e.getErrCode();
            String resultCode = e.getResultCode();
            // 支付中, 发起轮训同步
            // 提交支付请求后微信会同步返回支付结果。当返回结果为“系统错误”时，商户系统等待5秒后调用【查询订单API】
            // 查询支付实际交易结果；当返回结果为“USERPAYING”时，商户系统可设置间隔时间(建议10秒)重新查询支付结果，直到支付成功或超时(建议45秒)；
            if (Objects.equals(resultCode, WxPayErrorCode.UnifiedOrder.SYSTEMERROR)
                    || Objects.equals(errCode, WxpayTradeStatus.USER_PAYING)) {
                SpringUtil.getBean(this.getClass()).rotationSync(payOrder);
                return;
            }
            log.error("微信V2付款码支付失败", e);
            throw new TradeFailException("微信V2付款码支付失败: "+e.getMessage());
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


    /**
     * 多次重试同步支付状态, 最多10次, 30秒不操作微信会自动关闭
     */
    @Async
    @Retryable(retryFor = RetryableException.class, maxAttempts = 10, backoff = @Backoff(value = 5000L))
    public void rotationSync(PayOrder payOrder) {
//        PaySyncResult paySyncResult = paySyncService.syncPayOrder(payOrder);
//        // 不为支付中状态后, 调用系统同步更新状态, 支付状态则继续重试
//        if (Objects.equals(PROGRESS.getCode(), paySyncResult.getStatus())) {
//            throw new RetryableException();
//        }
    }
}
