package org.dromara.daxpay.core.result.cashier;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.core.enums.PayMethodEnum;

/**
 * 收银码牌信息和配置
 * @author xxm
 * @since 2024/11/20
 */
@Data
@Accessors(chain = true)
@Schema(title = "收银码牌信息和配置")
public class CashierCodeResult {

    /** 码牌名称 */
    @Schema(description = "码牌名称")
    private String name;

    /** 模板编号 */
    @Schema(description = "模板编号")
    private String templateCode;

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

    /** 应用号 */
    @Schema(description = "应用号")
    private String appId;

    /** 是否分账 */
    @Schema(description = "是否分账")
    private boolean allocation;

}
