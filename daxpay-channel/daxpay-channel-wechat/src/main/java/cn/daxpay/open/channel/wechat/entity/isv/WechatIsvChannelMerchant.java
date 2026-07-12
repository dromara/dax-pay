package cn.daxpay.open.channel.wechat.entity.isv;

import cn.daxpay.open.channel.wechat.convert.isv.WechatIsvChannelMerchantConvert;
import cn.daxpay.open.channel.wechat.result.isv.WechatIsvChannelMerchantResult;
import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 微信服务商通道商户绑定
///
/// 微信特约商户关联到服务商本身(服务商密钥全局唯一), 不挂靠具体服务商应用,
/// 因此本表不保存应用关联字段。同一商户下特约商户号不重复。
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "wechat_isv_channel_merchant", autoResultMap = true)
public class WechatIsvChannelMerchant extends MchBaseEntity implements ToResult<WechatIsvChannelMerchantResult> {

    /// 通道商户号(WECHAT+雪花)
    private String channelMchNo;

    /// 所属支付产品
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum
    private String product;

    /// 微信特约商户号/二级商户号(V3 服务商支付 sub_mchid)
    private String subMchId;

    /// 认证应用类型(服务商模式下授权获取openId时使用)
    /// SP_APP=服务商应用(sp_appid), SUB_APP=子商户应用(sub_appid), 默认 SP_APP
    /// @see cn.daxpay.open.channel.wechat.code.WechatAuthAppTypeEnum
    private String authAppType;

    /// 转换
    @Override
    public WechatIsvChannelMerchantResult toResult() {
        return WechatIsvChannelMerchantConvert.CONVERT.toResult(this);
    }
}
