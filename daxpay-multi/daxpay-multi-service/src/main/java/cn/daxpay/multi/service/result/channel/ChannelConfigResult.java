package cn.daxpay.multi.service.result.channel;

import cn.daxpay.multi.core.enums.ChannelEnum;
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
public class ChannelConfigResult {

    @Schema(description = "主键ID")
    private Long id;

    /**
     * 支付通道
     * @see ChannelEnum
     */
    @Schema(description = "支付通道")
    private String channel;

    /** 通道商户号 */
    @Schema(description = "通道商户号")
    private String outMchNo;

    /** 通道APPID */
    @Schema(description = "通道APPID")
    private String outAppId;

    /** 是否启用 */
    @Schema(description = "是否启用")
    private Boolean enable;

}
