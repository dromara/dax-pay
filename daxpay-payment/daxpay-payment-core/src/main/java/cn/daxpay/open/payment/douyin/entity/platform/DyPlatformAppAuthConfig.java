package cn.daxpay.open.payment.douyin.entity.platform;

import cn.daxpay.open.payment.douyin.convert.platform.DyPlatformAppAuthConfigConvert;
import cn.daxpay.open.payment.douyin.result.platform.DyPlatformAppAuthConfigResult;
import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 平台抖音应用授权认证配置
///
/// 平台应用本身为全局配置，其 appSecret 同样为全局配置。
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "dy_platform_app_auth_config", autoResultMap = true)
public class DyPlatformAppAuthConfig extends MpBaseEntity implements ToResult<DyPlatformAppAuthConfigResult> {

    /// 平台抖音应用ID
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Long dyPlatformAppId;

    /// 应用密钥(加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String appSecret;

    /// 转换
    @Override
    public DyPlatformAppAuthConfigResult toResult() {
        return DyPlatformAppAuthConfigConvert.CONVERT.toResult(this);
    }
}
