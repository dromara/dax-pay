package cn.daxpay.open.payment.common.util;

import lombok.experimental.UtilityClass;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

/// # 支付工具类
///
@UtilityClass
public class PayUtil {

    /// 获取支付单的超时时间
    public OffsetDateTime getPaymentExpiredTime(Integer minute) {
        return OffsetDateTime.now(ZoneOffset.UTC).plus(minute, ChronoUnit.MINUTES);
    }

}
