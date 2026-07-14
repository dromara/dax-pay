package cn.daxpay.open.payment.common.util;

import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.platform.core.code.CommonCode;
import lombok.experimental.UtilityClass;

/// # 支付相应参数构造工具类
///
@UtilityClass
public class DaxRes {

    public <T> DaxResult<T> ok(T data) {
        return new DaxResult<>(CommonCode.SUCCESS_CODE, data, CommonCode.SUCCESS_MSG);
    }

    public static <T> DaxResult<T> ok() {
        return new DaxResult<>(CommonCode.SUCCESS_CODE, CommonCode.SUCCESS_MSG);
    }
}
