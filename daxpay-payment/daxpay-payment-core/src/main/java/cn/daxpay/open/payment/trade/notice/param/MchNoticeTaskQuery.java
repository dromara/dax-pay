package cn.daxpay.open.payment.trade.notice.param;

import cn.daxpay.open.platform.core.annotation.QueryParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 商户出站通知任务查询
///
@Data
@Accessors(chain = true)
@Schema(title = "商户出站通知任务查询")
public class MchNoticeTaskQuery {

    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "商户号")
    private String mchNo;

    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "应用号")
    private String appId;

    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    @Schema(description = "业务单号")
    private String bizNo;

    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "通知事件码")
    private String event;

    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "传输通道 (http/mq)")
    private String transport;

    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "报文格式 (system/easy_pay)")
    private String format;

    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "URL来源")
    private String source;

    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "是否发送成功")
    private Boolean success;
}
