package org.dromara.daxpay.payment.pay.strategy;

import cn.hutool.core.util.StrUtil;
import org.dromara.daxpay.payment.pay.enums.PayMethodEnum;
import org.dromara.daxpay.payment.unipay.enums.GatewayCallTypeEnum;
import org.dromara.daxpay.payment.unipay.param.gateway.AggregateQrPayParam;
import org.dromara.daxpay.payment.unipay.param.trade.pay.PayParam;

import java.util.List;

import static org.dromara.daxpay.payment.pay.enums.PayMethodEnum.*;


/**
 * 抽象网关支付策略
 * @author xxm
 * @since 2024/12/2
 */
public abstract class AbsGatewayPayStrategy implements PaymentStrategy{

    /**
     * 支付参数处理(收银台)
     */
    public void handlePayParam(AggregateQrPayParam cashierPayParam, PayParam payParam) {
    }

    /**
     * 收银台码牌授权认证路径
     */
    public String getWxCashierAuthPath(String cashierCode) {
        return StrUtil.format("/wechat/cashier/code/{}/0/{}",getChannel(), cashierCode);
    }

    /**
     * 聚合支付授权认证路径
     */
    public String getWxAggregateAuthPath(String orderNo) {
        return StrUtil.format("/aggregate/wechat/{}/0/{}", getChannel(), orderNo);
    }

    /**
     * 获取微信类支付类型, 第一个作为默认支付方式(扫码聚合类)
     */
    public List<PayMethodEnum> wechatPayMethods() {
        return List.of(WECHAT_JSAPI, WECHAT_H5);
    }

    /**
     * 获取支付宝类支付类型, 第一个作为默认支付方式(扫码聚合类)
     */
    public List<PayMethodEnum> alipayPayMethods() {
        return List.of(ALIPAY_QR, ALIPAY_H5, ALIPAY_JSAPI);
    }

    /**
     * 银联类支付方式, 第一个作为默认支付方式(扫码聚合类)
     */
    public List<PayMethodEnum> unionPayMethods() {
        return List.of(UNION_JSAPI, UNION_QR);
    }

    /**
     * 获取是否需要OpenId认证
     */
    public boolean isNeedOpenIdAuth(String payMethod){
        var methodEnum = findByCode(payMethod);
        // jsapi方式
        return List.of(WECHAT_JSAPI, ALIPAY_JSAPI, UNION_JSAPI).contains(methodEnum);
    }

    /**
     * 获取调用方式
     */
    public GatewayCallTypeEnum getCallType(String payMethod){
        var methodEnum = findByCode(payMethod);
        // 跳转链接
        if(List.of(QRCODE, WECHAT_QR, ALIPAY_QR, UNION_QR, ALIPAY_H5, WECHAT_H5, ALIPAY_PC).contains(methodEnum)){
            return GatewayCallTypeEnum.LINK;
        }
        // jsapi
        if(List.of(WECHAT_JSAPI, ALIPAY_JSAPI, UNION_JSAPI).contains(methodEnum)){
            return GatewayCallTypeEnum.JSAPI;
        }
        // 表单
        return null;
    }

}
