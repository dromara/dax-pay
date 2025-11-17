package org.dromara.daxpay.payment.common.util;

import org.dromara.daxpay.payment.common.exception.OperationFailException;
import org.dromara.daxpay.payment.pay.enums.PayMethodEnum;
import org.dromara.daxpay.payment.pay.enums.PaymentVendorEnum;
import org.dromara.daxpay.payment.unipay.enums.CashierSceneEnum;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import org.springframework.util.LinkedMultiValueMap;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 支付工具类
 *
 * @author xxm
 * @since 2023/12/24
 */
@UtilityClass
public class PayUtil {
    private static final BigDecimal HUNDRED = new BigDecimal(100);

    /**
     * 获取支付单的超时时间
     */
    public LocalDateTime getPaymentExpiredTime(Integer minute) {
        return LocalDateTimeUtil.offset(LocalDateTime.now(), minute, ChronoUnit.MINUTES);
    }

    /**
     * 获取支付单的超时分钟数, 舍去秒数， 所以会有大约一分钟的误差
     */
    public int getPaymentExpiredTime(LocalDateTime date) {
        Duration duration = LocalDateTimeUtil.between(LocalDateTime.now(), date);
        return Math.toIntExact(duration.getSeconds() / 60);
    }

    /**
     * 元转分
     *
     * @param amount 元的金额
     * @return 分的金额
     */
    public int convertCentAmount(BigDecimal amount) {
        return amount.multiply(HUNDRED)
                .setScale(0, RoundingMode.HALF_UP)
                .intValue();
    }

    /**
     * 分转元,保留两位小数
     *
     * @param amount 元的金额
     * @return 元的金额 两位小数
     */
    public BigDecimal conversionAmount(int amount) {
        return BigDecimal.valueOf(amount)
                .divide(HUNDRED, 2, RoundingMode.HALF_UP);
    }

    /**
     * xx.xx%转换为基点表示费率(万分之多少)
     */
    public String toBasisPointRate(BigDecimal rate) {
        return Opt.ofBlankAble(rate)
                .map(o -> o.multiply(new BigDecimal("100"))
                        .intValue())
                .map(Object::toString)
                .orElse(null);
    }

    /**
     * 保留两位小数
     */
    public BigDecimal toDecimal(BigDecimal amount) {
        return amount.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * 获取请求参数
     */
    public Map<String, String> toMap(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();

        for (String name : requestParams.keySet()) {
            String[] values = requestParams.get(name);
            String valueStr = "";

            for (int i = 0; i < values.length; ++i) {
                valueStr = i == values.length - 1 ? valueStr + values[i] : valueStr + values[i] + ",";
            }

            params.put(name, valueStr);
        }
        return params;
    }

    /**
     * 获取支付厂商
     */
    public PaymentVendorEnum getPayVendor(String payMethod) {
        PayMethodEnum payMethodEnum = PayMethodEnum.findByCode(payMethod);
        // 不确定的支付方式
        if (List.of(PayMethodEnum.OTHER, PayMethodEnum.QRCODE).contains(payMethodEnum)){
            return null;
        }
        // 支付宝
        boolean isAli = List.of(PayMethodEnum.ALIPAY_QR, PayMethodEnum.ALIPAY_JSAPI, PayMethodEnum.ALIPAY_MINI, PayMethodEnum.ALIPAY_PC, PayMethodEnum.ALIPAY_H5,
                        PayMethodEnum.ALIPAY_APP)
                .contains(payMethodEnum);
        if (isAli) {
            return PaymentVendorEnum.ALIPAY;
        }
        // 微信
        boolean isWx = List.of(PayMethodEnum.WECHAT_JSAPI, PayMethodEnum.WECHAT_MINI, PayMethodEnum.WECHAT_QR, PayMethodEnum.WECHAT_H5,
                        PayMethodEnum.WECHAT_APP)
                .contains(payMethodEnum);
        if (isWx) {
            return PaymentVendorEnum.WECHAT;
        }
        // 银联
        if (List.of(PayMethodEnum.UNION_QR, PayMethodEnum.UNION_JSAPI)
                .contains(payMethodEnum)) {
            return PaymentVendorEnum.UNION_PAY;
        }
        return null;
    }

    /**
     * 获取付款码类型
     */
    public CashierSceneEnum getBarCodeType(String barCode) {
        // 支付宝
        String[] ali = {"25", "26", "27", "28", "29", "30"};
        if (StrUtil.startWithAny(barCode.substring(0, 2), ali)) {
            return CashierSceneEnum.ALIPAY;
        }
        // 微信
        String[] wx = {"10", "11", "12", "13", "14", "15"};
        if (StrUtil.startWithAny(barCode.substring(0, 2), wx)) {
            return CashierSceneEnum.WECHAT_PAY;
        }
        // 银联
        if (StrUtil.startWith(barCode.substring(0, 2), "62")) {
            return CashierSceneEnum.UNION_PAY;
        }
        throw new OperationFailException("不支持的条码类型");
    }

    /**
     * 佣金计算, 保留四位小数
     *
     * @param amount     金额
     * @param profitRate 佣金比例, 百分比
     */
    public BigDecimal calculateProfit(BigDecimal amount, BigDecimal profitRate) {
        BigDecimal profit = amount.multiply(profitRate.divide(new BigDecimal(100), 4, RoundingMode.HALF_EVEN));
        return profit.divide(BigDecimal.ONE, 4, RoundingMode.HALF_EVEN);
    }

    /**
     * 转换为RestClient使用的form表单参数
     */
    public LinkedMultiValueMap<String, String> toFormData(Map<String, ?> param) {
        // 转换为 MultiValueMap<String, String>
        var formData = new LinkedMultiValueMap<String, String>();
        // 注意：Object 要转 String
        param.forEach((key, value) -> formData.add(key, String.valueOf(value)));
        return formData;
    }

}
