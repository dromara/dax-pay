package org.dromara.daxpay.core.result.checkout;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 收银台结算订单和配置信息
 * @author xxm
 * @since 2024/11/26
 */
@Data
@Accessors(chain = true)
@Schema(title = "收银台结算订单和配置信息")
public class CheckoutOrderAndConfigResult {

    /** 订单信息 */
    @Schema(description = "订单信息")
    private CheckoutOrderResult order;

    /** 收银台配置信息 */
    @Schema(description = "收银台配置信息")
    private CheckoutConfigResult config;

    /** 收银台分类配置信息 */
    @Schema(description = "分类配置信息")
    private List<CheckoutGroupConfigResult> groupConfigs;
}
