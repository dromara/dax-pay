package org.dromara.daxpay.channel.union.service.pay;

import cn.bootx.platform.core.exception.ValidationFailedException;
import cn.bootx.platform.core.util.BigDecimalUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.channel.union.code.UnionPayCode;
import org.dromara.daxpay.channel.union.entity.config.UnionPayConfig;
import org.dromara.daxpay.channel.union.param.pay.UnionPayParam;
import org.dromara.daxpay.channel.union.sdk.api.UnionPayKit;
import org.dromara.daxpay.channel.union.sdk.bean.UnionPayOrder;
import org.dromara.daxpay.channel.union.sdk.bean.UnionTransactionType;
import org.dromara.daxpay.channel.union.service.config.UnionPayConfigService;
import org.dromara.daxpay.core.enums.PayMethodEnum;
import org.dromara.daxpay.core.exception.AmountExceedLimitException;
import org.dromara.daxpay.core.exception.TradeFailException;
import org.dromara.daxpay.core.param.trade.pay.PayParam;
import org.dromara.daxpay.service.bo.trade.PayResultBo;
import org.dromara.daxpay.service.entity.order.pay.PayOrder;
import org.dromara.daxpay.unisdk.common.bean.NoticeParams;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * 云闪付支付
 * @author xxm
 * @since 2022/3/11
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UnionPayService {

    private final UnionPayConfigService configService;

    /**
     * 支付前检查支付方式是否可用
     */
    public void validation(PayParam payParam, UnionPayConfig unionPayConfig) {

        // 支付金额是否超限
        if (BigDecimalUtil.isGreaterThan(payParam.getAmount(),unionPayConfig.getLimitAmount())) {
            throw new AmountExceedLimitException("微信支付金额超限");
        }
    }

    /**
     * 支付接口
     */
    public PayResultBo pay(PayOrder payOrder, UnionPayParam unionPayParam, UnionPayKit unionPayKit){
        BigDecimal totalFee = payOrder.getAmount();
        String payBody = null;
        PayMethodEnum payMethodEnum = PayMethodEnum.findByCode(payOrder.getMethod());

        // 二维码支付
        if (payMethodEnum == PayMethodEnum.QRCODE) {
            payBody = this.qrCodePay(totalFee, payOrder, unionPayKit);
        }
        // 付款码支付
        else if (payMethodEnum == PayMethodEnum.BARCODE) {
            this.barCodePay(totalFee, payOrder, unionPayParam.getAuthCode(), unionPayKit);
        }
        // APP支付
        else if (payMethodEnum == PayMethodEnum.APP) {
            payBody = this.appPay(totalFee, payOrder, unionPayParam, unionPayKit);
        }
        // web支付
        else if (payMethodEnum == PayMethodEnum.WEB) {
            payBody = this.formPay(totalFee, payOrder, unionPayKit, UnionTransactionType.WEB);
        }
        // wap支付
        else if (payMethodEnum == PayMethodEnum.WAP) {
            payBody = this.formPay(totalFee, payOrder, unionPayKit, UnionTransactionType.WAP );
        }

        return new PayResultBo().setPayBody(payBody);
    }

    /**
     * jsapi支付
     */
    private String formPay(BigDecimal amount, PayOrder payOrder, UnionPayKit unionPayKit, UnionTransactionType type) {
        Date expiredTime = DateUtil.date(payOrder.getExpiredTime());

        UnionPayOrder unionPayOrder = new UnionPayOrder();
        unionPayOrder.setOutTradeNo(payOrder.getOrderNo());
        unionPayOrder.setSubject(payOrder.getTitle());
        unionPayOrder.setPrice(amount);
        unionPayOrder.setExpirationTime(expiredTime);
        unionPayOrder.setTransactionType(type);
        unionPayOrder.setNotifyUrl(configService.getPayNotifyUrl());
        return unionPayKit.toPay(unionPayOrder);
    }


    /**
     * APP支付
     */
    private String appPay(BigDecimal amount, PayOrder payOrder, UnionPayParam unionPayParam, UnionPayKit unionPayKit) {

        Date expiredTime = DateUtil.date(payOrder.getExpiredTime());


        UnionPayOrder unionPayOrder = new UnionPayOrder();
        unionPayOrder.setOutTradeNo(payOrder.getOrderNo());
        unionPayOrder.setSubject(payOrder.getTitle());
        unionPayOrder.setPrice(amount);
        unionPayOrder.setExpirationTime(expiredTime);
        unionPayOrder.setNotifyUrl(configService.getPayNotifyUrl());

        Map<String, Object> result = unionPayKit.app(unionPayOrder);
        String resultCode = MapUtil.getStr(result, UnionPayCode.RESP_CODE);

        // 支付失败
        if (!(Objects.equals(resultCode, UnionPayCode.RESP_SUCCESS))) {
            log.warn("云闪付支付失败:{}", result);
            String errMsg = MapUtil.getStr(result, UnionPayCode.RESP_MSG);
            throw new TradeFailException(errMsg);
        }

        return MapUtil.getStr(result, UnionPayCode.PAY_APP_TN);
    }

    /**
     * 扫码支付
     */
    private String qrCodePay(BigDecimal amount, PayOrder payOrder, UnionPayKit unionPayKit){
        Date expiredTime = DateUtil.date(payOrder.getExpiredTime());

        UnionPayOrder unionPayOrder = new UnionPayOrder();
        unionPayOrder.setOutTradeNo(payOrder.getOrderNo());
        unionPayOrder.setSubject(payOrder.getTitle());
        unionPayOrder.setPrice(amount);
        unionPayOrder.setExpirationTime(expiredTime);
        unionPayOrder.setNotifyUrl(configService.getPayNotifyUrl());
        return unionPayKit.getQrPay(unionPayOrder);
    }

    /**
     * 付款码支付
     */
    private void barCodePay(BigDecimal amount, PayOrder payOrder, String authCode, UnionPayKit unionPayKit) {
        Date expiredTime = DateUtil.date(payOrder.getExpiredTime());

        UnionPayOrder unionPayOrder = new UnionPayOrder();
        unionPayOrder.setAuthCode(authCode);
        unionPayOrder.setOutTradeNo(payOrder.getOrderNo());
        unionPayOrder.setSubject(payOrder.getTitle());
        unionPayOrder.setPrice(amount);
        unionPayOrder.setExpirationTime(expiredTime);
        unionPayOrder.setNotifyUrl(configService.getPayNotifyUrl());
        Map<String, Object> result = unionPayKit.microPay(unionPayOrder);

        if (!unionPayKit.verify(new NoticeParams(result))) {
            log.warn("云闪付支付验签失败:{}", result);
            throw new ValidationFailedException("云闪付支付验签失败");
        }

        String resultCode = MapUtil.getStr(result, UnionPayCode.RESP_CODE);

        // 支付失败
        if (!(Objects.equals(resultCode, UnionPayCode.RESP_SUCCESS))) {
            log.warn("云闪付支付失败:{}", result);
            String errMsg = MapUtil.getStr(result, UnionPayCode.RESP_MSG);
            throw new TradeFailException(errMsg);
        }
    }
}
