package org.dromara.daxpay.service.result.config.cashier;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.dromara.daxpay.core.enums.CashierCodeTypeEnum;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.core.enums.PayMethodEnum;
import org.dromara.daxpay.core.result.MchAppResult;

/**
 * 特定类型码牌配置
 * @author xxm
 * @since 2024/11/20
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "特定类型码牌配置")
public class CashierCodeTypeConfigResult extends MchAppResult {

    /** 码牌ID */
    @Schema(description = "主键ID")
    private Long cashierCodeId;

    /**
     * 码牌类型
     * @see CashierCodeTypeEnum
     */
    @Schema(description = "收银台类型")
    private String type;

    /**
     * 支付通道
     * @see ChannelEnum
     */
    @Schema(description = "支付通道")
    private String channel;

    /**
     * 支付方式
     * @see PayMethodEnum
     */
    @Schema(description = "支付方式")
    private String payMethod;


    /** 是否开启分账 */
    @Schema(description = "是否开启分账")
    private boolean allocation;

    /** 自动分账 */
    @Schema(description = "自动分账")
    private boolean autoAllocation;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;
}
