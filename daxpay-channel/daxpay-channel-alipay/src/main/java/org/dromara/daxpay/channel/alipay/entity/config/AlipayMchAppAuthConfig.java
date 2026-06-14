package org.dromara.daxpay.channel.alipay.entity.config;

import org.dromara.daxpay.channel.alipay.convert.AlipayMchAppAuthConfigConvert;
import org.dromara.daxpay.channel.alipay.result.config.AlipayMchAppAuthConfigResult;
import org.dromara.daxpay.payment.common.entity.merchant.MchBaseEntity;
import org.dromara.daxpay.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 支付宝直连商户应用授权认证配置
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "alipay_mch_app_auth_config", autoResultMap = true)
public class AlipayMchAppAuthConfig extends MchBaseEntity implements ToResult<AlipayMchAppAuthConfigResult> {

    /// 通道商户号
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channelMchNo;

    /// 关联应用ID
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Long appId;

    /// 用户标识类型
    private String userIdType;

    /// 授权回调地址
    private String authCallbackUrl;

    /// 转换
    @Override
    public AlipayMchAppAuthConfigResult toResult() {
        return AlipayMchAppAuthConfigConvert.CONVERT.toResult(this);
    }
}
