package cn.daxpay.open.payment.trade.order.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 订单关闭请求参数
///
/// 统一入口关闭网关支付/普通支付订单的请求体。
@Data
@Accessors(chain = true)
@Schema(title = "订单关闭请求参数")
public class OrderCloseParam {

    /// 业务容器ID(pay_gateway_order / pay_normal_order 主键)
    @NotNull(message = "{validation.field.id.notNull}")
    @Schema(description = "业务容器ID")
    private Long containerId;

    /// 容器类型: gateway=网关支付, normal=普通支付
    @NotBlank
    @Schema(description = "容器类型: gateway / normal")
    private String tradeType;
}
