package cn.daxpay.open.payment.common.util;

import cn.daxpay.open.payment.common.result.DaxResult;
import lombok.experimental.UtilityClass;

import static cn.daxpay.open.platform.core.code.CommonCode.SUCCESS_CODE;
import static cn.daxpay.open.platform.core.code.CommonCode.SUCCESS_MSG;

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
