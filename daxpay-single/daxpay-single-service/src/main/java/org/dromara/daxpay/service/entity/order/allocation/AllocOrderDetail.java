package org.dromara.daxpay.service.entity.order.allocation;

import org.dromara.daxpay.core.enums.AllocDetailResultEnum;
import org.dromara.daxpay.core.enums.AllocReceiverTypeEnum;
import org.dromara.daxpay.service.common.entity.MchAppBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 分账订单明细
 * @author xxm
 * @since 2024/6/1
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_alloc_order_detail")
public class AllocOrderDetail extends MchAppBaseEntity {

    /** 分账订单ID */
    private Long allocId;

    /** 接收者ID */
    private Long receiverId;

    /** 分账接收方编号 */
    private String receiverNo;

    /** 分账比例(百分之多少) */
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
}
