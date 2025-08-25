package org.dromara.daxpay.service.common.param.config;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 平台访问地址配置参数
 * @author xxm
 * @since 2025/6/29
 */
@Data
@Accessors(chain = true)
@Schema(title = "平台访问地址配置参数")
public class PlatformUrlConfigParam {


    /** 运营端网址 */
    @Schema(description = "运营端网址")
    private String adminWebUrl;

    /** 代理商端网址 */
    @Schema(description = "代理商端网址")
    private String agentWebUrl;

    /** 商户端网址 */
    @Schema(description = "商户端网址")
    private String mchWebUrl;

    /** 支付网关地址 */
    @Schema(description = "支付网关地址")
    private String gatewayServiceUrl;

    /** 网关h5地址 */
    @Schema(description = "网关h5地址")
    private String gatewayH5Url;
}
