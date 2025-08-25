package org.dromara.daxpay.service.pay.param.constant;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 商户订阅消息类型
 * @author xxm
 * @since 2024/8/5
 */
@Data
@Accessors(chain = true)
@Schema(title = "商户订阅消息类型")
public class MerchantNotifyConstQuery {
    /** 编码 */
    @Schema(description = "编码")
    private String code;

    /** 名称 */
    @Schema(description = "名称")
    private String name;
}
