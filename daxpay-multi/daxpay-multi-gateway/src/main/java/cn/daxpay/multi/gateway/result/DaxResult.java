package cn.daxpay.multi.gateway.result;

import cn.bootx.platform.core.code.CommonCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.MDC;

/**
 * 支付通用响应参数
 * @author xxm
 * @since 2023/12/17
 */
@Getter
@Setter
@ToString(callSuper = true)
public class DaxResult<T>{

    /** 追踪ID */
    private String traceId = MDC.get(CommonCode.TRACE_ID);

    public DaxResult(int successCode, T data, String success) {
    }

    public DaxResult(int successCode, String success) {
    }
}
