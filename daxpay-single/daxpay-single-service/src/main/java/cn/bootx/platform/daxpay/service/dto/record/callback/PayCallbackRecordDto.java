package cn.bootx.platform.daxpay.service.dto.record.callback;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.table.modify.mysql.annotation.DbMySqlFieldType;
import cn.bootx.table.modify.mysql.constants.MySqlFieldTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 支付回调记录
 * @author xxm
 * @since 2024/1/4
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "回调记录")
public class PayCallbackRecordDto extends BaseDto {

    /** 支付记录id */
    @Schema(description = "支付记录id")
    private Long paymentId;

    /**
     * 支付通道
     * @see PayChannelEnum#getCode()
     */
    @Schema(description = "支付通道")
    private String payChannel;

    /** 通知消息 */
    @DbMySqlFieldType(MySqlFieldTypeEnum.LONGTEXT)
    @Schema(description = "通知消息")
    private String notifyInfo;

    /**
     * 支付状态
     * @see PayStatusEnum
     */
    @Schema(description = "支付状态")
    private String payStatus;

    /**
     * 回调处理状态
     */
    @Schema(description = "回调处理状态")
    private String status;

    /** 提示信息 */
    @Schema(description = "提示信息")
    private String msg;

    /** 回调时间 */
    @Schema(description = "回调时间")
    private LocalDateTime notifyTime;
}
