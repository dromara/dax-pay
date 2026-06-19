package cn.daxpay.open.payment.common.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

/// # 商户交易基础返回结果
///
/// 注意：此类位于 common 模块，无法添加翻译注解
@EqualsAndHashCode(callSuper = true)
@Data
@FieldNameConstants
@Accessors(chain = true)
@Schema(title = "商户交易基础返回结果")
public class MchTradeBaseResult extends MchBaseResult {

    @Schema(description = "商户应用AppId")
    private String appId;

    @Schema(description = "商户应用名称")
    private String appName;
}
