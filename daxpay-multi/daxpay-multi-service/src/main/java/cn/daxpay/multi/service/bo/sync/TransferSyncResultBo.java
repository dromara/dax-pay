package cn.daxpay.multi.service.bo.sync;

import cn.daxpay.multi.service.enums.RefundSyncResultEnum;
import cn.daxpay.multi.service.enums.TransferSyncResultEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 转账同步结果
 * @author xxm
 * @since 2024/7/25
 */
@Data
@Accessors(chain = true)
public class TransferSyncResultBo {

    /**
     * 支付网关订单状态, 默认为转账中
     * @see RefundSyncResultEnum
     */
    private TransferSyncResultEnum syncStatus = TransferSyncResultEnum.PROGRESS;

    /** 同步时网关返回的对象, 序列化为json字符串 */
    private String syncInfo;

    /**
     * 外部三方支付网关生成的转账交易号, 用与将记录关联起来
     */
    private String outRefundNo;

    /** 完成时间(通常用于接收网关返回的时间) */
    private LocalDateTime finishTime;

    /** 错误提示码 */
    private String errorCode;

    /** 错误提示 */
    private String errorMsg;
}
