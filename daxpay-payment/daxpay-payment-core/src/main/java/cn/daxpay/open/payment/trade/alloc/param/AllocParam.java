package cn.daxpay.open.payment.trade.alloc.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
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
    @Size(max = 32, message = "商户号不可超过32位")
    @Schema(description = "商户号")
    private String mchNo;

    /// 应用号
    @Size(max = 32, message = "应用号不可超过32位")
    @Schema(description = "应用号")
    private String appId;

    /// 商户分账单号(幂等键, 同一应用下唯一)
    @NotBlank(message = "商户分账单号必填")
    @Size(max = 100, message = "商户分账单号不可超过100位")
    @Schema(description = "商户分账单号")
    private String bizAllocNo;

    /// 原支付资金交易号(tradeNo 与 bizOrderNo 二选一, tradeNo 优先)
    @Size(max = 100, message = "原支付交易号不可超过100位")
    @Schema(description = "原支付资金交易号")
    private String tradeNo;

    /// 原支付商户业务订单号(tradeNo 为空时用此定位原支付)
    @Size(max = 100, message = "商户业务订单号不可超过100位")
    @Schema(description = "原支付商户业务订单号")
    private String bizOrderNo;

    /// 分账标题
    @Size(max = 100, message = "分账标题不可超过100位")
    @Schema(description = "分账标题")
    private String title;

    /// 分账描述
    @Size(max = 500, message = "分账描述不可超过500位")
    @Schema(description = "分账描述")
    private String description;

    /// 接收方列表(至少一个, 上限50, 避免超大请求并匹配通道侧单次分账接收方上限)
    @NotEmpty(message = "接收方列表不可为空")
    @Size(max = 50, message = "接收方列表不可超过50个")
    @Valid
    @Schema(description = "接收方列表")
    private List<AllocReceiverParam> receivers;

    /// 商户扩展参数, 回调时原样返回
    @Size(max = 500, message = "商户扩展参数不可超过500位")
    @Schema(description = "商户扩展参数, 回调时原样返回")
    private String attach;

    /// 异步通知地址
    @Size(max = 200, message = "回调通知地址不可超过200位")
    @Schema(description = "回调通知地址")
    private String notifyUrl;

    /// 接收方参数(单个)
    @Data
    @Accessors(chain = true)
    @Schema(title = "分账接收方参数")
    public static class AllocReceiverParam {

        /// 接收方类型
        /// @see cn.daxpay.open.payment.trade.alloc.enums.AllocReceiverTypeEnum
        @NotBlank(message = "接收方类型必填")
        @Size(max = 32, message = "接收方类型不可超过32位")
        @Schema(description = "接收方类型")
        private String receiverType;

        /// 接收方账号
        @NotBlank(message = "接收方账号必填")
        @Size(max = 128, message = "接收方账号不可超过128位")
        @Schema(description = "接收方账号")
        private String receiverAccount;

        /// 接收方姓名(部分通道/类型必填, 如抖音个人)
        @Size(max = 64, message = "接收方姓名不可超过64位")
        @Schema(description = "接收方姓名")
        private String receiverName;

        /// 分账金额(元)
        @NotNull(message = "分账金额必填")
        @DecimalMin(value = "0.01", message = "分账金额不可小于0.01元")
        @Digits(integer = 8, fraction = 2, message = "分账金额精度到分, 且要小于一亿元")
        @Schema(description = "分账金额(元)")
        private BigDecimal amount;
    }
}
