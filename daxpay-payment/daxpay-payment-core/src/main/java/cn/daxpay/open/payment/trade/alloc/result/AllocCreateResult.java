package cn.daxpay.open.payment.trade.alloc.result;

import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 分账发起结果
///
/// 对外/管理端发起分账后的返回, 包含分账单号和当前状态。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "分账发起结果")
public class AllocCreateResult extends BaseResult {

    /// 平台分账单号
    @Schema(description = "平台分账单号")
    private String allocNo;

    /// 商户分账单号
    @Schema(description = "商户分账单号")
    private String bizAllocNo;

    /// 分账状态
    /// @see cn.daxpay.open.payment.trade.alloc.enums.AllocOrderStatusEnum
    @Schema(description = "分账状态")
    private String status;

    /// 错误信息(失败时)
    @Schema(description = "错误信息")
    private String errorMsg;
}
