package cn.daxpay.multi.service.bo.sync;

import cn.daxpay.multi.service.enums.PaySyncResultEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

import static cn.daxpay.multi.service.enums.PaySyncResultEnum.FAIL;

/**
 *
 * @author xxm
 * @since 2024/7/25
 */
@Data
@Accessors(chain = true)
public class PaySyncResultBo {

    /**
     * 支付网关订单状态
     * @see PaySyncResultEnum
     */
    private PaySyncResultEnum syncStatus = FAIL;

    /**
     * 外部第三方支付系统的交易号, 用与和本地记录关联起来
     */
    private String outOrderNo;

    /** 支付完成时间 */
    private LocalDateTime finishTime;

    /** 同步时网关返回的对象, 序列化为json字符串 */
    private String syncInfo;

    /** 错误提示码 */
    private String errorCode;

    /** 错误提示 */
    private String errorMsg;
}
