package cn.daxpay.open.payment.trade.alloc.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/// # 分账发起参数(内部编排用)
///
/// 各通道差异校验下沉到通道策略 [cn.daxpay.open.payment.strategy.alloc.AbsAllocStrategy]。
/// 金额采用直接传值模式(不传比例), 由调用方自行计算各接收方金额。
@Data
@Accessors(chain = true)
@Schema(title = "分账发起参数")
public class AllocParam {

    /// 商户号(运营端代发必填; 商户端/开放API由上下文强制)
    @Size(max = 32, message = "{validation.field.mchNo.size}")
    @Schema(description = "商户号")
    private String mchNo;

    /// 商户分账单号(幂等键, 同一商户下唯一, 与唯一约束 uk_pay_alloc_order_mch_biz 维度一致)
    @NotBlank(message = "{validation.field.bizAllocNo.notBlank}")
    @Size(max = 100, message = "{validation.field.bizAllocNo.size}")
    @Schema(description = "商户分账单号")
    private String bizAllocNo;

    /// 原支付资金交易号(tradeNo 与 bizOrderNo 二选一, tradeNo 优先)
    @Size(max = 100, message = "{validation.field.tradeNo.size}")
    @Schema(description = "原支付资金交易号")
    private String tradeNo;

    /// 原支付商户业务订单号(tradeNo 为空时用此定位原支付)
    @Size(max = 100, message = "{validation.field.bizOrderNo.size}")
    @Schema(description = "原支付商户业务订单号")
    private String bizOrderNo;

    /// 分账标题(必填, 不再回退原支付标题)
    @NotBlank(message = "{validation.field.allocTitle.notBlank}")
    @Size(max = 100, message = "{validation.field.allocTitle.size}")
    @Schema(description = "分账标题")
    private String title;

    /// 分账描述
    @Size(max = 500, message = "{validation.field.allocDescription.size}")
    @Schema(description = "分账描述")
    private String description;

    /// 接收方列表(至少一个, 上限50, 避免超大请求并匹配通道侧单次分账接收方上限)
    @NotEmpty(message = "{validation.field.receivers.notEmpty}")
    @Size(max = 50, message = "{validation.field.receivers.size}")
    @Valid
    @Schema(description = "接收方列表")
    private List<AllocReceiverParam> receivers;

    /// 分账完结模式(auto-分账即完结 / manual-需显式完结, 未传默认 auto 兼容存量单次语义)
    /// 多次分账: 前几笔传 manual(剩余资金保持冻结可继续分账), 最后一笔传 auto 或调用完结接口
    /// @see cn.daxpay.open.payment.trade.alloc.enums.AllocFinishModeEnum
    @Size(max = 32, message = "{validation.field.finishMode.size}")
    @Schema(description = "分账完结模式(auto/manual, 默认 auto)")
    private String finishMode;

    /// 商户扩展参数, 回调时原样返回
    @Size(max = 500, message = "{validation.field.attach.size}")
    @Schema(description = "商户扩展参数, 回调时原样返回")
    private String attach;

    /// 异步通知地址
    @Size(max = 200, message = "{validation.field.notifyUrl.size}")
    @Schema(description = "回调通知地址")
    private String notifyUrl;

    /// 接收方参数(单个)
    @Data
    @Accessors(chain = true)
    @Schema(title = "分账接收方参数")
    public static class AllocReceiverParam {

        /// 接收方类型
        /// @see cn.daxpay.open.payment.trade.alloc.enums.AllocReceiverTypeEnum
        @NotBlank(message = "{validation.field.receiverType.notBlank}")
        @Size(max = 32, message = "{validation.field.receiverType.size}")
        @Schema(description = "接收方类型")
        private String receiverType;

        /// 接收方账号
        @NotBlank(message = "{validation.field.receiverAccount.notBlank}")
        @Size(max = 128, message = "{validation.field.receiverAccount.size}")
        @Schema(description = "接收方账号")
        private String receiverAccount;

        /// 接收方姓名(部分通道/类型必填, 如抖音个人)
        @Size(max = 64, message = "{validation.field.receiverName.size}")
        @Schema(description = "接收方姓名")
        private String receiverName;

        /// 分账金额(分, 与内部金额单位统一)
        @NotNull(message = "{validation.field.allocAmount.notNull}")
        @Min(value = 1, message = "{validation.field.allocAmount.min}")
        @Schema(description = "分账金额(分)")
        private Long amount;
    }
}
