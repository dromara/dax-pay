package org.dromara.daxpay.service.param.config;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 商户消息订阅参数
 * @author xxm
 * @since 2024/8/2
 */
@Data
@Accessors(chain = true)
@Schema(title = "商户消息订阅参数")
public class NotifySubscribeParam {

    /** 应用ID */
    @Schema(description = "应用ID")
    private String appId;
    /** 消息类型 */
    @Schema(description = "消息类型")
    private String notifyType;
    /** 是否订阅 */
    @Schema(description = "是否订阅")
    private boolean subscribe;
}
