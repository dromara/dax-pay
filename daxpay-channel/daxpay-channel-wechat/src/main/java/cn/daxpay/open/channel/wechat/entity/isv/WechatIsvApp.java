package cn.daxpay.open.channel.wechat.entity.isv;

import cn.daxpay.open.channel.wechat.convert.isv.WechatIsvAppConvert;
import cn.daxpay.open.channel.wechat.result.isv.WechatIsvAppResult;
import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 微信服务商应用
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "wechat_isv_app", autoResultMap = true)
public class WechatIsvApp extends MpBaseEntity implements ToResult<WechatIsvAppResult> {

    /// 应用名称
    private String appName;

    /// 应用类型
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String appType;

    /// 微信应用AppId
    private String wxAppId;

    /// 转换
    @Override
    public WechatIsvAppResult toResult() {
        return WechatIsvAppConvert.CONVERT.toResult(this);
    }
}
