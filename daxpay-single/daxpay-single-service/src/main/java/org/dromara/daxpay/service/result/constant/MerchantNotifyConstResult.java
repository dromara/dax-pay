package org.dromara.daxpay.service.result.constant;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 商户通知类型
 * @author xxm
 * @since 2024/7/30
 */
@Data
@Accessors(chain = true)
@Schema(title = "商户通知类型")
public class MerchantNotifyConstResult {
    /** 通道编码 */
    @Schema(description = "通知类型编码")
    private String code;

    /** 通道名称 */
    @Schema(description = "名称")
    private String name;

    /** 描述 */
    @Schema(description = "描述")
    private String description;

    /** 是否启用 */
    @Schema(description = "是否启用")
    private boolean enable;

}
