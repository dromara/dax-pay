package org.dromara.daxpay.service.pay.result.notice.notify;

import org.dromara.daxpay.service.merchant.result.info.MchResult;
import org.dromara.daxpay.service.pay.enums.NoticeSendTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 客户订阅通知发送记录
 * @author xxm
 * @since 2024/8/5
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "客户订阅通知发送记录")
public class MerchantNotifyRecordResult extends MchResult {

    /** 任务ID */
    @Schema(description = "任务ID")
    private Long taskId;

    /** 请求次数 */
    @Schema(description = "请求次数")
    private Integer reqCount;

    /** 发送是否成功 */
    @Schema(description = "发送是否成功")
    private boolean success;

    /**
     * 发送类型, 自动发送, 手动发送
     * @see NoticeSendTypeEnum
     */
    @Schema(description = "发送类型")
    private String sendType;

    /** 错误码 */
    @Schema(description = "错误码")
    private String errorCode;

    /** 错误信息 */
    @Schema(description = "错误信息")
    private String errorMsg;
}
