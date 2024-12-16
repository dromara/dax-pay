package org.dromara.daxpay.service.result.allocation.order;

import com.fhs.core.trans.anno.Trans;
import com.fhs.core.trans.constant.TransType;
import com.fhs.core.trans.vo.TransPojo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;
import org.dromara.daxpay.core.enums.AllocDetailResultEnum;
import org.dromara.daxpay.core.enums.AllocReceiverTypeEnum;
import org.dromara.daxpay.core.result.MchAppResult;
import org.dromara.daxpay.service.entity.allocation.receiver.AllocReceiver;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 分账订单明细
 * @author xxm
 * @since 2024/5/21
 */
@EqualsAndHashCode(callSuper = true)
@Data
@FieldNameConstants
@Accessors(chain = true)
@Schema(title = "分账订单明细")
public class AllocDetailVo extends MchAppResult implements TransPojo {

    /** 分账订单ID */
    @Schema(description = "分账订单ID")
    private Long allocationId;

    /** 外部明细ID */
    @Schema(description = "外部明细ID")
    private String outDetailId;

    @Schema(description = "分账接收方编号")
    private String receiverNo;

    @Trans(type = TransType.SIMPLE, target = AllocReceiver.class, fields = AllocReceiver.Fields.name, ref = AllocDetailVo.Fields.name)
    @Schema(description = "分账接收方Id")
    private Long receiverId;

    @Schema(description = "名称")
    private String name;

    /** 分账金额 */
    @Schema(description = "分账金额")
    private BigDecimal amount;

    /** 分账比例 */
    @Schema(description = "分账比例(百分之多少)")
    private BigDecimal rate;

    /**
     * 分账接收方类型
     * @see AllocReceiverTypeEnum
     */
    @Schema(description = "分账接收方类型")
    private String receiverType;

    /** 接收方账号 */
    @Schema(description = "接收方账号")
    private String receiverAccount;

    /** 接收方姓名 */
    @Schema(description = "接收方姓名")
    private String receiverName;

    /**
     * 分账结果
     * @see AllocDetailResultEnum
     */
    @Schema(description = "分账结果")
    private String result;

    /** 错误代码 */
    @Schema(description = "错误代码")
    private String errorCode;

    /** 错误原因 */
    @Schema(description = "错误原因")
    private String errorMsg;

    /** 分账完成时间 */
    @Schema(description = "分账完成时间")
    private LocalDateTime finishTime;
}
