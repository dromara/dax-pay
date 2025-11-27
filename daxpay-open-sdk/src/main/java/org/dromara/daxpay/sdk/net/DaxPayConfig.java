package org.dromara.daxpay.sdk.net;

import cn.hutool.core.util.StrUtil;
import lombok.Builder;
import lombok.Getter;

/**
 * 支付配置
 * @author xxm
 * @since 2024/2/2
 */
@Getter
@Builder
public class DaxPayConfig {

    /** 服务地址 */
    private String serviceUrl;

    /** 商户号 */
    private String mchNo;

    /** 应用号 */
    private String appId;

    /** 商户私钥 */
    private String privateKey;

    /** 平台公钥 */
    private String publicKey;

    /** 请求超时时间 */
    @Builder.Default
    private int reqTimeout = 30000;


    public String getServiceUrl() {
        return StrUtil.removeSuffix(serviceUrl, "/");
    }
}
