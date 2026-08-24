package cn.daxpay.open.payment.unipay.result.trade.transfer;

import cn.daxpay.open.payment.trade.enums.PayFundStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 转账订单(统一查询)
///
/// 对外查询结果(基于跨通道公共凭证 TransferTrade 的精简快照),
/// 不复用管理端 [cn.daxpay.open.payment.trade.transfer.result.TransferTradeResult]
/// (后者含翻译等内部字段, 不宜对商户暴露); 收款人等敏感信息留在通道容器侧, 不对外提供。
@Data
@Accessors(chain = true)
@Schema(title = "转账订单")
public class TransferOrderResult {

    /// 平台转账单号
    @Schema(description = "平台转账单号")
    private String transferNo;

    /// 商户转账号
    @Schema(description = "商户转账号")
    private String bizTransferNo;

    /// 通道转账单号
    @Schema(description = "通道转账单号")
    private String outTransferNo;

    /// 实际上送通道的商户转账号
    @Schema(description = "上送通道的商户转账号")
    private String relationNo;

    /// 转账金额(分)
    @Schema(description = "转账金额(分)")
    private Long amount;

    /// 币种
    @Schema(description = "币种")
    private String currency;

    /// 转账通道
    @Schema(description = "转账通道")
    private String channel;

    /// 钱包渠道
    @Schema(description = "钱包渠道")
    private String provider;

    /// 转账状态
    /// @see PayFundStatusEnum
    @Schema(description = "转账状态")
    private String status;

    /// 转账标题
    @Schema(description = "转账标题")
    private String title;

    /// 转账完成时间(UTC)
    @Schema(description = "转账完成时间(UTC)")
    private OffsetDateTime finishTime;

    /// 错误信息(失败时从通道转账单回填)
    @Schema(description = "错误信息")
    private String errorMsg;

}
