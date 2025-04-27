package org.dromara.daxpay.core.param.gateway;

import org.dromara.daxpay.core.enums.AggregatePayTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 获取收银台认证结果参数
 * @author xxm
 * @since 2024/9/28
 */
@Data
@Accessors(chain = true)
@Schema(title = "获取收银台认证结果参数")
public class GatewayAuthCodeParam {

    /** 支付订单号 */
    @NotBlank(message = "支付订单号不可为空")
    @Schema(description = "支付订单号")
    private String orderNo;

    /**
     * 聚合支付类型
     * @see AggregatePayTypeEnum
     */
    @Schema(description = "聚合支付类型")
    private String aggregateType;

    /** 认证Code */
    @Schema(description = "认证Code")
    private String authCode;
}
