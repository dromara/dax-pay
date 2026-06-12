package org.dromara.daxpay.payment.pay.param.reconcile;

import org.dromara.daxpay.platform.core.enums.pay.reconcile.ReconcileFileTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 对账文件上传参数
///
@Data
@Accessors(chain = true)
@Schema(title = "对账文件上传参数")
public class ReconcileUploadParam {

    @Schema(description = "对账订单ID")
    @NotNull(message = "{validation.field.id.notNull}")
    private Long id;

    /// @see ReconcileFileTypeEnum
    @Schema(description = "文件类型")
    @NotNull(message = "{validation.field.fileType.notNull}")
    private String fileType;
}
