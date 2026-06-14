package org.dromara.daxpay.channel.alipay.entity.app;

import org.dromara.daxpay.channel.alipay.convert.AlipayMchAppConvert;
import org.dromara.daxpay.channel.alipay.result.app.AlipayMchAppResult;
import org.dromara.daxpay.payment.common.entity.merchant.MchBaseEntity;
import org.dromara.daxpay.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 支付宝直连商户应用
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "alipay_mch_app", autoResultMap = true)
public class AlipayMchApp extends MchBaseEntity implements ToResult<AlipayMchAppResult> {

    /// 通道商户号
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channelMchNo;

    /// 应用名称
    private String appName;

    /// 支付宝应用ID
    private String aliAppId;

    /// 转换
    @Override
    public AlipayMchAppResult toResult() {
        return AlipayMchAppConvert.CONVERT.toResult(this);
    }
}
