package cn.daxpay.open.plugin.risk.result;

import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 黑名单结果
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "黑名单结果")
public class PayBlacklistResult extends BaseResult {

    @Schema(description = "类型")
    private String type;

    @Schema(description = "名单值")
    private String value;

    @Schema(description = "微信平台支付应用 AppId")
    private String wxAppId;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "拉黑原因")
    private String reason;

    @Schema(description = "过期时间")
    private OffsetDateTime expireTime;

    @Schema(description = "备注")
    private String remark;
}
