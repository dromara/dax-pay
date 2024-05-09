package cn.daxpay.single.demo.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 聚合支付发起信息
 * @author xxm
 * @since 2024/2/9
 */
@Data
@Accessors(chain = true)
@Schema(title = "聚合支付发起信息")
public class AggregatePayInfo {
    /** 标题 */
    @Schema(description = "标题")
    private String title;

    /** 订单业务号 */
    @Schema(description = "订单业务号")
    private String businessNo;

    @Schema(description = "是否分账")
    private Boolean allocation;

    @Schema(description = "自动分账")
    private Boolean autoAllocation;

    /** 支付金额 */
    @Schema(description = "支付金额")
    private Integer amount;
}
