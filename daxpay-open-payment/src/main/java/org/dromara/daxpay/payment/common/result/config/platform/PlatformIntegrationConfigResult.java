package org.dromara.daxpay.payment.common.result.config.platform;

import cn.bootx.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 平台集成配置
 * @author xxm
 * @since 2025/1/15
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "平台集成配置")
public class PlatformIntegrationConfigResult extends BaseResult {

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
