package cn.daxpay.open.channel.alipay.entity.direct;

import cn.daxpay.open.channel.alipay.convert.direct.AlipayDirectAppAuthConfigConvert;
import cn.daxpay.open.channel.alipay.result.direct.AlipayDirectAppAuthConfigResult;
import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 支付宝直连商户应用授权认证配置
///
/// 配置直连商户应用的用户标识类型，用于支付宝用户授权流程中的身份识别。
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
    private Long alipayDirectAppId;

    /// 用户标识类型
    private String userIdType;

    /// 转换
    @Override
    public AlipayDirectAppAuthConfigResult toResult() {
        return AlipayDirectAppAuthConfigConvert.CONVERT.toResult(this);
    }
}
