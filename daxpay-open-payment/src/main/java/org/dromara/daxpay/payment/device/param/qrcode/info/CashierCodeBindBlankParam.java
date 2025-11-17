package org.dromara.daxpay.payment.device.param.qrcode.info;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 空白码牌绑定参数对象
 * @author xxm
 * @since 2025/9/4
 */
@Data
@Accessors(chain = true)
@Schema(title = "空白码牌绑定参数对象")
public class CashierCodeBindBlankParam {
    @Schema(description = "码牌编号")
    private String code;

    @Schema(description = "商户应用")
    private String appId;
}
