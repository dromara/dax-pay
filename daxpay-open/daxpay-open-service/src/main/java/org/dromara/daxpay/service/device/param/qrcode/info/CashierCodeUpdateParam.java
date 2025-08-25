package org.dromara.daxpay.service.device.param.qrcode.info;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 码牌更新参数
 * @author xxm
 * @since 2025/7/1
 */
@Data
@Accessors(chain = true)
@Schema(title = "码牌更新参数")
public class CashierCodeUpdateParam {

    /** 主键 */
    @Schema(description = "主键")
    private Long id;

    /** 名称 */
    @Schema(description = "名称")
    private String name;

    /** 收款金额类型 固定金额/任意金额 */
    @Schema(description = "收款金额类型")
    private String amountType;

    /** 金额 */
    @Schema(description = "金额")
    private BigDecimal amount;

    /** 配置Id */
    @Schema(description = "配置Id")
    private Long configId;

    /** 模板ID */
    @Schema(description = "模板ID")
    private Long templateId;

    /** 是否启用 */
    @Schema(description = "是否启用")
    private Boolean enable;

}
