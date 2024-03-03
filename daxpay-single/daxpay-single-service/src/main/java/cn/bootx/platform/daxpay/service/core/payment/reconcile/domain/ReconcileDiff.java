package cn.bootx.platform.daxpay.service.core.payment.reconcile.domain;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 对账差异内容
 * @author xxm
 * @since 2024/3/1
 */
@Data
@Accessors(chain = true)
public class ReconcileDiff {

    /** 字段名 */
    private String fieldName;

    /** 本地订单字段值 */
    private String localValue;

    /** 网关订单字段值 */
    private String gatewayValue;
}
