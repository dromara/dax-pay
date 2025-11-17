package org.dromara.daxpay.payment.merchant.param.onboarded;

import cn.bootx.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 进件商户信息
 * @author xxm
 * @since 2025/11/11
 */
@Data
@Accessors(chain = true)
@Schema(title = "进件商户信息")
public class OnbMchInfoParam {

    /** 主键 */
    @Schema(description = "主键")
    @NotNull(message = "主键ID不可为空", groups = ValidationGroup.edit.class)
    private Long id;

    /** 商户号 */
    @Schema(description = "商户号")
    @NotBlank(message = "商户号不可为空")
    private String mchNo;

    /** 进件商户号 */
    @Schema(description = "进件商户号")
    @NotBlank(message = "进件商户号不可为空")
    private String onbMchNo;

    /** 商户名称 */
    @Schema(description = "商户名称")
    @NotBlank(message = "商户名称不可为空")
    private String onbMchName;

    /** 所属通道 */
    @Schema(description = "所属通道")
    @NotBlank(message = "所属通道不可为空")
    private String onbChannel;
}
