package org.dromara.daxpay.sdk.result.reconcile;

import lombok.Data;

/**
 * 对账单文件下载链接
 * @author xxm
 * @since 2024/8/21
 */
@Data
public class ReconcileDownResult {
    /**
     * 文件下载链接
     */
    private String fileUrl;
}
