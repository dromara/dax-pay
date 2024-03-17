package cn.bootx.platform.daxpay.service.core.report.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 通道订单折线图
 * @author xxm
 * @since 2024/3/17
 */
@Data
@Accessors(chain = true)
@Schema(title = "通道订单折线图")
public class ChannelOrderLine {

    @Schema(description = "通道编码")
    private String channel;

    @Schema(description = "订单金额")
    private Integer count;

    @Schema(description = "订单数量")
    private Integer sum;
}
