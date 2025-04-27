package org.dromara.daxpay.core.param.gateway;

import org.dromara.daxpay.core.enums.GatewayCashierTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 网关支付订单和配置信息查询类
 * @author xxm
 * @since 2025/3/29
 */
@Data
@Accessors(chain = true)
@Schema(title = "网关支付订单和配置信息查询类")
public class GatewayOrderAndConfigParam {

    @NotBlank(message = "订单号不能为空")
    @Schema(description = "订单号")
    private String orderNo;

    /**
     * 收银台类型
     * @see GatewayCashierTypeEnum
     */
    @NotBlank(message = "收银台类型不能为空")
    @Schema(description = "收银台类型")
    private String cashierType;

    /**
     * 非必填, 不传不进行筛选
     */
    @Schema(description = "支付环境类型")
    private String payEnvType;
}
