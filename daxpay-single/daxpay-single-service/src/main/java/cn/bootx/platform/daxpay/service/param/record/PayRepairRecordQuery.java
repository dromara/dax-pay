package cn.bootx.platform.daxpay.service.param.record;

import cn.bootx.platform.common.core.annotation.QueryParam;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.service.code.PayRepairSourceEnum;
import cn.bootx.platform.daxpay.service.code.PayRepairWayEnum;
import cn.bootx.platform.daxpay.service.code.PaymentTypeEnum;
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

    /** 本地订单ID */
    @Schema(description = "本地订单ID")
    private Long orderId;

    /** 本地订单号 */
    @Schema(description = "本地订单号")
    private String orderNo;

    @Schema(description = "修复单号")
    private String repairNo;

    /**
     * 修复类型
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
     */
    @Schema(description = "修复方式")
    private String repairWay;

    /** 修复的异步通道 */
    @Schema(description = "修复的异步通道")
    private String asyncChannel;

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

    @Schema(description = "请求链路ID")
    private String reqId;
}
