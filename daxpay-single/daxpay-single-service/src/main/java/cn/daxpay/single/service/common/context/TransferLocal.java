package cn.daxpay.single.service.common.context;

import cn.daxpay.single.code.TransferStatusEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 转账相关信息
 * @author xxm
 * @since 2024/6/14
 */
@Data
@Accessors(chain = true)
public class TransferLocal {

    /** 通道转账订单号 */
    private String outTransferNo;

    /** 状态 */
    private TransferStatusEnum status = TransferStatusEnum.SUCCESS;

    /** 完成时间 */
    private LocalDateTime finishTime;
}

