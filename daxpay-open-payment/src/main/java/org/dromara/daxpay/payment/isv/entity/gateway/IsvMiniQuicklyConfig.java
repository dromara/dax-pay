package org.dromara.daxpay.payment.isv.entity.gateway;

import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.payment.isv.convert.gateway.IsvMiniQuicklyConfigConvert;
import org.dromara.daxpay.payment.isv.result.gateway.IsvMiniQuicklyConfigResult;
import com.baomidou.mybatisplus.annotation.FieldFill;
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
@TableName("pay_isv_mini_quickly_config")
public class IsvMiniQuicklyConfig extends MpBaseEntity implements ToResult<IsvMiniQuicklyConfigResult> {

    /** 限制小程序支付方式 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String miniAppLimitPay;

    /** 服务商号 */
    @TableField(updateStrategy = FieldStrategy.NEVER, fill = FieldFill.INSERT)
    private String isvNo;

    /**
     * 转换为结果对象
     */
    @Override
    public IsvMiniQuicklyConfigResult toResult() {
        return IsvMiniQuicklyConfigConvert.CONVERT.toResult(this);
    }
}
