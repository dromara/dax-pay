package cn.daxpay.single.service.common.context;

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

    /** 签名 */
    private String sign;

    /** 请求时间，时间戳转时间 */
    private LocalDateTime reqTime;

}
