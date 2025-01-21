package org.dromara.daxpay.single.sdk.model.reconcile;

import lombok.Data;

/**
 * 对账单文件下载链接
 * @author xxm
 * @since 2024/8/21
 */
@Data
public class ReconcileDownModel {
    /**
     * 文件下载链接
     */
    private String fileUrl;
}
