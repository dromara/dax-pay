package cn.bootx.platform.daxpay.service.dto.record.notice;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import cn.bootx.platform.daxpay.service.code.ClientNoticeSendTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 消息通知记录
 * @author xxm
 * @since 2024/2/23
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "消息通知记录")
public class ClientNoticeRecordDto extends BaseDto {

    /** 任务ID */
    @Schema(description = "任务ID")
    private Long taskId;

    /**
     * 发送类型, 自动发送, 手动发送
     * @see ClientNoticeSendTypeEnum
     */
    @Schema(description = "发送类型")
    private String sendType;

    /** 请求次数 */
    @Schema(description = "请求次数")
    private Integer reqCount;

    /** 发送是否成功 */
    @Schema(description = "发送是否成功")
    private boolean success;

    /** 错误编码 */
    @Schema(description = "错误编码")
    private String errorCode;

    /** 错误信息 */
    @Schema(description = "错误信息")
    private String errorMsg;

}
