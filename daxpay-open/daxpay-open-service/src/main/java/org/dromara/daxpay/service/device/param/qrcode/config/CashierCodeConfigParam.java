package org.dromara.daxpay.service.device.param.qrcode.config;

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

    @Schema(description = "码牌名称")
    private String name;

    @Schema(description = "是否开启分账")
    private Boolean allocation;

    @Schema(description = "自动分账")
    private Boolean autoAllocation;

    /** 限制用户支付方式 */
    @Schema(description = "限制用户支付方式")
    private String limitPay;

    @Schema(description = "备注")
    private String remark;

    private String appId;

}
