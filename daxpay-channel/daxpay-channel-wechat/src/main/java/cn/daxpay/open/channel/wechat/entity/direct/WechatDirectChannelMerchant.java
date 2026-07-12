package cn.daxpay.open.channel.wechat.entity.direct;

import cn.daxpay.open.channel.wechat.convert.direct.WechatDirectChannelMerchantConvert;
import cn.daxpay.open.channel.wechat.result.direct.WechatDirectChannelMerchantResult;
import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 微信直连通道商户绑定
///
/// 一个微信商户号(wxMchId)对应一个 channelMchNo, 商户的多个应用共享此绑定。
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "wechat_direct_channel_merchant", autoResultMap = true)
public class WechatDirectChannelMerchant extends MchBaseEntity implements ToResult<WechatDirectChannelMerchantResult> {

    /// 通道商户号(WECHAT+雪花)
    private String channelMchNo;

    /// 所属支付产品
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum
    private String product;

    /// 微信直连商户号
    private String wxMchId;

    /// 转换
    @Override
    public WechatDirectChannelMerchantResult toResult() {
        return WechatDirectChannelMerchantConvert.CONVERT.toResult(this);
    }
}
