package cn.daxpay.open.payment.wx.entity.merchant;

import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.payment.wx.convert.merchant.WxMchAppConvert;
import cn.daxpay.open.payment.wx.result.merchant.WxMchAppResult;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 商户微信应用
///
/// 商户域开放平台身份主数据：跨商户不共享，同商户跨通道可引用。
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "wx_mch_app", autoResultMap = true)
public class WxMchApp extends MchBaseEntity implements ToResult<WxMchAppResult> {

    /// 应用名称
    private String appName;

    /// 应用类型
    /// @see cn.daxpay.open.payment.wx.enums.WxAppTypeEnum
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String appType;

    /// 微信应用AppId
    private String wxAppId;

    /// 应用密钥(加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String appSecret;

    /// 转换
    @Override
    public WxMchAppResult toResult() {
        return WxMchAppConvert.CONVERT.toResult(this);
    }
}
