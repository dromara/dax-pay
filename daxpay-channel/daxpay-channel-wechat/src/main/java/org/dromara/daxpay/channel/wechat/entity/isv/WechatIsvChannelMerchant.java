package org.dromara.daxpay.channel.wechat.entity.isv;

import org.dromara.daxpay.channel.wechat.convert.isv.WechatIsvChannelMerchantConvert;
import org.dromara.daxpay.channel.wechat.result.isv.WechatIsvChannelMerchantResult;
import org.dromara.daxpay.payment.common.entity.merchant.MchBaseEntity;
import org.dromara.daxpay.platform.common.mybatisplus.function.ToResult;
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
@TableName(value = "mch_wechat_isv_channel_merchant", autoResultMap = true)
public class WechatIsvChannelMerchant extends MchBaseEntity implements ToResult<WechatIsvChannelMerchantResult> {

    /// 通道商户号(WISV+雪花)
    private String channelMchNo;

    /// 所属支付产品
    /// @see org.dromara.daxpay.platform.core.enums.pay.channel.ProductEnum
    private String product;

    /// 微信特约商户号/二级商户号(V3 服务商支付 sub_mchid)
    private String subMchId;

    /// 转换
    @Override
    public WechatIsvChannelMerchantResult toResult() {
        return WechatIsvChannelMerchantConvert.CONVERT.toResult(this);
    }
}
