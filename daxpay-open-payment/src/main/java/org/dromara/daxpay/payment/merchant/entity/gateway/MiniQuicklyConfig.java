package org.dromara.daxpay.payment.merchant.entity.gateway;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.payment.merchant.common.entity.MchAppBaseEntity;
import org.dromara.daxpay.payment.merchant.convert.gateway.MiniQuicklyConfigConvert;
import org.dromara.daxpay.payment.merchant.result.gateway.MiniQuicklyConfigResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 小程序快捷支付配置
 * @author xxm
 * @since 2025/10/10
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_mini_quickly_config")
public class MiniQuicklyConfig extends MchAppBaseEntity implements ToResult<MiniQuicklyConfigResult> {

    /** 限制小程序支付方式 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String limitPay;

    /** 小程序付款终端号 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String terminalNo;

    /**
     * 转换
     */
    @Override
    public MiniQuicklyConfigResult toResult() {
        return MiniQuicklyConfigConvert.CONVERT.toResult(this);
    }
}
