package cn.bootx.platform.daxpay.service.core.payment.reconcile.domain;

import cn.bootx.platform.daxpay.service.code.PaymentTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 通用交易对象对象，用于与网关进行对账
 * @author xxm
 * @since 2024/3/1
 */
@Data
public class GeneralTradeInfo {
    /** 标题 */
    private String title;

    /** 金额 */
    private Integer amount;

    /**
     * 业务类型
     * @see PaymentTypeEnum
     */
    private String type;

    /** 本地交易号 */
    private String tradeNo;

    /** 网关交易号 */
    private String outTradeNo;

    /** 网关完成时间 */
    private LocalDateTime finishTime;
}
