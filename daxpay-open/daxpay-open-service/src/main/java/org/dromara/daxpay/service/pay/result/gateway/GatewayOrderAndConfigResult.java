package org.dromara.daxpay.service.pay.result.gateway;

import org.dromara.daxpay.service.merchant.result.gateway.CashierGroupConfigResult;
import org.dromara.daxpay.service.merchant.result.gateway.GatewayPayConfigResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 网关收银台结算订单和配置信息
 * @author xxm
 * @since 2024/11/26
 */
@Data
@Accessors(chain = true)
@Schema(title = "网关收银台结算订单和配置信息")
public class GatewayOrderAndConfigResult {

    /** 订单信息 */
    @Schema(description = "订单信息")
    private GatewayOrderResult order;

    /** 网关支付配置信息 */
    @Schema(description = "网关支付配置信息")
    private GatewayPayConfigResult config;

    /** 收银台分类配置信息 */
    @Schema(description = "分类配置信息")
    private List<CashierGroupConfigResult> groupConfigs;

    /** 聚合扫码支付链接 */
    @Schema(description = "聚合扫码支付链接")
    private String aggregateUrl;
}
