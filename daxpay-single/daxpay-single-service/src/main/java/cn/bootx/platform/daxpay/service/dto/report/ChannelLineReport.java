package cn.bootx.platform.daxpay.service.dto.report;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 支付通道折线图报表
 * @author xxm
 * @since 2024/3/17
 */
@Data
@Accessors(chain = true)
@Schema(title = "支付通道折线图报表")
public class ChannelLineReport {

    @Schema(description = "支付通道编码")
    private String channelCode;

    @Schema(description = "支付通道名称")
    private String channelName;

    @Schema(description = "订单金额")
    private Integer orderAmount;

    @Schema(description = "订单数量")
    private Integer orderCount;

}
