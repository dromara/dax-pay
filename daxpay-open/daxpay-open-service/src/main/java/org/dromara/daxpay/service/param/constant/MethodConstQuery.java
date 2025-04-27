package org.dromara.daxpay.service.param.constant;

import cn.bootx.platform.core.annotation.QueryParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 支付方式
 * @author xxm
 * @since 2024/7/14
 */
@QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
@Data
@Accessors(chain = true)
@Schema(title = "支付方式")
public class MethodConstQuery {
    /** 编码 */
    @Schema(description = "编码")
    private String code;

    /** 名称 */
    @Schema(description = "名称")
    private String name;

}
