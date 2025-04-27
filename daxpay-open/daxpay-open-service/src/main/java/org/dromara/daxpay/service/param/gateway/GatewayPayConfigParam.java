package org.dromara.daxpay.service.param.gateway;

import cn.bootx.platform.core.validation.ValidationGroup;
import org.dromara.daxpay.core.enums.PayLimitPayEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 网关支付有配置
 * @author xxm
 * @since 2024/11/27
 */
@Data
@Accessors(chain = true)
@Schema(title = "收银台配置")
public class GatewayPayConfigParam {

    /** 主键 */
    @Null(message = "Id需要为空", groups = ValidationGroup.add.class)
    @NotNull(message = "Id不可为空", groups = ValidationGroup.edit.class)
    @Schema(description = "主键")
    private Long id;

    /** 名称 */
    @Schema(description = "名称")
    private String name;

    /** PC收银台是否同时显示聚合收银码 */
    @Schema(description = "PC收银台是否同时显示聚合收银码")
    private Boolean aggregateShow;

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
    @Size(max = 128, message = "限制用户支付类型不能超过128位")
    private String miniAppLimitPay;

    /** 小程序关联终端号 */
    @Schema(description = "小程序关联终端号")
    private String miniAppTerminalNo;

    /** 应用号 */
    @NotBlank(message = "应用号不可为空")
    @Size(max = 32, message = "应用号不可超过32位")
    @Schema(description = "应用号")
    private String appId;
}
