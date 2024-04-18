package cn.bootx.platform.daxpay.service.dto.order.allocation;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import cn.bootx.platform.daxpay.code.AllocationDetailResultEnum;
import cn.bootx.platform.daxpay.code.AllocationReceiverTypeEnum;
import cn.bootx.platform.starter.data.perm.sensitive.SensitiveInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 分账订单详情
 * @author xxm
 * @since 2024/4/7
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "分账订单详情")
public class AllocationOrderDetailDto extends BaseDto {


    /** 分账订单ID */
    @Schema(description = "分账订单ID")
    private Long allocationId;

    /** 接收者ID */
    @Schema(description = "接收者ID")
    private Long receiverId;

    /** 分账比例 */
    @Schema(description = "分账比例(万分之多少)")
    private Integer rate;

    /** 分账金额 */
    @Schema(description = "分账金额")
    private Integer amount;

    /**
     * 分账接收方类型
     * @see AllocationReceiverTypeEnum
     */
    @Schema(description = "分账接收方类型")
    private String receiverType;

    /** 接收方账号 */
    @Schema(description = "接收方账号")
    @SensitiveInfo
    private String receiverAccount;

    /** 接收方姓名 */
    @Schema(description = "接收方姓名")
    private String receiverName;

    /**
     * 分账结果
     * @see AllocationDetailResultEnum
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
