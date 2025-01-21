package org.dromara.daxpay.service.result.config;

import org.dromara.daxpay.core.result.MchAppResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 商户应用消息通知配置
 * @author xxm
 * @since 2024/8/2
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "商户应用消息通知配置")
public class MerchantNotifyConfigResult extends MchAppResult {

    /** 消息通知类型编码 */
    @Schema(description = "消息通知类型编码")
    private String code;

    /** 订阅名称 */
    @Schema(description = "订阅名称")
    private String name;

    /** 描述 */
    @Schema(description = "描述")
    private String description;

    /** 是否订阅 */
    @Schema(description = "是否订阅")
    private boolean subscribe;
}
