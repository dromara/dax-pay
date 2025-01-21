package org.dromara.daxpay.service.result.config;

import org.dromara.daxpay.core.enums.ChannelEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 *
 * @author xxm
 * @since 2024/6/25
 */
@Data
@Accessors(chain = true)
@Schema(title = "通道配置")
public class ChannelConfigResult{

    @Schema(description = "主键ID")
    private Long id;

    /**
     * 支付通道
     * @see ChannelEnum
     */
    @Schema(description = "支付通道")
    private String channel;

    /** 通道名称 */
    @Schema(description = "通道名称")
    private String name;

    /** 通道商户号 */
    @Schema(description = "通道商户号")
    private String outMchNo;

    /** 通道APPID */
    @Schema(description = "通道APPID")
    private String outAppId;

    /** 是否启用 */
    @Schema(description = "是否启用")
    private Boolean enable;

    /** 应用号 */
    @Schema(description = "应用号")
    private String appId;

}
