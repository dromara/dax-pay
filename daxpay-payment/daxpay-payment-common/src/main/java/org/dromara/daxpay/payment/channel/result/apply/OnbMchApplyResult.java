package org.dromara.daxpay.payment.channel.result.apply;

import org.dromara.daxpay.platform.core.enums.channel.OnbApplyStatusEnum;
import org.dromara.daxpay.payment.common.result.MchBaseResult;
import org.dromara.daxpay.platform.core.enums.channel.OnbApplySourceEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

import java.time.OffsetDateTime;

/// # `商户入驻申请结果
///
@EqualsAndHashCode(callSuper = true)
@Data
@FieldNameConstants
@Accessors(chain = true)
@Schema(title = "商户入驻申请")
public class OnbMchApplyResult extends MchBaseResult {

    @Schema(description = "进件通道")
    private String channel;

    /// 进件类型
    @Schema(description = "进件类型")
    private String applyType;

    /// 单据名称
    @Schema(description = "单据名称")
    private String name;

    /// 外部状态
    @Schema(description = "外部状态")
    private String outStatus;

    /// 状态
    /// @see OnbApplyStatusEnum
    @Schema(description = "状态")
    private String status;

    /// 最后提交时间
    @Schema(description = "最后提交时间")
    private OffsetDateTime lastSubmitTime;

    /// 来源
    /// @see OnbApplySourceEnum
    @Schema(description = "来源")
    private String source;

    /// 错误提示
    @Schema(description = "错误提示")
    private String errorMsg;
}

