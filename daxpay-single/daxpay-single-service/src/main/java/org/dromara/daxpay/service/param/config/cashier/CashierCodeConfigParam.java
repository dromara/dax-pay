package org.dromara.daxpay.service.param.config.cashier;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 收款码牌配置
 * @author xxm
 * @since 2024/11/20
 */
@Data
@Accessors(chain = true)
@Schema(title = "收款码牌配置")
public class CashierCodeConfigParam {

    @Schema(description = "主健")
    private Long id;

    @Schema(description = "模板编号")
    private String templateCode;
    @Schema(description = "码牌名称")
    private String name;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "是否启用")
    private boolean enable;

    @Schema(description = "应用ID")
    private String appId;
}
