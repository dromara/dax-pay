package org.dromara.daxpay.service.result.config.checkout;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.core.enums.CheckoutAggregateEnum;
import org.dromara.daxpay.core.enums.PayMethodEnum;
import org.dromara.daxpay.core.result.MchAppResult;

/**
 * 收银台聚合支付配置
 * @author xxm
 * @since 2024/11/27
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "收银台聚合支付配置")
public class CheckoutAggregateConfigVo extends MchAppResult {

    /**
     * 聚合支付类型
     * @see CheckoutAggregateEnum
     */
    @Schema(description = "聚合支付类型")
    private String type;

    /**
     * 通道
     * @see ChannelEnum
     */
    @Schema(description = "通道")
    private String channel;

    /**
     * 支付方式
     * @see PayMethodEnum
     */
    @Schema(description = "支付方式")
    private String payMethod;

    /** 自动拉起支付 */
    @Schema(description = "自动拉起支付")
    private boolean autoLaunch;

}
