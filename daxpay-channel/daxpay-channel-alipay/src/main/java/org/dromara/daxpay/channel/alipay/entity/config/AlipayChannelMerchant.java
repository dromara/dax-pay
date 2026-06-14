package org.dromara.daxpay.channel.alipay.entity.config;

import org.dromara.daxpay.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.channel.alipay.convert.AlipayChannelMerchantConvert;
import org.dromara.daxpay.channel.alipay.result.config.AlipayChannelMerchantResult;
import org.dromara.daxpay.payment.common.entity.merchant.MchBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 支付宝通道商户配置
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "mch_alipay_channel_merchant", autoResultMap = true)
public class AlipayChannelMerchant extends MchBaseEntity implements ToResult<AlipayChannelMerchantResult> {

    /// 通道商户号
    private String channelMchNo;

    /// 所属支付产品
    private String product;

    /// 支付宝服务商应用ID
    private String isvAppId;

    /// 支付宝商家用户唯一识别码(2088开头)
    private String alipayUserId;

    /// 应用授权令牌，服务商代商户调用接口的凭据
    private String appAuthToken;

    /// 转换
    @Override
    public AlipayChannelMerchantResult toResult() {
        return AlipayChannelMerchantConvert.CONVERT.toResult(this);
    }
}
