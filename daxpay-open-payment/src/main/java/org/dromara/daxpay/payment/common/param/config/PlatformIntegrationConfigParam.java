package org.dromara.daxpay.payment.common.param.config;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 平台集成配置参数
 * @author xxm
 * @since 2025/9/13
 */
@Data
@Accessors(chain = true)
@Schema(title = "平台集成配置参数")
public class PlatformIntegrationConfigParam {

    /** 是否对请求进行验签 */
    @Schema(description = "是否对请求进行验签")
    private Boolean reqSign;

    /** 是否验证请求时间是否超时 */
    @Schema(description = "是否验证请求时间是否超时")
    private Boolean reqTimeout;

    /** 请求超时时间(秒) */
    @Schema(description = "请求超时时间(秒)")
    private Integer apiReqTimeout;

    /** 通道SDK请求接口超时时间(秒) */
    @Schema(description = "通道SDK请求接口超时时间(秒)")
    private Integer channelSdkReqTimeout;
}
