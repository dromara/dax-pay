package cn.bootx.platform.daxpay.service.param.reconcile;

import cn.bootx.platform.daxpay.service.code.ReconcileFileTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * 对账文件上传参数
 * @author xxm
 * @since 2024/5/4
 */
@Data
@Accessors(chain = true)
@Schema(title = "对账文件上传参数")
public class ReconcileUploadParam {

    @Schema(description = "对账订单ID")
    @NotNull(message = "对账订单ID不能为空")
    private Long id;

    /**
     * @see ReconcileFileTypeEnum
     */
    @Schema(description = "文件类型")
    @NotNull(message = "文件类型不能为空")
    private String fileType;
}
