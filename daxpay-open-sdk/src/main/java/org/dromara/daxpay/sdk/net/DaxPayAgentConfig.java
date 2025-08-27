package org.dromara.daxpay.sdk.net;

import lombok.Builder;
import lombok.Getter;
import org.dromara.daxpay.sdk.code.SignTypeEnum;

/**
 * 代理商配置
 * @author xxm
 * @since 2025/8/24
 */
@Getter
@Builder
public class DaxPayAgentConfig {
    /** 服务地址 */
    private String serviceUrl;

    /** 代理商号 */
    private String agentNo;

    /** 签名方式 */
    @Builder.Default
    private SignTypeEnum signType = SignTypeEnum.HMAC_SHA256;

    /** 签名秘钥 */
    private String signSecret;

    /** 请求超时时间 */
    @Builder.Default
    private int reqTimeout = 30000;
}
