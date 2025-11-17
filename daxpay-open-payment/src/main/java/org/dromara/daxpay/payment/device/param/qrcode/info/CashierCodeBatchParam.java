package org.dromara.daxpay.payment.device.param.qrcode.info;

import org.dromara.daxpay.payment.pay.enums.ChannelEnum;
import org.dromara.daxpay.payment.pay.enums.PayMethodEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 批量创建收款码参数
 * @author xxm
 * @since 2025/7/1
 */
@Data
@Accessors(chain = true)
@Schema(title = "批量创建收款码参数")
public class CashierCodeBatchParam {

    /** 批次号 */
    @Schema(description = "批次号")
    private String batchNo;

    /** 创建数量 */
    @Schema(description = "创建数量")
    private Integer count;

    /** 模板Id */
    @Schema(description = "模板Id")
    private Long templateId;

    /** 金额类型 */
    @Schema(description = "金额类型")
    private String amountType;

    /** 金额 */
    @Schema(description = "金额")
    private BigDecimal amount;

    /** 是否启用 */
    @Schema(description = "是否启用")
    private boolean enable;

    /** 读取预设配置 */
    @Schema(description = "读取预设配置")
    private boolean readSystem;

    /**
     * 微信场景对应通道
     * @see ChannelEnum
     */
    @Schema(description = "微信场景对应通道")
    private String wxChannel;

    /**
     * 微信场景对应支付方式
     * @see PayMethodEnum
     */

    private String wxMethod;

    /**
     * 支付宝场景对应通道
     */
    @Schema(description = "支付宝场景对应通道")
    private String alipayChannel;

    /**
     * 支付宝场景对应支付方式
     * @see PayMethodEnum
     */
    @Schema(description = "支付宝场景对应支付方式")
    private String alipayMethod;

    /**
     * 银联场景对应通道
     */
    @Schema(description = "银联场景对应通道")
    private String unionChannel;

    /**
     * 银联场景对应支付方式
     * @see PayMethodEnum
     */
    @Schema(description = "银联场景对应支付方式")
    private String unionMethod;
}
