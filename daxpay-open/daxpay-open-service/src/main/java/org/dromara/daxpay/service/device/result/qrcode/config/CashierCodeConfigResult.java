package org.dromara.daxpay.service.device.result.qrcode.config;

import org.dromara.daxpay.service.merchant.result.app.MchAppResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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

    /** 名称 */
    @Schema(description = "码牌名称")
    private String name;

    /** 是否启用 */
    @Schema(description = "是否启用")
    private Boolean enable;

    /** 是否开启分账 */
    @Schema(description = "是否开启分账")
    private Boolean allocation;

    /** 自动分账 */
    @Schema(description = "自动分账")
    private Boolean autoAllocation;

    /** 限制用户支付方式 */
    @Schema(description = "限制用户支付方式")
    private String limitPay;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;
}
