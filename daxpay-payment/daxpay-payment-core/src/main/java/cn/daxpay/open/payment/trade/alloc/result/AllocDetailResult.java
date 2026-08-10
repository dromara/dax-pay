package cn.daxpay.open.payment.trade.alloc.result;

import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 分账明细结果
///
/// 接收方账号/姓名在通道适配层已做脱敏(参照 [TransferPayeeTypeEnum] 场景),
/// 管理端展示时由 [cn.daxpay.open.payment.trade.transfer.util.PayeeDesensitizeUtil] 脱敏。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "分账明细")
public class AllocDetailResult extends BaseResult {

    /// 分账单号
    @Schema(description = "分账单号")
    private String allocNo;

    /// 接收方类型
    /// @see cn.daxpay.open.payment.trade.alloc.enums.AllocReceiverTypeEnum
    @Schema(description = "接收方类型")
    private String receiverType;

    /// 接收方账号
    @Schema(description = "接收方账号")
    private String receiverAccount;

    /// 接收方姓名
    @Schema(description = "接收方姓名")
    private String receiverName;

    /// 分账金额(分)
    @Schema(description = "分账金额(分)")
    private Long amount;

    /// 明细结果
    /// @see cn.daxpay.open.payment.trade.alloc.enums.AllocDetailResultEnum
    @Schema(description = "明细结果")
    private String result;

    /// 通道侧明细ID
    @Schema(description = "通道侧明细ID")
    private String outDetailId;

    /// 错误码
    @Schema(description = "错误码")
    private String errorCode;

    /// 错误信息
    @Schema(description = "错误信息")
    private String errorMsg;

    /// 明细完成时间
    @Schema(description = "明细完成时间")
    private OffsetDateTime finishTime;
}
