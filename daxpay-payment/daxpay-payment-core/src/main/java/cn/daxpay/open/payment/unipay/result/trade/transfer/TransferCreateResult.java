package cn.daxpay.open.payment.unipay.result.trade.transfer;

import cn.daxpay.open.payment.trade.enums.PayFundStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 统一转账响应参数
///
/// 发起失败会直接以错误响应返回(单据已置失败, 可复用原商户转账号重试);
/// 正常返回时状态为 processing(通道处理中/待收款人确认)或 success(通道同步成功)。
@Data
@Accessors(chain = true)
@Schema(title = "统一转账响应参数")
public class TransferCreateResult {

    /// 平台转账单号
    @Schema(description = "平台转账单号")
    private String transferNo;

    /// 商户转账号
    @Schema(description = "商户转账号")
    private String bizTransferNo;

    /// 转账状态
    /// @see PayFundStatusEnum
    @Schema(description = "转账状态")
    private String status;

    /// 确认收款链接(仅微信转账且待收款人确认时返回, 由商户发给收款人在微信内打开; 平台未配置网关地址时为空)
    @Schema(description = "确认收款链接")
    private String confirmUrl;

}
