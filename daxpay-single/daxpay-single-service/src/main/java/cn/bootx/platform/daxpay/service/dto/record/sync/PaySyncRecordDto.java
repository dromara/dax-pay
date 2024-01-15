package cn.bootx.platform.daxpay.service.dto.record.sync;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.PaySyncStatusEnum;
import cn.bootx.table.modify.mysql.annotation.DbMySqlFieldType;
import cn.bootx.table.modify.mysql.constants.MySqlFieldTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 支付同步记录
 * @author xxm
 * @since 2023/7/14
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "支付同步订单")
public class PaySyncRecordDto extends BaseDto {

    /** 支付记录id */
    @Schema(description = "支付记录id")
    private Long paymentId;

    /** 业务号 */
    @Schema(description = "业务号")
    private String businessNo;

    /**
     * 同步通道
     * @see PayChannelEnum#getCode()
     */
    @Schema(description = "同步通道")
    private String asyncChannel;

    /** 通知消息 */
    @DbMySqlFieldType(MySqlFieldTypeEnum.LONGTEXT)
    @Schema(description = "通知消息")
    private String syncInfo;

    /**
     * 网关返回状态
     * @see PaySyncStatusEnum
     */
    @Schema(description = "网关返回状态")
    private String gatewayStatus;

    /**
     * 支付单如果状态不一致, 是否进行修复
     */
    @Schema(description = "是否进行修复")
    private boolean repairOrder;

    @Schema(description = "支付单修复ID")
    private Long repairOrderId;

    @Schema(description = "错误消息")
    private String errorMsg;

    /** 客户端IP */
    @Schema(description = "客户端IP")
    private String clientIp;

    /** 请求链路ID */
    @Schema(description = "请求链路ID")
    private String reqId;
}
