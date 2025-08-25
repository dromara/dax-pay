package org.dromara.daxpay.service.isv.result.config;

import org.dromara.daxpay.core.enums.ChannelEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 服务商通道配置
 * @author xxm
 * @since 2024/10/30
 */
@Data
@Accessors(chain = true)
@Schema(title = "服务商通道配置")
public class IsvChannelConfigResult {

    @Schema(description = "主键ID")
    private Long id;

    /**
     * 支付通道
     * @see ChannelEnum
     */
    @Schema(description = "通道")
    private String channel;

    /** 通道名称 */
    @Schema(description = "通道名称")
    private String name;
    /** 是否启用 */
    @Schema(description = "是否启用")
    private Boolean enable;

}
