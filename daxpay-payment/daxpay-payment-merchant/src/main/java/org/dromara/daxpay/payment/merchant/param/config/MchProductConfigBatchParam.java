package org.dromara.daxpay.payment.merchant.param.config;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/// # 商户产品配置批量保存参数
///
@Data
@Accessors(chain = true)
@Schema(title = "商户产品配置批量保存参数")
public class MchProductConfigBatchParam {

    @Schema(description = "商户号")
    @NotBlank(message = "{validation.field.mchNo.notBlank}")
    private String mchNo;

    @Schema(description = "产品配置列表")
    private List<MchProductConfigItem> items;
}
