package org.dromara.daxpay.service.isv.result.gateway;

import org.dromara.daxpay.core.enums.AggregateBarPayTypeEnum;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.core.enums.PayMethodEnum;
import org.dromara.daxpay.service.merchant.result.info.MchResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 聚合付款码支付配置
 * @author xxm
 * @since 2025/3/24
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "聚合付款码支付配置")
public class IsvAggregateBarPayConfigResult extends MchResult {

    /**
     * 付款码类型
     * @see AggregateBarPayTypeEnum
     */
    @Schema(description = "付款码类型")
    private String barPayType;

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

    /**
     * 其他支付方式
     */
    @Schema(description = "其他支付方式")
    private String otherMethod;
}
