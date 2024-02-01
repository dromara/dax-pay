package cn.bootx.platform.daxpay.service.param.record;

import cn.bootx.platform.common.core.annotation.QueryParam;
import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.PaySyncStatusEnum;
import cn.bootx.table.modify.annotation.DbColumn;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 支付同步记录查询参数
 * @author xxm
 * @since 2024/1/9
 */
@QueryParam(type = QueryParam.CompareTypeEnum.EQ)
@Data
@Accessors(chain = true)
@Schema(title = "支付同步记录查询参数")
public class PaySyncRecordQuery {

    /** 本地订单id */
    @Schema(description = "本地订单id")
    private Long orderId;

    @Schema(description = "本地订单号")
    private String orderNo;

    /** 网关订单号 */
    @Schema(description = "网关订单号")
    private String gatewayOrderNo;

    /** 同步类型 */
    @Schema(description = "同步类型")
    private String syncType;

    /**
     * 支付通道
     * @see PayChannelEnum#getCode()
     */
    @Schema(description = "支付通道")
    private String asyncChannel;

    @Schema(description = "请求链路ID")
    private String reqId;

    /**
     * 同步状态
     * @see PaySyncStatusEnum
     */
    @Schema(description = "同步状态")
    private String gatewayStatus;

    /**
     * 支付单如果状态不一致, 是否修复成功
     */
    @DbColumn(comment = "是否进行修复")
    private Boolean repairOrder;

    /** 客户端IP */
    @Schema(description = "客户端IP")
    private String clientIp;
}
