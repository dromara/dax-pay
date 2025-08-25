package org.dromara.daxpay.service.common.param.config;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 默认提现配置参数
 * @author xxm
 * @since 2025/6/28
 */
@Data
@Accessors(chain = true)
@Schema(title = "默认提现配置参数")
public class PlatformCashoutsConfigParam {

    /** 起始提现额度 */
    @Schema(description = "起始提现额度")
    private BigDecimal startAmount;

    /**
     * 手续费计算公式
     * @see AgentCashoutsFeeFormulaEnum
     */
    @Schema(description = "手续费计算公式")
    private String feeFormula;

    /** 单笔固定 */
    @Schema(description = "单笔固定")
    private BigDecimal fixedFee;

    /** 单笔费率 */
    @Schema(description = "单笔费率")
    private BigDecimal fixedRate;

    /** 组合 固定手续费 */
    @Schema(description = "组合 固定手续费")
    private BigDecimal fixedFeeCombined;

    /** 组合 费率 */
    @Schema(description = "组合 费率")
    private BigDecimal fixedRateCombined;

    /** 冻结金额 */
    @Schema(description = "冻结金额")
    private BigDecimal freezeAmount;

}
