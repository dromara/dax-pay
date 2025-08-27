package org.dromara.daxpay.core.param.gateway;

import org.dromara.daxpay.core.enums.AggregatePayTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 收银台认证链接生成参数
 * @author xxm
 * @since 2024/9/28
 */
@Data
@Accessors(chain = true)
@Schema(title = "收银台认证链接生成参数")
public class GatewayAuthUrlParam {

    @NotBlank(message = "订单号不可为空")
    @Schema(description = "要支付的订单号")
    private String orderNo;

    /**
     * 聚合支付类型
     * @see AggregatePayTypeEnum
     */
    @NotBlank(message = "聚合支付类型不可为空")
    @Schema(description = "聚合支付类型")
    private String aggregateType;
}
