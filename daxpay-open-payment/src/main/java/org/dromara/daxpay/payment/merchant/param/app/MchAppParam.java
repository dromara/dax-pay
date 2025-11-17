package org.dromara.daxpay.payment.merchant.param.app;

import cn.bootx.platform.core.validation.ValidationGroup;
import org.dromara.daxpay.payment.merchant.enums.MchAppStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 商户应用
 * @author xxm
 * @since 2024/6/24
 */
@Data
@Accessors(chain = true)
@Schema(title = "商户应用")
public class MchAppParam {

    /** 主键 */
    @Schema(description = "主键")
    @NotNull(message = "主键ID不可为空", groups = ValidationGroup.edit.class)
    private Long id;

    /** 商户号 */
    @Schema(description = "商户号")
    private String mchNo;

    /** 应用名称 */
    @Schema(description = "应用名称")
    @NotNull(message = "应用名称不可为空")
    private String appName;

    /**
     * 应用状态
     * @see MchAppStatusEnum
     */
    @Schema(description = "应用状态")
    @NotBlank(message = "应用状态不可为空")
    private String status;

    /**
     * 通知地址, http/WebSocket 需要配置
     */
    @Schema(description = "通知地址")
    private String notifyUrl;
}
