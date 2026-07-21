package cn.daxpay.open.payment.trade.notice.param;

import cn.daxpay.open.platform.core.annotation.QueryParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 商户出站通知发送记录查询
///
@Data
@Accessors(chain = true)
@Schema(title = "商户出站通知发送记录查询")
public class MchNoticeRecordQuery {

    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "任务ID")
    private Long taskId;

    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "商户号")
    private String mchNo;

    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "是否成功")
    private Boolean success;
}
