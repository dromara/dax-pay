package cn.bootx.platform.daxpay.service.core.channel.union.service;

import cn.bootx.platform.daxpay.code.PayWayEnum;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.param.channel.UnionPayParam;
import cn.bootx.platform.daxpay.param.pay.PayChannelParam;
import cn.bootx.platform.daxpay.service.code.AliPayWay;
import cn.bootx.platform.daxpay.service.common.context.PayLocal;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.channel.union.entity.UnionPayConfig;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.service.sdk.union.api.UnionPayKit;
import cn.bootx.platform.daxpay.service.sdk.union.bean.UnionPayOrder;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

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
    public void pay(PayOrder payOrder, PayChannelParam payChannelParam, UnionPayParam unionPayParam, UnionPayKit unionPayKit){
        Integer amount = payChannelParam.getAmount();
        BigDecimal totalFee = BigDecimal.valueOf(amount * 0.01);
        PayLocal asyncPayInfo = PaymentContextLocal.get().getPayInfo();;
        String payBody = null;
        PayWayEnum payWayEnum = PayWayEnum.findByCode(payChannelParam.getWay());

        // 二维码支付
        if (payWayEnum == PayWayEnum.QRCODE) {
            payBody = this.qrCodePay(totalFee, payOrder, unionPayKit);
        }
        // 付款码支付
        else if (payWayEnum == PayWayEnum.BARCODE) {
            this.barCodePay(totalFee, payOrder, unionPayParam.getAuthCode(), unionPayKit);
        }
        // APP支付
        else if (payWayEnum == PayWayEnum.APP) {
            payBody = this.appPay(totalFee, payOrder, unionPayParam, unionPayKit);
        }

        asyncPayInfo.setPayBody(payBody);
    }

    /**
     * APP支付
     */
    private String appPay(BigDecimal amount, PayOrder payOrder, UnionPayParam unionPayParam, UnionPayKit unionPayKit) {

        Date expiredTime = DateUtil.date(payOrder.getExpiredTime());


        UnionPayOrder unionPayOrder = new UnionPayOrder();
        unionPayOrder.setOutTradeNo(String.valueOf(payOrder.getId()));
        unionPayOrder.setSubject(payOrder.getTitle());
        unionPayOrder.setPrice(amount);
        unionPayOrder.setExpirationTime(expiredTime);

        Map<String, Object> app = unionPayKit.app(unionPayOrder);
        return null;
    }

    /**
     * 扫码支付
     */
    private String qrCodePay(BigDecimal amount, PayOrder payOrder, UnionPayKit unionPayKit){
        Date expiredTime = DateUtil.date(payOrder.getExpiredTime());

        UnionPayOrder unionPayOrder = new UnionPayOrder();

        unionPayOrder.setOutTradeNo(String.valueOf(payOrder.getId()));
        unionPayOrder.setSubject(payOrder.getTitle());
        unionPayOrder.setPrice(amount);
        unionPayOrder.setExpirationTime(expiredTime);

        return unionPayKit.getQrPay(unionPayOrder);


//        Map<String, String> params = UnifiedOrderModel.builder()
//                .service(ServiceEnum.NATIVE.toString())
//                .mch_id(unionPayKit.getMachId())
//                .out_trade_no(String.valueOf(payOrder.getId()))
//                .body(payOrder.getTitle())
//                .total_fee(amount)
//                .time_expire(PayUtil.getUnionExpiredTime(payOrder.getExpiredTime()))
//                .mch_create_ip("127.0.0.1")
//                .notify_url(unionPayKit.getNotifyUrl())
//                .nonce_str(WxPayKit.generateStr())
//                .build()
//                .createSign(unionPayKit.getAppKey(), SignType.MD5);
//        String xmlResult = UnionPayApi.execution(unionPayKit.getServerUrl(), params);
//        Map<String, String> result = WxPayKit.xmlToMap(xmlResult);
//        this.verifyErrorMsg(result);
//        return result.get("code_url");
    }

    /**
     * 付款码支付
     */
    private void barCodePay(BigDecimal amount, PayOrder payOrder, String authCode, UnionPayKit unionPayKit) {
        Date expiredTime = DateUtil.date(payOrder.getExpiredTime());

        UnionPayOrder unionPayOrder = new UnionPayOrder();

        unionPayOrder.setAuthCode(authCode);
        unionPayOrder.setOutTradeNo(String.valueOf(payOrder.getId()));
        unionPayOrder.setSubject(payOrder.getTitle());
        unionPayOrder.setPrice(amount);
        unionPayOrder.setExpirationTime(expiredTime);
        Map<String, Object> stringObjectMap = unionPayKit.microPay(unionPayOrder);

//        Map<String, String> params = MicroPayModel.builder()
//                .service(ServiceEnum.MICRO_PAY.toString())
//                .mch_id(unionPayKit.getMachId())
//                .out_trade_no(WxPayKit.generateStr())
//                .body(payOrder.getTitle())
//                .total_fee(amount)
//                .op_device_id("daxpay")
//                .mch_create_ip("127.0.0.1")
//                .auth_code(authCode)
//                .nonce_str(WxPayKit.generateStr())
//                .build()
//                .createSign(unionPayKit.getAppKey(), SignType.MD5);
//
//        String xmlResult = UnionPayApi.execution(unionPayKit.getServerUrl(), params);

    }
}
