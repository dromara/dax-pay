package cn.bootx.platform.daxpay.service.dto.record.notice;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.code.RefundStatusEnum;
import cn.bootx.platform.daxpay.service.code.ClientNoticeTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 消息通知任务
 * @author xxm
 * @since 2024/2/23
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "消息通知任务")
public class ClientNoticeTaskDto extends BaseDto {

    /** 本地交易ID */
    @Schema(description = "本地交易ID")
    private Long tradeId;

    /** 本地交易号 */
    @Schema(description = "本地交易号")
    private String tradeNo;

    /**
     * 回调类型
     * @see ClientNoticeTypeEnum
     */
    @Schema(description = "回调类型")
    private String noticeType;

    /**
     * 交易状态
     * @see PayStatusEnum
     * @see RefundStatusEnum
     */
    @Schema(description = "订单状态")
    private String tradeStatus;

    /** 消息内容 */
    @Schema(description = "消息内容")
    private String content;

    /** 是否发送成功 */
    @Schema(description = "是否发送成功")
    private boolean success;

    /** 发送次数 */
    @Schema(description = "发送次数")
    private Integer sendCount;

    /** 发送地址 */
    @Schema(description = "发送地址")
    private String url;

    /** 最后发送时间 */
    @Schema(description = "最后发送时间")
    private LocalDateTime latestTime;
}
