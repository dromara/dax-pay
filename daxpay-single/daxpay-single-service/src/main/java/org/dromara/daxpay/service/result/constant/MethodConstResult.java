package org.dromara.daxpay.service.result.constant;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 支付方式
 * @author xxm
 * @since 2024/7/14
 */
@Data
@Accessors(chain = true)
@Schema(title = "支付方式")
public class MethodConstResult {
    /** 通道编码 */
    @Schema(description = "通道编码")
    private String code;

    /** 通道名称 */
    @Schema(description = "通道名称")
    private String name;

    /** 是否启用 */
    @Schema(description = "是否启用")
    private boolean enable;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;
}
