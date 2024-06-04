package cn.daxpay.single.sdk.net;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author xxm
 * @since 2024/2/2
 */
@Setter
@Getter
@ToString(callSuper = true)
public abstract class DaxPayResponseModel {

    /** 状态码 */
    private Integer code;

    /** 错误信息 */
    private String msg;

    /** 响应时间 */
    private Long resTime;

    /** 响应数据签名值 */
    private String sign;
}
