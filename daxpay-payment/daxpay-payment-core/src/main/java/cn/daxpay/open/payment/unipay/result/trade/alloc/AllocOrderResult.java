package cn.daxpay.open.payment.unipay.result.trade.alloc;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;
import java.util.List;

/// # 分账订单查询结果(对外)
///
/// 不复用管理端 [cn.daxpay.open.payment.trade.alloc.result.AllocOrderResult]
/// (管理端含 mchName 翻译、channelMchNo 等内部字段不宜暴露给商户)。
@Data
@Accessors(chain = true)
@Schema(title = "分账订单查询结果")
public class AllocOrderResult {

    /// 平台分账单号
    @Schema(description = "平台分账单号")
    private String allocNo;

    /// 商户分账单号
    @Schema(description = "商户分账单号")
    private String bizAllocNo;

    /// 原支付资金交易号
    @Schema(description = "原支付资金交易号")
    private String tradeNo;

    /// 商户业务订单号
    @Schema(description = "商户业务订单号")
    private String bizOrderNo;

    /// 通道分账单号
    @Schema(description = "通道分账单号")
    private String outAllocNo;

    /// 分账总金额(分)
    @Schema(description = "分账总金额(分)")
    private Long amount;

    /// 分账状态
    /// @see cn.daxpay.open.payment.trade.alloc.enums.AllocOrderStatusEnum
    @Schema(description = "分账状态")
    private String status;

    /// 分账完成时间
    @Schema(description = "分账完成时间")
    private OffsetDateTime finishTime;

    /// 支付通道
    @Schema(description = "支付通道")
    private String channel;

    /// 商户扩展参数
    @Schema(description = "商户扩展参数")
    private String attach;

    /// 错误信息
    @Schema(description = "错误信息")
    private String errorMsg;

    /// 明细列表
    @Schema(description = "分账明细列表")
    private List<AllocDetailResult> details;

    /// 分账明细结果(对外)
    @Data
    @Accessors(chain = true)
    @Schema(title = "分账明细结果")
    public static class AllocDetailResult {

        /// 接收方类型
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

        /// 错误信息
        @Schema(description = "错误信息")
        private String errorMsg;

        /// 明细完成时间
        @Schema(description = "明细完成时间")
        private OffsetDateTime finishTime;
    }
}
