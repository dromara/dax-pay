package org.dromara.daxpay.payment.common.result;

import org.dromara.daxpay.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

/// # 商户基础返回结果
///
/// 注意：此类位于 common 模块，无法添加翻译注解
@EqualsAndHashCode(callSuper = true)
@Data
@FieldNameConstants
@Accessors(chain = true)
@Schema(title = "商户基础返回结果")
public class MchBaseResult extends BaseResult {

    @Schema(description = "商户号")
    private String mchNo;

    @Schema(description = "商户名称")
    private String mchName;

    @Schema(description = "服务商号")
    private String isvNo;

    @Schema(description = "服务商名称")
    private String isvName;
}
