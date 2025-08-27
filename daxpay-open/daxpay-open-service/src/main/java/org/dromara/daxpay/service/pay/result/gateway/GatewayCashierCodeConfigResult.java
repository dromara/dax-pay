package org.dromara.daxpay.service.pay.result.gateway;

import org.dromara.daxpay.core.enums.CashierAmountTypeEnum;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.core.enums.GatewayCallTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 支付码牌配置信息
 * @author xxm
 * @since 2025/4/11
 */
@Data
@Accessors(chain = true)
@Schema(title = "支付码牌配置信息")
public class GatewayCashierCodeConfigResult {

    /** 码牌名称 */
    @Schema(description = "码牌名称")
    private String name;

    /** 是否启用 */
    @Schema(description = "是否启用")
    private Boolean enable;

    /**
     * 金额类型
     * @see CashierAmountTypeEnum
     */
    @Schema(description = "金额类型")
    private String amountType;

    /** 金额 */
    @Schema(description = "金额")
    private BigDecimal amount;

    /**
     * 支付通道
     * @see ChannelEnum
     */
    @Schema(description = "支付通道")
    private String channel;

    /** 需要获取OpenId */
    @Schema(description = "需要获取OpenId")
    private Boolean needOpenId;

    /** OpenId获取方式 */
    @Schema(description = "OpenId获取方式")
    private String openIdGetType;

    /**
     * 发起调用的类型
     * @see GatewayCallTypeEnum
     */
    @Schema(description = "发起调用的类型")
    private String callType;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;
}
