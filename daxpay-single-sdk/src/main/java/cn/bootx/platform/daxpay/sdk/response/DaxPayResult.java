package cn.bootx.platform.daxpay.sdk.response;

import cn.bootx.platform.daxpay.sdk.net.DaxPayResponseModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
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

    /** 数据签名 */
    private String sign;

    /** 追踪ID */
    private String traceId;
}
