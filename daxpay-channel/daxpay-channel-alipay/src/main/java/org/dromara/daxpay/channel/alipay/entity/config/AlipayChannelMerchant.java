package org.dromara.daxpay.channel.alipay.entity.config;

import org.dromara.daxpay.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.channel.alipay.convert.AlipayChannelMerchantConvert;
import org.dromara.daxpay.channel.alipay.result.config.AlipayChannelMerchantResult;
import org.dromara.daxpay.payment.common.entity.merchant.MchBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "mch_alipay_channel_merchant", autoResultMap = true)
public class AlipayChannelMerchant extends MchBaseEntity implements ToResult<AlipayChannelMerchantResult> {

    private String channelMchNo;

    private String product;

    private String isvAppId;

    private String alipayUserId;

    private String appAuthToken;

    @Override
    public AlipayChannelMerchantResult toResult() {
        return AlipayChannelMerchantConvert.CONVERT.toResult(this);
    }
}
