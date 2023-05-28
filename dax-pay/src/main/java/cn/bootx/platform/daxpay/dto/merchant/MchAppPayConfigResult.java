package cn.bootx.platform.daxpay.dto.merchant;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 支付通道配置
 *
 * @author xxm
 * @date 2023/5/24
 */
@Data
@Accessors(chain = true)
@Schema(title = "支付通道配置")
public class MchAppPayConfigResult {

    @Schema(description = "支付通道编码")
    private String channelCode;

    @Schema(description = "支付通道名称")
    private String channelName;

    @Schema(description = "状态")
    private String state;

    @Schema(description = "图片")
    private Long img;

    @Schema(description = "关联配置ID")
    private Long configId;

}
