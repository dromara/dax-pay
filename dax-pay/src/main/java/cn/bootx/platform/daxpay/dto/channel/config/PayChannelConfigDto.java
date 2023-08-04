package cn.bootx.platform.daxpay.dto.channel.config;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 支付渠道配置
 *
 * @author xxm
 * @since 2023-05-24
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "支付渠道配置")
@Accessors(chain = true)
public class PayChannelConfigDto extends BaseDto {

    @Schema(description = "渠道编码")
    private String code;

    @Schema(description = "渠道名称")
    private String name;

    @Schema(description = "图片")
    private Long image;

    @Schema(description = "排序")
    private Double sortNo;

}
