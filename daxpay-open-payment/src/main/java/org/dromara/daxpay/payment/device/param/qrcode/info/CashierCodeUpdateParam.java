package org.dromara.daxpay.payment.device.param.qrcode.info;

import org.dromara.daxpay.payment.pay.enums.ChannelEnum;
import org.dromara.daxpay.payment.pay.enums.PayMethodEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 码牌更新参数
 * @author xxm
 * @since 2025/7/1
 */
@Data
@Accessors(chain = true)
@Schema(title = "码牌更新参数")
public class CashierCodeUpdateParam {

    /** 主键 */
    @Schema(description = "主键")
    private Long id;

    /** 名称 */
    @Schema(description = "名称")
    private String name;

    /** 收款金额类型 固定金额/任意金额 */
    @Schema(description = "收款金额类型")
    private String amountType;

    /** 金额 */
    @Schema(description = "金额")
    private BigDecimal amount;

    /** 模板ID */
    @Schema(description = "模板ID")
    private Long templateId;

    /** 是否启用 */
    @Schema(description = "是否启用")
    private Boolean enable;

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
