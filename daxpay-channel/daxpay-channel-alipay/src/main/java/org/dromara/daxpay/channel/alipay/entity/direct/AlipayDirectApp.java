package org.dromara.daxpay.channel.alipay.entity.direct;

import org.dromara.daxpay.channel.alipay.convert.AlipayDirectAppConvert;
import org.dromara.daxpay.channel.alipay.result.direct.AlipayDirectAppResult;
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
/// 表示支付宝直连模式下商户注册的应用实体，每个应用关联一个通道商户号，拥有独立的支付宝应用ID。
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "alipay_direct_app", autoResultMap = true)
public class AlipayDirectApp extends MchBaseEntity implements ToResult<AlipayDirectAppResult> {

    /// 通道商户号
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channelMchNo;

    /// 应用名称
    private String appName;

    /// 支付宝应用ID
    private String aliAppId;

    /// 转换
    @Override
    public AlipayDirectAppResult toResult() {
        return AlipayDirectAppConvert.CONVERT.toResult(this);
    }
}
