package org.dromara.daxpay.service.bo.sync;

import org.dromara.daxpay.core.enums.PayStatusEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *
 * @author xxm
 * @since 2024/7/25
 */
@Data
@Accessors(chain = true)
public class PaySyncResultBo {


    /** 同步是否成功 */
    private boolean syncSuccess = true;

    /**
     * 支付网关订单状态
     */
    private PayStatusEnum payStatus;

    /** 支付通道对应系统的交易号, 用与和本地记录关联起来 */
    private String outOrderNo;

    /** 交易错误信息 */
    private String tradeErrorMsg;

    /** 交易金额 */
    private BigDecimal amount;

    /** 支付完成时间 */
    private LocalDateTime finishTime;

    /** 同步时网关返回的对象, 序列化为json字符串 */
    private String syncData;

    /** 错误提示码 */
    private String syncErrorCode;

    /** 错误提示 */
    private String syncErrorMsg;
}
