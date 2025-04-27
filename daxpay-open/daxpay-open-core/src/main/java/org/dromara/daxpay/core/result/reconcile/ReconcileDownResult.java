package org.dromara.daxpay.core.result.reconcile;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 对账单文件下载链接
 * @author xxm
 * @since 2024/8/21
 */
@Data
@Accessors(chain = true)
@Schema(title = "对账单文件下载链接")
public class ReconcileDownResult {

    @Schema(description = "文件下载链接")
    private String fileUrl;
}
