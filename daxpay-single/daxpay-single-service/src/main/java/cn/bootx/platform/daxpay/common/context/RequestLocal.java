package cn.bootx.platform.daxpay.common.context;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 支付请求相关信息
 * @author xxm
 * @since 2023/12/25
 */
@Data
@Accessors(chain = true)
public class RequestLocal {

    /** 客户端ip */
    private String clientIp;

    /** 商户扩展参数,回调时会原样返回 */
    private String extraParam;

    /** 签名 */
    private String sign;

    /** API版本号 */
    private String version;

    /** 请求时间，时间戳转时间 */
    private LocalDateTime reqTime;

    /** 请求链路id */
    private String reqId;
}
