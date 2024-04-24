package cn.bootx.platform.daxpay.sdk.net;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 *
 * @author xxm
 * @since 2024/2/2
 */
@Setter
@Getter
@ToString
public abstract class DaxPayResponseModel {

    /** 响应数据签名值 */
    private String sign;

    /** 错误码 */
    private String code = "0";

    /** 错误信息 */
    private String msg;

    /** 响应时间 */
    private LocalDateTime resTime = LocalDateTime.now();
}
