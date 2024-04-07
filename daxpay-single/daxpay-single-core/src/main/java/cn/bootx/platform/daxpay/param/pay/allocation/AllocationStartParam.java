package cn.bootx.platform.daxpay.param.pay.allocation;

import cn.bootx.platform.daxpay.param.PaymentCommonParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 开始分账请求参数
 * @author xxm
 * @since 2024/4/6
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "开始分账请求参数")
public class AllocationStartParam extends PaymentCommonParam {

    @Schema(description = "支付单ID")
    private Long paymentId;

    @Schema(description = "业务号")
    private String businessNo;

    @Schema(description = "分账单号(保证唯一)")
    private String allocationNo;

    @Schema(description = "分账描述")
    private String description;

    /**
     * 不传输分账组使用默认分账组进行分账
     */
    @Schema(description = "分账组ID")
    private Long allocationGroupId;

}
