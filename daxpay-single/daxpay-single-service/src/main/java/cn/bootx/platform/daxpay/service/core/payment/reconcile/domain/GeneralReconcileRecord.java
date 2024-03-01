package cn.bootx.platform.daxpay.service.core.payment.reconcile.domain;

import cn.bootx.platform.daxpay.service.code.AliPayRecordTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 通用对账记录对象，用于与网关进行对账
 * @author xxm
 * @since 2024/3/1
 */
@Data
public class GeneralReconcileRecord {
    /** 标题 */
    private String title;

    /** 金额 */
    private Integer amount;

    /**
     * 业务类型
     * @see AliPayRecordTypeEnum
     */
    private String type;

    /** 本地订单号 */
    private Long orderId;

    /** 网关订单号 */
    private String gatewayOrderNo;

    /** 网关完成时间 */
    private LocalDateTime gatewayTime;


}
