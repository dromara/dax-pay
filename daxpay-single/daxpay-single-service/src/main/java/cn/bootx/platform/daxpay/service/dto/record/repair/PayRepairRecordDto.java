package cn.bootx.platform.daxpay.service.dto.record.repair;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.service.code.PayRepairSourceEnum;
import cn.bootx.platform.daxpay.service.code.PayRepairTypeEnum;
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

    /** 支付ID */
    @Schema(description = "支付ID")
    private Long paymentId;

    /** 业务号 */
    @Schema(description = "业务号")
    private String businessNo;

    /**
     * 修复来源
     * @see PayRepairSourceEnum
     */
    @Schema(description = "修复来源")
    private String repairSource;

    /**
     * 修复类型
     * @see PayRepairTypeEnum
     */
    @Schema(description = "修复类型")
    private String repairType;

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