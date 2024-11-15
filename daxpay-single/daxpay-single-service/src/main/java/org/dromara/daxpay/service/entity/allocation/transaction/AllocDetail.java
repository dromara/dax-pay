package org.dromara.daxpay.service.entity.allocation.transaction;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.dromara.daxpay.core.enums.AllocDetailResultEnum;
import org.dromara.daxpay.core.enums.AllocReceiverTypeEnum;
import org.dromara.daxpay.core.result.allocation.transaction.AllocDetailResult;
import org.dromara.daxpay.service.common.entity.MchAppBaseEntity;
import org.dromara.daxpay.service.convert.allocation.AllocTransactionConvert;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 分账明细
 * @author xxm
 * @since 2024/11/14
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class AllocDetail extends MchAppBaseEntity implements ToResult<AllocDetailResult> {

    /** 分账订单ID */
    private Long allocationId;

    /** 接收者ID */
    private Long receiverId;

    /** 分账接收方编号 */
    private String receiverNo;

    /** 分账比例 */
    private BigDecimal rate;

    /** 分账金额 */
    private BigDecimal amount;

    /**
     * 分账接收方类型
     * @see AllocReceiverTypeEnum
     */
    private String receiverType;

    /** 接收方账号 */
    private String receiverAccount;

    /** 接收方姓名 */
    private String receiverName;

    /**
     * 分账结果
     * @see AllocDetailResultEnum
     */
    private String result;

    /** 错误代码 */
    private String errorCode;

    /** 错误原因 */
    private String errorMsg;

    /** 分账完成时间 */
    private LocalDateTime finishTime;

    /**
     * 转换
     */
    @Override
    public AllocDetailResult toResult() {
        return AllocTransactionConvert.CONVERT.toResult(this);
    }
}
