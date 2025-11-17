package org.dromara.daxpay.payment.pay.result.gateway;

import org.dromara.daxpay.payment.unipay.enums.GatewayCallTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 网关聚合支付订单和配置信息
 * @author xxm
 * @since 2024/11/27
 */
@Data
@Accessors(chain = true)
@Schema(title = "网关聚合支付订单和配置信息")
public class AggregateOrderAndConfigResult {

    /** 订单信息 */
    @Schema(description = "订单信息")
    private GatewayOrderResult order;

    /**
     * 调用方式
     * @see GatewayCallTypeEnum
     */
    @Schema(description = "调用方式")
    private String callType;

    /** 需要OpenId认证 */
    private boolean needOpenId;

    /** 自动拉起支付 */
    @Schema(description = "自动拉起支付")
    private boolean autoLaunch;
}
