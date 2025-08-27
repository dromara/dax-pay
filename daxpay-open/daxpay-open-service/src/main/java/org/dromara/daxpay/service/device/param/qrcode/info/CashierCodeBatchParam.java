package org.dromara.daxpay.service.device.param.qrcode.info;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 批量创建收款码参数
 * @author xxm
 * @since 2025/7/1
 */
@Data
@Accessors(chain = true)
@Schema(title = "批量创建收款码参数")
public class CashierCodeBatchParam {

    /** 批次号 */
    @Schema(description = "批次号")
    private String batchNo;

    /** 创建数量 */
    @Schema(description = "创建数量")
    private Integer count;

    /** 模板Id */
    @Schema(description = "模板Id")
    private Long templateId;

    /** 配置Id */
    @Schema(description = "配置Id")
    private Long configId;

    /** 金额类型 */
    @Schema(description = "金额类型")
    private String amountType;

    /** 金额 */
    @Schema(description = "二维码金额")
    private BigDecimal amount;

    /** 是否启用 */
    @Schema(description = "是否启用")
    private Boolean enable;
}
