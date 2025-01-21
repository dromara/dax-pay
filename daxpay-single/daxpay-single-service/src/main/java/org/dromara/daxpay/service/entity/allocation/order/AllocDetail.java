package org.dromara.daxpay.service.entity.allocation.order;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.dromara.daxpay.core.enums.AllocDetailResultEnum;
import org.dromara.daxpay.core.enums.AllocReceiverTypeEnum;
import org.dromara.daxpay.service.common.entity.MchAppBaseEntity;
import org.dromara.daxpay.service.convert.allocation.AllocOrderConvert;
import org.dromara.daxpay.service.result.allocation.order.AllocDetailVo;

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
@TableName("pay_alloc_detail")
public class AllocDetail extends MchAppBaseEntity implements ToResult<AllocDetailVo> {

    /** 分账订单ID */
    private Long allocationId;

    /** 外部明细ID */
    private String outDetailId;

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
    public AllocDetailVo toResult() {
        return AllocOrderConvert.CONVERT.toVo(this);
    }
}
