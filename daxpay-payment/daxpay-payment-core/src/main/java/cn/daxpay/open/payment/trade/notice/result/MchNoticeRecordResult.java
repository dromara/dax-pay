package cn.daxpay.open.payment.trade.notice.result;

import cn.daxpay.open.payment.common.result.MchBaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 商户出站通知发送记录结果
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "商户出站通知发送记录")
public class MchNoticeRecordResult extends MchBaseResult {

    @Schema(description = "通知任务ID")
    private Long taskId;

    @Schema(description = "发送序号")
    private Integer reqCount;

    @Schema(description = "发送类型")
    private String sendType;

    @Schema(description = "是否成功")
    private Boolean success;

    @Schema(description = "HTTP状态码")
    private Integer httpStatus;

    @Schema(description = "错误摘要")
    private String errorMsg;

    @Schema(description = "请求摘要")
    private String requestDigest;
}
