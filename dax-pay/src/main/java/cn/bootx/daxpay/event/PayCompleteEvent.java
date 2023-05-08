package cn.bootx.daxpay.event;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 支付完成事件
 *
 * @author xxm
 * @date 2022/7/11
 */
@Data
@Accessors(chain = true)
public class PayCompleteEvent {

    /** 支付单ID */
    private Long paymentId;

    /** 业务单号 */
    private String businessId;

}
