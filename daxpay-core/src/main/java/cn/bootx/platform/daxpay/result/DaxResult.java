package cn.bootx.platform.daxpay.result;

import cn.bootx.platform.common.core.rest.ResResult;
import lombok.*;

/**
 * 支付通用响应参数
 * @author xxm
 * @since 2023/12/17
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class DaxResult<T> extends ResResult<T> {

    /** 数据签名 */
    private String sign;

    public DaxResult(int successCode, T data, String success) {
        super(successCode, data, success);
    }

    public DaxResult(int successCode, String success) {
        super(successCode, success);
    }
}
