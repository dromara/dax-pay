package org.dromara.daxpay.core.param.allocation.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.daxpay.core.param.PaymentCommonParam;

/**
 * 分账完结参数
 * @author xxm
 * @since 2024/4/7
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "分账请求参数")
public class AllocFinishParam extends PaymentCommonParam {

    @Schema(description = "商户分账单号")
    private String bizAllocNo;

    @Schema(description = "分账单号")
    private String allocNo;
}
