package cn.daxpay.open.payment.unipay.result.trade.alloc;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 统一分账响应(发起返回)
@Data
@Accessors(chain = true)
@Schema(title = "统一分账响应")
public class AllocResult {

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

    /// 错误信息(失败时返回)
    @Schema(description = "错误信息")
    private String errorMsg;
}
