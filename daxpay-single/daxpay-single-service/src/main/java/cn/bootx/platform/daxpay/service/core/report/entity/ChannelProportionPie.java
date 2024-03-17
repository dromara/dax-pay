package cn.bootx.platform.daxpay.service.core.report.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 支付通道占比饼图
 * @author xxm
 * @since 2024/3/17
 */
@Data
@Accessors(chain = true)
@Schema(title = "支付通道占比饼图")
public class ChannelProportionPie {

    @Schema(description = "通道编码")
    private String code;

    @Schema(description = "通道名称")
    private String name;

    @Schema(description = "占比")
    private Double proportion;

    @Schema(description = "数量")
    private Integer count;

}
