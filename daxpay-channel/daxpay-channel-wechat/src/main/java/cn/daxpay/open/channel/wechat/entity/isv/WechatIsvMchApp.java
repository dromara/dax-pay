package cn.daxpay.open.channel.wechat.entity.isv;

import cn.daxpay.open.channel.wechat.convert.isv.WechatIsvMchAppConvert;
import cn.daxpay.open.channel.wechat.result.isv.WechatIsvMchAppResult;
import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 微信服务商通道商户应用
///
/// 表示微信服务商模式下特约商户(子商户)注册的微信应用实体,挂在通道商户号(channelMchNo)维度下。
/// 每个应用对应一个微信AppId,支付时作为 sub_appid 使用。同一通道商户下微信应用AppId唯一。
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "wechat_isv_mch_app", autoResultMap = true)
public class WechatIsvMchApp extends MchBaseEntity implements ToResult<WechatIsvMchAppResult> {

    /// 通道商户号(服务商特约商户)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channelMchNo;

    /// 应用名称
    private String appName;

    /// 应用类型
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String appType;

    /// 微信应用AppId(对应微信支付sub_appid)
    private String wxAppId;

    /// 转换
    @Override
    public WechatIsvMchAppResult toResult() {
        return WechatIsvMchAppConvert.CONVERT.toResult(this);
    }
}
