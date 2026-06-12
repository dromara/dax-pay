package org.dromara.daxpay.payment.pay.param.masterdata.method;

import org.dromara.daxpay.platform.core.annotation.QueryParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 支付方式
///
@QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
@Data
@Accessors(chain = true)
@Schema(title = "支付方式")
public class MethodConstQuery {
    /// 编码
    @Schema(description = "编码")
    private String code;

    /// 名称
    @Schema(description = "名称")
    private String name;

}