package org.dromara.daxpay.payment.isv.param.gateway;

import cn.bootx.platform.core.validation.ValidationGroup;
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
public class IsvGatewayPayConfigParam {

    /** 主键 */
    @Null(message = "Id需要为空", groups = ValidationGroup.add.class)
    @NotNull(message = "Id不可为空", groups = ValidationGroup.edit.class)
    @Schema(description = "主键")
    private Long id;


    /** 聚合二维码是否显示 */
    @Schema(description = "聚合二维码是否显示")
    private boolean aggregateShow;

    /** h5收银台自动升级聚合支付 */
    @Schema(description = "h5收银台自动升级聚合支付")
    private boolean h5AutoUpgrade;

    /** 服务商号 */
    @NotBlank(message = "服务商号不可为空")
    @Size(max = 32, message = "服务商号不可超过32位")
    @Schema(description = "服务商号")
    private String isvNo;
}
