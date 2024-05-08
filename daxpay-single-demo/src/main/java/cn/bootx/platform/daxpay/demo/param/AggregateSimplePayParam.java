package cn.bootx.platform.daxpay.demo.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 聚合简单支付
 * @author xxm
 * @since 2024/2/9
 */
@Data
@Accessors(chain = true)
@Schema(title = "聚合简单支付")
public class AggregateSimplePayParam {
    @Schema(description = "业务号")
    @NotNull
    private String businessNo;

    @Schema(description = "是否分账")
    private Boolean allocation;

    @Schema(description = "自动分账")
    private Boolean autoAllocation;

    @Schema(description = "标题")
    @NotNull
    private String title;

    @Schema(description = "金额")
    @NotNull
    private BigDecimal amount;

    @Schema(description = "付款码")
    private String authCode;
}
