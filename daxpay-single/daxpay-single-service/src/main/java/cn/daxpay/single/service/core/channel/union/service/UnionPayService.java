package cn.daxpay.single.service.core.channel.union.service;

import cn.daxpay.single.code.PayMethodEnum;
import cn.daxpay.single.exception.pay.PayFailureException;
import cn.daxpay.single.param.channel.UnionPayParam;
import cn.daxpay.single.param.payment.pay.PayParam;
import cn.daxpay.single.service.code.UnionPayCode;
import cn.daxpay.single.service.code.UnionPayWay;
import cn.daxpay.single.service.common.context.PayLocal;
import cn.daxpay.single.service.common.local.PaymentContextLocal;
import cn.daxpay.single.service.core.channel.union.entity.UnionPayConfig;
import cn.daxpay.single.service.core.order.pay.entity.PayOrder;
import cn.daxpay.single.service.sdk.union.api.UnionPayKit;
import cn.daxpay.single.service.sdk.union.bean.UnionPayOrder;
import cn.daxpay.single.util.PayUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import com.egzosn.pay.common.bean.NoticeParams;
import com.egzosn.pay.union.bean.UnionTransactionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
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
    public void validation(PayParam payParam, UnionPayConfig unionPayConfig) {

        if (CollUtil.isEmpty(unionPayConfig.getPayWays())){
            throw new PayFailureException("云闪付未配置可用的支付方式");
        }
        // 发起的支付类型是否在支持的范围内
        PayMethodEnum payMethodEnum = Optional.ofNullable(UnionPayWay.findByCode(payParam.getMethod()))
                .orElseThrow(() -> new PayFailureException("非法的云闪付支付类型"));
        if (!unionPayConfig.getPayWays().contains(payMethodEnum.getCode())) {
            throw new PayFailureException("该云闪付支付方式不可用");
        }
        // 支付金额是否超限
        if (payParam.getAmount() > unionPayConfig.getSingleLimit()) {
            throw new PayFailureException("云闪付支付金额超限");
        }
        // 分账
        if (Objects.equals(payParam.getAllocation(),true)) {
            throw new PayFailureException("云闪付不支持分账");
        }
    }

    /**
     * 支付接口
     */
    public void pay(PayOrder payOrder, UnionPayParam unionPayParam, UnionPayKit unionPayKit){
        BigDecimal totalFee =  PayUtil.conversionAmount(payOrder.getAmount());
        PayLocal payInfo = PaymentContextLocal.get().getPayInfo();
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
        // b2b支付 TODO 未完成
        else if (payMethodEnum == PayMethodEnum.B2B) {
            payBody = this.b2bPay(totalFee, payOrder, unionPayKit);
        }

        payInfo.setPayBody(payBody);
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
        return unionPayKit.toPay(unionPayOrder);
    }

    /**
     * jsapi支付
     */
    private String b2bPay(BigDecimal amount, PayOrder payOrder, UnionPayKit unionPayKit) {
        Date expiredTime = DateUtil.date(payOrder.getExpiredTime());

        UnionPayOrder unionPayOrder = new UnionPayOrder();
        unionPayOrder.setOutTradeNo(payOrder.getOrderNo());
        unionPayOrder.setSubject(payOrder.getTitle());
        unionPayOrder.setPrice(amount);
        unionPayOrder.setExpirationTime(expiredTime);
        unionPayOrder.setTransactionType(UnionTransactionType.B2B);
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

        Map<String, Object> result = unionPayKit.app(unionPayOrder);
        String resultCode = MapUtil.getStr(result, UnionPayCode.RESP_CODE);

        // 支付失败
        if (!(Objects.equals(resultCode, UnionPayCode.RESP_SUCCESS))) {
            log.warn("云闪付支付失败:{}", result);
            String errMsg = MapUtil.getStr(result, UnionPayCode.RESP_MSG);
            throw new PayFailureException(errMsg);
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
        Map<String, Object> result = unionPayKit.microPay(unionPayOrder);

        if (!unionPayKit.verify(new NoticeParams(result))) {
            log.warn("云闪付支付验签失败:{}", result);
            throw new PayFailureException("云闪付支付验签失败");
        }

        String resultCode = MapUtil.getStr(result, UnionPayCode.RESP_CODE);

        // 支付失败
        if (!(Objects.equals(resultCode, UnionPayCode.RESP_SUCCESS))) {
            log.warn("云闪付支付失败:{}", result);
            String errMsg = MapUtil.getStr(result, UnionPayCode.RESP_MSG);
            throw new PayFailureException(errMsg);
        }
    }
}
