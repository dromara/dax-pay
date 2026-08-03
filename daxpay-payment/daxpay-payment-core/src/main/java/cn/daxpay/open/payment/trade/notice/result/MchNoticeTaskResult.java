package cn.daxpay.open.payment.trade.notice.result;

import cn.daxpay.open.payment.common.result.MchBaseResult;
import cn.daxpay.open.payment.merchant.entity.info.MerchantInfo;
import cn.daxpay.open.platform.core.annotation.Trans;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 商户出站通知任务结果
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "商户出站通知任务")
public class MchNoticeTaskResult extends MchBaseResult {

    @Schema(description = "应用号")
    private String appId;

    @Schema(description = "业务主键ID")
    private Long bizId;

    @Schema(description = "业务单号")
    private String bizNo;

    @Schema(description = "通知事件码")
    private String event;

    @Schema(description = "传输通道 (http/mq)")
    private String transport;

    @Schema(description = "报文格式 (system/easy_pay)")
    private String format;

    @Schema(description = "URL来源")
    private String source;

    @Schema(description = "内容策略")
    private String contentMode;

    @Schema(description = "通知内容")
    private String content;

    @Schema(description = "目标地址 (HTTP回调URL或MQ Topic名)")
    private String url;

    @Schema(description = "是否发送成功")
    private Boolean success;

    @Schema(description = "已发送次数")
    private Integer sendCount;

    @Schema(description = "延迟重试次数")
    private Integer delayCount;

    @Schema(description = "下次发送时间")
    private OffsetDateTime nextTime;

    @Schema(description = "最后发送时间")
    private OffsetDateTime latestTime;

    @Schema(description = "最近一次错误摘要")
    private String errorMsg;
}
