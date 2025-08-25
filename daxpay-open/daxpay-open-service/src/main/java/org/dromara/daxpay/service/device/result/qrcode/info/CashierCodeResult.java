package org.dromara.daxpay.service.device.result.qrcode.info;

import org.dromara.daxpay.service.merchant.result.info.MchResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 收款码牌
 * @author xxm
 * @since 2025/7/1
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "收款码牌")
public class CashierCodeResult extends MchResult {

    /** 收款金额类型 固定金额/任意金额 */
    @Schema(description = "收款金额类型")
    private String amountType;

    /** 金额 */
    @Schema(description = "金额")
    private BigDecimal amount;

    /** 名称 */
    @Schema(description = "名称")
    private String name;

    /** 编号 */
    @Schema(description = "编号")
    private String code;

    /** 配置Id */
    @Schema(description = "配置Id")
    private Long configId;

    /** 模板ID */
    @Schema(description = "模板ID")
    private Long templateId;

    /** 是否启用 */
    @Schema(description = "是否启用")
    private Boolean enable;

    /** 批次号 */
    @Schema(description = "批次号")
    private String batchNo;
}
