package org.dromara.daxpay.service.device.param.qrcode.info;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 码牌创建参数
 * @author xxm
 * @since 2025/7/6
 */
@Data
@Accessors(chain = true)
@Schema(title = "码牌创建参数")
public class CashierCodeCreateParam {

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
