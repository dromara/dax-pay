package cn.daxpay.open.payment.masterdata.constants.product.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 支付产品关联的支付能力项
@Data
@Accessors(chain = true)
@Schema(title = "支付产品支付能力项")
public class PayProductCapabilityResult {

    @Schema(description = "支付能力编码")
    private String code;

    @Schema(description = "支付能力名称（i18n）")
    private String name;

    @Schema(description = "关联表排序")
    private Integer sortNo;
}