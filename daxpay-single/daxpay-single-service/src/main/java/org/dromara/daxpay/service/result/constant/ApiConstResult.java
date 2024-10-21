package org.dromara.daxpay.service.result.constant;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 支付接口
 * @author xxm
 * @since 2024/7/14
 */
@Data
@Accessors(chain = true)
@Schema(title = "支付接口")
public class ApiConstResult {
    /** 接口编码 */
    @Schema(description = "编码")
    private String code;

    /** 接口名称 */
    @Schema(description = "接口名称")
    private String name;

    /** 接口地址 */
    @Schema(description = "接口地址")
    private String api;

    /** 是否启用 */
    @Schema(description = "是否启用")
    private boolean enable;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;
}
