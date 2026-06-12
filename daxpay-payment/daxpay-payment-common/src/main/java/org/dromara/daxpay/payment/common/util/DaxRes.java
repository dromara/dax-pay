package org.dromara.daxpay.payment.common.util;

import org.dromara.daxpay.payment.common.result.DaxResult;
import lombok.experimental.UtilityClass;

import static org.dromara.daxpay.platform.core.code.CommonCode.SUCCESS_CODE;
import static org.dromara.daxpay.platform.core.code.CommonCode.SUCCESS_MSG;

/// # 支付相应参数构造工具类
///
@UtilityClass
public class DaxRes {

    public <T> DaxResult<T> ok(T data) {
        return new DaxResult<>(SUCCESS_CODE, data, SUCCESS_MSG);
    }

    public static <T> DaxResult<T> ok() {
        return new DaxResult<>(SUCCESS_CODE, SUCCESS_MSG);
    }
}
