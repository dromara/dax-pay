package cn.bootx.platform.daxpay.service.param.record;

import cn.bootx.platform.common.core.annotation.QueryParam;
import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.PaySyncStatusEnum;
import cn.bootx.platform.daxpay.code.RefundSyncStatusEnum;
import cn.bootx.platform.daxpay.service.code.PaymentTypeEnum;
import cn.bootx.table.modify.mysql.annotation.DbMySqlFieldType;
import cn.bootx.table.modify.mysql.constants.MySqlFieldTypeEnum;
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

    /** 本地交易号 */
    @Schema(description = "本地交易号")
    private String tradeNo;

    /** 商户交易号 */
    @Schema(description = "商户交易号")
    private String bizTradeNo;

    /** 通道交易号 */
    @Schema(description = "通道交易号")
    private String outTradeNo;


    /**
     * 同步结果
     * @see PaySyncStatusEnum
     * @see RefundSyncStatusEnum
     */
    @Schema(description = "同步结果")
    private String outTradeStatus;


    /**
     * 同步类型 支付/退款
     * @see PaymentTypeEnum
     */
    @Schema(description = "同步类型")
    private String syncType;

    /**
     * 同步的异步通道
     * @see PayChannelEnum#getCode()
     */
    @Schema(description = "同步的异步通道")
    private String channel;

    /** 三方支付返回的消息内容 */
    @DbMySqlFieldType(MySqlFieldTypeEnum.LONGTEXT)
    @Schema(description = "消息内容")
    private String syncInfo;

    /**
     * 支付单如果状态不一致, 是否进行修复
     */
    @Schema(description = "是否进行修复")
    private boolean repair;

    /** 修复单号 */
    @Schema(description = "修复单号")
    private String repairNo;

    /** 错误码 */
    @Schema(description = "错误码")
    private String errorCode;

    /** 错误消息 */
    @Schema(description = "错误消息")
    private String errorMsg;

    /** 客户端IP */
    @Schema(description = "客户端IP")
    private String clientIp;
}
