package org.dromara.daxpay.core.param.allocation.order;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.daxpay.core.param.PaymentCommonParam;

/**
 * 分账同步请求参数
 * @author xxm
 * @since 2024/4/12
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "分账同步请求参数")
public class AllocSyncParam extends PaymentCommonParam {

    @Schema(description = "分账号")
    @Size(max = 32, message = "分账号不可超过32位")
    private String allocNo;

    @Schema(description = "商户分账号")
    @Size(max = 100, message = "商户分账号不可超过100位")
    private String bizAllocNo;
}
