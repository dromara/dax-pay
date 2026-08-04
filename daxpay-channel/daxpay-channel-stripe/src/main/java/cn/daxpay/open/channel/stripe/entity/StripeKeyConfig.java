package cn.daxpay.open.channel.stripe.entity;

import cn.daxpay.open.channel.stripe.convert.StripeKeyConfigConvert;
import cn.daxpay.open.channel.stripe.result.StripeKeyConfigResult;
import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # Stripe 密钥配置
///
/// 商户维度的密钥配置，一个 Stripe 账户(Account)对应一套密钥，与具体应用无关，敏感字段加密存储。
/// 密钥在测试环境(test mode)与生产环境(live mode)各自独立, 由密钥前缀(sk_test_/sk_live_)自标识环境,
/// 沙箱/生产路由隔离由通道商户 [cn.daxpay.open.payment.merchant.entity.channel.ChannelMerchant#sandbox] 快照承担。
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "stripe_key_config", autoResultMap = true)
public class StripeKeyConfig extends MchBaseEntity implements ToResult<StripeKeyConfigResult> {

    /// 通道商户号
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channelMchNo;

    /// Stripe Secret Key(sk_test_xxx 沙箱 / sk_live_xxx 生产, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String secretKey;

    /// Stripe Publishable Key(pk_test_xxx 沙箱 / pk_live_xxx 生产, 加密存储, 前端 Elements 用)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String publishableKey;

    /// Webhook 签名密钥(whsec_xxx, 回调验签用, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String webhookSecret;

    /// 转换
    @Override
    public StripeKeyConfigResult toResult() {
        return StripeKeyConfigConvert.CONVERT.toResult(this);
    }
}
