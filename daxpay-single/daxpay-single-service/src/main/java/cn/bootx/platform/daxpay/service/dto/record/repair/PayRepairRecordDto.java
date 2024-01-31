package cn.bootx.platform.daxpay.service.dto.record.repair;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.service.code.PayRepairSourceEnum;
import cn.bootx.platform.daxpay.service.code.PayRepairWayEnum;
import cn.bootx.platform.daxpay.service.code.RefundRepairWayEnum;
import cn.bootx.table.modify.annotation.DbColumn;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 支付修复记录
 * @author xxm
 * @since 2024/1/9
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "支付修复记录")
public class PayRepairRecordDto extends BaseDto {

    /**
     * 修复号
     * 如果一次修复产生的修复记录只有一条, 则该字段为与ID一致
     * 如果一次修复产生的修复记录有多个, 则使用这个ID作为关联
     */
    @Schema(description = "修复号")
    private String repairNo;

    /** 支付ID/退款ID */
    @Schema(description = "业务ID")
    private Long orderId;

    /** 支付业务号/退款号 业务号 */
    @Schema(description = "业务号")
    private String orderNo;

    /** 类型  支付修复/退款修复 */
    @Schema(description = "修复类型")
    private String repairType;

    /**
     * 修复来源
     * @see PayRepairSourceEnum
     */
    @Schema(description = "修复来源")
    private String repairSource;

    /**
     * 修复类型
     * @see PayRepairWayEnum
     * @see RefundRepairWayEnum
     */
    @Schema(description = "修复类型")
    private String repairWay;

    /** 修复的异步通道 */
    @DbColumn(comment = "修复的异步通道")
    private String asyncChannel;

    /**
     * 修复前状态
     * @see PayStatusEnum
     */
    @Schema(description = "修复前订单状态")
    private String beforeStatus;

    /**
     * 修复后状态
     * @see PayStatusEnum
     */
    @Schema(description = "修复后订单状态")
    private String afterStatus;

    /** 金额变动 */
    @Schema(description = "金额变动")
    private Integer amount;
}
