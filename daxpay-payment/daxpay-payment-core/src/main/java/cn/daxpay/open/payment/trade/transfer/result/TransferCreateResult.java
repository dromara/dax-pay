package cn.daxpay.open.payment.trade.transfer.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 转账发起结果
///
/// 返回平台转账单号与(微信)确认收款链接, 供商户前端展示给收款人。
@Data
@Accessors(chain = true)
@Schema(title = "转账发起结果")
public class TransferCreateResult {

    @Schema(description = "平台转账单号")
    private String transferNo;

    @Schema(description = "确认收款链接(微信转账, 收款人在微信内打开确认)")
    private String confirmUrl;
}
