package org.dromara.daxpay.payment.isv.param.isv;

import cn.bootx.platform.core.validation.ValidationGroup;
import org.dromara.daxpay.payment.isv.enums.IsvStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 服务商
 * @author xxm
 * @since 2024/10/29
 */
@Data
@Accessors(chain = true)
@Schema(title = "服务商")
public class IsvInfoParam {

    /** 主键 */
    @Null(message = "Id需要为空", groups = ValidationGroup.add.class)
    @NotNull(message = "Id不可为空", groups = ValidationGroup.edit.class)
    @Schema(description = "主键")
    private Long id;

    /** 名称 */
    @NotBlank(message = "名称不可为空")
    @Schema(description = "名称")
    private String name;

    /**
     * 状态
     * @see IsvStatusEnum
     */
    @NotBlank(message = "状态不可为空")
    @Schema(description = "状态")
    private String status;

}
