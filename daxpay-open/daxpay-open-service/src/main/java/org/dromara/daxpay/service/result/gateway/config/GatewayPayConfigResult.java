package org.dromara.daxpay.service.result.gateway.config;

import org.dromara.daxpay.core.enums.PayLimitPayEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 网关支付配置
 * @author xxm
 * @since 2024/11/27
 */
@Data
@Accessors(chain = true)
@Schema(title = "网关支付配置")
public class GatewayPayConfigResult {

    /** 主键 */
    @Schema(description = "主键")
    private Long id;

    /** 名称 */
    @Schema(description = "名称")
    private String name;

    /** PC收银台是否同时显示聚合收银码 */
    @Schema(description = "PC收银台是否同时显示聚合收银码")
    private Boolean aggregateShow;

    /** PC收银台是否显示聚合付款码支付 */
    @Schema(description = "PC收银台是否显示聚合付款码支付")
    private Boolean barPayShow;

    /** h5收银台自动升级聚合支付 */
    @Schema(description = "h5收银台自动升级聚合支付")
    private Boolean h5AutoUpgrade;

    /** 小程序开启分账 */
    @Schema(description = "小程序开启分账")
    private Boolean miniAppAllocation;

    /** 小程序自动分账 */
    @Schema(description = "小程序自动分账")
    private Boolean miniAppAutoAllocation;

    /**
     * 限制用户支付类型, 目前支持限制信用卡
     * @see PayLimitPayEnum
     */
    @Schema(description = "限制用户支付类型")
    private String miniAppLimitPay;

}
