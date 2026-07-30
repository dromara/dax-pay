package cn.daxpay.open.payment.douyin.entity.platform;

import cn.daxpay.open.payment.douyin.convert.platform.DyPlatformAppConvert;
import cn.daxpay.open.payment.douyin.result.platform.DyPlatformAppResult;
import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 平台抖音应用
///
/// 开放平台身份主数据（跨通道可引用），无商户行级隔离。
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "dy_platform_app", autoResultMap = true)
public class DyPlatformApp extends MpBaseEntity implements ToResult<DyPlatformAppResult> {

    /// 应用名称
    private String appName;

    /// 应用类型
    /// @see cn.daxpay.open.payment.douyin.enums.DyAppTypeEnum
    private String appType;

    /// 抖音应用AppId
    private String douyinAppId;

    /// 应用密钥(加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String appSecret;

    /// 转换
    @Override
    public DyPlatformAppResult toResult() {
        return DyPlatformAppConvert.CONVERT.toResult(this);
    }
}
