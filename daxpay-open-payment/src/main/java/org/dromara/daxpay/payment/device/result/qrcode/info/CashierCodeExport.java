package org.dromara.daxpay.payment.device.result.qrcode.info;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 批量导出参数对象
 * @author xxm
 * @since 2025/9/4
 */
@Data
@Accessors(chain = true)
@Schema(title = "批量导出参数对象")
public class CashierCodeExport {

    /** 收款金额类型 固定金额/任意金额 */
    @Schema(description = "收款金额类型")
    private String amountType;

    /** 金额 */
    @Schema(description = "金额")
    private String amount;

    /** 名称 */
    @Schema(description = "名称")
    private String name;

    /** 编号 */
    @Schema(description = "编号")
    private String code;

    /** 是否启用 */
    @Schema(description = "是否启用")
    private String enable;

    /** 批次号 */
    @Schema(description = "批次号")
    private String batchNo;

    /** 码牌地址 */
    @Schema(description = "码牌地址")
    private String codeUrl;
}
