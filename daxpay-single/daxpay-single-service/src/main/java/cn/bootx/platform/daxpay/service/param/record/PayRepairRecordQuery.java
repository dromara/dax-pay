package cn.bootx.platform.daxpay.service.param.record;

import cn.bootx.platform.common.core.annotation.QueryParam;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.service.code.PayRepairSourceEnum;
import cn.bootx.platform.daxpay.service.code.PayRepairWayEnum;
import cn.bootx.platform.daxpay.service.code.PaymentTypeEnum;
import cn.bootx.platform.daxpay.service.code.RefundRepairWayEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 支付修复记录查询参数
 * @author xxm
 * @since 2024/1/9
 */
@QueryParam(type = QueryParam.CompareTypeEnum.EQ)
@Data
@Accessors(chain = true)
@Schema(title = "支付修复记录查询参数")
public class PayRepairRecordQuery {


    /**
     * 修复号
     * 如果一次修复产生的修复记录有多个记录, 使用这个作为关联
     */
    @Schema(description = "修复号")
    private String repairNo;

    /** 支付ID/退款ID */
    @Schema(description = "本地订单ID")
    private Long tradeId;

    /**
     * 本地交易号, 支付号/退款号
     */
    @Schema(description = "本地业务号")
    private String tradeNo;

    /**
     * 修复类型 支付修复/退款修复
     * @see PaymentTypeEnum
     */
    @Schema(description = "修复类型")
    private String repairType;

    /**
     * 修复来源
     * @see PayRepairSourceEnum
     */
    @Schema(description = "修复来源")
    private String repairSource;

    /**
     * 修复方式
     * @see PayRepairWayEnum
     * @see RefundRepairWayEnum
     */
    @Schema(description = "修复方式")
    private String repairWay;

    /** 修复的异步通道 */
    @Schema(description = "修复的异步通道")
    private String channel;

    /**
     * 修复前状态
     * @see PayStatusEnum
     */
    @Schema(description = "修复前状态")
    private String beforeStatus;

    /**
     * 修复后状态
     * @see PayStatusEnum
     */
    @Schema(description = "修复后状态")
    private String afterStatus;
}
