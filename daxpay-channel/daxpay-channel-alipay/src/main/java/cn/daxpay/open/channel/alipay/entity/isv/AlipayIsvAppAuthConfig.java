package cn.daxpay.open.channel.alipay.entity.isv;

import cn.daxpay.open.channel.alipay.convert.isv.AlipayIsvAppAuthConfigConvert;
import cn.daxpay.open.channel.alipay.result.isv.AlipayIsvAppAuthConfigResult;
import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 支付宝服务商应用授权认证配置
///
/// 配置服务商应用的用户标识类型，用于支付宝用户授权流程中的身份识别。
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "alipay_isv_app_auth_config", autoResultMap = true)
public class AlipayIsvAppAuthConfig extends MpBaseEntity implements ToResult<AlipayIsvAppAuthConfigResult> {

    /// 支付宝服务商应用ID
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Long alipayIsvAppId;

    /// 用户标识类型
    private String userIdType;

    /// 转换
    @Override
    public AlipayIsvAppAuthConfigResult toResult() {
        return AlipayIsvAppAuthConfigConvert.CONVERT.toResult(this);
    }
}
