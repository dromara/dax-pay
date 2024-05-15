package cn.daxpay.single.sdk.response;

import cn.daxpay.single.sdk.net.DaxPayResponseModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 响应参数接收类
 * @author xxm
 * @since 2024/2/2
 */
@Getter
@Setter
@ToString
public class DaxPayResult<T extends DaxPayResponseModel> {

    /** 提示信息 */
    private String msg = "success";

    /** 响应码 */
    private int code = 0;

    /** 响应体 */
    private T data;

    /** 追踪ID */
    private String traceId;
}
