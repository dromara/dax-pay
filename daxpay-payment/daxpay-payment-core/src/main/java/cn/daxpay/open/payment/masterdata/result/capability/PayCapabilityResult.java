package cn.daxpay.open.payment.masterdata.result.capability;

import cn.daxpay.open.platform.core.rest.dto.LabelValue;
import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/// # 支付能力
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "支付能力")
public class PayCapabilityResult extends BaseResult {

    @Schema(description = "支付能力编码")
    private String code;

    @Schema(description = "支付能力名称（i18n）")
    private String name;

    @Schema(description = "排序")
    private Integer sortNo;

    @Schema(description = "说明")
    private String description;

    @Schema(description = "已关联支付产品")
    private List<LabelValue> products;
}