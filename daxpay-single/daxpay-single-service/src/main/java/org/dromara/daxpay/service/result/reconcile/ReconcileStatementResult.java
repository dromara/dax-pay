package org.dromara.daxpay.service.result.reconcile;

import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.core.result.MchAppResult;
import org.dromara.daxpay.service.enums.ReconcileResultEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 对账报告
 * @author xxm
 * @since 2024/8/6
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "对账报告")
public class ReconcileStatementResult extends MchAppResult {

    /** 名称 */
    @Schema(description = "名称")
    private String name;

    /** 对账号 */
    @Schema(description = "对账号")
    private String reconcileNo;

    /** 日期 */
    @Schema(description = "日期")
    private LocalDate date;

    /**
     * 通道
     * @see ChannelEnum
     */
    @Schema(description = "通道")
    private String channel;

    /** 交易对账文件是否下载或上传成功 */
    @Schema(description = "交易对账文件是否下载或上传成功")
    private boolean downOrUpload;

    /** 交易对账文件是否比对完成 */
    @Schema(description = "交易对账文件是否比对完成")
    private boolean compare;

    /** 支付订单数 */
    @Schema(description = "支付订单数")
    private Integer orderCount;

    /** 支付交易金额 */
    @Schema(description = "支付交易金额")
    private BigDecimal orderAmount;

    /** 退款订单数 */
    @Schema(description = "退款订单数")
    private Integer refundCount;

    /** 退款交易金额 */
    @Schema(description = "退款交易金额")
    private BigDecimal refundAmount;

    /** 通道支付订单数 */
    @Schema(description = "通道支付订单数")
    private Integer channelOrderCount;

    /** 通道支付交易金额 */
    @Schema(description = "通道支付交易金额")
    private BigDecimal channelOrderAmount;

    /** 通道退款订单数 */
    @Schema(description = "通道退款订单数")
    private Integer channelRefundCount;

    /** 通道退款交易金额 */
    @Schema(description = "通道退款交易金额")
    private BigDecimal channelRefundAmount;

    /**
     * 交易对账结果
     * @see ReconcileResultEnum
     */
    @Schema(description = "交易对账结果")
    private String result;

    /**
     *  通道对账单文件url
     */
    @Schema(description = "通道对账单文件url")
    private String channelFileUrl;

    /**
     * 平台对账单文件url
     */
    @Schema(description = "平台对账单文件url")
    private String platformFileUrl;

    /** 错误码 */
    @Schema(description = "错误码")
    private String errorCode;

    /** 错误信息 */
    @Schema(description = "错误信息")
    private String errorMsg;
}
