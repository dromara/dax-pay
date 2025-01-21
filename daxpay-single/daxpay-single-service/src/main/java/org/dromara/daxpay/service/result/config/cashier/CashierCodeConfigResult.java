package org.dromara.daxpay.service.result.config.cashier;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.dromara.daxpay.core.result.MchAppResult;

/**
 * 收银台码牌配置
 * @author xxm
 * @since 2024/11/20
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "收银台码牌配置")
public class CashierCodeConfigResult extends MchAppResult {

    /** 模板编号 */
    @Schema(description = "模板编号")
    private String templateCode;

    /** 码牌名称 */
    @Schema(description = "码牌名称")
    private String name;

    /** 码牌code */
    @Schema(description = "码牌code")
    private String code;

    /** 是否启用 */
    @Schema(description = "是否启用")
    private boolean enable;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;
}
