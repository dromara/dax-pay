package org.dromara.daxpay.core.util;

import org.dromara.daxpay.core.result.DaxResult;
import lombok.experimental.UtilityClass;

import static cn.bootx.platform.core.code.CommonCode.SUCCESS_CODE;

/**
 * 支付相应参数构造工具类
 * @author xxm
 * @since 2023/12/17
 */
@UtilityClass
public class DaxRes {
    private final static String SUCCESS = "success";

    public <T> DaxResult<T> ok(T data) {
        return new DaxResult<>(SUCCESS_CODE, data, SUCCESS);
    }

    public static <T> DaxResult<T> ok() {
        return new DaxResult<>(SUCCESS_CODE, SUCCESS);
    }
}
