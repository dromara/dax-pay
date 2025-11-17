package org.dromara.daxpay.payment.device.entity.qrcode.info;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.payment.device.convert.qrcode.CashierCodeConvert;
import org.dromara.daxpay.payment.device.result.qrcode.info.CashierCodeResult;
import org.dromara.daxpay.payment.merchant.common.entity.MchAppEditEntity;
import org.dromara.daxpay.payment.pay.enums.ChannelEnum;
import org.dromara.daxpay.payment.pay.enums.PayMethodEnum;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 收款码牌
 * @author xxm
 * @since 2025/7/1
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_cashier_code")
public class CashierCode extends MchAppEditEntity implements ToResult<CashierCodeResult> {

    /** 码牌名称 */
    private String name;

    /** 金额类型 固定金额/任意金额 */
    private String amountType;

    /** 金额 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private BigDecimal amount;

    /** 编号 */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String code;

    /** 模板ID */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private Long templateId;

    /** 限制用户支付方式 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String limitPay;

    /** 读取预设配置 */
    private boolean readSystem;

    /**
     * 微信场景对应通道
     * @see ChannelEnum
     */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String wxChannel;

    /**
     * 微信场景对应支付方式
     * @see PayMethodEnum
     */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String wxMethod;

    /**
     * 支付宝场景对应通道
     */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String alipayChannel;

    /**
     * 支付宝场景对应支付方式
     * @see PayMethodEnum
     */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String alipayMethod;

    /**
     * 银联场景对应通道
     */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String unionChannel;

    /**
     * 银联场景对应支付方式
     * @see PayMethodEnum
     */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String unionMethod;

    /** 状态 */
    private boolean enable;

    /** 批次号 */
    private String batchNo;

    /**
     * 转换
     */
    @Override
    public CashierCodeResult toResult() {
        return CashierCodeConvert.CONVERT.toResult(this);
    }
}
