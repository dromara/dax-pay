package org.dromara.daxpay.service.param.config;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 平台配置
 * @author xxm
 * @since 2024/9/19
 */
@Data
@Accessors(chain = true)
@Schema(title = "平台配置")
public class PlatformConfigParam {

    /** 支付网关地址 */
    @Schema(description = "支付网关地址")
    private String gatewayServiceUrl;

    /** 网关移动端是否为嵌入式 */
    @Schema(description = "网关移动端是否为嵌入式")
    private boolean mobileEmbedded;

    /** 网关移动端地址 */
    @Schema(description = "网关移动端地址")
    private String gatewayMobileUrl;

    /** 网关PC端是否为嵌入式  */
    @Schema(description = "网关PC端是否为嵌入式")
    private boolean pcEmbedded;

    /** 网关PC端地址 */
    @Schema(description = "网关PC端地址")
    private String gatewayPcUrl;
}
