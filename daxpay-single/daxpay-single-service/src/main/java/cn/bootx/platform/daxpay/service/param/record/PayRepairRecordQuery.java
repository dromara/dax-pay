package cn.bootx.platform.daxpay.service.param.record;

import cn.bootx.platform.common.core.annotation.QueryParam;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.service.code.PayRepairSourceEnum;
import cn.bootx.platform.daxpay.service.code.PayRepairTypeEnum;
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
