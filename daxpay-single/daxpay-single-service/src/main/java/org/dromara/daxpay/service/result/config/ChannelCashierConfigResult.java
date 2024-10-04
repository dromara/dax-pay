package org.dromara.daxpay.service.result.config;

import org.dromara.daxpay.core.enums.CashierTypeEnum;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.core.enums.PayMethodEnum;
import org.dromara.daxpay.core.result.MchAppResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 通道收银台配置
 * @author xxm
 * @since 2024/9/28
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "通道收银台配置")
public class ChannelCashierConfigResult extends MchAppResult {

    /**
     * 收银台类型
     * @see CashierTypeEnum
     */
    @Schema(description = "收银台类型")
    private String cashierType;

    /**
     * 收银台名称
     */
    @Schema(description = "收银台名称")
    private String cashierName;

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
    private Boolean allocation;

    /** 自动分账 */
    @Schema(description = "自动分账")
    private Boolean autoAllocation;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;
}
