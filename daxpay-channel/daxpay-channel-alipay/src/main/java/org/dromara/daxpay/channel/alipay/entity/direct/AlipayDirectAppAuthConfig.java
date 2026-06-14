package org.dromara.daxpay.channel.alipay.entity.direct;

import org.dromara.daxpay.channel.alipay.convert.AlipayDirectAppAuthConfigConvert;
import org.dromara.daxpay.channel.alipay.result.direct.AlipayDirectAppAuthConfigResult;
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
/// 配置直连商户应用的用户标识类型和授权回调地址，用于支付宝用户授权流程中的身份识别与回调跳转。
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "alipay_direct_app_auth_config", autoResultMap = true)
public class AlipayDirectAppAuthConfig extends MchBaseEntity implements ToResult<AlipayDirectAppAuthConfigResult> {

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
    public AlipayDirectAppAuthConfigResult toResult() {
        return AlipayDirectAppAuthConfigConvert.CONVERT.toResult(this);
    }
}
