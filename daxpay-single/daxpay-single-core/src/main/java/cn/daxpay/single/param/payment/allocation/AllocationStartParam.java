package cn.daxpay.single.param.payment.allocation;

import cn.daxpay.single.param.PaymentCommonParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * 开始分账请求参数
 * @author xxm
 * @since 2024/4/6
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "开始分账请求参数")
public class AllocationStartParam extends PaymentCommonParam {

    @Schema(description = "商户分账单号")
    @NotBlank(message = "商户分账单号不可为空")
    private String bizAllocationNo;

    @Schema(description = "支付订单号")
    private String orderNo;

    @Schema(description = "商户订单号")
    private String bizOrderNo;

    @Schema(description = "分账描述")
    private String description;

    /**
     * 不传输分账组使用默认分账组进行分账
     */
    @Schema(description = "分账组ID")
    private Long allocationGroupId;

}
