package cn.daxpay.open.channel.stripe.entity;

import cn.daxpay.open.channel.stripe.convert.StripeChannelMerchantConvert;
import cn.daxpay.open.channel.stripe.result.StripeChannelMerchantResult;
import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # Stripe 通道商户绑定
///
/// 一个 Stripe 账户(Account)对应一个 channelMchNo, 商户的多个应用共享此绑定。
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "stripe_channel_merchant", autoResultMap = true)
public class StripeChannelMerchant extends MchBaseEntity implements ToResult<StripeChannelMerchantResult> {

    /// 通道商户号(STRIPE+雪花)
    private String channelMchNo;

    /// 所属支付产品
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum
    private String product;

    /// Stripe 账户 ID(acct_xxx)
    private String accountId;

    /// 转换
    @Override
    public StripeChannelMerchantResult toResult() {
        return StripeChannelMerchantConvert.CONVERT.toResult(this);
    }
}
