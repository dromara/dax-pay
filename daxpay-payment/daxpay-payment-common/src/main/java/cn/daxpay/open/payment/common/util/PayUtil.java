package cn.daxpay.open.payment.common.util;

import cn.daxpay.open.platform.core.exception.operation.OperationFailException;
import cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.PayProviderEnum;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import org.springframework.util.LinkedMultiValueMap;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import cn.daxpay.open.platform.core.code.CommonCode;

/// # 支付工具类
///
@UtilityClass
public class PayUtil {

    /// 获取支付单的超时时间
    public OffsetDateTime getPaymentExpiredTime(Integer minute) {
        return OffsetDateTime.now(ZoneOffset.UTC).plus(minute, ChronoUnit.MINUTES);
    }

    /// 获取支付单的超时分钟数, 舍去秒数， 所以会有大约一分钟的误差
    public int getPaymentExpiredTime(OffsetDateTime date) {
        Duration duration = Duration.between(OffsetDateTime.now(ZoneOffset.UTC), date);
        return Math.toIntExact(duration.getSeconds() / 60);
    }

    /// 保留两位小数
    public BigDecimal toDecimal(BigDecimal amount) {
        return amount.setScale(2, RoundingMode.HALF_UP);
    }

    /// 获取请求参数
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

    /// 是否为付款码类支付方式
    public boolean isBarcodePayMethod(String methodCode) {
        if (StrUtil.isBlank(methodCode)) {
            return false;
        }
        PayMethodEnum method = PayMethodEnum.findByCode(methodCode);
        return method == PayMethodEnum.AGGREGATE_PAY_BARCODE
                || method == PayMethodEnum.WECHAT_BARCODE
                || method == PayMethodEnum.ALIPAY_BARCODE
                || method == PayMethodEnum.UNION_PAY_BARCODE;
    }

    /// 获取付款码类型
    public PayProviderEnum getBarCodeType(String barCode) {
        // 支付宝
        String[] ali = {"25", "26", "27", "28", "29", "30"};
        if (StrUtil.startWithAny(barCode.substring(0, 2), ali)) {
            return PayProviderEnum.ALIPAY;
        }
        // 微信
        String[] wx = {"10", "11", "12", "13", "14", "15"};
        if (StrUtil.startWithAny(barCode.substring(0, 2), wx)) {
            return PayProviderEnum.WECHAT;
        }
        // 银联
        if (StrUtil.startWith(barCode.substring(0, 2), "62")) {
            return PayProviderEnum.UNION_PAY;
        }
        // 订单: 不支持的条码类型
        throw new OperationFailException(CommonCode.FAIL_CODE, "error.payment.order.unsupportedBarcodeType");
    }

    /// 转换为RestClient使用的form表单参数
    public LinkedMultiValueMap<String, String> toFormData(Map<String, ?> param) {
        // 转换为 MultiValueMap<String, String>
        var formData = new LinkedMultiValueMap<String, String>();
        // 注意：Object 要转 String
        param.forEach((key, value) -> formData.add(key, String.valueOf(value)));
        return formData;
    }

    /// 转换为Get请求使用的QueryParam参数，不进行编码
    public String toQueryParam(Map<String, String> param) {
        StringBuilder sb = new StringBuilder();
        param.forEach((key, value) -> sb.append(key).append("=").append(URLEncoder.encode(value, Charset.defaultCharset())).append("&"));
        if (!sb.isEmpty()) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

}
