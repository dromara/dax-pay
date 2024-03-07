package cn.bootx.platform.daxpay.service.core.channel.union.service;

import cn.bootx.platform.daxpay.code.PayWayEnum;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.param.channel.UnionPayParam;
import cn.bootx.platform.daxpay.param.pay.PayChannelParam;
import cn.bootx.platform.daxpay.service.code.AliPayWay;
import cn.bootx.platform.daxpay.service.code.UnionPayCode;
import cn.bootx.platform.daxpay.service.common.context.PayLocal;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.channel.union.entity.UnionPayConfig;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.util.PayUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.ijpay.core.enums.SignType;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.unionpay.UnionPayApi;
import com.ijpay.unionpay.enums.ServiceEnum;
import com.ijpay.unionpay.model.MicroPayModel;
import com.ijpay.unionpay.model.UnifiedOrderModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static cn.bootx.platform.daxpay.service.code.UnionPayCode.*;

/**
 * 云闪付支付
 * @author xxm
 * @since 2022/3/11
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UnionPayService {


    /**
     * 支付前检查支付方式是否可用
     */
    public void validation(PayChannelParam payChannelParam, UnionPayConfig unionPayConfig) {

        if (CollUtil.isEmpty(unionPayConfig.getPayWays())){
            throw new PayFailureException("云闪付未配置可用的支付方式");
        }
        // 发起的支付类型是否在支持的范围内
        PayWayEnum payWayEnum = Optional.ofNullable(AliPayWay.findByCode(payChannelParam.getWay()))
                .orElseThrow(() -> new PayFailureException("非法的云闪付支付类型"));
        if (!unionPayConfig.getPayWays().contains(payWayEnum.getCode())) {
            throw new PayFailureException("该云闪付支付方式不可用");
        }
    }

    /**
     * 支付接口
     */
    public void pay(PayOrder payOrder, PayChannelParam payChannelParam, UnionPayParam unionPayParam, UnionPayConfig unionPayConfig){
        Integer amount = payChannelParam.getAmount();
        String totalFee = String.valueOf(amount);
        PayLocal asyncPayInfo = PaymentContextLocal.get().getPayInfo();;
        String payBody = null;
        PayWayEnum payWayEnum = PayWayEnum.findByCode(payChannelParam.getWay());

        // 微信APP支付
        if (payWayEnum == PayWayEnum.APP) {
            payBody = this.wxAppPay(totalFee, payOrder, unionPayParam, unionPayConfig);
        }
        // 微信公众号支付或者小程序支付
        else if (payWayEnum == PayWayEnum.JSAPI_WX_PAY) {
            payBody = this.wxJsPay(totalFee, payOrder, unionPayParam.getOpenId(), unionPayConfig);
        }
        // 支付宝JS支付
        else if (payWayEnum == PayWayEnum.JSAPI_ALI_PAY) {
            payBody = this.aliJsPay(totalFee, payOrder, unionPayParam, unionPayConfig);
        }
        // 银联JS支付
        else if (payWayEnum == PayWayEnum.JSAPI) {
            payBody = this.unionJsPay(totalFee, payOrder, unionPayParam, unionPayConfig);
        }
        // 二维码支付
        else if (payWayEnum == PayWayEnum.QRCODE) {
            payBody = this.qrCodePay(totalFee, payOrder, unionPayConfig);
        }
        // 付款码支付
        else if (payWayEnum == PayWayEnum.BARCODE) {
            this.barCodePay(totalFee, payOrder, unionPayParam.getAuthCode(), unionPayConfig);
        }

        asyncPayInfo.setPayBody(payBody);
    }

    /**
     * 支付宝生活号支付
     */
    private String aliJsPay(String amount, PayOrder payOrder, UnionPayParam unionPayParam, UnionPayConfig unionPayConfig) {
        Map<String, String> params = UnifiedOrderModel.builder()
                .service(ServiceEnum.ALI_PAY_JS_PAY.toString())
                .mch_id(unionPayConfig.getMachId())
                .out_trade_no(WxPayKit.generateStr())
                .body(payOrder.getTitle())
                .total_fee(amount)
                .mch_create_ip("127.0.0.15")
                .notify_url(unionPayConfig.getNotifyUrl())
                .nonce_str(WxPayKit.generateStr())
                .buyer_id(unionPayParam.getBuyerId())
                .build()
                .createSign(unionPayConfig.getAppKey(), SignType.MD5);

        String xmlResult = UnionPayApi.execution(unionPayConfig.getServerUrl(), params);
        Map<String, String> result = WxPayKit.xmlToMap(xmlResult);
        this.verifyErrorMsg(result);
        return null;
    }

    /**
     * 银联JS支付
     */
    private String unionJsPay(String amount, PayOrder payOrder, UnionPayParam unionPayParam, UnionPayConfig unionPayConfig) {
        Map<String, String> params = UnifiedOrderModel.builder()
                .service(ServiceEnum.UNION_JS_PAY.toString())
                .mch_id(unionPayConfig.getMachId())
                .out_trade_no(WxPayKit.generateStr())
                .body(payOrder.getTitle())
                .user_id(unionPayParam.getUserId())
                .customer_ip(unionPayParam.getCustomerIp())
                .total_fee(amount)
                .mch_create_ip("127.0.0.1")
                .notify_url(unionPayConfig.getNotifyUrl())
                .nonce_str(WxPayKit.generateStr())
                .build()
                .createSign(unionPayConfig.getAppKey(), SignType.MD5);

        System.out.println(params);

        String xmlResult = UnionPayApi.execution(unionPayConfig.getServerUrl(), params);
        Map<String, String> result = WxPayKit.xmlToMap(xmlResult);
        this.verifyErrorMsg(result);
        return null;
    }

    /**
     * 微信APP支付
     */
    private String wxAppPay(String amount, PayOrder payOrder, UnionPayParam unionPayParam, UnionPayConfig unionPayConfig) {
        Map<String, String> params = UnifiedOrderModel.builder()
                .service(ServiceEnum.WEI_XIN_APP_PAY.toString())
                .mch_id(unionPayConfig.getMachId())
                .appid(unionPayParam.getAppId())
                .sub_appid(unionPayParam.getSubAppId())
                .out_trade_no(WxPayKit.generateStr())
                .body(payOrder.getTitle())
                .attach("聚合支付 SDK")
                .total_fee(amount)
                .mch_create_ip("127.0.0.1")
                .notify_url(unionPayConfig.getNotifyUrl())
                .nonce_str(WxPayKit.generateStr())
                .build()
                .createSign(unionPayConfig.getAppKey(), SignType.MD5);

        String xmlResult = UnionPayApi.execution(unionPayConfig.getServerUrl(), params);
        Map<String, String> result = WxPayKit.xmlToMap(xmlResult);
        this.verifyErrorMsg(result);
        return null;
    }

    /**
     * 微信公众号支付或者小程序支付
     */
    private String wxJsPay(String amount, PayOrder payOrder, String openId, UnionPayConfig unionPayConfig) {

        Map<String, String> params = UnifiedOrderModel.builder()
                .service(ServiceEnum.WEI_XIN_JS_PAY.toString())
                .mch_id(unionPayConfig.getMachId())
                // 原生JS 值为1
                .is_raw("1")
                .out_trade_no(WxPayKit.generateStr())
                .body(payOrder.getTitle())
                .sub_openid(openId)
                .total_fee(amount)
                .mch_create_ip("127.0.0.1")
                .notify_url(unionPayConfig.getNotifyUrl())
                .nonce_str(WxPayKit.generateStr())
                .build()
                .createSign(unionPayConfig.getAppKey(), SignType.MD5);
        String xmlResult = UnionPayApi.execution(unionPayConfig.getServerUrl(), params);
        Map<String, String> result = WxPayKit.xmlToMap(xmlResult);
        this.verifyErrorMsg(result);
        return null;
    }

    /**
     * 扫码支付
     */
    private String qrCodePay(String amount, PayOrder payOrder, UnionPayConfig config){
        Map<String, String> params = UnifiedOrderModel.builder()
                .service(ServiceEnum.NATIVE.toString())
                .mch_id(config.getMachId())
                .out_trade_no(String.valueOf(payOrder.getId()))
                .body(payOrder.getTitle())
                .total_fee(amount)
                .time_expire(PayUtil.getUnionExpiredTime(payOrder.getExpiredTime()))
                .mch_create_ip("127.0.0.1")
                .notify_url(config.getNotifyUrl())
                .nonce_str(WxPayKit.generateStr())
                .build()
                .createSign(config.getAppKey(), SignType.MD5);
        String xmlResult = UnionPayApi.execution(config.getServerUrl(), params);
        Map<String, String> result = WxPayKit.xmlToMap(xmlResult);
        this.verifyErrorMsg(result);
        return result.get("code_url");
    }

    /**
     * 付款码支付
     */
    private void barCodePay(String amount, PayOrder payOrder, String authCode, UnionPayConfig unionPayConfig) {
        Map<String, String> params = MicroPayModel.builder()
                .service(ServiceEnum.MICRO_PAY.toString())
                .mch_id(unionPayConfig.getMachId())
                .out_trade_no(WxPayKit.generateStr())
                .body(payOrder.getTitle())
                .total_fee(amount)
                .op_device_id("daxpay")
                .mch_create_ip("127.0.0.1")
                .auth_code(authCode)
                .nonce_str(WxPayKit.generateStr())
                .build()
                .createSign(unionPayConfig.getAppKey(), SignType.MD5);

        String xmlResult = UnionPayApi.execution(unionPayConfig.getServerUrl(), params);

    }

    /**
     * 验证错误信息
     */
    private void verifyErrorMsg(Map<String, String> result) {
        String status = result.get(UnionPayCode.STATUS);
        String returnCode = result.get(UnionPayCode.RESULT_CODE);

        // 判断查询是否成功
        if (!(Objects.equals(SUCCESS, status) && Objects.equals(SUCCESS, returnCode))){
            String errorMsg = result.get(ERR_MSG);
            if (StrUtil.isBlank(errorMsg)) {
                errorMsg = result.get(MESSAGE);
            }
            log.error("订单关闭失败 {}", errorMsg);
            throw new PayFailureException(errorMsg);
        }
    }

}
