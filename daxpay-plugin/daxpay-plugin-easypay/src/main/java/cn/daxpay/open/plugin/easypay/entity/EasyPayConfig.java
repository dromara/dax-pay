package cn.daxpay.open.plugin.easypay.entity;

import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.plugin.easypay.convert.EasyPayConfigConvert;
import cn.daxpay.open.plugin.easypay.result.config.EasyPayConfigResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 易支付场景配置（应用级，一期精简）
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_easy_pay_config")
public class EasyPayConfig extends MchBaseEntity implements ToResult<EasyPayConfigResult> {

    /// 易支付商户号
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Integer pid;

    /// 应用号
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String appId;

    /// 限制支付
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String limitPay;

    @Override
    public EasyPayConfigResult toResult() {
        return EasyPayConfigConvert.CONVERT.toResult(this);
    }
}
