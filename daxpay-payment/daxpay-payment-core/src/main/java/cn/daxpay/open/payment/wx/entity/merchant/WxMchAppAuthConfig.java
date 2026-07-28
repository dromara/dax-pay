package cn.daxpay.open.payment.wx.entity.merchant;

import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.payment.wx.convert.merchant.WxMchAppAuthConfigConvert;
import cn.daxpay.open.payment.wx.result.merchant.WxMchAppAuthConfigResult;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 商户微信应用授权认证配置
///
/// 挂商户维度，appSecret 加密存储。
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "wx_mch_app_auth_config", autoResultMap = true)
public class WxMchAppAuthConfig extends MchBaseEntity implements ToResult<WxMchAppAuthConfigResult> {

    /// 商户微信应用ID
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Long wxMchAppId;

    /// 应用密钥(加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String appSecret;

    /// 转换
    @Override
    public WxMchAppAuthConfigResult toResult() {
        return WxMchAppAuthConfigConvert.CONVERT.toResult(this);
    }
}
