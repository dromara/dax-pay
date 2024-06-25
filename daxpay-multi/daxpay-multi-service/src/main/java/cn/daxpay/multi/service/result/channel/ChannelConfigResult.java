package cn.daxpay.multi.service.result.channel;

import cn.daxpay.multi.core.enums.PayChannelEnum;
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

    @Schema(title = "主键ID")
    private Long id;

    /**
     * 支付通道
     * @see PayChannelEnum
     */
    @Schema(title = "支付通道")
    private String channel;

    /** 通道商户号 */
    @Schema(title = "通道商户号")
    private String outMchNo;

    /** 通道APPID */
    @Schema(title = "通道APPID")
    private String outAppId;

    /** 是否启用 */
    @Schema(title = "是否启用")
    private Boolean enable;

}
