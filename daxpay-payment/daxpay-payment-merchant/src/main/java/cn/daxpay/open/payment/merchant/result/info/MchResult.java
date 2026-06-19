package cn.daxpay.open.payment.merchant.result.info;

import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

/// # 商户应用基础返回结果
///
@EqualsAndHashCode(callSuper = true)
@Data
@FieldNameConstants
@Accessors(chain = true)
@Schema(title = "商户应用基础返回结果")
public class MchResult extends BaseResult {

    @Schema(description = "商户号")
    private String mchNo;

    @Schema(description = "商户名称")
    private String mchName;

    @Schema(description = "商户应用AppId")
    private String appId;

    @Schema(description = "商户应用名称")
    private String appName;
}
