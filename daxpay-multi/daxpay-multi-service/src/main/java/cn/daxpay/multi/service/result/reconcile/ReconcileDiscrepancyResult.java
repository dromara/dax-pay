package cn.daxpay.multi.service.result.reconcile;

import cn.daxpay.multi.core.enums.TradeTypeEnum;
import cn.daxpay.multi.core.result.MchResult;
import cn.daxpay.multi.service.enums.ReconcileDiscrepancyTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 对账差异记录
 * @author xxm
 * @since 2024/8/6
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "对账差异记录")
public class ReconcileDiscrepancyResult extends MchResult {

    /** 对账单ID */
    @Schema(description = "对账单ID")
    private Long reconcileId;

    /** 对账号 */
    @Schema(description = "对账号")
    private String reconcileNo;

    /** 对账日期 */
    @Schema(description = "对账日期")
    private LocalDate reconcileDate;

    /** 支付通道 */
    @Schema(description = "支付通道")
    private String channel;

    /**
     * 差异类型
     * @see ReconcileDiscrepancyTypeEnum
     */
    @Schema(description = "差异类型")
    private String discrepancyType;

    /* 平台侧信息 */
    /** 平台交易号 */
    @Schema(description = "平台交易号")
    private String TradeNo;

    /** 商户订单号 */
    @Schema(description = "商户订单号")
    private String bizTradeNo;

    /**
     * 交易类型
     * @see TradeTypeEnum
     */
    @Schema(description = "交易类型")
    private String tradeType;

    /** 交易金额 */
    @Schema(description = "交易金额")
    private BigDecimal tradeAmount;

    /** 交易状态 */
    @Schema(description = "交易状态")
    private String tradeStatus;

    /** 交易时间 */
    @Schema(description = "交易时间")
    private LocalDateTime tradeTime;

    /* 通道侧信息 */
    /** 通道交易号 */
    @Schema(description = "通道交易号")
    private String channelTradeNo;

    /** 通道交易类型 */
    @Schema(description = "通道交易类型")
    private String channelTradeType;

    /** 通道交易金额 */
    @Schema(description = "通道交易金额")
    private BigDecimal channelTradeAmount;

    /** 通道交易状态 */
    @Schema(description = "通道交易状态")
    private String channelTradeStatus;

    /** 通道交易时间 */
    @Schema(description = "通道交易时间")
    private LocalDateTime channelTradeTime;

}

